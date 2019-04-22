

package laboratory.sniffer.detector.metrics;



import laboratory.sniffer.detector.entities.DetectorClass;

public class IsInnerClass extends UnaryMetric<Boolean> {

    private IsInnerClass(DetectorClass entity, boolean value) {
        this.value = value;
        this.entity = entity;
        this.name = "is_inner_class";
    }

    public static IsInnerClass createIsInnerClass(DetectorClass entity, boolean value) {
        IsInnerClass isInner = new IsInnerClass(entity, value);
        isInner.updateEntity();
        return isInner;
    }
}
