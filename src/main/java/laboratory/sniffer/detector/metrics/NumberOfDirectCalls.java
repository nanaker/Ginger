

package laboratory.sniffer.detector.metrics;

import laboratory.sniffer.detector.entities.DetectorMethod;


public class NumberOfDirectCalls extends UnaryMetric<Integer> {

    private NumberOfDirectCalls(DetectorMethod detectorMethod, int value) {
        this.value = value;
        this.entity = detectorMethod;
        this.name = "number_of_direct_calls";
    }

    public static NumberOfDirectCalls createNumberOfDirectCalls(DetectorMethod detectorMethod, int value) {
        NumberOfDirectCalls numberOfDirectCalls = new NumberOfDirectCalls(detectorMethod, value);
        numberOfDirectCalls.updateEntity();
        return numberOfDirectCalls;
    }
}
