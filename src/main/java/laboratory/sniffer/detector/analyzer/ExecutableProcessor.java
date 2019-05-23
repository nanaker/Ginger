package laboratory.sniffer.detector.analyzer;

import laboratory.sniffer.detector.entities.DetectorArgument;
import laboratory.sniffer.detector.entities.DetectorMethod;
import laboratory.sniffer.detector.entities.DetectorModifiers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spoon.reflect.code.*;
import spoon.reflect.declaration.*;
import spoon.reflect.reference.CtVariableReference;
import spoon.reflect.visitor.filter.TypeFilter;
import spoon.support.reflect.code.*;
import spoon.support.reflect.declaration.CtFieldImpl;
import spoon.support.reflect.reference.CtTypeReferenceImpl;
import spoon.support.reflect.reference.CtVariableReferenceImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class ExecutableProcessor<T extends CtExecutable> {
    private static final Logger logger = LoggerFactory.getLogger(ExecutableProcessor.class.getName());

    public void process(T ctExecutable) {
        String name = ctExecutable.getSimpleName();

        String returnType = ctExecutable.getType().getQualifiedName();

        String visibility = "null";
        if (ctExecutable instanceof CtModifiable) {
            CtModifiable modifiable = (CtModifiable) ctExecutable;
            visibility = modifiable.getVisibility() == null ? "null" : modifiable.getVisibility().toString();
        }
        DetectorModifiers detectorModifiers = DataConverter.convertTextToModifier(visibility);
        if (detectorModifiers == null) {
            detectorModifiers = DetectorModifiers.PROTECTED;
        }
        int position = 0;
        String qualifiedName;

        DetectorMethod detectorMethod = DetectorMethod.createDetectorMethod(name, detectorModifiers, returnType,
                MainProcessor.currentClass);
        MainProcessor.currentMethod = detectorMethod;
        for (CtParameter<?> ctParameter : (List<CtParameter>) ctExecutable.getParameters()) {
            qualifiedName = ctParameter.getType().getQualifiedName();
            DetectorArgument.createDetectorArgument(qualifiedName, position, detectorMethod);
            position++;
        }
        int numberOfDeclaredLocals = ctExecutable.getElements(new TypeFilter<CtLocalVariable>(CtLocalVariable.class)).size();
        detectorMethod.setNumberOfLines(countEffectiveCodeLines(ctExecutable));
        //System.out.println("detectorMethod= "+detectorMethod+" "+ctExecutable);
        handleUsedVariables(ctExecutable, detectorMethod);
        handleInvocations(ctExecutable, detectorMethod);
        detectorMethod.setComplexity(getComplexity(ctExecutable));
        detectorMethod.setNumberOfDeclaredLocals(numberOfDeclaredLocals);

        process(ctExecutable, detectorMethod);
    }

    /**
     * Define the process behavior specific to the current element.
     *
     * @param ctExecutable  The processed corrector executable.
     * @param detectorMethod The processed output so far.
     */
    protected abstract void process(T ctExecutable, DetectorMethod detectorMethod);

    private int countEffectiveCodeLines(T ctMethod) {
        try {

            int numberOfLogicalLines=0;
            int nbCommentwithSemicoloone=0;
            int numbeOfLinesWithSemicolonnes=ctMethod.getBody().toString().split(";").length;
            int nbCtFor=ctMethod.getBody().getElements(new TypeFilter(CtFor.class)).size();
            int nbCtSwitch=ctMethod.getBody().getElements(new TypeFilter(CtSwitch.class)).size();
            int nbCtConditional=ctMethod.getBody().getElements(new TypeFilter(CtConditional.class)).size();
            int nbCtDo=ctMethod.getBody().getElements(new TypeFilter(CtDo.class)).size();
            int nbCtForEach=ctMethod.getBody().getElements(new TypeFilter(CtForEach.class)).size();
            int nbCtIf=ctMethod.getBody().getElements(new TypeFilter(CtIf.class)).size();
            int nbCtThrow=ctMethod.getBody().getElements(new TypeFilter(CtThrow.class)).size();
            int nbCtAnnotation=ctMethod.getBody().getElements(new TypeFilter(CtAnnotation.class)).size();
            int nbCtTry=ctMethod.getBody().getElements(new TypeFilter(CtTry.class)).size();
            int nbCtCatch=ctMethod.getBody().getElements(new TypeFilter(CtCatch.class)).size();
            int nbCtWhile=ctMethod.getBody().getElements(new TypeFilter(CtWhile.class)).size();
            int nbCtComment=ctMethod.getBody().getElements(new TypeFilter(CtComment.class)).size();
            List<CtElement> comments=ctMethod.getBody().getElements(new TypeFilter(CtComment.class));
            for (CtElement elem:comments) {
                nbCommentwithSemicoloone=+elem.toString().split(";").length-1;
            }



            numberOfLogicalLines=numbeOfLinesWithSemicolonnes+nbCtFor-(nbCtFor*2)
                    +nbCtSwitch+nbCtConditional+nbCtDo
                    +nbCtForEach+nbCtIf+nbCtTry+nbCtCatch+nbCtWhile-nbCommentwithSemicoloone+1;
            // + 1 pour le prototype de la methode
            // le nombre de block s'ajoute automatiquement


            return numberOfLogicalLines;

        } catch (NullPointerException npe) {
            return ctMethod.getPosition().getEndLine() - ctMethod.getPosition().getLine();
        }
    }

    private void handleUsedVariables(T ctExecutable, DetectorMethod detectorMethod) {
        List<CtFieldAccess> elements = ctExecutable.getElements(new TypeFilter<CtFieldAccess>(CtFieldAccess.class));
        if (detectorMethod.getDetectorClass().isInnerClass()){
        List<CtTypeAccessImpl> elements2 = ctExecutable.getElements(new TypeFilter<CtTypeAccessImpl>(CtTypeAccessImpl.class));
        //System.out.println("ctExecutable in handleUsedVariables "+ctExecutable.getSimpleName());
        //System.out.println("List<CtFieldAccess> elements "+elements);
        //if (detectorMethod.getDetectorClass().getName().contains("InvalidateUpdateListener")) {

            for (CtTypeAccessImpl elem : elements2) {


                if(elem.getParent().toString().equals("this")){

                 //   System.out.println("element parent this " + elem);
                   String  variableTarget = elem.toString();
                   String  variableName = "this";
                    detectorMethod.getUsedVariablesData().add(new VariableData(variableTarget, variableName));
                }


            }



        }










        String variableTarget = null;
        String variableName;

        CtTypeMember member = ctExecutable instanceof CtTypeMember ? (CtTypeMember) ctExecutable : null;


        searchUsedVariable(detectorMethod, elements, member);
    }

    private void searchUsedVariable(DetectorMethod detectorMethod, List<CtFieldAccess> elements, CtTypeMember member) {
        String variableTarget;
        String variableName;
        for (CtFieldAccess ctFieldAccess : elements) {
            variableUsedInMethod(detectorMethod, member, ctFieldAccess);
        }
    }

    private void variableUsedInMethod(DetectorMethod detectorMethod, CtTypeMember member, CtFieldAccess ctFieldAccess) {
        String variableTarget;
        String variableName;

        if (ctFieldAccess.getTarget() != null && ctFieldAccess.getTarget().getType() != null) {

             if (member != null && ctFieldAccess.getTarget().getType().getDeclaration() == member.getDeclaringType()) {


                variableTarget = ctFieldAccess.getTarget().getType().getQualifiedName();
                variableName = ctFieldAccess.getVariable().getSimpleName();
                detectorMethod.getUsedVariablesData().add(new VariableData(variableTarget, variableName));
            }
            //in case of static variable
            else  if(member != null && ctFieldAccess.getVariable().getDeclaringType()!=null && ctFieldAccess.getVariable().getDeclaringType().getDeclaration()==member.getDeclaringType()) {
                variableTarget = ctFieldAccess.getTarget().toString();
                variableName = ctFieldAccess.getVariable().getSimpleName();
                detectorMethod.getUsedVariablesData().add(new VariableData(variableTarget, variableName));

            }
            else  if(detectorMethod.getDetectorClass().isInnerClass()){

                if (detectorMethod.getDetectorClass().getName().contains("QueryThread")&&(detectorMethod.getName().contains("run()"))){
                    //System.out.println("ct field"+ctFieldAccess);
                }
                //Chercher si la methode utilise des variable de la classe mère


                 CtElement parent=detectorMethod.getDetectorClass().getClasse();

                    // System.out.println("method name "+detectorMethod.getName());
                    // System.out.println("ctFieldAccess.getTarget().getType().getDeclaration() "+ctFieldAccess.getTarget().getType().getDeclaration());


                 while (parent.getParent()!=null){





                       //  System.out.println("parent "+parent.getParent());


                     if(member!=null && ctFieldAccess.getTarget().getType().getDeclaration()!=null &&
                             ctFieldAccess.getTarget().getType().getDeclaration().equals(parent.getParent())){

                         variableTarget = ctFieldAccess.getTarget().getType().getQualifiedName();
                         variableName = ctFieldAccess.getVariable().getSimpleName();
                       //  System.out.println("method name "+detectorMethod.getName());
                        // System.out.println("variable name "+variableName);

                         detectorMethod.getUsedVariablesData().add(new VariableData(variableTarget, variableName));

                         break;
                     }

                     parent=parent.getParent();

                 }


            }
        }
    }

    private void handleInvocations(T ctConstructor, DetectorMethod detectorMethod) {

        //System.out.println("usedVaribles "+usedVaribles);

        String targetName;
        String executable;
        String type = "Unknown";

        // Thanks to corrector we have to use a CtAbstractInvocation
        List<CtAbstractInvocation> invocations = ctConstructor.getElements(new TypeFilter<>(CtAbstractInvocation.class));
        List<CtAbstractInvocation> invocationsPb=null;
        if (detectorMethod.getDetectorClass().isInnerClass()){ invocationsPb=ctConstructor.getElements(new TypeFilter<>(CtAbstractInvocation.class));}

            for (CtAbstractInvocation invocation : invocations) {

            executable = invocation.getExecutable().getSimpleName();
            targetName = getTarget(invocation);
//            //if (detectorMethod.getDetectorClass().getName().contains("QueryThread")&&(detectorMethod.getName().contains("run"))){
//
//                //System.out.println("ctConstructor "+ctConstructor);
//
//               System.out.println("invocation 2 "+invocation );
////                System.out.println("Role in parent "+invocation.getRoleInParent() );
////                System.out.println("executable "+invocation.getExecutable());
////                System.out.println("declaration "+invocation.getExecutable().getDeclaration() );
////                System.out.println("signature "+invocation.getExecutable().getSignature() );
////                System.out.println("invocation to String "+invocation.toString());
////                System.out.println("executable to String  "+invocation.getExecutable().toString() );
////
////
//
//
//            }

            if (invocation.getExecutable().getType() != null) {
                type = invocation.getExecutable().getType().getQualifiedName();

            }

            if (targetName != null) {

                    detectorMethod.getInvocationData().add(new InvocationData(targetName, executable, type));
                    if(invocationsPb!=null) {Boolean remove=invocationsPb.remove(invocation);}
                   // System.out.println("removeinvocation "+remove);


            }
            else {
                if ( detectorMethod.getDetectorClass().isInnerClass()) {


                        List<CtVariableAccess> varibles = ctConstructor.getElements(new TypeFilter<CtVariableAccess>(CtVariableAccess.class));
                        List<CtThisAccessImpl> thisacess = ctConstructor.getElements(new TypeFilter<CtThisAccessImpl>(CtThisAccessImpl.class));

                        //System.out.println(" thisacess "+thisacess);
                       // System.out.println(" invocation "+invocation);
                        for ( CtVariableAccess variableAccess :varibles) {
                            //System.out.println(" variable access " + variableAccess.toString());
                            //System.out.println("variableAccess.getVariable() " + variableAccess.getVariable());
                           // System.out.println("variableAccess.getVariable().type" + variableAccess.getType());

                            try {


                                if (variableAccess.toString().contains(".") && (variableAccess.getType() != null) && (variableAccess.getVariable().getModifiers().contains(ModifierKind.STATIC))) {

                                    if (invocation.toString().startsWith(variableAccess + ".")) {
                                       // System.out.println("nekherjou");
                                        Boolean remove = invocationsPb.remove(invocation);
                                        //System.out.println("removeinvocation static  " + remove);

                                    }
                                } else {
                                    if (invocation.toString().startsWith(variableAccess + ".")) {
                                        //System.out.println("mliha");
                                        Boolean remove = invocationsPb.remove(invocation);
                                        //System.out.println("removeinvocation local   " + remove);
                                    }
                                }

                            } catch(Exception e){

                            }
                        }


                            if (invocation.toString().startsWith("this.")) {




                                int size = invocationsPb.size();

                                boolean remove = invocationsPb.remove(invocation);

                                //System.out.println("removeinvocation this   " + remove + invocation);
                            }




                }




            }

            }
            if (invocationsPb!=null) {
                for (CtAbstractInvocation invocationpb : invocationsPb) {

                    String executable2 = invocationpb.getExecutable().getSimpleName();
                    /*if (detectorMethod.getDetectorClass().getName().contains("InvalidateUpdateListener")){

                   System.out.println("invocation pb " + invocationpb);
                    System.out.println("invocation pb executable " + invocationpb.getExecutable());
                    System.out.println(" method " + detectorMethod.getName());
                    System.out.println("class " + detectorMethod.getDetectorClass().getName());}*/
                    detectorMethod.getInvocationData().add(new InvocationData("inconnu", executable2, "inconnu"));


                }
            }
            }




    private String getTarget(CtAbstractInvocation ctInvocation) {
        try {
            return ctInvocation.getExecutable().getDeclaringType().getQualifiedName();
        } catch (NullPointerException nullPointerException) {
            logger.warn("Could not find qualified name for method call: " + ctInvocation.toString() + " (" + nullPointerException.getMessage() + ")");
        }
        return null;
    }

    private int getComplexity(T ctConstructor) {
        int numberOfTernaries = ctConstructor.getElements(new TypeFilter<CtConditional>(CtConditional.class)).size();
        int numberOfIfs = ctConstructor.getElements(new TypeFilter<CtIf>(CtIf.class)).size();

        int numberOfCases = ctConstructor.getElements(new TypeFilter<CtCase>(CtCase.class)).size();
        int numberOfReturns = ctConstructor.getElements(new TypeFilter<CtReturn>(CtReturn.class)).size();
        int numberOfLoops = ctConstructor.getElements(new TypeFilter<CtLoop>(CtLoop.class)).size();
        int numberOfBinaryOperators = ctConstructor.getElements(new TypeFilter<CtBinaryOperator>(CtBinaryOperator.class) {
            private final List<BinaryOperatorKind> operators = Arrays.asList(BinaryOperatorKind.AND, BinaryOperatorKind.OR);

            @Override
            public boolean matches(CtBinaryOperator element) {
                return super.matches(element) && operators.contains(element.getKind());
            }
        }).size();

        int numberOfCatches = ctConstructor.getElements(new TypeFilter<CtCatch>(CtCatch.class)).size();
        int numberOfThrows = ctConstructor.getElements(new TypeFilter<CtThrow>(CtThrow.class)).size();
        int numberOfBreaks = ctConstructor.getElements(new TypeFilter<CtBreak>(CtBreak.class)).size();
        int numberOfContinues = ctConstructor.getElements(new TypeFilter<CtContinue>(CtContinue.class)).size();
        int complexity=numberOfBreaks + numberOfCases + numberOfCatches + numberOfContinues + numberOfIfs + numberOfLoops +
                numberOfTernaries + numberOfThrows + numberOfBinaryOperators + 1;


        return complexity;
    }
}
