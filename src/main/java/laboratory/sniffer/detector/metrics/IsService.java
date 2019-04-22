

package laboratory.sniffer.detector.metrics;



import laboratory.sniffer.detector.entities.DetectorClass;

public class IsService extends UnaryMetric<Boolean> {

    private IsService(DetectorClass entity, boolean value) {
        this.value = value;
        this.entity = entity;
        this.name = "is_service";
    }

    public static IsService createIsService(DetectorClass entity, boolean value) {
        IsService isService= new IsService(entity, value);
        isService.updateEntity();
        return isService;
    }
}
