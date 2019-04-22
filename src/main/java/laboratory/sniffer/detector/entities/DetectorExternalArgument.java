

package laboratory.sniffer.detector.entities;

public class DetectorExternalArgument extends Entity{
    private DetectorExternalMethod detectorExternalMethod;
    private int position;

    private DetectorExternalArgument(String name, int position, DetectorExternalMethod detectorExternalMethod) {
        this.detectorExternalMethod = detectorExternalMethod;
        this.name = name;
        this.position = position;
    }

    public static DetectorExternalArgument createDetectorExternalArgument(String name, int position, DetectorExternalMethod detectorExternalMethod){
        DetectorExternalArgument detectorExternalArgument = new DetectorExternalArgument(name,position, detectorExternalMethod);
        detectorExternalMethod.addExternalArgument(detectorExternalArgument);
        return detectorExternalArgument;
    }

    public int getPosition() {
        return position;
    }
}
