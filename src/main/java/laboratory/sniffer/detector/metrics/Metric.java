

package laboratory.sniffer.detector.metrics;


public abstract class  Metric<E> {
    protected Object value;
    protected String name = "anonymous_metric";

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getName(){ return name;}
}
