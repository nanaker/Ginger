package laboratory.sniffer.detector.corrector.Correction;

import spoon.processing.AbstractProcessor;
import spoon.reflect.code.*;
import spoon.reflect.declaration.*;
import spoon.reflect.reference.CtTypeReference;
import spoon.support.reflect.code.CtConstructorCallImpl;
import spoon.support.reflect.code.CtInvocationImpl;
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
            //System.out.println("Classe anonyme getQualifiedName "+element.getQualifiedName());
            //System.out.println("Classe anonyme "+element.getParent().getParent());
            // List<CtElement> ctElements=element.getParent().getElements(new MatchAllFilter<CtElement>());
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
               System.out.println("CtInvocationImpl  ");
              RefactorCtInvocationImpl(element);


            }
            else if((element.getParent().getParent() instanceof CtConstructorCallImpl)){
                System.out.println("CtConstructorCallImpl  ");

               refactorCtConstructorCallImpl(element);

            }else if((element.getParent().getParent() instanceof CtVariable)){
                System.out.println("CtVariable  ");
               refactorCtVariable(element);

            }

        }else if(!element.isAnonymous()){


                System.out.println("element.getParent() instanceof CtClass");
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
        String[] splitName = variableName.split("\\$");
        variableName = splitName[0] + "$" +
                ((CtNewClass) element.getParent()).getType().getQualifiedName() + splitName[1];
        variableName= variableName.substring(variableName.lastIndexOf("$")+1);
        variableName=Character.toLowerCase(variableName.charAt(0)) + variableName.substring(1);//la declaration d'une variable commence toujours par une miniscule

        CtTypeReference ref = getFactory().Core().createTypeReference();//C'est le type de la nouvelle variable
        ref.setSimpleName(getType(element.getParent().toString()));

        ModifierKind[] k=new ModifierKind[3];
        k[0]=ModifierKind.PRIVATE;
        k[1]=ModifierKind.STATIC;
        k[2]=ModifierKind.FINAL;
        CtField variable=getFactory().createCtField(variableName,ref,element.getParent().toString(),k);

        CtClass classeMere=element.getParent().getParent().getParent(CtClass.class);
        classeMere.addFieldAtTop(variable);//on declare la nouvelle variable en haut de la classe

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
        String[] splitName = variableName.split("\\$");
        variableName = splitName[0] + "$" +
                ((CtNewClass) element.getParent()).getType().getQualifiedName() + splitName[1];
        variableName= variableName.substring(variableName.lastIndexOf("$")+1);
        variableName=Character.toLowerCase(variableName.charAt(0)) + variableName.substring(1);//la declaration d'une variable commence toujours par une miniscule


        CtTypeReference ref = getFactory().Core().createTypeReference();//C'est le type de la nouvelle variable
        ref.setSimpleName(getType(element.getParent().toString()));

        ModifierKind[] k=new ModifierKind[3];
        k[0]=ModifierKind.PRIVATE;
        k[1]=ModifierKind.STATIC;
        k[2]=ModifierKind.FINAL;
        CtField variable=getFactory().createCtField(variableName,ref,element.getParent().toString(),k);

        CtClass classeMere=element.getParent().getParent().getParent(CtClass.class);
        classeMere.addFieldAtTop(variable);//on declare la nouvelle variable en haut de la classe

        //On remplace la classe anonyme par l'utilisation de la variable deja declarée
        List<CtExpression> listeOfArguments=((CtInvocationImpl) element.getParent().getParent()).getArguments();
        int argumentPosition=getArgumentPosition(listeOfArguments,element.getParent().toString());
        if(argumentPosition!=-1){
            ((CtInvocationImpl) element.getParent().getParent()).removeArgument(listeOfArguments.get(argumentPosition));
            CtExpression e=getFactory().createCodeSnippetExpression(variableName);
            ((CtInvocationImpl) element.getParent().getParent()).addArgument(e);

        }



        SaverOfTheFile fileSaver=new SaverOfTheFile();

        //this.getEnvironment().setShouldCompile(false);

        fileSaver.reWriteFileInvocationImpl(this,classeMere);
        return 1;
    }






}
