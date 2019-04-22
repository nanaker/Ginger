

package laboratory.sniffer.detector.metrics;

import laboratory.sniffer.detector.entities.DetectorClass;


public class CouplingBetweenObjects extends UnaryMetric<Integer> {

    private CouplingBetweenObjects(DetectorClass detectorClass) {
        this.value = detectorClass.getCouplingValue();
        this.entity = detectorClass;
        this.name = "coupling_between_object_classes";
    }

    public static CouplingBetweenObjects createCouplingBetweenObjects(DetectorClass detectorClass) {
        CouplingBetweenObjects couplingBetweenObjects = new CouplingBetweenObjects(detectorClass);
        couplingBetweenObjects.updateEntity();
        return couplingBetweenObjects;
    }
}
