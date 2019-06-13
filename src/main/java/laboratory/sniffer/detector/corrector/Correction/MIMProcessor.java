package laboratory.sniffer.detector.corrector.Correction;

import spoon.processing.AbstractProcessor;
import spoon.reflect.declaration.CtAnnotation;

import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.ModifierKind;
import utils.CsvReader;
import utils.SaverOfTheFile;

import java.util.HashSet;
import java.util.concurrent.TimeUnit;


public class MIMProcessor extends AbstractProcessor<CtMethod> {

    HashSet<String> meth_toStatic;

    public MIMProcessor(String file)
    {
        System.out.println("Processor MIMProcessor Start ... ");

        // Get applications information from the CSV - output
        meth_toStatic = CsvReader.formatCsv(file,8);

        //System.out.println("method to static "+meth_toStatic);



    }

    @Override
    public boolean isToBeProcessed(CtMethod candidate) {


        Boolean chek=checkValidToCsv(candidate) && checkAnnotation(candidate);



        return checkValidToCsv(candidate) && checkAnnotation(candidate);
    }


    public void process(CtMethod element) {

        element.addModifier(ModifierKind.STATIC);
        SaverOfTheFile fileSaver=new SaverOfTheFile();
        fileSaver.reWriteFile(this,element);



    }


    private boolean checkValidToCsv(CtMethod candidate){
            String class_file = candidate.getPosition().getFile().getName().split("\\.")[0];
            for(String occurence : meth_toStatic){
                String csvClassName = occurence.substring(occurence.lastIndexOf(".")+1);
                if(csvClassName.contains(class_file) &&
                        occurence.split("#")[0].equals(candidate.getSimpleName().split("\\(")[0])){
                    return true;
                }
            }


        return false;
    }


    private boolean checkAnnotation(CtMethod candidate){
        for(CtAnnotation annotation : candidate.getAnnotations()){
            if(annotation.toString().trim().matches("(.*)@Override(.*)")){
                return false;
            }
        }
        return true;
    }


}
