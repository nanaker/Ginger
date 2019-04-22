

package laboratory.sniffer.detector.metrics;

import laboratory.sniffer.detector.entities.DetectorApp;


public class NumberOfInnerClasses extends UnaryMetric<Integer> {

    private NumberOfInnerClasses(DetectorApp detectorApp, int value) {
        this.value = value;
        this.entity = detectorApp;
        this.name = "number_of_inner_classes";
    }

    public static NumberOfInnerClasses createNumberOfInnerClasses(DetectorApp detectorApp, int value) {
        NumberOfInnerClasses numberOfInnerClasses = new NumberOfInnerClasses(detectorApp, value);
        numberOfInnerClasses.updateEntity();
        return numberOfInnerClasses;
    }

}
