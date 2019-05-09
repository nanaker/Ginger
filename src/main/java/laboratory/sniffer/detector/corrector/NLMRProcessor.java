package laboratory.sniffer.detector.corrector;

import spoon.processing.AbstractProcessor;
import spoon.reflect.code.CtBlock;
import spoon.reflect.declaration.*;
import spoon.reflect.factory.TypeFactory;
import spoon.reflect.reference.CtTypeReference;
import spoon.reflect.visitor.filter.TypeFilter;
import spoon.support.reflect.declaration.CtAnnotationImpl;
import utils.CsvReader;
import utils.SaverOfTheFile;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class NLMRProcessor extends AbstractProcessor<CtClass> {

    HashSet<String> nlmr_classes;

    public NLMRProcessor(String file)
    {
        System.out.println("Processor NLMRProcessor Start ... ");
        // Get applications information from the CSV - output
        nlmr_classes= CsvReader.formatCsv_NLMR(file);

        //System.out.println("method to static "+meth_toStatic);



    }

    @Override
    public boolean isToBeProcessed(CtClass candidate) {


        Boolean chek=checkValidToCsv(candidate);



        return checkValidToCsv(candidate);
    }

    public void process(CtClass element) {

        System.out.println("inProcess "+element.getSimpleName());
        CtMethod onLowMemory=getFactory().Core().createMethod();
        onLowMemory.setSimpleName("onLowMemory");
        onLowMemory.addModifier(ModifierKind.PUBLIC);
        TypeFactory typeFactory = new TypeFactory();
        onLowMemory.setType(typeFactory.voidPrimitiveType());

        CtBlock body = getFactory().Core().createBlock();
        body.setParent(onLowMemory);
        onLowMemory.setBody(body);

        CtTypeReference ref = getFactory().Core().createTypeReference();
        ref.setSimpleName("Override");

        final CtAnnotation<Annotation> annotation = getFactory().Code().createAnnotation(ref);
        onLowMemory.addAnnotation(annotation);




        System.out.println("this is the method: "+onLowMemory);
        System.out.println("this is the method body"+onLowMemory.getBody());

        element.addMethod(onLowMemory);



        SaverOfTheFile fileSaver=new SaverOfTheFile();
        fileSaver.reWriteFile(this,element);


    }

    private boolean checkValidToCsv(CtClass candidate){
        String class_file = candidate.getPosition().getFile().getName().split("\\.")[0];



        for(String occurence :nlmr_classes ){
            String csvClassName = occurence.substring(occurence.lastIndexOf(".")+1);
            if(csvClassName.contains(class_file)){
                return true;
            }
        }

        return false;
    }





}
