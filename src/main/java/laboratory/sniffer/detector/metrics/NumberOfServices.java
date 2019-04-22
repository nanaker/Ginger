

package laboratory.sniffer.detector.metrics;

import laboratory.sniffer.detector.entities.DetectorApp;


public class NumberOfServices extends UnaryMetric<Integer> {

    private NumberOfServices(DetectorApp detectorApp, int value) {
        this.value = value;
        this.entity = detectorApp;
        this.name = "number_of_services";
    }

    public static NumberOfServices createNumberOfServices(DetectorApp detectorApp, int value) {
        NumberOfServices numberOfServices = new NumberOfServices(detectorApp, value);
        numberOfServices.updateEntity();
        return numberOfServices;
    }

}
