

package laboratory.sniffer.detector.metrics;



import laboratory.sniffer.detector.entities.DetectorClass;

public class IsActivity extends UnaryMetric<Boolean> {

    private IsActivity(DetectorClass entity, boolean value) {
        this.value = value;
        this.entity = entity;
        this.name = "is_activity";
    }

    public static IsActivity createIsActivity(DetectorClass entity, boolean value) {
        IsActivity isActivity= new IsActivity(entity, value);
        isActivity.updateEntity();
        return isActivity;
    }
}
