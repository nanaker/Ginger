

package laboratory.sniffer.detector.neo4j;

import org.neo4j.cypher.CypherException;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class QueryEngine {
    private static final Logger logger = LoggerFactory.getLogger(QueryEngine.class.getName());

    protected GraphDatabaseService graphDatabaseService;
    protected DatabaseManager databaseManager;

    protected String csvPrefix;

    public String getCsvPrefix() {
        return csvPrefix;
    }

    public void setCsvPrefix(String csvPrefix) {
        this.csvPrefix = csvPrefix;
    }

    public GraphDatabaseService getGraphDatabaseService() {
        return graphDatabaseService;
    }


    public QueryEngine(String DatabasePath) {
        this.databaseManager = new DatabaseManager(DatabasePath);
        databaseManager.start();
        graphDatabaseService = databaseManager.getGraphDatabaseService();
        csvPrefix = "";
    }

    public void shutDown() {
        databaseManager.shutDown();
    }



    public void getPropertyForAllApk(String nodeType, String property, String suffix) throws IOException {
        Result result;
        try (Transaction ignored = graphDatabaseService.beginTx()) {
            String query = "MATCH (n:" + nodeType + ") RETURN n.app_key as app_key, n.name as name, n." + property + " as " + property;
            result = graphDatabaseService.execute(query);
            resultToCSV(toMap(result), suffix);
        }
    }








    public void resultToCSV(List<Map<String, Object>> result, String csvSuffix) throws IOException {
        String name = csvSuffix;
        FileWriter fw = new FileWriter(name);
        BufferedWriter writer = new BufferedWriter(fw);
        List<String> columns = result.isEmpty() ? new ArrayList<String>() : new ArrayList<>(result.get(0).keySet());
        Object val;

        int i;
        int columns_size = columns.size() - 1;
        for (i = 0; i < columns_size; i++) {
            writer.write(columns.get(i));
            writer.write(',');
        }


       if(columns.size()!=0) writer.write(columns.get(i));
        writer.newLine();
        for (Map<String, Object> row : result) {
            for (i = 0; i < columns_size; i++) {
                val = row.get(columns.get(i));
                if (val != null) {
                    writer.write(val.toString());
                    writer.write(',');
                }
            }
            val = row.get(columns.get(i));
            if (val != null) {
                writer.write(val.toString());
            }
            writer.newLine();
        }
        writer.close();
        fw.close();
    }

    public void resultToCSV(List<Map> rows, List<String> columns, String csvSuffix) throws IOException {
        String name = csvPrefix + csvSuffix;
        FileWriter fw = new FileWriter(name);
        BufferedWriter writer = new BufferedWriter(fw);
        Object val;
        int i;
        int columns_size = columns.size() - 1;
        for (i = 0; i < columns_size; i++) {
            writer.write(columns.get(i));
            writer.write(',');
        }
        writer.write(columns.get(i));
        writer.newLine();
        for (Map<String, Object> row : rows) {
            for (i = 0; i < columns_size; i++) {
                val = row.get(columns.get(i));
                if (val != null) {
                    writer.write(val.toString());
                    writer.write(',');
                }
            }
            val = row.get(columns.get(i));
            if (val != null) {
                writer.write(val.toString());
            }
            writer.newLine();
        }
        writer.close();
        fw.close();
    }

    public void statsToCSV(Map<String, Double> stats, String csvSuffix) throws IOException {
        String name = csvPrefix + csvSuffix;
        FileWriter fw = new FileWriter(name);
        BufferedWriter writer = new BufferedWriter(fw);
        Set<String> keys = stats.keySet();
        for (String key : keys) {
            writer.write(key);
            writer.write(',');
        }
        writer.newLine();
        for (String key : keys) {
            writer.write(String.valueOf(stats.get(key)));
            writer.write(',');
        }
        writer.close();
        fw.close();
    }



    private void deleteExternalClasses(String appKey) {
        deleteEntityOut(appKey, "ExternalClass", "ExternalMethod", "CLASS_OWNS_METHOD");
    }

    private void deleteExternalMethods(String appKey) {
        deleteEntityIn(appKey, "ExternalMethod", "Method", "CALLS");
    }

    private void deleteCalls(String appKey) {
        deleteRelations(appKey, "Method", "Method", "CALLS");
    }

    private void deleteMethods(String appKey) {
        deleteEntityIn(appKey, "Method", "Class", "CLASS_OWNS_METHOD");
    }

    private void deleteClasses(String appKey) {
        deleteEntityIn(appKey, "Class", "App", "APP_OWNS_CLASS");
    }

    private void deleteVariables(String appKey) {
        deleteEntityIn(appKey, "Variable", "Class", "CLASS_OWNS_VARIABLE");
    }

    private void deleteArguments(String appKey) {
        deleteEntityIn(appKey, "Argument", "Method", "METHOD_OWNS_ARGUMENT");
    }

    private void deleteUses(String appKey) {
        deleteRelations(appKey, "Method", "Variable", "USES");
    }

    private void deleteImplements(String appKey) {
        deleteRelations(appKey, "Class", "Class", "IMPLEMENTS");
    }

    private void deleteExtends(String appKey) {
        deleteRelations(appKey, "Class", "Class", "EXTENDS");
    }

    private void deleteApp(String appKey) {
        Result result;
        try (Transaction tx = graphDatabaseService.beginTx()) {
            String request = "MATCH (n:App {app_key: '" + appKey + "'}) DELETE n";
            logger.debug(request);
            result = graphDatabaseService.execute(request);
            logger.debug(result.resultAsString());
            tx.success();
        }
    }

    private void deleteRelations(String appKey, String nodeType1, String nodeType2, String reltype) {
        Result result;
        try (Transaction tx = graphDatabaseService.beginTx()) {
            String request = "MATCH (n:" + nodeType1 + "  {app_key: '" + appKey + "'})-[r:" + reltype + "]->(m:" + nodeType2 + "{app_key: '" + appKey + "'}) DELETE r";
            logger.debug(request);
            result = graphDatabaseService.execute(request);
            logger.debug(result.resultAsString());
            tx.success();
        }
    }

    private void deleteEntityIn(String appKey, String nodeType1, String nodeType2, String reltype) {
        Result result;
        try (Transaction tx = graphDatabaseService.beginTx()) {
            String request = "MATCH (n:" + nodeType1 + " {app_key: '" + appKey + "'})<-[r:" + reltype + "]-(m:" + nodeType2 + "{app_key: '" + appKey + "'}) DELETE n,r";
            logger.debug(request);
            result = graphDatabaseService.execute(request);
            logger.debug(result.resultAsString());
            tx.success();
        }
    }

    private void deleteEntityOut(String appKey, String nodeType1, String nodeType2, String reltype) {
        Result result;
        try (Transaction tx = graphDatabaseService.beginTx()) {
            String request = "MATCH (n:" + nodeType1 + " {app_key: '" + appKey + "'})-[r:" + reltype + "]->(m:" + nodeType2 + "{app_key: '" + appKey + "'}) DELETE n,r";
            logger.debug(request);
            result = graphDatabaseService.execute(request);
            logger.debug(result.resultAsString());
            tx.success();
        }
    }

    public void deleteEntireApp(String appKey) {
        //Delete have to be done in that order to ensure that relations are correctly deleted
        deleteExternalClasses(appKey);
        deleteExternalMethods(appKey);
        deleteUses(appKey);
        deleteCalls(appKey);
        deleteVariables(appKey);
        deleteArguments(appKey);
        deleteMethods(appKey);
        deleteImplements(appKey);
        deleteExtends(appKey);
        deleteClasses(appKey);
        deleteApp(appKey);
    }

    public List<String> findKeysFromPackageName(String appName) throws CypherException, IOException {
        ArrayList<String> keys = new ArrayList<>();
        try (Transaction ignored = graphDatabaseService.beginTx()) {
            Result result = graphDatabaseService.execute("MATCH (n:App) WHERE n.package='" + appName + "' RETURN n.app_key as key");
            while (result.hasNext()) {
                Map<String, Object> row = result.next();
                keys.add((String) row.get("key"));
            }
        }
        return keys;
    }



    public List<Map<String, Object>> toMap(Result result) {
        List<Map<String, Object>> output = new ArrayList<>();
        while (result.hasNext()) {
            Map<String, Object> entry = result.next();
            output.add(entry);
        }
        return output;
    }
}
