

package laboratory.sniffer.detector.metrics;

import laboratory.sniffer.detector.entities.DetectorMethod;


public class NumberOfParameters extends UnaryMetric<Integer> {

    private NumberOfParameters(DetectorMethod detectorMethod, int value) {
        this.value = value;
        this.entity = detectorMethod;
        this.name = "number_of_parameters";
    }

    public static NumberOfParameters createNumberOfParameters(DetectorMethod detectorMethod, int value) {
        NumberOfParameters numberOfParameters = new NumberOfParameters(detectorMethod, value);
        numberOfParameters.updateEntity();
        return numberOfParameters;
    }

}
