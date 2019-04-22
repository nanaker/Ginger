

package laboratory.sniffer.detector.metrics;

import laboratory.sniffer.detector.entities.DetectorClass;


public class DepthOfInheritance extends UnaryMetric<Integer> {
    private DepthOfInheritance(DetectorClass detectorClass, int value) {
        this.value = value;
        this.entity = detectorClass;
        this.name = "depth_of_inheritance";
    }

    public static DepthOfInheritance createDepthOfInheritance(DetectorClass detectorClass, int value) {
        DepthOfInheritance depthOfInheritance = new DepthOfInheritance(detectorClass, value);
        depthOfInheritance.updateEntity();
        return depthOfInheritance;
    }

}
