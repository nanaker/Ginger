package laboratory.sniffer.detector.analyzer;

import laboratory.sniffer.detector.corrector.Correction.LICProcessor;
import laboratory.sniffer.detector.corrector.Correction.MIMProcessor;
import laboratory.sniffer.detector.corrector.Correction.NLMRProcessor;
import laboratory.sniffer.detector.corrector.Recommandation.HASProcessor;
import laboratory.sniffer.detector.corrector.Recommandation.HBRProcessor;
import laboratory.sniffer.detector.detector.classifier;
import laboratory.sniffer.detector.entities.DetectorClass;
import laboratory.sniffer.detector.metrics.MetricsCalculator;

import laboratory.sniffer.detector.entities.DetectorApp;
import laboratory.sniffer.detector.entities.DetectorLibrary;
import laboratory.sniffer.detector.neo4j.*;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.*;

import java.io.File;
import java.nio.file.FileSystems;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spoon.IncrementalLauncher;
import spoon.Launcher;
import spoon.reflect.declaration.CtType;
import utils.CsvReader;

import javax.security.auth.callback.LanguageCallback;

import static utils.CsvReader.readAllDataAtOnce;


public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class.getName());

    public static void main(String[] args) {
        //testRun();
        String save=args[0];
        args=new String[6];
        args[0]="analyse";
        args[1]="-n";
        args[2]="MyApplication";
        args[3]="-db";
        args[4]="db";
        args[5]=save;


        ArgumentParser parser = ArgumentParsers.newArgumentParser("detector");
        Subparsers subparsers = parser.addSubparsers().dest("sub_command");
        Subparser analyseParser = subparsers.addParser("analyse").help("Analyse an app");
        analyseParser.addArgument("folder").help("Path of the code source folder");
        analyseParser.addArgument("-db", "--database").required(false).help("Path to neo4J Database folder");
        analyseParser.addArgument("-n", "--name").required(false).help("Name of the application");


        Subparser queryParser = subparsers.addParser("query").help("Query the database");
        queryParser.addArgument("-db", "--database").required(true).help("Path to neo4J Database folder");
        queryParser.addArgument("-r", "--request").help("Request to execute");
        queryParser.addArgument("-c", "--csv").help("path to register csv files").setDefault("");
        queryParser.addArgument("-dk", "--delKey").help("key to delete");
        queryParser.addArgument("-dp", "--delPackage").help("Package of the applications to delete");
        queryParser.addArgument("-d", "--details").type(Boolean.class).setDefault(false).help("Show the concerned entity in the results");

        try {



            Namespace pathOfApplicationToAnalyse=parser.parseArgs(args);
            //System.out.println("pathOfApplicationToAnalyse = "+pathOfApplicationToAnalyse);

            runAnalysis(pathOfApplicationToAnalyse);



            String[] argumentsQyery=new String[5];
            argumentsQyery[0]="query";
            argumentsQyery[1]="-db";
            argumentsQyery[2]="db";
            argumentsQyery[3]="-d";
            argumentsQyery[4]="TRUE";


            Namespace res = parser.parseArgs(argumentsQyery);


            queryMode(res);

            // Detection des défauts de code

            System.out.println("Detecting code smells...");
            String base_path = FileSystems.getDefault().getPath("").normalize().toAbsolutePath().toString();
           classifier classifier=new classifier(base_path);
            String result=classifier.exec();
            //logger.info(result);
             System.out.println(result);
            System.out.println("Done");



            //Correction des défauts de code
            //runRefactor();


        } catch (ArgumentParserException e) {
            analyseParser.handleError(e);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }



    public static void runAnalysis(Namespace arg) throws Exception {


        deteleContenetOfDirectory("db");

        logger.info("Collecting metrics");
        System.out.println("Collecting metrics...");
        String path = arg.getString("folder");
        path = new File(path).getAbsolutePath();
        String name = arg.getString("name");


        MainProcessor mainProcessor = new MainProcessor(name, path);

        mainProcessor.process();

        List<DetectorClass> classes=mainProcessor.getCurrentApp().getDetectorClasses();
        System.out.println("Save classes path ... ");
        writeClasses(classes);

        GraphCreator graphCreator = new GraphCreator(MainProcessor.currentApp);

        graphCreator.createClassHierarchy();

        graphCreator.createCallGraph();



        MetricsCalculator.calculateAppMetrics(MainProcessor.currentApp);

        ModelToGraph modelToGraph = new ModelToGraph(arg.getString("database"));

        modelToGraph.insertApp(MainProcessor.currentApp);

        logger.info("Saving into database " + arg.getString("database"));
        System.out.println("saving into database");


        modelToGraph.getDatabaseManager().shutDown();



    }
    public static void runRefactor(){

        logger.info("Refactoring ...  " );




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

            executor.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {

            e.printStackTrace();
        }


        logger.info("fin refactor");
        System.out.println(" Refactor done ");

    }

    public static void queryMode(Namespace arg) throws Exception {

        logger.info("Executing Queries");
        System.out.println("Executing Queries");
        QueryEngine queryEngine = new QueryEngine(arg.getString("database"));


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


        String request = arg.get("request");

        Calendar cal = new GregorianCalendar();
        String csvDate = String.valueOf(cal.get(Calendar.YEAR)) + "_" + String.valueOf(cal.get(Calendar.MONTH) + 1) + "_" + String.valueOf(cal.get(Calendar.DAY_OF_MONTH)) + "_" + String.valueOf(cal.get(Calendar.HOUR_OF_DAY)) + "_" + String.valueOf(cal.get(Calendar.MINUTE));
        String csvPrefix = arg.getString("csv") + csvDate;


        queryEngine.setCsvPrefix(csvPrefix);
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


        queryEngine.shutDown();
        logger.info("Done");
        System.out.println("Done ");
    }


    public static void addLibrary(DetectorApp detectorApp, String libraryString) {
        DetectorLibrary.createDetectorLibrary(libraryString, detectorApp);
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
