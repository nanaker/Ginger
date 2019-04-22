

package laboratory.sniffer.detector.metrics;



import laboratory.sniffer.detector.entities.Entity;

public class IsStatic extends UnaryMetric<Boolean> {

    private IsStatic(Entity entity, boolean value) {
        this.value = value;
        this.entity = entity;
        this.name = "is_static";
    }

    public static IsStatic createIsStatic(Entity entity, boolean value) {
        IsStatic isStatic = new IsStatic(entity, value);
        isStatic.updateEntity();
        return isStatic;
    }
}
