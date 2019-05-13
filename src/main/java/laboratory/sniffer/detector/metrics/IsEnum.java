

package laboratory.sniffer.detector.metrics;



import laboratory.sniffer.detector.entities.Entity;

public class IsEnum extends UnaryMetric<Boolean> {

    private IsEnum(Entity entity, boolean value) {
        this.value = value;
        this.entity = entity;
        this.name = "is_enum";
    }

    public static IsEnum createIsEnum(Entity entity, boolean value) {
        IsEnum isEnum = new IsEnum(entity, value);
        isEnum.updateEntity();
        return isEnum;
    }
}
