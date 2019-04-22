

package laboratory.sniffer.detector.metrics;

import laboratory.sniffer.detector.entities.DetectorMethod;


public class CyclomaticComplexity extends UnaryMetric<Integer> {

    private CyclomaticComplexity(DetectorMethod detectorMethod, int value) {
        this.value = value;
        this.entity = detectorMethod;
        this.name = "cyclomatic_complexity";
    }

    public static CyclomaticComplexity createCyclomaticComplexity(DetectorMethod detectorMethod, int value) {
        CyclomaticComplexity cyclomaticComplexity =  new CyclomaticComplexity(detectorMethod, value);
        cyclomaticComplexity.updateEntity();
        return cyclomaticComplexity;
    }

}
