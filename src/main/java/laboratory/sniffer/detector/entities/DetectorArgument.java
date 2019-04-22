

package laboratory.sniffer.detector.entities;


public class DetectorArgument extends Entity{
    private DetectorMethod detectorMethod;
    private int position;

    private DetectorArgument(String name, int position, DetectorMethod detectorMethod) {
        this.detectorMethod = detectorMethod;
        this.name = name;
        this.position = position;
    }

    public static DetectorArgument createDetectorArgument(String name, int position, DetectorMethod detectorMethod){
        DetectorArgument detectorArgument = new DetectorArgument(name,position, detectorMethod);
        detectorMethod.addArgument(detectorArgument);
        return detectorArgument;
    }

    public int getPosition() {
        return position;
    }
}
