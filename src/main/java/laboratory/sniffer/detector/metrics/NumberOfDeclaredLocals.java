

package laboratory.sniffer.detector.metrics;

import laboratory.sniffer.detector.entities.DetectorMethod;


public class NumberOfDeclaredLocals extends UnaryMetric<Integer> {

    private NumberOfDeclaredLocals(DetectorMethod detectorMethod, int value) {
        this.value = value;
        this.entity = detectorMethod;
        this.name = "number_of_declared_locals";
    }

    public static NumberOfDeclaredLocals createNumberOfDeclaredLocals(DetectorMethod detectorMethod, int value) {
        NumberOfDeclaredLocals numberOfDeclaredLocals = new NumberOfDeclaredLocals(detectorMethod, value);
        numberOfDeclaredLocals.updateEntity();
        return  numberOfDeclaredLocals;
    }

}
