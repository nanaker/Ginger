

package laboratory.sniffer.detector.metrics;

import laboratory.sniffer.detector.entities.DetectorMethod;


public class NumberOfCallers extends UnaryMetric<Integer> {

    private NumberOfCallers(DetectorMethod detectorMethod, int value) {
        this.value = value;
        this.entity = detectorMethod;
        this.name = "number_of_callers";
    }

    public static NumberOfCallers createNumberOfCallers(DetectorMethod detectorMethod, int value) {
        NumberOfCallers numberOfCallers = new NumberOfCallers(detectorMethod, value);
        numberOfCallers.updateEntity();
        return numberOfCallers;
    }
}
