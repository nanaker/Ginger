

package laboratory.sniffer.detector.metrics;

import laboratory.sniffer.detector.entities.DetectorApp;


public class NumberOfArgb8888 extends UnaryMetric<Integer> {

    private NumberOfArgb8888(DetectorApp detectorApp, int nbOfArgb8888) {
        this.value = nbOfArgb8888;
        this.entity = detectorApp;
        this.name = "number_of_argb_8888";
    }

    public static NumberOfArgb8888 createNumberOfArgb8888(DetectorApp detectorApp, int nbOfArgb8888) {
        NumberOfArgb8888 numberOfArgb8888 = new NumberOfArgb8888(detectorApp, nbOfArgb8888);
        numberOfArgb8888.updateEntity();
        return numberOfArgb8888;
    }

}
