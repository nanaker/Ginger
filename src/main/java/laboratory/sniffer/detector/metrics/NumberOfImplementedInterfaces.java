

package laboratory.sniffer.detector.metrics;

import laboratory.sniffer.detector.entities.DetectorClass;


public class NumberOfImplementedInterfaces extends UnaryMetric<Integer> {

    private NumberOfImplementedInterfaces(DetectorClass detectorClass, int value) {
        this.value = value;
        this.entity = detectorClass;
        this.name = "number_of_implemented_interfaces";
    }

    public static NumberOfImplementedInterfaces createNumberOfImplementedInterfaces(DetectorClass detectorClass, int value) {
        NumberOfImplementedInterfaces numberOfImplementedInterfaces =new NumberOfImplementedInterfaces(detectorClass, value);
        numberOfImplementedInterfaces.updateEntity();
        return numberOfImplementedInterfaces;
    }

}
