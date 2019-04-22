

package laboratory.sniffer.detector.metrics;

import laboratory.sniffer.detector.entities.DetectorClass;


public class NPathComplexity extends UnaryMetric<Integer> {

    private NPathComplexity(DetectorClass detectorClass) {
        this.value = detectorClass.computeNPathComplexity();
        this.entity = detectorClass;
        this.name = "npath_complexity";
    }

    public static NPathComplexity createNPathComplexity(DetectorClass detectorClass) {
        NPathComplexity npath_complexity = new NPathComplexity(detectorClass);
        npath_complexity.updateEntity();
        return npath_complexity;
    }

}
