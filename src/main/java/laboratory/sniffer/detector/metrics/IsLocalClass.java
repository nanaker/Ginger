

package laboratory.sniffer.detector.metrics;



import laboratory.sniffer.detector.entities.Entity;

public class IsLocalClass extends UnaryMetric<Boolean> {

    private IsLocalClass(Entity entity, boolean value) {
        this.value = value;
        this.entity = entity;
        this.name = "is_local_class";
    }

    public static IsLocalClass createisLocalClass(Entity entity, boolean value) {
        IsLocalClass isLocalClass = new IsLocalClass(entity, value);
        isLocalClass.updateEntity();
        return isLocalClass;
    }
}
