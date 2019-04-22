

package laboratory.sniffer.detector.metrics;



import laboratory.sniffer.detector.entities.DetectorClass;

public class IsView extends UnaryMetric<Boolean> {

    private IsView(DetectorClass entity, boolean value) {
        this.value = value;
        this.entity = entity;
        this.name = "is_view";
    }

    public static IsView createIsView(DetectorClass entity, boolean value) {
        IsView isView= new IsView(entity, value);
        isView.updateEntity();
        return isView;
    }
}
