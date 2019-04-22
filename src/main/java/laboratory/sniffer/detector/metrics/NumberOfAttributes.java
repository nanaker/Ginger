

package laboratory.sniffer.detector.metrics;

import laboratory.sniffer.detector.entities.DetectorClass;


public class NumberOfAttributes extends UnaryMetric<Integer> {

    private NumberOfAttributes(DetectorClass detectorClass, int value) {
        this.value = value;
        this.entity = detectorClass;
        this.name = "number_of_attributes";
    }

    public static NumberOfAttributes createNumberOfAttributes(DetectorClass detectorClass, int value) {
        NumberOfAttributes numberOfAttributes = new NumberOfAttributes(detectorClass, value);
        numberOfAttributes.updateEntity();
        return numberOfAttributes;
    }

}
