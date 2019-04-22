

package laboratory.sniffer.detector.metrics;



import laboratory.sniffer.detector.entities.DetectorMethod;

public class IsGetter extends UnaryMetric<Boolean> {

    private IsGetter(DetectorMethod entity, boolean value) {
        this.value = value;
        this.entity = entity;
        this.name = "is_getter";
    }

    public static IsGetter createIsGetter(DetectorMethod entity, boolean value) {
        IsGetter isGetter = new IsGetter(entity, value);
        isGetter.updateEntity();
        return isGetter;
    }
}
