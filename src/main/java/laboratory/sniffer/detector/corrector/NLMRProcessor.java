package laboratory.sniffer.detector.corrector;

import spoon.processing.AbstractProcessor;
import spoon.reflect.code.*;
import spoon.reflect.cu.SourcePosition;
import spoon.reflect.declaration.*;
import spoon.reflect.factory.Factory;
import spoon.reflect.factory.TypeFactory;
import spoon.reflect.path.CtPath;
import spoon.reflect.path.CtRole;
import spoon.reflect.reference.CtTypeReference;
import spoon.reflect.visitor.CtVisitor;
import spoon.reflect.visitor.Filter;
import spoon.reflect.visitor.chain.CtConsumableFunction;
import spoon.reflect.visitor.chain.CtFunction;
import spoon.reflect.visitor.chain.CtQuery;
import spoon.reflect.visitor.filter.TypeFilter;
import spoon.support.reflect.code.CtCommentImpl;
import spoon.support.reflect.declaration.CtAnnotationImpl;
import utils.CsvReader;
import utils.SaverOfTheFile;

import java.lang.annotation.Annotation;
import java.util.*;

public class NLMRProcessor extends AbstractProcessor<CtClass> {

    HashSet<String> nlmr_classes;

    public NLMRProcessor(String file)
    {
        System.out.println("Processor NLMRProcessor Start ... ");
        // Get applications information from the CSV - output
        nlmr_classes= CsvReader.formatCsv_NLMR(file);
        System.out.println("nlmr_classes ="+nlmr_classes);

        //System.out.println("method to static "+meth_toStatic);



    }

    @Override
    public boolean isToBeProcessed(CtClass candidate) {


        Boolean chek=checkValidToCsv(candidate);



        return checkValidToCsv(candidate);
    }

    public void process(CtClass element) {

        System.out.println("inProcess CtClass"+element.getSimpleName());
        CtMethod onLowMemory=getFactory().Core().createMethod();
        onLowMemory.setSimpleName("onLowMemory");
        onLowMemory.addModifier(ModifierKind.PUBLIC);
        TypeFactory typeFactory = new TypeFactory();
        onLowMemory.setType(typeFactory.voidPrimitiveType());

        CtBlock body = getFactory().Core().createBlock();
        body.setParent(onLowMemory);
        onLowMemory.setBody(body);
        final CtCodeSnippetStatement statement = getFactory().Code().createCodeSnippetStatement("super.onLowMemory()");
        body.addStatement(statement);

        //final CtCodeSnippetStatement comment = getFactory().Code().createCodeSnippetStatement("/* You should implement this method to release any caches or other unnecessary resources you may\n be holding on to. The system will perform a garbage collection for you after returning from this\n method.*/");
        //body.addStatement(comment);



        CtComment comment=getFactory().Core().createComment().setContent("You should implement this method to release any caches or other unnecessary resources you may\n be holding on to. The system will perform a garbage collection for you after returning from this\n method.").setCommentType(CtComment.CommentType.BLOCK);



        //CtBlock b=getFactory().Code().createCtBlock(comment2);
        body.insertEnd(comment);


        CtTypeReference ref = getFactory().Core().createTypeReference();
        ref.setSimpleName("Override");

        final CtAnnotation<Annotation> annotation = getFactory().Code().createAnnotation(ref);
        onLowMemory.addAnnotation(annotation);




        System.out.println("this is the element: "+isToBeProcessed(element)+"###"+element.getSimpleName()+"###"+element.getQualifiedName());
        //System.out.println("this is the method: "+onLowMemory);


        element.addMethod(onLowMemory);


        System.out.println("isToBeProcessed "+isToBeProcessed(element));
        SaverOfTheFile fileSaver=new SaverOfTheFile();
        fileSaver.reWriteFile(this,element);


    }

    private boolean checkValidToCsv(CtClass candidate){

            for (String occurence : nlmr_classes) {

                if (occurence.equals(candidate.getQualifiedName())) {
                    return true;
                }
            }

        return false;
    }





}
