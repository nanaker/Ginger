package laboratory.sniffer.detector.corrector;

import spoon.processing.AbstractProcessor;
import spoon.reflect.code.*;
import spoon.reflect.declaration.*;
import spoon.support.reflect.code.CtConstructorCallImpl;
import spoon.support.reflect.code.CtExpressionImpl;
import spoon.support.reflect.code.CtInvocationImpl;
import spoon.support.reflect.code.CtNewClassImpl;
import spoon.support.reflect.declaration.CtClassImpl;
import spoon.support.reflect.declaration.CtFieldImpl;
import utils.CsvReader;
import utils.SaverOfTheFile;

import java.util.HashSet;
import java.util.List;

public class LICProcessor extends AbstractProcessor<CtClass> {

    HashSet<String> lic_classes;

    public LICProcessor(String file)
    {
        System.out.println("Processor LICProcessor Start ... ");
        // Get applications information from the CSV - output
        lic_classes= CsvReader.formatCsv(file,4);

    }


    @Override
    public void process(CtClass element) {

        System.out.println("in LIC process");
        if(element.isAnonymous()){
            System.out.println("Classe anonyme getQualifiedName "+element.getQualifiedName());
            System.out.println("Classe anonyme "+element.getParent().getParent());
            List<CtElement> ctElements=element.getParent().getParent().getElements(new MatchAllFilter<CtElement>());
           /* for(CtElement item:ctElements){
                System.out.println("    ELEMENT: " + item.getClass().getSimpleName() + " item: " + item);
            }*/

           System.out.println("getClass "+element.getParent().getParent().getClass().getSimpleName());
            /*if(element.getParent().getParent() instanceof CtAnonymousExecutable){
                System.out.println("CtAnonymousExecutable");
            }else if(element.getParent().getParent() instanceof CtMethod){
                System.out.println("CtMethod");
            }else if(element.getParent().getParent() instanceof CtClass){
                System.out.println("CtClass");
            } else if(element.getParent().getParent() instanceof CtVariable){
                System.out.println("CtVariable");
            }else if(element.getParent().getParent() instanceof CtFieldImpl){
                System.out.println("CtFieldImpl");
            }
            else if(element.getParent().getParent() instanceof CtInvocationImpl){
                System.out.println("CtInvocationImpl");
            }
            else if(element.getParent().getParent() instanceof CtInvocation){
                System.out.println("CtInvocation");
            }
            else if(element.getParent().getParent() instanceof CtConstructorCallImpl){
                System.out.println("CtConstructorCallImpl");
            }*/
            if((element.getParent().getParent() instanceof CtConstructorCallImpl)||(element.getParent().getParent() instanceof CtInvocationImpl)){
                //CtField variable=getFactory().createField();
                //variable.setSimpleName("variableTest");
                //CtExpression expression=getFactory().createFieldRead();
                CtType type=null;
                CtCodeSnippetStatement getterStatement = getFactory().Code().createCodeSnippetStatement("variableName= " + element.getParent());

                //variable.setAssignment(expression);
               System.out.println("hi here "+getterStatement);
            }

        }else{
            element.addModifier(ModifierKind.STATIC);
            SaverOfTheFile fileSaver=new SaverOfTheFile();
            fileSaver.reWriteFile(this,(element.getParent(CtClass.class)));

        }

    }


    @Override
    public boolean isToBeProcessed(CtClass candidate) {
        /*System.out.println("isToBeProcessed "+checkValidToCsv(candidate));
        if(candidate.isAnonymous()){
            System.out.println("classe anonyme "+candidate);
        }*/
        return checkValidToCsv(candidate);
    }




    private boolean checkValidToCsv(CtClass candidate){

        for (String occurence : lic_classes) {
            //System.out.println("occurence "+occurence);
            //System.out.println("candidate.getQualifiedName() "+candidate.getQualifiedName());
            if(candidate.isAnonymous()){
                /*System.out.println("candidate.isAnonymous() 1");
                System.out.println("occurence "+occurence);
                System.out.println("candidate "+candidate.getSimpleName());*/
                String qualifiedName=candidate.getQualifiedName();
                String[] splitName = qualifiedName.split("\\$");
                qualifiedName = splitName[0] + "$" +
                        ((CtNewClass) candidate.getParent()).getType().getQualifiedName() + splitName[1];
                if (occurence.contains(qualifiedName)) {
                    //System.out.println("candidate.isAnonymous()");
                    return true;
                }
            }else{
                //System.out.println("candidate not isAnonymous()");
                if (occurence.equals(candidate.getQualifiedName())) {
                    return true;
                }
            }

        }

        return false;
    }
}
