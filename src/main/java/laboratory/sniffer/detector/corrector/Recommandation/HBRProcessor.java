package laboratory.sniffer.detector.corrector.Recommandation;

import spoon.processing.AbstractProcessor;
import spoon.reflect.code.CtComment;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.visitor.filter.TypeFilter;
import utils.CsvReader;
import utils.SaverOfTheFile;

import java.util.HashSet;
import java.util.List;

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
        CtComment comment=getFactory().Core().createComment().setContent("You should never perform long-running operations in the onReceive method (there is a timeout\n of 10 seconds that the system allows before considering the receiver to be blocked and\n a candidate to be killed). for more information please visit\n https://developer.android.com/reference/android/content/BroadcastReceiver").setCommentType(CtComment.CommentType.BLOCK);
        //Remove old comments
        //Remove old comments
        List<CtComment> listOfComments=element.getElements(new TypeFilter(CtComment.class));
        boolean var=false;
        for(CtComment c:listOfComments){

            if(c.getContent().equals(comment.getContent())){
                var=true;
                break;

            }
        }
        if(!var){
            //add the new comment to the method
            element.getBody().insertEnd(comment);
        }

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
