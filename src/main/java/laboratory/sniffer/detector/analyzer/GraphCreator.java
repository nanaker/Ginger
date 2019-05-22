package laboratory.sniffer.detector.analyzer;

import laboratory.sniffer.detector.entities.DetectorClass;
import laboratory.sniffer.detector.entities.Entity;
import laboratory.sniffer.detector.entities.DetectorApp;
import laboratory.sniffer.detector.entities.DetectorExternalClass;
import laboratory.sniffer.detector.entities.DetectorExternalMethod;
import laboratory.sniffer.detector.entities.DetectorMethod;
import laboratory.sniffer.detector.entities.DetectorVariable;
import spoon.reflect.declaration.CtClass;

import java.util.ArrayList;


public class GraphCreator {

    DetectorApp detectorApp;

    public GraphCreator(DetectorApp detectorApp) {
        this.detectorApp = detectorApp;
    }

    public void createCallGraph() {
        Entity targetClass;
        Entity targetMethod;
        DetectorClass detectorClass;
        DetectorVariable detectorVariable;
        ArrayList<DetectorMethod> detectorMethods = detectorApp.getMethods();
        for (DetectorMethod detectorMethod : detectorMethods) {
            addUsedVariableAndCallMethod(detectorMethod);
        }

    }

    public void addUsedVariableAndCallMethod(DetectorMethod detectorMethod) {
        Entity targetClass;
        Entity targetMethod;
        DetectorClass detectorClass;
        DetectorVariable detectorVariable;
        for (InvocationData invocationData : detectorMethod.getInvocationData()) {
            targetClass = detectorApp.getDetectorClass(invocationData.getTarget());
            if (targetClass instanceof DetectorClass) {
                targetMethod = ((DetectorClass) targetClass).getCalledDetectorMethod(invocationData.getMethod());
                detectorMethod.getDetectorClass().coupledTo((DetectorClass) targetClass);
            } else {

                targetMethod = DetectorExternalMethod.createDetectorExternalMethod(invocationData.getMethod(), invocationData.getType(),
                        (DetectorExternalClass) targetClass);
            }
            detectorMethod.callMethod(targetMethod);

        }

        addUsedVariable(detectorMethod);
    }

    public void addUsedVariable(DetectorMethod detectorMethod) {
        DetectorClass detectorClass;
        DetectorVariable detectorVariable;
        for (VariableData variableData : detectorMethod.getUsedVariablesData()) {
            detectorClass = detectorApp.getDetectorInternalClass(variableData.getClassName());
            if (detectorClass != null) {

                detectorVariable = detectorClass.findVariable(variableData.getVariableName());

                if (detectorVariable != null) {

                    detectorMethod.useVariable(detectorVariable);
                }
                else{

                    DetectorClass parent_Class=detectorMethod.getDetectorClass().getDetectorApp().findClass(variableData.getClassName()).getParent();
                    if(parent_Class!=null) {
                        detectorVariable=parent_Class.findVariable(variableData.getVariableName());
                        if (detectorVariable != null) {
                            detectorMethod.useVariable(detectorVariable);
                        }
                    }
                }
            }
        }
    }

    public String getClassName(String child){
        String[] classes_name=child.split("\\.");

        String class_name="";
        for (int i=0;i<classes_name.length-1;i++ ){

            class_name=class_name+classes_name[i]+".";
        }

        class_name=class_name.substring(0,class_name.length()-1);
        return class_name;
    }

    public void createClassHierarchy() {
        for (DetectorClass detectorClass : detectorApp.getDetectorClasses()) {
            String parentName = detectorClass.getParentName();
            DetectorClass implementedInterface;
            if (parentName != null) {
                DetectorClass parentClass = detectorClass.getDetectorApp().getDetectorInternalClass(parentName);
                detectorClass.setParent(parentClass);
                if (parentClass != null) {
                    parentClass.addChild();
                    detectorClass.setParentName(null);
                }
            }
            for (String interfaceName : detectorClass.getInterfacesNames()) {
                implementedInterface = detectorClass.getDetectorApp().getDetectorInternalClass(interfaceName);
                if (implementedInterface != null) {
                    detectorClass.implement(implementedInterface);
                }
            }

        }
    }


}
