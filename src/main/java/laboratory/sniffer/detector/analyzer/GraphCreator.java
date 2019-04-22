package laboratory.sniffer.detector.analyzer;

import laboratory.sniffer.detector.entities.DetectorClass;
import laboratory.sniffer.detector.entities.Entity;
import laboratory.sniffer.detector.entities.DetectorApp;
import laboratory.sniffer.detector.entities.DetectorExternalClass;
import laboratory.sniffer.detector.entities.DetectorExternalMethod;
import laboratory.sniffer.detector.entities.DetectorMethod;
import laboratory.sniffer.detector.entities.DetectorVariable;

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

            for (VariableData variableData : detectorMethod.getUsedVariablesData()) {
                detectorClass = detectorApp.getDetectorInternalClass(variableData.getClassName());
                if (detectorClass != null) {
                    detectorVariable = detectorClass.findVariable(variableData.getVariableName());
                    if (detectorVariable != null) {
                        detectorMethod.useVariable(detectorVariable);
                    }
                }
            }
        }

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
