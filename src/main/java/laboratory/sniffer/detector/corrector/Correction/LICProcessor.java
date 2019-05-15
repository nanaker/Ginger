package laboratory.sniffer.detector.corrector.Correction;

import spoon.processing.AbstractProcessor;
import spoon.reflect.code.*;
import spoon.reflect.declaration.*;
import spoon.reflect.reference.CtTypeReference;
import spoon.support.reflect.code.CtConstructorCallImpl;
import spoon.support.reflect.code.CtInvocationImpl;
import utils.ClassUtil;
import utils.CsvReader;
import utils.SaverOfTheFile;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LICProcessor extends AbstractProcessor<CtClass> {

    HashSet<String> lic_classes;

    public LICProcessor(String file)
    {
        System.out.println("Processor LICProcessor Start ... ");
        // Get applications information from the CSV - output
        lic_classes= CsvReader.formatCsv(file,7);

    }


    @Override
    public void process(CtClass element) {


        if(element.isAnonymous()){

            if(element.getParent().getParent() instanceof CtInvocationImpl){

              RefactorCtInvocationImpl(element);


            }
            else if((element.getParent().getParent() instanceof CtConstructorCallImpl)){


               refactorCtConstructorCallImpl(element);

            }else if((element.getParent().getParent() instanceof CtVariable)){

               refactorCtVariable(element);

            }

        }else if(!element.isAnonymous()){



                element.addModifier(ModifierKind.STATIC);
                SaverOfTheFile fileSaver=new SaverOfTheFile();
                fileSaver.reWriteFile(this,(element.getParent(CtClass.class)));



        }

    }


    @Override
    public boolean isToBeProcessed(CtClass candidate) {
        return checkValidToCsv(candidate);
    }




    private boolean checkValidToCsv(CtClass candidate){

        for (String occurence : lic_classes) {

            if(candidate.isAnonymous()){
                String qualifiedName=candidate.getQualifiedName();
                String[] splitName = qualifiedName.split("\\$");
                qualifiedName = splitName[0] + "$" +
                        ((CtNewClass) candidate.getParent()).getType().getQualifiedName() + splitName[1];
                if (occurence.contains(qualifiedName)) {
                    return true;
                }
            }else{

                if (occurence.equals(candidate.getQualifiedName())) {
                    return true;
                }
            }

        }

        return false;
    }






    public CtClass getClassMere(CtClass element){
        String theClassName=element.getQualifiedName();
        String[] splitName = theClassName.split("\\$");

        theClassName= splitName[0];
        //System.out.println("hi here "+theClassName);
        CtClass classeMere=null;
        CtClass saveElement=element;
        boolean sortir=true;
        if(saveElement!=null){
            while (sortir){
                if(saveElement!=null){
                    //System.out.println("hi hi here "+saveElement.getQualifiedName());
                    if(saveElement.getQualifiedName().equals(theClassName)){
                        sortir=false;
                        classeMere=saveElement;
                    }else{
                        saveElement=saveElement.getParent(CtClass.class);
                        //System.out.println("the element= "+saveElement);
                    }


                }
            }

        }

        return classeMere;
    }

    public int refactorCtConstructorCallImpl(CtClass element){

        /*Pour refactoiser un LIC avec une classe anonyme, on extrait cette classe dans une variable*/
        String variableName=element.getQualifiedName();

        CtClass classeMere= ClassUtil.createVariable(this, element);
        //On remplace la classe anonyme par l'utilisation de la variable deja declarée
        List<CtExpression> listeOfArguments=((CtConstructorCallImpl) element.getParent().getParent()).getArguments();
        int argumentPosition=getArgumentPosition(listeOfArguments,element.getParent().toString());
        if(argumentPosition!=-1){
            ((CtConstructorCallImpl) element.getParent().getParent()).removeArgument(listeOfArguments.get(argumentPosition));
            CtExpression e=getFactory().createCodeSnippetExpression(variableName);
            ((CtConstructorCallImpl) element.getParent().getParent()).addArgument(e);

        }
        SaverOfTheFile fileSaver=new SaverOfTheFile();
        fileSaver.reWriteFile(this,classeMere);
        return 1;
    }


    public int refactorCtVariable(CtClass element){

        CtVariable variableDeclaration=(CtVariable)element.getParent().getParent();
        variableDeclaration.addModifier(ModifierKind.STATIC);

        CtClass classeMere=element.getParent().getParent().getParent(CtClass.class);
        SaverOfTheFile fileSaver=new SaverOfTheFile();
        fileSaver.reWriteFile(this,classeMere);
        return 1;
    }


    public int RefactorCtInvocationImpl(CtClass element){
        /*Pour refactoiser un LIC avec une classe anonyme, on extrait cette classe dans une variable*/
        String variableName=element.getQualifiedName();
        CtClass classeMere= ClassUtil.createVariable(this, element);

        //On remplace la classe anonyme par l'utilisation de la variable deja declarée
        List<CtExpression> listeOfArguments=((CtInvocationImpl) element.getParent().getParent()).getArguments();
        int argumentPosition=getArgumentPosition(listeOfArguments,element.getParent().toString());
        if(argumentPosition!=-1){
            ((CtInvocationImpl) element.getParent().getParent()).removeArgument(listeOfArguments.get(argumentPosition));
            CtExpression e=getFactory().createCodeSnippetExpression(variableName);
            ((CtInvocationImpl) element.getParent().getParent()).addArgument(e);

        }

        SaverOfTheFile fileSaver=new SaverOfTheFile();
        fileSaver.reWriteFileInvocationImpl(this,classeMere);
        return 1;
    }


    public  int getArgumentPosition(List<CtExpression> listeOfArguments, String theArgument ){
        for(int i=0;i<listeOfArguments.size();i++){
            if(listeOfArguments.get(i).toString().equals(theArgument)){
                return i;
            }
        }
        return -1;
    }




}
