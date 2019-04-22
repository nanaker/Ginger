

package laboratory.sniffer.detector.metrics;

import laboratory.sniffer.detector.entities.DetectorApp;


public class NumberOfActivities extends UnaryMetric<Integer> {

    private NumberOfActivities(DetectorApp detectorApp, int value) {
        this.value = value;
        this.entity = detectorApp;
        this.name = "number_of_activities";
    }

    public static NumberOfActivities createNumberOfActivities(DetectorApp detectorApp, int value) {
        NumberOfActivities numberOfActivities =  new NumberOfActivities(detectorApp, value);
        numberOfActivities.updateEntity();
        return numberOfActivities;
    }

}
