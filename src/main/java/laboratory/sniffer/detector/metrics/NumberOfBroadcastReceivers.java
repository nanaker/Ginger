

package laboratory.sniffer.detector.metrics;

import laboratory.sniffer.detector.entities.DetectorApp;


public class NumberOfBroadcastReceivers extends UnaryMetric<Integer> {

    private NumberOfBroadcastReceivers(DetectorApp detectorApp, int value) {
        this.value = value;
        this.entity = detectorApp;
        this.name = "number_of_broadcast_receivers";
    }

    public static NumberOfBroadcastReceivers createNumberOfBroadcastReceivers(DetectorApp detectorApp, int value) {
        NumberOfBroadcastReceivers numberOfBroadcastReceivers =  new NumberOfBroadcastReceivers(detectorApp, value);
        numberOfBroadcastReceivers.updateEntity();
        return numberOfBroadcastReceivers;
    }

}
