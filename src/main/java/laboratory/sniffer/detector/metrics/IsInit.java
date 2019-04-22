

package laboratory.sniffer.detector.metrics;



import laboratory.sniffer.detector.entities.DetectorMethod;

public class IsInit extends UnaryMetric<Boolean> {

    private IsInit(DetectorMethod entity, boolean value) {
        this.value = value;
        this.entity = entity;
        this.name = "is_init";
    }

    public static IsInit createIsInit(DetectorMethod entity, boolean value) {
        IsInit isInit = new IsInit(entity, value);
        isInit.updateEntity();
        return isInit;
    }
}
