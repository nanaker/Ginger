package laboratory.sniffer.detector.neo4j;

import laboratory.sniffer.detector.entities.Entity;
import laboratory.sniffer.detector.entities.DetectorApp;
import laboratory.sniffer.detector.entities.DetectorArgument;
import laboratory.sniffer.detector.entities.DetectorClass;
import laboratory.sniffer.detector.entities.DetectorExternalArgument;
import laboratory.sniffer.detector.entities.DetectorExternalClass;
import laboratory.sniffer.detector.entities.DetectorExternalMethod;
import laboratory.sniffer.detector.entities.DetectorLibrary;
import laboratory.sniffer.detector.entities.DetectorMethod;
import laboratory.sniffer.detector.entities.DetectorVariable;
import laboratory.sniffer.detector.metrics.Metric;
import org.neo4j.graphdb.DynamicLabel;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class ModelToGraph {
    private static final Logger logger = LoggerFactory.getLogger(ModelToGraph.class.getName());

    private GraphDatabaseService graphDatabaseService;
    private DatabaseManager databaseManager;
    private static final Label appLabel = DynamicLabel.label("App");
    private static final Label classLabel = DynamicLabel.label("Class");
    private static final Label externalClassLabel = DynamicLabel.label("ExternalClass");
    private static final Label methodLabel = DynamicLabel.label("Method");
    private static final Label externalMethodLabel = DynamicLabel.label("ExternalMethod");
    private static final Label variableLabel = DynamicLabel.label("Variable");
    private static final Label argumentLabel = DynamicLabel.label("Argument");
    private static final Label externalArgumentLabel = DynamicLabel.label("ExternalArgument");
    private static final Label libraryLabel = DynamicLabel.label("Library");

    private Map<Entity, Node> methodNodeMap;
    private Map<DetectorClass, Node> classNodeMap;
    private Map<DetectorVariable, Node> variableNodeMap;

    private String key;
    private String appName;

    public ModelToGraph(String DatabasePath) {
        this.databaseManager = new DatabaseManager(DatabasePath);
        databaseManager.start();
        this.graphDatabaseService = databaseManager.getGraphDatabaseService();
        methodNodeMap = new HashMap<>();
        classNodeMap = new HashMap<>();
        variableNodeMap = new HashMap<>();
        IndexManager indexManager = new IndexManager(graphDatabaseService);
        indexManager.createIndex();
    }

    public Node insertApp(DetectorApp detectorApp) {
        this.key = detectorApp.getKey();
        this.appName = detectorApp.getName();
        Node appNode;
        try (Transaction tx = graphDatabaseService.beginTx()) {
            appNode = graphDatabaseService.createNode(appLabel);
            appNode.setProperty("app_key", key);
            appNode.setProperty("name", appName);
            //appNode.setProperty("version", detectorApp.getVersion());
            appNode.setProperty("commit_number", detectorApp.getCommitNumber());
            //appNode.setProperty("commit_status", detectorApp.getStatus());
            //appNode.setProperty("sdk_version", detectorApp.getSdkVersion());
            //appNode.setProperty("analyzed_module", detectorApp.getModule());
            Date date = new Date();
            SimpleDateFormat simpleFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.S");
            appNode.setProperty("date_analysis", simpleFormat.format(date));
            //appNode.setProperty("path", detectorApp.getPath());

            Node classNode;
            for (DetectorClass detectorClass : detectorApp.getDetectorClasses()) {
                classNode = insertClass(detectorClass);
                appNode.createRelationshipTo(classNode, RelationTypes.APP_OWNS_CLASS);
            }
            for (DetectorExternalClass detectorExternalClass : detectorApp.getDetectorExternalClasses()) {
                insertExternalClass(detectorExternalClass);
            }
            for (Metric metric : detectorApp.getMetrics()) {
                insertMetric(metric, appNode);
            }

            for (DetectorLibrary detectorLibrary : detectorApp.getDetectorLibraries()) {
                appNode.createRelationshipTo(insertLibrary(detectorLibrary), RelationTypes.APP_USES_LIBRARY);
            }
            tx.success();
        }
        try (Transaction tx = graphDatabaseService.beginTx()) {
            createHierarchy(detectorApp);
            createCallGraph(detectorApp);
            tx.success();
        }
        return appNode;
    }

    private void insertMetric(Metric metric, Node node) {
        node.setProperty(metric.getName(), metric.getValue());
    }


    public Node insertClass(DetectorClass detectorClass) {
        Node classNode = graphDatabaseService.createNode(classLabel);
        classNodeMap.put(detectorClass, classNode);
        classNode.setProperty("app_key", key);
        classNode.setProperty("name", detectorClass.getName());
        classNode.setProperty("modifier", detectorClass.getModifier().toString().toLowerCase());
        classNode.setProperty("file_path", detectorClass.getPath());
        classNode.setProperty("app_name", appName);
        if (detectorClass.getParentName() != null) {
            classNode.setProperty("parent_name", detectorClass.getParentName());
        }
        for (DetectorVariable detectorVariable : detectorClass.getDetectorVariables()) {
            classNode.createRelationshipTo(insertVariable(detectorVariable), RelationTypes.CLASS_OWNS_VARIABLE);

        }
        for (DetectorMethod detectorMethod : detectorClass.getDetectorMethods()) {
            classNode.createRelationshipTo(insertMethod(detectorMethod), RelationTypes.CLASS_OWNS_METHOD);
        }
        for (Metric metric : detectorClass.getMetrics()) {
            insertMetric(metric, classNode);
        }
        return classNode;
    }

    public Node insertLibrary(DetectorLibrary detectorLibrary) {
        Node libraryNode = graphDatabaseService.createNode(libraryLabel);
        libraryNode.setProperty("app_key", key);
        libraryNode.setProperty("name", detectorLibrary.getName());
        libraryNode.setProperty("app_name", appName);
        return libraryNode;
    }

    public Node insertExternalClass(DetectorExternalClass detectorClass) {
        Node classNode = graphDatabaseService.createNode(externalClassLabel);
        classNode.setProperty("app_key", key);
        classNode.setProperty("name", detectorClass.getName());
        classNode.setProperty("app_name", appName);
        if (detectorClass.getParentName() != null) {
            classNode.setProperty("parent_name", detectorClass.getParentName());
        }
        for (DetectorExternalMethod detectorExternalMethod : detectorClass.getDetectorExternalMethods()) {
            classNode.createRelationshipTo(insertExternalMethod(detectorExternalMethod), RelationTypes.CLASS_OWNS_METHOD);
        }
        for (Metric metric : detectorClass.getMetrics()) {
            insertMetric(metric, classNode);
        }
        return classNode;
    }

    public Node insertVariable(DetectorVariable detectorVariable) {
        Node variableNode = graphDatabaseService.createNode(variableLabel);
        variableNodeMap.put(detectorVariable, variableNode);
        variableNode.setProperty("app_key", key);
        variableNode.setProperty("name", detectorVariable.getName());
        variableNode.setProperty("modifier", detectorVariable.getModifier().toString().toLowerCase());
        variableNode.setProperty("type", detectorVariable.getType());
        variableNode.setProperty("app_name", appName);
        for (Metric metric : detectorVariable.getMetrics()) {
            insertMetric(metric, variableNode);
        }
        return variableNode;
    }

    public Node insertMethod(DetectorMethod detectorMethod) {
        Node methodNode = graphDatabaseService.createNode(methodLabel);
        methodNodeMap.put(detectorMethod, methodNode);
        methodNode.setProperty("app_key", key);
        methodNode.setProperty("name", detectorMethod.getName());
        methodNode.setProperty("modifier", detectorMethod.getModifier().toString().toLowerCase());
        methodNode.setProperty("full_name", detectorMethod.toString());
        methodNode.setProperty("app_name", appName);
        methodNode.setProperty("return_type", detectorMethod.getReturnType());

        for (Metric metric : detectorMethod.getMetrics()) {
            insertMetric(metric, methodNode);
        }
        Node variableNode;
        for (DetectorVariable detectorVariable : detectorMethod.getUsedVariables()) {
            variableNode = variableNodeMap.get(detectorVariable);
            if (variableNode != null) {
                methodNode.createRelationshipTo(variableNode, RelationTypes.USES);
            } else {
                logger.warn("problem");
            }

        }
        for (DetectorArgument arg : detectorMethod.getArguments()) {
            methodNode.createRelationshipTo(insertArgument(arg), RelationTypes.METHOD_OWNS_ARGUMENT);
        }
        return methodNode;
    }

    public Node insertExternalMethod(DetectorExternalMethod detectorMethod) {
        Node methodNode = graphDatabaseService.createNode(externalMethodLabel);
        methodNodeMap.put(detectorMethod, methodNode);
        methodNode.setProperty("app_key", key);
        methodNode.setProperty("name", detectorMethod.getName());
        methodNode.setProperty("full_name", detectorMethod.toString());
        methodNode.setProperty("return_type", detectorMethod.getReturnType());
        methodNode.setProperty("app_name", appName);
        for (Metric metric : detectorMethod.getMetrics()) {
            insertMetric(metric, methodNode);
        }
        for (DetectorExternalArgument arg : detectorMethod.getDetectorExternalArguments()) {
            methodNode.createRelationshipTo(insertExternalArgument(arg), RelationTypes.METHOD_OWNS_ARGUMENT);
        }
        return methodNode;
    }

    public Node insertArgument(DetectorArgument detectorArgument) {
        Node argNode = graphDatabaseService.createNode(argumentLabel);
        argNode.setProperty("app_key", key);
        argNode.setProperty("name", detectorArgument.getName());
        argNode.setProperty("position", detectorArgument.getPosition());
        argNode.setProperty("app_name", appName);
        return argNode;
    }

    public Node insertExternalArgument(DetectorExternalArgument detectorExternalArgument) {
        Node argNode = graphDatabaseService.createNode(externalArgumentLabel);
        argNode.setProperty("app_key", key);
        argNode.setProperty("name", detectorExternalArgument.getName());
        argNode.setProperty("position", detectorExternalArgument.getPosition());
        argNode.setProperty("app_name", appName);
        for (Metric metric : detectorExternalArgument.getMetrics()) {
            insertMetric(metric, argNode);
        }
        return argNode;
    }

    public void createHierarchy(DetectorApp detectorApp) {
        for (DetectorClass detectorClass : detectorApp.getDetectorClasses()) {
            DetectorClass parent = detectorClass.getParent();
            if (parent != null) {
                classNodeMap.get(detectorClass).createRelationshipTo(classNodeMap.get(parent), RelationTypes.EXTENDS);
            }
            for (DetectorClass pInterface : detectorClass.getInterfaces()) {
                classNodeMap.get(detectorClass).createRelationshipTo(classNodeMap.get(pInterface), RelationTypes.IMPLEMENTS);
            }
        }
    }

    public void createCallGraph(DetectorApp detectorApp) {
        for (DetectorClass detectorClass : detectorApp.getDetectorClasses()) {
            for (DetectorMethod detectorMethod : detectorClass.getDetectorMethods()) {
                for (Entity calledMethod : detectorMethod.getCalledMethods()) {
                    methodNodeMap.get(detectorMethod).createRelationshipTo(methodNodeMap.get(calledMethod), RelationTypes.CALLS);
                }
            }
        }
    }
}
