package laboratory.sniffer.detector.corrector;

import spoon.processing.AbstractProcessor;
import spoon.reflect.code.*;
import spoon.reflect.declaration.*;
import spoon.reflect.reference.CtTypeReference;
import spoon.support.reflect.code.CtConstructorCallImpl;
import spoon.support.reflect.code.CtExpressionImpl;
import spoon.support.reflect.code.CtInvocationImpl;
import spoon.support.reflect.code.CtNewClassImpl;
import spoon.support.reflect.declaration.CtClassImpl;
import spoon.support.reflect.declaration.CtFieldImpl;
import utils.CsvReader;
import utils.SaverOfTheFile;

import java.beans.Visibility;
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
        lic_classes= CsvReader.formatCsv(file,4);

    }


    @Override
    public void process(CtClass element) {

        System.out.println("in LIC process");
        if(element.isAnonymous()){
            System.out.println("Classe anonyme getQualifiedName "+element.getQualifiedName());
            System.out.println("Classe anonyme "+element.getParent().getParent());
            List<CtElement> ctElements=element.getParent().getElements(new MatchAllFilter<CtElement>());
           /*for(CtElement item:ctElements){
                System.out.println("    ELEMENT: " + item.getClass().getSimpleName() + " item: " + item);
            }*/

           //System.out.println("getClass "+element.getParent().getParent().getClass().getSimpleName());
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

            if(element.getParent().getParent() instanceof CtInvocationImpl){
                System.out.println("CtInvocationImpl*** "+element.getParent().getParent());
            }
            if((element.getParent().getParent() instanceof CtConstructorCallImpl)){

                System.out.println("CtConstructorCallImpl*** "+element.getParent().getParent());
                String variableName=element.getQualifiedName();
                String[] splitName = variableName.split("\\$");
                variableName = splitName[0] + "$" +
                        ((CtNewClass) element.getParent()).getType().getQualifiedName() + splitName[1];
                variableName= variableName.substring(variableName.lastIndexOf("$")+1);
                variableName=Character.toLowerCase(variableName.charAt(0)) + variableName.substring(1);

                CtTypeReference ref = getFactory().Core().createTypeReference();
                ref.setSimpleName(getType(element.getParent().toString()));

                ModifierKind[] k=new ModifierKind[3];
                k[0]=ModifierKind.PRIVATE;
                k[1]=ModifierKind.STATIC;
                k[2]=ModifierKind.FINAL;
                CtField variable=getFactory().createCtField(variableName,ref,element.getParent().toString(),k);

                CtClass classeMere=element.getParent().getParent().getParent(CtClass.class);
                classeMere.addFieldAtTop(variable);



                List<CtExpression> listeOfArguments=((CtConstructorCallImpl) element.getParent().getParent()).getArguments();
                System.out.println("liste des arg: "+listeOfArguments);
               int argumentPosition=getArgumentPosition(listeOfArguments,element.getParent().toString());
               if(argumentPosition!=-1){
                   ((CtConstructorCallImpl) element.getParent().getParent()).removeArgument(listeOfArguments.get(argumentPosition));
                   CtExpression e=getFactory().createCodeSnippetExpression(variableName);
                   ((CtConstructorCallImpl) element.getParent().getParent()).addArgument(e);

               }


                System.out.println("hi here "+classeMere);
                SaverOfTheFile fileSaver=new SaverOfTheFile();
                fileSaver.reWriteFile(this,classeMere);

            }

        }else{
            element.addModifier(ModifierKind.STATIC);
            SaverOfTheFile fileSaver=new SaverOfTheFile();
            //fileSaver.reWriteFile(this,(element.getParent(CtClass.class)));

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
                //System.out.println("candidate not isAnonymous()");
                if (occurence.equals(candidate.getQualifiedName())) {
                    return true;
                }
            }

        }

        return false;
    }

    public String getType(String text){
        String pattern1 = "new ";
        String pattern2 = "()";


        Pattern p = Pattern.compile(Pattern.quote(pattern1) + "(.*?)" + Pattern.quote(pattern2));
        Matcher m = p.matcher(text);
        if (m.find()) {
            System.out.println(m.group(1));
            return m.group(1);
        }

        return null;
    }


    public int getArgumentPosition(List<CtExpression> listeOfArguments, String theArgument ){
        for(int i=0;i<listeOfArguments.size();i++){
            if(listeOfArguments.get(i).toString().equals(theArgument)){
                return i;
            }
        }
        return -1;
    }
}
