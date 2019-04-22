

package laboratory.sniffer.detector.metrics;

import laboratory.sniffer.detector.entities.DetectorClass;

/**
 *
 * LCOM2 (Chidamber et Kemerer 1991)
 */
public class LackofCohesionInMethods extends UnaryMetric<Integer> {

    private LackofCohesionInMethods(DetectorClass detectorClass) {
        this.value = detectorClass.computeLCOM();
        this.entity = detectorClass;
        this.name = "lack_of_cohesion_in_methods";
    }

    public static LackofCohesionInMethods createLackofCohesionInMethods(DetectorClass detectorClass) {
        LackofCohesionInMethods couplingBetweenObjects = new LackofCohesionInMethods(detectorClass);
        couplingBetweenObjects.updateEntity();
        return couplingBetweenObjects;
    }
}
