

package laboratory.sniffer.detector.metrics;



import laboratory.sniffer.detector.entities.DetectorClass;

public class IsAsyncTask extends UnaryMetric<Boolean> {

    private IsAsyncTask(DetectorClass entity, boolean value) {
        this.value = value;
        this.entity = entity;
        this.name = "is_async_task";
    }

    public static IsAsyncTask createIsAsyncTask(DetectorClass entity, boolean value) {
        IsAsyncTask isAsyncTask= new IsAsyncTask(entity, value);
        isAsyncTask.updateEntity();
        return isAsyncTask;
    }
}
