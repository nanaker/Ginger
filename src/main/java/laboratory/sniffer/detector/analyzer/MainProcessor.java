package laboratory.sniffer.detector.analyzer;

import laboratory.sniffer.detector.entities.DetectorApp;
import laboratory.sniffer.detector.entities.DetectorClass;
import laboratory.sniffer.detector.entities.DetectorMethod;
import spoon.Launcher;
import spoon.processing.AbstractProcessor;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtInterface;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;


public class MainProcessor {

    static DetectorApp currentApp;
    static DetectorClass currentClass;
    static DetectorMethod currentMethod;
    static ArrayList<URL> paths = new ArrayList<>();
    String appPath;
    private String jarsPath;
    private String sdkPath;

    public MainProcessor(String appName,String appPath) {
        this.currentApp = DetectorApp.createDetectorApp(appName, appPath);
        currentClass = null;
        currentMethod = null;
        this.appPath = appPath;

    }

    public void process() {
        System.out.println("process");
        Launcher launcher = new Launcher();
        launcher.addInputResource(appPath);
        System.out.println("add input ressource");
        launcher.getEnvironment().setNoClasspath(true);
        System.out.println("get Environement");
        launcher.buildModel();
        System.out.println("build model");
        AbstractProcessor<CtClass> classProcessor = new ClassProcessor();
        AbstractProcessor<CtInterface> interfaceProcessor = new InterfaceProcessor();
        launcher.addProcessor(classProcessor);
        launcher.addProcessor(interfaceProcessor);
        System.out.println("lunch . process debut ");
        launcher.process();
    }

    private void update_classpath(Launcher launcher) {
        try {
            paths = new ArrayList<>();
            if (this.jarsPath != null) {
                File folder = new File(jarsPath);
                paths = this.listFilesForFolder(folder);
            }
            if (this.sdkPath != null) {
                paths.add(new File(sdkPath).toURI().toURL());
            }
            String[] cl = new String[paths.size()];
            for (int i = 0; i < paths.size(); i++) {
                URL url = paths.get(i);
                cl[i] = url.getPath();
            }
            launcher.getEnvironment().setSourceClasspath(cl);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    private ArrayList<URL> listFilesForFolder(final File folder) throws IOException {
        ArrayList<URL> jars = new ArrayList<>();
        File[] files = folder.listFiles();
        if (files == null) {
            return jars;
        }
        for (final File fileEntry : files) {
            jars.add(fileEntry.toURI().toURL());
        }
        return jars;
    }
}
