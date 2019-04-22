

package laboratory.sniffer.detector.metrics;



import laboratory.sniffer.detector.entities.DetectorMethod;

public class IsSetter extends UnaryMetric<Boolean> {

    private IsSetter(DetectorMethod entity, boolean value) {
        this.value = value;
        this.entity = entity;
        this.name = "is_setter";
    }

    public static IsSetter createIsSetter(DetectorMethod entity, boolean value) {
        IsSetter isSetter = new IsSetter(entity, value);
        isSetter.updateEntity();
        return isSetter;
    }
}
