package laboratory.sniffer.detector.analyzer;

import laboratory.sniffer.detector.entities.DetectorModifiers;


public class DataConverter {

    public static DetectorModifiers convertTextToModifier(String text) {
        if (text.toLowerCase().equals("public")) {
            return DetectorModifiers.PUBLIC;
        } else if (text.toLowerCase().equals("private")) {
            return DetectorModifiers.PRIVATE;
        } else if (text.toLowerCase().equals("protected")) {
            return DetectorModifiers.PROTECTED;
        } else {
            return null;
        }
    }
}
