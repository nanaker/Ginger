

package laboratory.sniffer.detector.metrics;

import laboratory.sniffer.detector.entities.DetectorMethod;


public class NumberOfLines extends UnaryMetric<Integer> {

    private NumberOfLines(DetectorMethod detectorMethod, int value) {
        this.value = value;
        this.entity = detectorMethod;
        this.name = "number_of_lines";
    }

    public static NumberOfLines createNumberOfLines(DetectorMethod detectorMethod, int value) {
        NumberOfLines numberOfLines = new NumberOfLines(detectorMethod, value);
        numberOfLines.updateEntity();
        return numberOfLines;
    }

}
