

package laboratory.sniffer.detector.metrics;



import laboratory.sniffer.detector.entities.DetectorClass;

public class IsContentProvider extends UnaryMetric<Boolean> {

    private IsContentProvider(DetectorClass entity, boolean value) {
        this.value = value;
        this.entity = entity;
        this.name = "is_content_provider";
    }

    public static IsContentProvider createIsContentProvider(DetectorClass entity, boolean value) {
        IsContentProvider isContentProvider= new IsContentProvider(entity, value);
        isContentProvider.updateEntity();
        return isContentProvider;
    }
}
