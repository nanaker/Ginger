package laboratory.sniffer.detector.corrector;

import spoon.processing.AbstractProcessor;
import spoon.reflect.code.CtNewClass;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.ModifierKind;
import utils.CsvReader;

import java.util.HashSet;

public class LICProcessor extends AbstractProcessor<CtClass> {

    HashSet<String> lic_classes;

    public LICProcessor(String file)
    {
        System.out.println("Processor LICProcessor Start ... ");
        // Get applications information from the CSV - output
        lic_classes= CsvReader.formatCsv(file,2);

    }


    @Override
    public void process(CtClass element) {
        System.out.println("in LIC process");
        if(element.isAnonymous()){
            //System.out.println("Classe anonyme "+element.getSimpleName());
        }else{
            element.addModifier(ModifierKind.STATIC);
           // System.out.println("body"+element);
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
                    System.out.println("candidate.isAnonymous()");
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
