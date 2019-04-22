

package laboratory.sniffer.detector.metrics;

import laboratory.sniffer.detector.entities.Entity;


public abstract class BinaryMetric<E> extends Metric{
    private Entity source;
    private Entity target;

    public Entity getTarget() {
        return target;
    }

    public void setTarget(Entity target) {
        this.target = target;
    }

    public Entity getSource() {
        return source;
    }

    public void setSource(Entity source) {
        this.source = source;
    }
}
