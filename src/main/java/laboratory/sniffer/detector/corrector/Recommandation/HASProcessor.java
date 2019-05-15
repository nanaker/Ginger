package laboratory.sniffer.detector.corrector.Recommandation;

import spoon.processing.AbstractProcessor;
import spoon.reflect.code.CtComment;
import spoon.reflect.declaration.CtMethod;
import utils.CsvReader;
import utils.SaverOfTheFile;

import java.util.HashSet;

public class HASProcessor extends AbstractProcessor<CtMethod> {


    HashSet<String> has_methods;

    public HASProcessor(String file)
    {
        System.out.println("Processor HASProcessor Start ... ");
        // Get applications information from the CSV - output
        has_methods= CsvReader.formatCsv(file,3);

    }

    @Override
    public void process(CtMethod element) {
        CtComment comment=getFactory().Core().createComment().setContent("AsyncTasks should ideally be used for short operations (a few seconds at the most.)\n If you need to keep threads running for long periods of time, it is highly recommended\n you use the various APIs provided by the java.util.concurrent package such as Executor,\n ThreadPoolExecutor and FutureTask.\n For more information please visit https://developer.android.com/reference/android/os/AsyncTask.html").setCommentType(CtComment.CommentType.BLOCK);
        //element.addComment(comment);
        element.getBody().insertEnd(comment);
        System.out.println("in process HAS "+element);
        SaverOfTheFile fileSaver=new SaverOfTheFile();
        fileSaver.reWriteFile(this,element);
    }

    @Override
    public boolean isToBeProcessed(CtMethod candidate) {
        return checkValidToCsv(candidate);
    }

    private boolean checkValidToCsv(CtMethod candidate){
        String class_file = candidate.getPosition().getFile().getName().split("\\.")[0];

        for(String occurence : has_methods){
            String csvClassName = occurence.substring(occurence.lastIndexOf(".")+1);
            if(csvClassName.contains(class_file) &&
                    occurence.split("#")[0].equals(candidate.getSimpleName().split("\\(")[0])){
                return true;
            }
        }
        return false;
    }

}
