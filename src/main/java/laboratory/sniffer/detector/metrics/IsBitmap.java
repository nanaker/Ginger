

package laboratory.sniffer.detector.metrics;

import laboratory.sniffer.detector.entities.DetectorClass;

public class IsBitmap extends UnaryMetric<Boolean> {

    private IsBitmap(DetectorClass detectorClass, boolean value){
        this.value = value;
        this.entity = detectorClass;
        this.name = "is_bitmap";
    }

    public static IsBitmap createIsBitmap(DetectorClass detectorClass, boolean value) {
        IsBitmap isBitmap = new IsBitmap(detectorClass, value);
        isBitmap.updateEntity();
        return isBitmap;
    }

}
