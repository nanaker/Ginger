package laboratory.sniffer.detector.analyzer;

import laboratory.sniffer.detector.entities.DetectorModifiers;
import laboratory.sniffer.detector.entities.DetectorClass;
import laboratory.sniffer.detector.entities.DetectorVariable;
import spoon.reflect.code.CtNewClass;
import spoon.reflect.declaration.CtField;
import spoon.reflect.declaration.CtInterface;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.ModifierKind;
import spoon.reflect.reference.CtTypeReference;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InterfaceProcessor extends TypeProcessor<CtInterface> {
    private static final Logger logger = LoggerFactory.getLogger(InterfaceProcessor.class.getName());

    private static final URLClassLoader classloader;

    static {
        classloader = new URLClassLoader(MainProcessor.paths.toArray(new URL[MainProcessor.paths.size()]));
    }

    @Override
    public void process(CtInterface ctType) {


        String qualifiedName = ctType.getQualifiedName();
        String absolutePath= ctType.getPosition().getFile().getAbsolutePath();
        //this step is to solve the problem of Java Regex
        absolutePath = absolutePath.replaceAll("\\\\", "/");
        String s = MainProcessor.currentApp.getPath();
        s = s.replaceAll("\\\\", "/");
        String relativePath = absolutePath.replaceFirst(s, "");
        if (ctType.isAnonymous()) {
            String[] splitName = qualifiedName.split("\\$");
            qualifiedName = splitName[0] + "$" +
                    ((CtNewClass) ctType.getParent()).getType().getQualifiedName() + splitName[1];
        }
        String visibility = ctType.getVisibility() == null ? "null" : ctType.getVisibility().toString();
        DetectorModifiers detectorModifiers = DataConverter.convertTextToModifier(visibility);
        if (detectorModifiers == null) {
            detectorModifiers = DetectorModifiers.DEFAULT;
        }
        DetectorClass detectorClass = DetectorClass.createDetectorClass(qualifiedName, MainProcessor.currentApp, detectorModifiers,relativePath,ctType);
        MainProcessor.currentClass = detectorClass;
        handleProperties(ctType, detectorClass);
        handleAttachments(ctType, detectorClass);
        if (ctType.getQualifiedName().contains("$")) {
            detectorClass.setInnerClass(true);
        }
        processMethods(ctType);    }

    @Override
    public void processMethods(CtInterface ctInterface) {
        MethodProcessor methodProcessor = new MethodProcessor();
        for (Object o : ctInterface.getMethods()) {
            methodProcessor.process((CtMethod) o);
        }
    }

    @Override
    public void handleAttachments(CtInterface ctInterface, DetectorClass detectorClass) {
        if (ctInterface.getSuperclass() != null) {
            detectorClass.setParentName(ctInterface.getSuperclass().getQualifiedName());
        }
        for (CtTypeReference<?> ctTypeReference : ctInterface.getSuperInterfaces()) {
            detectorClass.getInterfacesNames().add(ctTypeReference.getQualifiedName());
        }
        String modifierText;
        DetectorModifiers detectorModifiers1;
        for (CtField<?> ctField : (List<CtField>) ctInterface.getFields()) {
            modifierText = ctField.getVisibility() == null ? "null" : ctField.getVisibility().toString();
            detectorModifiers1 = DataConverter.convertTextToModifier(modifierText);
            if (detectorModifiers1 == null) {
                detectorModifiers1 = DetectorModifiers.PROTECTED;
            }
            DetectorVariable.createDetectorVariable(ctField.getSimpleName(), ctField.getType().getQualifiedName(), detectorModifiers1, detectorClass);
        }

    }

    @Override
    public void handleProperties(CtInterface ctInterface, DetectorClass detectorClass) {
        Integer doi = 0;
        boolean isStatic = false;
        for (ModifierKind modifierKind : ctInterface.getModifiers()) {
            if (modifierKind.toString().toLowerCase().equals("static")) {
                isStatic = true;
                break;
            }
        }

        CtTypeReference reference = findSuperClass(ctInterface, doi);
        if (reference != null) {
            try {
                Class myRealClass;
                myRealClass = classloader.loadClass(reference.getQualifiedName());
                while (myRealClass.getSuperclass() != null) {
                    doi++;
                    myRealClass = myRealClass.getSuperclass();
                }
            } catch (ClassNotFoundException e) {
                logger.warn("Class Not Found; message : " + e.getLocalizedMessage());
            } catch (NoClassDefFoundError e) {
                logger.warn("No Class Def Found : " + e.getLocalizedMessage());
            }
        }

        detectorClass.setInterface(true);
        detectorClass.setDepthOfInheritance(doi);
        detectorClass.setStatic(isStatic);

    }
}
