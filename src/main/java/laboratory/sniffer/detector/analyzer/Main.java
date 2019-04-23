package laboratory.sniffer.detector.analyzer;

import laboratory.sniffer.detector.metrics.MetricsCalculator;

import laboratory.sniffer.detector.entities.DetectorApp;
import laboratory.sniffer.detector.entities.DetectorLibrary;
import laboratory.sniffer.detector.neo4j.ARGB8888Query;
import laboratory.sniffer.detector.neo4j.BLOBQuery;
import laboratory.sniffer.detector.neo4j.CCQuery;
import laboratory.sniffer.detector.neo4j.CommitSizeQuery;
import laboratory.sniffer.detector.neo4j.HashMapUsageQuery;
import laboratory.sniffer.detector.neo4j.HeavyAsyncTaskStepsQuery;
import laboratory.sniffer.detector.neo4j.HeavyBroadcastReceiverQuery;
import laboratory.sniffer.detector.neo4j.HeavyServiceStartQuery;
import laboratory.sniffer.detector.neo4j.IGSQuery;
import laboratory.sniffer.detector.neo4j.InitOnDrawQuery;
import laboratory.sniffer.detector.neo4j.InvalidateWithoutRectQuery;
import laboratory.sniffer.detector.neo4j.LICQuery;
import laboratory.sniffer.detector.neo4j.LMQuery;
import laboratory.sniffer.detector.neo4j.MIMQuery;
import laboratory.sniffer.detector.neo4j.ModelToGraph;
import laboratory.sniffer.detector.neo4j.NLMRQuery;
import laboratory.sniffer.detector.neo4j.OverdrawQuery;
import laboratory.sniffer.detector.neo4j.QuartileCalculator;
import laboratory.sniffer.detector.neo4j.QueryEngine;
import laboratory.sniffer.detector.neo4j.SAKQuery;
import laboratory.sniffer.detector.neo4j.TrackingHardwareIdQuery;
import laboratory.sniffer.detector.neo4j.UnsuitedLRUCacheSizeQuery;
import laboratory.sniffer.detector.neo4j.UnsupportedHardwareAccelerationQuery;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.*;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


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

            runAnalysis(pathOfApplicationToAnalyse);


            String[] argumentsQyery=new String[5];
            argumentsQyery[0]="query";
            argumentsQyery[1]="-db";
            argumentsQyery[2]="db";
            argumentsQyery[3]="-d";
            argumentsQyery[4]="TRUE";


            Namespace res = parser.parseArgs(argumentsQyery);
            System.out.println("res=="+res);
            queryMode(res);

        } catch (ArgumentParserException e) {
            analyseParser.handleError(e);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }



    public static void runAnalysis(Namespace arg) throws Exception {


        deteleContenetOfDirectory("db");
        System.out.println("Collecting metrics");
        logger.info("Collecting metrics");
        String path = arg.getString("folder");
        path = new File(path).getAbsolutePath();
        String name = arg.getString("name");


        MainProcessor mainProcessor = new MainProcessor(name, path);
        mainProcessor.process();
        System.out.println("main process ; process");
        GraphCreator graphCreator = new GraphCreator(MainProcessor.currentApp);

        graphCreator.createClassHierarchy();
        System.out.println("create class hierarchy");
        graphCreator.createCallGraph();
        System.out.println("create call graph");


        MetricsCalculator.calculateAppMetrics(MainProcessor.currentApp);
        System.out.println("metrique claculator");
        ModelToGraph modelToGraph = new ModelToGraph(arg.getString("database"));

        modelToGraph.insertApp(MainProcessor.currentApp);
        System.out.println("Saving into database " + arg.getString("database"));
        logger.info("Saving into database " + arg.getString("database"));
        System.out.println("done");
        modelToGraph.getDatabaseManager().shutDown();

    }

    public static void queryMode(Namespace arg) throws Exception {
        System.out.println("Executing Queries");
        logger.info("Executing Queries");
        QueryEngine queryEngine = new QueryEngine(arg.getString("database"));
        String request = arg.get("request");

        Calendar cal = new GregorianCalendar();
        String csvDate = String.valueOf(cal.get(Calendar.YEAR)) + "_" + String.valueOf(cal.get(Calendar.MONTH) + 1) + "_" + String.valueOf(cal.get(Calendar.DAY_OF_MONTH)) + "_" + String.valueOf(cal.get(Calendar.HOUR_OF_DAY)) + "_" + String.valueOf(cal.get(Calendar.MINUTE));
        String csvPrefix = arg.getString("csv") + csvDate;
        System.out.println("Resulting csv file name will start with prefix");
        System.out.println("request "+request);

                queryEngine.setCsvPrefix(csvPrefix);
                MIMQuery.createMIMQuery(queryEngine).execute(true);
                LICQuery.createLICQuery(queryEngine).execute(true);
                NLMRQuery.createNLMRQuery(queryEngine).execute(true);
                HeavyBroadcastReceiverQuery.createHeavyBroadcastReceiverQuery(queryEngine).execute(true);
                HeavyAsyncTaskStepsQuery.createHeavyAsyncTaskStepsQuery(queryEngine).execute(true);


        queryEngine.shutDown();
        logger.info("Done");
    }


    public static void addLibrary(DetectorApp detectorApp, String libraryString) {
        DetectorLibrary.createDetectorLibrary(libraryString, detectorApp);
    }

    public static void deteleContenetOfDirectory(String path){
        File directory = new File("db");
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


}
