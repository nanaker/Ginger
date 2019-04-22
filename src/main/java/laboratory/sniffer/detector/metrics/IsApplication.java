

package laboratory.sniffer.detector.metrics;



import laboratory.sniffer.detector.entities.DetectorClass;

public class IsApplication extends UnaryMetric<Boolean> {

    private IsApplication(DetectorClass entity, boolean value) {
        this.value = value;
        this.entity = entity;
        this.name = "is_application";
    }

    public static IsApplication createIsApplication(DetectorClass entity, boolean value) {
        IsApplication isApplication= new IsApplication(entity, value);
        isApplication.updateEntity();
        return isApplication;
    }
}
