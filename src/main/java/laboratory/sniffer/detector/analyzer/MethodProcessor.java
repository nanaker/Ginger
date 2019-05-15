package laboratory.sniffer.detector.analyzer;

import laboratory.sniffer.detector.entities.DetectorMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spoon.reflect.code.*;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtType;
import spoon.reflect.declaration.ModifierKind;


public class MethodProcessor extends ExecutableProcessor<CtMethod> {
    private static final Logger logger = LoggerFactory.getLogger(MethodProcessor.class.getName());

    @Override
    protected void process(CtMethod ctMethod, DetectorMethod detectorMethod) {
        detectorMethod.setSetter(checkSetter(ctMethod));
        detectorMethod.setGetter(checkGetter(ctMethod));
        detectorMethod.setOverride(isOverride(ctMethod,detectorMethod));

        for (ModifierKind modifierKind : ctMethod.getModifiers()) {
            if (modifierKind.toString().toLowerCase().equals("static")) {
                detectorMethod.setStatic(true);
                break;
            }
        }
    }

    private boolean isOverride(CtMethod element, DetectorMethod detectorMethod){

        if(element.getAnnotations().toString().trim().matches("(.*)Override(.*)")){
            return true;
        }
        if(detectorMethod.getDetectorClass().isInterface()){
            return true;
        }
        else return false;
    }



    private boolean checkGetter(CtMethod element) {
        if (element.getBody() == null) {
            return false;
        }
        if (element.getBody().getStatements().size() != 1) {
            return false;
        }
        CtStatement statement = element.getBody().getStatement(0);
        if (!(statement instanceof CtReturn)) {
            return false;
        }

        CtReturn retur = (CtReturn) statement;
        if (!(retur.getReturnedExpression() instanceof CtFieldRead)) {
            return false;
        }
        CtFieldRead returnedExpression = (CtFieldRead) retur.getReturnedExpression();

        CtType parent = element.getParent(CtType.class);
        if (parent == null) {
            return false;
        }
        try {
            if (parent.equals(returnedExpression.getVariable().getDeclaration().getDeclaringType())) {
                return true;
            }
        } catch (NullPointerException npe) {
            logger.warn("Could not find declaring type for getter: " + returnedExpression.getVariable().toString() + " (" + npe.getMessage() + ")");
        }
        return false;

    }

    private boolean checkSetter(CtMethod element) {
        if (element.getBody() == null) {
            return false;
        }
        if (element.getBody().getStatements().size() != 1) {
            return false;
        }
        if (element.getParameters().size() != 1) {
            return false;
        }
        CtStatement statement = element.getBody().getStatement(0);
        // the last statement is an assignment
        if (!(statement instanceof CtAssignment)) {
            return false;
        }

        CtAssignment ctAssignment = (CtAssignment) statement;
        if (!(ctAssignment.getAssigned() instanceof CtFieldWrite)) {
            return false;
        }
        if (!(ctAssignment.getAssignment() instanceof CtVariableRead)) {
            return false;
        }
        CtVariableRead ctVariableRead = (CtVariableRead) ctAssignment.getAssignment();
        if (element.getParameters().size() != 1) {
            return false;
        }
        try {
            if ((ctVariableRead.getVariable().getDeclaration()!=null) && !(ctVariableRead.getVariable().getDeclaration().equals(element.getParameters().get(0)))) {
                return false;
            }
        }catch (NullPointerException npe){
            System.out.println(npe.getCause());
        }
        CtFieldWrite returnedExpression = (CtFieldWrite) ((CtAssignment) statement).getAssigned();
        if (returnedExpression.getTarget() instanceof CtThisAccess) {
            return true;
        }

        return false;
    }
}
