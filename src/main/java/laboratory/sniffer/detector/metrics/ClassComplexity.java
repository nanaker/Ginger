

package laboratory.sniffer.detector.metrics;

import laboratory.sniffer.detector.entities.DetectorClass;


public class ClassComplexity extends UnaryMetric<Integer> {

    private ClassComplexity(DetectorClass detectorClass) {
        this.value = detectorClass.computeComplexity();
        this.entity = detectorClass;
        this.name = "class_complexity";
    }

    public static ClassComplexity createClassComplexity(DetectorClass detectorClass) {
        ClassComplexity classComplexity =  new ClassComplexity(detectorClass);
        classComplexity.updateEntity();
        return classComplexity;
    }

}
