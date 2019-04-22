

package laboratory.sniffer.detector.metrics;

import laboratory.sniffer.detector.entities.DetectorApp;


public class NumberOfViews extends UnaryMetric<Integer> {

    private NumberOfViews(DetectorApp detectorApp, int value) {
        this.value = value;
        this.entity = detectorApp;
        this.name = "number_of_views";
    }

    public static NumberOfViews createNumberOfViews(DetectorApp detectorApp, int value) {
        NumberOfViews numberOfViews = new NumberOfViews(detectorApp, value);
        numberOfViews.updateEntity();
        return numberOfViews;
    }

}
