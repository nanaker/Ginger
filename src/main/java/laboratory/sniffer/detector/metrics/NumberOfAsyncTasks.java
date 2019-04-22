

package laboratory.sniffer.detector.metrics;

import laboratory.sniffer.detector.entities.DetectorApp;

public class NumberOfAsyncTasks extends UnaryMetric<Integer> {

    private NumberOfAsyncTasks(DetectorApp detectorApp, int value) {
        this.value = value;
        this.entity = detectorApp;
        this.name = "number_of_async_tasks";
    }

    public static NumberOfAsyncTasks createNumberOfAsyncTasks(DetectorApp detectorApp, int value) {
        NumberOfAsyncTasks numberOfAsyncTasks = new NumberOfAsyncTasks(detectorApp, value);
        numberOfAsyncTasks.updateEntity();
        return numberOfAsyncTasks;
    }

}
