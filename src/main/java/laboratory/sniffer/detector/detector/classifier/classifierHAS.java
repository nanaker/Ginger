package laboratory.sniffer.detector.detector.classifier;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class classifierHAS {
    private String base_path;

    public classifierHAS(String base_path)
    {
        this.base_path=base_path;
    }

    public String exec(){


        String s = null;
        String result = "";

        String commande="./py/ginger_py.exe  predict -b \""+base_path+"\" -c HAS";


        try {



            Process p = Runtime.getRuntime().exec(commande);

            BufferedReader stdInput = new BufferedReader(new
                    InputStreamReader(p.getInputStream()));

            BufferedReader stdError = new BufferedReader(new
                    InputStreamReader(p.getErrorStream()));

            // read the output from the command
           // System.out.println("Here is the standard output of the command:\n");
            while ((s = stdInput.readLine()) != null) {
                result=result+s;
                result=result+"\n";
                //System.out.println(s);
            }

            // read any errors from the attempted command
            //System.out.println("Here is the standard error of the command (if any):\n");
            while ((s = stdError.readLine()) != null) {
                result=result+s;
                result=result+"\n";
                //System.out.println(s);
            }



            //System.exit(0);
        }
        catch (IOException e) {
            //System.out.println("exception happened - here's what I know: ");
            e.printStackTrace();
            System.exit(-1);
        }
        return result;
    }


}

