package laboratory.sniffer.detector.corrector.Recommandation;

import spoon.processing.AbstractProcessor;
import spoon.reflect.code.CtComment;
import spoon.reflect.declaration.CtMethod;
import utils.CsvReader;
import utils.SaverOfTheFile;

import java.util.HashSet;

public class HBRProcessor extends AbstractProcessor<CtMethod> {


    HashSet<String> hbr_methods;

    public HBRProcessor(String file)
    {
        System.out.println("Processor HBRProcessor Start ... ");
        // Get applications information from the CSV - output
        hbr_methods= CsvReader.formatCsv(file,3);

    }

    @Override
    public void process(CtMethod element) {
        CtComment comment=getFactory().Core().createComment().setContent("Commentaire de recommandation").setCommentType(CtComment.CommentType.BLOCK);
        //element.addComment(comment);
        element.getBody().insertEnd(comment);
        System.out.println("in process HBR "+element);
        SaverOfTheFile fileSaver=new SaverOfTheFile();
        fileSaver.reWriteFile(this,element);
    }

    @Override
    public boolean isToBeProcessed(CtMethod candidate) {
        return checkValidToCsv(candidate);
    }

    private boolean checkValidToCsv(CtMethod candidate){
        String class_file = candidate.getPosition().getFile().getName().split("\\.")[0];

        for(String occurence : hbr_methods){
            String csvClassName = occurence.substring(occurence.lastIndexOf(".")+1);
            if(csvClassName.contains(class_file) &&
                    occurence.split("#")[0].equals(candidate.getSimpleName().split("\\(")[0])){
                return true;
            }
        }
        return false;
    }

}
