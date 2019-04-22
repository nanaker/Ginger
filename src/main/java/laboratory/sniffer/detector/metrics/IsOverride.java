

package laboratory.sniffer.detector.metrics;



import laboratory.sniffer.detector.entities.DetectorMethod;

public class IsOverride extends UnaryMetric<Boolean> {

    private IsOverride(DetectorMethod entity, boolean value) {
        this.value = value;
        this.entity = entity;
        this.name = "is_override";
    }

    public static IsOverride createIsOverride(DetectorMethod entity, boolean value) {
        IsOverride isOverride = new IsOverride(entity, value);
        isOverride.updateEntity();
        return isOverride;
    }
}
