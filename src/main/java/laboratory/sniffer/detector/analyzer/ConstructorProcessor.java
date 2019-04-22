package laboratory.sniffer.detector.analyzer;

import laboratory.sniffer.detector.entities.DetectorMethod;
import spoon.reflect.declaration.CtConstructor;


public class ConstructorProcessor extends ExecutableProcessor<CtConstructor> {
    @Override
    protected void process(CtConstructor ctExecutable, DetectorMethod detectorMethod) {
        detectorMethod.setConstructor(true);
    }
}
