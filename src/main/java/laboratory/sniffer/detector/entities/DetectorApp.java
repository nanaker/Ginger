package laboratory.sniffer.detector.entities;

import java.util.ArrayList;
import java.util.List;


public class DetectorApp extends Entity{
    private double rating;
    private String date;
    private String pack; //Package
    private int size;
    private String developer;
    private String category;
    private String price;
    private String key;
    private String nbDownload;
    private String versionCode;
    private int version;
    private int commitNumber;
    private String status;
    private int sdkVersion;
    private String targetSdkVersion;
    private String path;
    private List<DetectorClass> detectorClasses;
    private List<DetectorExternalClass> detectorExternalClasses;
    private ArrayList<DetectorLibrary> detectorLibraries;
    private String module;

    private DetectorApp(String name, String key, String pack, String date, int size, String developer, String category, String price, double rating, String nbDownload, String versionCode, int version, int sdkVersion, String targetSdkVersion) {
        this.name = name;
        this.key = key;
        this.pack = pack;
        this.date = date;
        this.size = size;
        this.developer = developer;
        this.category = category;
        this.price = price;
        this.rating = rating;
        this.nbDownload = nbDownload;
        this.detectorClasses = new ArrayList<>();
        this.detectorExternalClasses = new ArrayList<>();
        this.versionCode = versionCode;
        this.version = version;
        this.sdkVersion = sdkVersion;
        this.targetSdkVersion = targetSdkVersion;
        this.detectorLibraries = new ArrayList<>();
    }

    private DetectorApp(String name, int version, int commitNumber, String status, String key, String path, int sdkVersion, String module) {
        this.name=name;
        this.key = key;
        this.version = version;
        this.detectorClasses = new ArrayList<>();
        this.detectorExternalClasses = new ArrayList<>();
        this.detectorLibraries = new ArrayList<>();
        this.path =path;
        this.commitNumber =commitNumber;
        this.status =status;
        this.sdkVersion = sdkVersion;
        this.module = module;
    }

    public static DetectorApp createDetectorApp(String name, int version, int commitNumber, String status, String key, String path, int sdkVersion, String module){
        return new DetectorApp(name,version, commitNumber, status, key, path,sdkVersion,module);
    }

    public List<DetectorExternalClass> getDetectorExternalClasses() {
        return detectorExternalClasses;
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

    public static DetectorApp createDetectorApp(String name, String key, String pack, String date, int size, String dev, String cat, String price, double rating, String nbDownload, String versionCode, int version, int sdkVersion, String targetSdkVersion) {
        return new DetectorApp(name,key,pack,date,size,dev,cat,price,rating,nbDownload,versionCode,version,sdkVersion,targetSdkVersion);
    }

    public double getRating() {
        return rating;
    }

    public String getDate() {
        return date;
    }

    public String getPack() {
        return pack;
    }

    public int getSize() {
        return size;
    }

    public String getDeveloper() {
        return developer;
    }

    public String getCategory() {
        return category;
    }

    public String getPrice() {
        return price;
    }

    public String getKey() {
        return key;
    }

    public String getNbDownload() {
        return nbDownload;
    }

    public String getVersionCode() {
        return versionCode;
    }

    public int getVersion() {
        return version;
    }

    public int getSdkVersion() {
        return sdkVersion;
    }

    public String getTargetSdkVersion() {
        return targetSdkVersion;
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

    public void setPath(String path) {
        this.path = path;
    }

    public int getCommitNumber() {
        return commitNumber;
    }

    public String getStatus() {
        return status;
    }

    public String getModule() {
        return module;
    }
}
