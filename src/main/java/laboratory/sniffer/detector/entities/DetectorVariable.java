

package laboratory.sniffer.detector.entities;


public class DetectorVariable extends Entity{
    private DetectorClass detectorClass;
    private String type;
    private DetectorModifiers modifier;
    private boolean isStatic;

    public String getType() {
        return type;
    }

    public DetectorModifiers getModifier() {
        return modifier;
    }

    private DetectorVariable(String name, String type, DetectorModifiers modifier, DetectorClass detectorClass) {
        this.type = type;
        this.name = name;
        this.modifier = modifier;
        this.detectorClass = detectorClass;
        this.isStatic=false;
    }

    public static DetectorVariable createDetectorVariable(String name, String type, DetectorModifiers modifier, DetectorClass detectorClass) {
        DetectorVariable detectorVariable = new DetectorVariable(name, type, modifier, detectorClass);
        detectorClass.addDetectorVariable(detectorVariable);
        return detectorVariable;
    }

    public boolean isPublic(){
        return modifier == DetectorModifiers.PUBLIC;
    }

    public boolean isPrivate(){
        return modifier == DetectorModifiers.PRIVATE;
    }

    public boolean isProtected(){ return modifier == DetectorModifiers.PROTECTED; }

    public DetectorClass getDetectorClass() {
        return detectorClass;
    }

    public void setDetectorClass(DetectorClass detectorClass) {
        this.detectorClass = detectorClass;
    }

    public boolean isStatic() {
        return isStatic;
    }

    public void setStatic(boolean aStatic) {
        isStatic = aStatic;
    }
}
