

package laboratory.sniffer.detector.metrics;

import laboratory.sniffer.detector.entities.DetectorExternalArgument;


public class IsARGB8888 extends UnaryMetric<Boolean> {

    private IsARGB8888(DetectorExternalArgument detectorExternalArgument, boolean value) {
        this.value = value;
        this.entity = detectorExternalArgument;
        this.name = "is_argb_8888";
    }

    public static IsARGB8888 createIsARGB8888(DetectorExternalArgument detectorExternalArgument, boolean value) {
        IsARGB8888 isARGB8888 = new IsARGB8888(detectorExternalArgument, value);
        isARGB8888.updateEntity();
        return isARGB8888;
    }

}
