

package laboratory.sniffer.detector.metrics;



import laboratory.sniffer.detector.entities.DetectorMethod;

public class IsSynchronized extends UnaryMetric<Boolean> {

    private IsSynchronized(DetectorMethod entity, boolean value) {
        this.value = value;
        this.entity = entity;
        this.name = "is_synchronized";
    }

    public static IsSynchronized createIsSynchronized(DetectorMethod entity, boolean value) {
        IsSynchronized isSynchronized = new IsSynchronized(entity, value);
        isSynchronized.updateEntity();
        return isSynchronized;
    }
}
