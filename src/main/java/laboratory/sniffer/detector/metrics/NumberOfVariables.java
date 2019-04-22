

package laboratory.sniffer.detector.metrics;

import laboratory.sniffer.detector.entities.DetectorApp;


public class NumberOfVariables extends UnaryMetric<Integer> {

    private NumberOfVariables(DetectorApp detectorApp, int value) {
        this.value = value;
        this.entity = detectorApp;
        this.name = "number_of_variables";
    }

    public static NumberOfVariables createNumberOfVariables(DetectorApp detectorApp, int value) {
        NumberOfVariables numberOfVariables = new NumberOfVariables(detectorApp, value);
        numberOfVariables.updateEntity();
        return numberOfVariables;
    }

}
