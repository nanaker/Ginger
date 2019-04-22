

package laboratory.sniffer.detector.metrics;

import laboratory.sniffer.detector.entities.Entity;


public class DumbUnaryMetric<E> extends UnaryMetric<E> {
    private String description;

    private DumbUnaryMetric(Entity entity, E value, String description) {
        this.value = value;
        this.entity = entity;
        this.description = description;
    }

    public static <E> DumbUnaryMetric<E> createDumbMetric(Entity entity, E value, String description) {
        DumbUnaryMetric dumbUnaryMetric = new DumbUnaryMetric<E>(entity, value, description);
        dumbUnaryMetric.updateEntity();
        return dumbUnaryMetric;
    }

}
