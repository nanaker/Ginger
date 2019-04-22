

package laboratory.sniffer.detector.metrics;



import laboratory.sniffer.detector.entities.Entity;

public class IsAbstract extends UnaryMetric<Boolean> {

    private IsAbstract(Entity entity, boolean value) {
        this.value = value;
        this.entity = entity;
        this.name = "is_abstract";
    }

    public static IsAbstract createIsAbstract(Entity entity, boolean value) {
        IsAbstract isFinal = new IsAbstract(entity, value);
        isFinal.updateEntity();
        return isFinal;
    }
}
