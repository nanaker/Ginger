

package laboratory.sniffer.detector.metrics;

import laboratory.sniffer.detector.entities.DetectorApp;


public class NumberOfInterfaces extends UnaryMetric<Integer> {

    private NumberOfInterfaces(DetectorApp detectorApp, int value) {
        this.value = value;
        this.entity = detectorApp;
        this.name = "number_of_interfaces";
    }

    public static NumberOfInterfaces createNumberOfInterfaces(DetectorApp detectorApp, int value) {
        NumberOfInterfaces numberOfInterfaces = new NumberOfInterfaces(detectorApp, value);
        numberOfInterfaces.updateEntity();
        return numberOfInterfaces;
    }

}
