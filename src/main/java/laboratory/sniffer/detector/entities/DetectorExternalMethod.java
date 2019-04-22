

package laboratory.sniffer.detector.entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DetectorExternalMethod extends Entity{
    private DetectorExternalClass detectorExternalClass;
    private List<DetectorExternalArgument> detectorExternalArguments;
    private String returnType;
    private static Map<String, DetectorExternalMethod> externalMethods =new HashMap<>();

    public String getReturnType() {
        return returnType;
    }

    public List<DetectorExternalArgument> getDetectorExternalArguments() {
        return detectorExternalArguments;
    }

    private DetectorExternalMethod(String name, String returnType, DetectorExternalClass detectorExternalClass) {
        this.setName(name);
        this.detectorExternalClass = detectorExternalClass;
        this.returnType = returnType;
        this.detectorExternalArguments = new ArrayList<>();
    }

    public static DetectorExternalMethod createDetectorExternalMethod(String name, String returnType, DetectorExternalClass detectorClass) {
        String fullName=name + "#" + detectorClass;
        DetectorExternalMethod detectorMethod;
        if((detectorMethod =externalMethods.get(fullName))!=null){
            return detectorMethod;
        }
        detectorMethod = new DetectorExternalMethod(name, returnType, detectorClass);
        externalMethods.put(fullName,detectorMethod);
        detectorClass.addDetectorExternalMethod(detectorMethod);
        return  detectorMethod;
    }

    public DetectorExternalClass getDetectorExternalClass() {
        return detectorExternalClass;
    }

    public void setDetectorExternalClass(DetectorExternalClass detectorClass) {
        this.detectorExternalClass = detectorClass;
    }

    @Override
    public String toString() {
        return this.getName() + "#" + detectorExternalClass;
    }

    public void addExternalArgument(DetectorExternalArgument detectorExternalArgument) {
        this.detectorExternalArguments.add(detectorExternalArgument);
    }
}
