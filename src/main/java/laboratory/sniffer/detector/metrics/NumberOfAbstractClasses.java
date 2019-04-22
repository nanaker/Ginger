

package laboratory.sniffer.detector.metrics;

import laboratory.sniffer.detector.entities.DetectorApp;


public class NumberOfAbstractClasses extends UnaryMetric<Integer> {

    private NumberOfAbstractClasses(DetectorApp detectorApp, int value) {
        this.value = value;
        this.entity = detectorApp;
        this.name = "number_of_abstract_classes";
    }

    public static NumberOfAbstractClasses createNumberOfAbstractClasses(DetectorApp detectorApp, int value) {
        NumberOfAbstractClasses numberOfAbstractClasses = new NumberOfAbstractClasses(detectorApp, value);
        numberOfAbstractClasses.updateEntity();
        return numberOfAbstractClasses;
    }

}
