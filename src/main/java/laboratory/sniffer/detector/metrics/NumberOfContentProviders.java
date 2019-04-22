

package laboratory.sniffer.detector.metrics;

import laboratory.sniffer.detector.entities.DetectorApp;


public class NumberOfContentProviders extends UnaryMetric<Integer> {

    private NumberOfContentProviders(DetectorApp detectorApp, int value) {
        this.value = value;
        this.entity = detectorApp;
        this.name = "number_of_content_providers";
    }

    public static NumberOfContentProviders createNumberOfContentProviders(DetectorApp detectorApp, int value) {
        NumberOfContentProviders numberOfContentProviders = new NumberOfContentProviders(detectorApp, value);
        numberOfContentProviders.updateEntity();
        return numberOfContentProviders;
    }

}
