package laboratory.sniffer.detector.entities;

import java.util.ArrayList;
import java.util.List;


public class DetectorApp extends Entity{



    private String path;
    private List<DetectorClass> detectorClasses;
    private List<DetectorExternalClass> detectorExternalClasses;
    private ArrayList<DetectorLibrary> detectorLibraries;



    private DetectorApp(String name,String path) {
        this.name=name;
        this.detectorClasses = new ArrayList<>();
        this.detectorExternalClasses = new ArrayList<>();
        this.detectorLibraries = new ArrayList<>();
        this.path =path;

    }

    public static DetectorApp createDetectorApp(String name, String path){
        return new DetectorApp(name, path);
    }

    public List<DetectorExternalClass> getDetectorExternalClasses() {
        return detectorExternalClasses;
    }

    public DetectorClass findClass(String class_name){

        for (DetectorClass detectorClass : detectorClasses){
                       if (detectorClass.getName().equals(class_name)) return detectorClass;
        }
        //otherwise we return null
        return null;

    }

    public void addDetectorExternalClass(DetectorExternalClass detectorExternalClass){
        detectorExternalClasses.add(detectorExternalClass);
    }

    public List<DetectorClass> getDetectorClasses() {
        return detectorClasses;
    }


    public void addDetectorClass(DetectorClass detectorClass){
        detectorClasses.add(detectorClass);
    }


    public ArrayList<DetectorMethod> getMethods(){
        ArrayList<DetectorMethod> detectorMethods = new ArrayList<>();
        for(DetectorClass detectorClass : this.getDetectorClasses()){
            for(DetectorMethod detectorMethod : detectorClass.getDetectorMethods()){
                detectorMethods.add(detectorMethod);
            }
        }
        return detectorMethods;
    }

    public Entity getDetectorClass(String className){
        for(DetectorClass detectorClass : this.getDetectorClasses()){
            if(detectorClass.getName().equals(className)){
                return detectorClass;
            }
        }
        for(DetectorExternalClass detectorExternalClass : this.getDetectorExternalClasses()){
            if(detectorExternalClass.getName().equals(className)){
                return detectorExternalClass;
            }
        }
        return DetectorExternalClass.createDetectorExternalClass(className,this);
    }

    public DetectorClass getDetectorInternalClass(String className){
        for(DetectorClass detectorClass : this.getDetectorClasses()){
            if(detectorClass.getName().equals(className)){
                return detectorClass;
            }
        }
        return null;
    }



    public void addDetectorLibrary(DetectorLibrary detectorLibrary){
        this.detectorLibraries.add(detectorLibrary);
    }

    public ArrayList<DetectorLibrary> getDetectorLibraries() {
        return detectorLibraries;
    }

    public String getPath() {
        return path;
    }



}
