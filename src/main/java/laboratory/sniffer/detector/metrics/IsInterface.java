

package laboratory.sniffer.detector.metrics;



import laboratory.sniffer.detector.entities.DetectorClass;

public class IsInterface extends UnaryMetric<Boolean> {

    private IsInterface(DetectorClass entity, boolean value) {
        this.value = value;
        this.entity = entity;
        this.name = "is_interface";
    }

    public static IsInterface createIsInterface(DetectorClass entity, boolean value) {
        IsInterface isInterface = new IsInterface(entity, value);
        isInterface.updateEntity();
        return isInterface;
    }
}
