

package laboratory.sniffer.detector.metrics;

import laboratory.sniffer.detector.entities.DetectorApp;


public class NumberOfClasses extends UnaryMetric<Integer> {

    private NumberOfClasses(DetectorApp detectorApp, int value) {
        this.value = value;
        this.entity = detectorApp;
        this.name = "number_of_classes";
    }

    public static NumberOfClasses createNumberOfClasses(DetectorApp detectorApp, int value) {
        NumberOfClasses numberOfClasses = new NumberOfClasses(detectorApp, value);
        numberOfClasses.updateEntity();
        return numberOfClasses;
    }

}
