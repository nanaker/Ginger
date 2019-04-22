

package laboratory.sniffer.detector.metrics;

import laboratory.sniffer.detector.entities.DetectorClass;


public class NumberOfChildren extends UnaryMetric<Integer> {

    private NumberOfChildren(DetectorClass detectorClass) {
        this.value = detectorClass.getChildren();
        this.entity = detectorClass;
        this.name = "number_of_children";
    }

    public static NumberOfChildren createNumberOfChildren(DetectorClass detectorClass) {
        NumberOfChildren numberOfChildren = new NumberOfChildren(detectorClass);
        numberOfChildren.updateEntity();
        return numberOfChildren;
    }
}
