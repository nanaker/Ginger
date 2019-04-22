

package laboratory.sniffer.detector.entities;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class DetectorExternalClass extends Entity{
    private DetectorApp detectorApp;
    private String parentName;
    private Set<DetectorExternalMethod> detectorExternalMethods;
    private static Map<String, DetectorExternalClass> externalClasses =new HashMap<>();

    public Set<DetectorExternalMethod> getDetectorExternalMethods() {
        return detectorExternalMethods;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    private DetectorExternalClass(String name, DetectorApp detectorApp) {
        this.setName(name);
        this.detectorApp = detectorApp;
        this.detectorExternalMethods = new HashSet<>();
    }

    public static DetectorExternalClass createDetectorExternalClass(String name, DetectorApp detectorApp) {
        DetectorExternalClass detectorClass;
        if ((detectorClass =externalClasses.get(name)) !=null){
            return detectorClass;
        }
        detectorClass = new DetectorExternalClass(name, detectorApp);
        externalClasses.put(name,detectorClass);
        detectorApp.addDetectorExternalClass(detectorClass);
        return detectorClass;
    }

    public void addDetectorExternalMethod(DetectorExternalMethod detectorMethod){
        detectorExternalMethods.add(detectorMethod);
    }

    public DetectorApp getDetectorApp() {
        return detectorApp;
    }

    public void setDetectorApp(DetectorApp detectorApp) {
        this.detectorApp = detectorApp;
    }

}
