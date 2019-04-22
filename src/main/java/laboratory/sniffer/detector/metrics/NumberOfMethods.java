

package laboratory.sniffer.detector.metrics;

import laboratory.sniffer.detector.entities.Entity;


public class NumberOfMethods extends UnaryMetric<Integer> {

    private NumberOfMethods(Entity entity, int value) {
        this.value = value;
        this.entity = entity;
        this.name = "number_of_methods";
    }

    public static NumberOfMethods createNumberOfMethods(Entity entity, int value) {
        NumberOfMethods numberOfMethods = new NumberOfMethods(entity, value);
        numberOfMethods.updateEntity();
        return numberOfMethods;
    }
}
