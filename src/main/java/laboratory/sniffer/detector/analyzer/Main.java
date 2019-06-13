package laboratory.sniffer.detector.analyzer;

import laboratory.sniffer.detector.corrector.Correction.LICProcessor;
import laboratory.sniffer.detector.corrector.Correction.MIMProcessor;
import laboratory.sniffer.detector.corrector.Correction.NLMRProcessor;
import laboratory.sniffer.detector.corrector.Recommandation.HASProcessor;
import laboratory.sniffer.detector.corrector.Recommandation.HBRProcessor;
import laboratory.sniffer.detector.detector.classifier.*;
import laboratory.sniffer.detector.entities.DetectorClass;
import laboratory.sniffer.detector.metrics.MetricsCalculator;

import laboratory.sniffer.detector.entities.DetectorApp;
import laboratory.sniffer.detector.entities.DetectorLibrary;
import laboratory.sniffer.detector.neo4j.*;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spoon.Launcher;
import spoon.reflect.declaration.CtType;
import utils.CsvReader;

import static utils.CsvReader.readAllDataAtOnce;


public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class.getName());

    public static void main(String[] args) {
        //testRun();



        ArgumentParser parser = ArgumentParsers.newArgumentParser("ginger");
        Subparsers subparsers = parser.addSubparsers().dest("sub_command");
        Subparser analyseParser = subparsers.addParser("detect").help("Analyse and detect code smells");
        analyseParser.addArgument("-f","--folder").required(true).help("Path of the code source folder /src");
        analyseParser.addArgument("-c","--codeSmell").required(true).help("Code smell to detect MIM,LIC,NLMR,HBR,HAS or ALL");


        Subparser correctorParser = subparsers.addParser("correct").help("Correct the code smells");
        correctorParser.addArgument("-c","--codeSmell").required(true).help("Code smell to correct MIM,LIC,NLMR,HBR,HAS or ALL");


        Subparser allParser = subparsers.addParser("detect&correct").help("Detect and correct all code smells ");
        allParser.addArgument("-f","--folder").required(true).help("Path of the code source folder /src");
        allParser.addArgument("-c","--codeSmell").required(true).help("Code smell to correct MIM,LIC,NLMR,HBR,HAS or ALL");

        try {
            Namespace res = parser.parseArgs(args);
            if (res.getString("sub_command").equals("detect")) {
                // Analyse de l'application
                runAnalysis(res);
                queryMode(res);

                // Detection des défauts de code
                detection(res);


                
            } else if (res.getString("sub_command").equals("correct")) {
                //Correction des défauts de code
                runRefactor(res);
            }
            else if (res.getString("sub_command").equals("detect&correct")) {


                // Analyse de l'application
                runAnalysis(res);
                queryMode(res);

                // Detection des défauts de code
                detection(res);

                //Correction des défauts de code
                runRefactor(res);
            }
        } catch (ArgumentParserException e) {
            analyseParser.handleError(e);
            allParser.handleError(e);
            correctorParser.handleError(e);
        } catch (Exception e) {
            e.printStackTrace();
        }



    }

    private static void runRefactor(Namespace arg) {

        System.out.println("Refactoring ...  " );


        final String MIM = "result/classification_result_MIM";
        final String NLMR = "result/classification_result_NLMR";
        final String LIC = "result/classification_result_LIC";
        final String HBR = "result/classification_result_HBR";
        final String HAS = "result/classification_result_HAS";




        HashSet<String> classPath = new HashSet<>();
        List<String[]> abs_Path_classes=readAllDataAtOnce("classes_path/classes.csv");
        for(String[] element:abs_Path_classes)
        {


            String absPath = element[0];

            classPath.add(absPath);

        }

        String request = arg.get("codeSmell");
       // System.out.println(" code smell "+request);

        String result="";
        Launcher run=new Launcher();
        switch (request) {
            case "LIC":
                run=new Launcher();
                run.getEnvironment().setNoClasspath(true);
                run.getEnvironment().setShouldCompile(false);
                run.setOutputFilter();
                run.addProcessor(new LICProcessor(LIC));
                for (String e : classPath)
                {
                    //System.out.println("in main "+e);
                    run.addInputResource(e);


                }
                run.run();
                break;
            case "MIM":

                run=new Launcher();
                run.getEnvironment().setNoClasspath(true);
                run.getEnvironment().setShouldCompile(false);
                run.setOutputFilter();
                run.addProcessor(new MIMProcessor(MIM));
                for (String e : classPath)
                {
                    run.addInputResource(e);


                }
                run.run();
                break;
            case "NLMR":
                run=new Launcher();
                run.getEnvironment().setNoClasspath(true);
                run.getEnvironment().setShouldCompile(false);
                run.setOutputFilter();
                run.addProcessor(new NLMRProcessor(NLMR));
                for (String e : classPath)
                {
                    run.addInputResource(e);


                }
                run.run();
                break;
            case "HAS":
                run=new Launcher();
                run.getEnvironment().setNoClasspath(true);
                run.getEnvironment().setShouldCompile(false);
                run.setOutputFilter();
                run.addProcessor(new HASProcessor(HAS));
                for (String e : classPath)
                {
                    run.addInputResource(e);

                }
                run.run();
                break;
            case "HBR":
                run=new Launcher();
                run.getEnvironment().setNoClasspath(true);
                run.getEnvironment().setShouldCompile(false);
                run.setOutputFilter();
                run.addProcessor(new HBRProcessor(HBR));
                for (String e : classPath)
                {
                    run.addInputResource(e);


                }
                run.run();
                break;
            case "ALL":
                //Process now
                Runnable Rmim = new Runnable() {

                    @Override
                    public void run() {

                        Launcher run=new Launcher();
                        run.getEnvironment().setNoClasspath(true);
                        run.getEnvironment().setShouldCompile(false);
                        run.setOutputFilter();
                        run.addProcessor(new MIMProcessor(MIM));
                        for (String e : classPath)
                        {
                            run.addInputResource(e);


                        }
                        run.run();

                    }
                };

                Runnable Rnlmr = new Runnable() {

                    @Override
                    public void run() {

                        Launcher run=new Launcher();
                        run.getEnvironment().setNoClasspath(true);
                        run.getEnvironment().setShouldCompile(false);
                        run.setOutputFilter();
                        run.addProcessor(new NLMRProcessor(NLMR));
                        for (String e : classPath)
                        {
                            run.addInputResource(e);


                        }
                        run.run();

                    }
                };

                Runnable Rlic = new Runnable() {

                    @Override
                    public void run() {

                        Launcher run=new Launcher();
                        run.getEnvironment().setNoClasspath(true);
                        run.getEnvironment().setShouldCompile(false);
                        run.setOutputFilter();
                        run.addProcessor(new LICProcessor(LIC));
                        for (String e : classPath)
                        {
                            //System.out.println("in main "+e);
                            run.addInputResource(e);


                        }
                        run.run();

                    }
                };
                Runnable Rhbr = new Runnable() {

                    @Override
                    public void run() {

                        Launcher run=new Launcher();
                        run.getEnvironment().setNoClasspath(true);
                        run.getEnvironment().setShouldCompile(false);
                        run.setOutputFilter();
                        run.addProcessor(new HBRProcessor(HBR));
                        for (String e : classPath)
                        {
                            run.addInputResource(e);


                        }
                        run.run();

                    }
                };


                Runnable Rhas = new Runnable() {

                    @Override
                    public void run() {

                        Launcher run=new Launcher();
                        run.getEnvironment().setNoClasspath(true);
                        run.getEnvironment().setShouldCompile(false);
                        run.setOutputFilter();
                        run.addProcessor(new HASProcessor(HAS));
                        for (String e : classPath)
                        {
                            run.addInputResource(e);

                        }
                        run.run();

                    }
                };
                try {
                    ExecutorService executor = Executors.newSingleThreadExecutor();

                    executor.submit(Rmim);
                    executor.submit(Rnlmr);
                    executor.submit(Rlic);
                    executor.submit(Rhbr);
                    executor.submit(Rhas);

                    executor.shutdown();

                    executor.awaitTermination(15, TimeUnit.SECONDS);
                } catch (InterruptedException e) {

                    e.printStackTrace();
                }


                break;
        }








        logger.info("fin refactor");
        System.out.println(" Refactor done ");

    }


    private static void detection(Namespace arg) {

        System.out.println("Detecting code smells...");
        String base_path = FileSystems.getDefault().getPath("").normalize().toAbsolutePath().toString();
        String request = arg.get("codeSmell");
       // System.out.println(" code smell "+request);

        String result="";
        switch (request) {
            case "LIC":
                classifierLIC classifierLic=new classifierLIC(base_path);
                result=classifierLic.exec();
                System.out.println(result);
                break;
            case "MIM":

                classifierMIM classifierMim=new classifierMIM(base_path);
                result=classifierMim.exec();
                System.out.println(result);
                break;
            case "NLMR":
                classifierNLMR classifierNlmr=new classifierNLMR(base_path);
                result=classifierNlmr.exec();
                System.out.println(result);
                break;
            case "HAS":
                classifierHAS classifierHas=new classifierHAS(base_path);
                result=classifierHas.exec();
                System.out.println(result);
                   break;
            case "HBR":
                classifierHBR classifierHbr=new classifierHBR(base_path);
                result=classifierHbr.exec();
                System.out.println(result);
                 break;
            case "ALL":
                classifier classifier=new classifier(base_path);
                result=classifier.exec();
                System.out.println(result);

                break;
        }


        System.out.println("Done");
    }


    public static void runAnalysis(Namespace arg) throws Exception {


        deteleContenetOfDirectory("db");

        logger.info("Collecting metrics");
        System.out.println("Collecting metrics...");
        String path = arg.getString("folder");
       // System.out.println(" path "+path);
        path = new File(path).getAbsolutePath();
        String name = "MyApplication";


        MainProcessor mainProcessor = new MainProcessor(name, path);

        mainProcessor.process();

        List<DetectorClass> classes=mainProcessor.getCurrentApp().getDetectorClasses();
        System.out.println("Save classes path ... ");
        writeClasses(classes);

        GraphCreator graphCreator = new GraphCreator(MainProcessor.currentApp);

        graphCreator.createClassHierarchy();

        graphCreator.createCallGraph();



        MetricsCalculator.calculateAppMetrics(MainProcessor.currentApp);


        ModelToGraph modelToGraph = new ModelToGraph("db");

        modelToGraph.insertApp(MainProcessor.currentApp);


        System.out.println("saving into database");


        modelToGraph.getDatabaseManager().shutDown();



    }


    public static void queryMode(Namespace arg) throws Exception {

        logger.info("Executing Queries");
        System.out.println("Executing Queries");
        QueryEngine queryEngine = new QueryEngine("db");


        String query="\n" +
                "MATCH (v1:Method)\n" +
                "WITH v1, \n" +
                "     CASE WHEN v1.is_static=true THEN true ELSE false END AS static     \n" +
                "SET v1.is_static=static\n" +
                "RETURN v1.is_static\n";


        String query1="\n" +
                "MATCH (v1:Variable)\n" +
                "WITH v1, \n" +
                "     CASE WHEN v1.is_static=true THEN true ELSE false END AS static     \n" +
                "SET v1.is_static=static\n" +
                "RETURN v1.is_static\n";

        Pretraitement pretraitement=new Pretraitement(queryEngine,query);
        pretraitement.exec();
        pretraitement.setQuery(query1);
        pretraitement.exec();


        String request = arg.get("codeSmell");
      //  System.out.println(" code smell "+request);


        switch (request) {
            case "LIC":
                System.out.println("Execute LIC query ");
                LICQuery.createLICQuery(queryEngine).execute(true);
                break;
            case "MIM":
                System.out.println("Execute MIM query ");
                MIMQuery.createMIMQuery(queryEngine).execute(true);
                break;
            case "NLMR":
                System.out.println("Execute NLMR query ");
                NLMRQuery.createNLMRQuery(queryEngine).execute(true);
                break;
            case "HAS":
                System.out.println("Execute HAS query ");
                HeavyAsyncTaskStepsQuery.createHeavyAsyncTaskStepsQuery(queryEngine).execute(true);
                break;
            case "HBR":
                System.out.println("Execute HBR query ");
                HeavyBroadcastReceiverQuery.createHeavyBroadcastReceiverQuery(queryEngine).execute(true);
                break;
            case "ALL":
               
                System.out.println("Execute MIM query ");
                MIMQuery.createMIMQuery(queryEngine).execute(true);
                System.out.println("Execute LIC query ");
                LICQuery.createLICQuery(queryEngine).execute(true);
                System.out.println("Execute NLMR query ");
                NLMRQuery.createNLMRQuery(queryEngine).execute(true);
                System.out.println("Execute HBR query ");
                HeavyBroadcastReceiverQuery.createHeavyBroadcastReceiverQuery(queryEngine).execute(true);
                System.out.println("Execute HAS query ");
                HeavyAsyncTaskStepsQuery.createHeavyAsyncTaskStepsQuery(queryEngine).execute(true);
                break;
        }

        


        queryEngine.shutDown();
        logger.info("Done");
        System.out.println("Done ");
    }



    public static void deteleContenetOfDirectory(String path){
        File directory = new File(path);
        File[] contents = directory.listFiles();
        for ( File f : contents) {

            if(f.isDirectory()){
                File[] c = f.listFiles();
                for(File ff:c){
                    ff.delete();
                }
            }else{
                f.delete();
            }
        }
    }

    public static void writeClasses(List<DetectorClass> classes){

        List<String[]> data = new ArrayList<String[]>();



        for(DetectorClass element:classes)
        {

            CtType ctClass=element.getClasse();
            String absPath = ctClass.getPosition().getFile().getAbsolutePath();
            absPath = absPath.replaceAll("\\\\", "/");
            data.add(new String[] { absPath});

        }
        CsvReader.writeData("classes_path/classes.csv",data);



    }





}
