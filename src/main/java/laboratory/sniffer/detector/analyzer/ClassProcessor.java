package laboratory.sniffer.detector.analyzer;

import laboratory.sniffer.detector.entities.DetectorClass;
import laboratory.sniffer.detector.entities.DetectorModifiers;
import laboratory.sniffer.detector.entities.DetectorVariable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spoon.reflect.code.CtNewClass;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtConstructor;
import spoon.reflect.declaration.CtField;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.ModifierKind;
import spoon.reflect.reference.CtTypeReference;
import spoon.support.reflect.declaration.CtInterfaceImpl;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;



public class ClassProcessor extends TypeProcessor<CtClass> {
    private static final Logger logger = LoggerFactory.getLogger(ClassProcessor.class.getName());
    private static final URLClassLoader classloader;

    static {
        if (MainProcessor.paths == null) {
            classloader = new URLClassLoader(new URL[0]);
        } else {
            classloader = new URLClassLoader(MainProcessor.paths.toArray(new URL[MainProcessor.paths.size()]));
        }
    }

    @Override
    public void process(CtClass ctType) {
        String qualifiedName = ctType.getQualifiedName();


        String absolutePath= ctType.getPosition().getFile().getAbsolutePath();



        String relativePath = absolutePath.replaceFirst(MainProcessor.currentApp.getPath(),"");


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
        DetectorClass detectorClass = DetectorClass.createDetectorClass(qualifiedName, MainProcessor.currentApp, detectorModifiers, relativePath);

        MainProcessor.currentClass = detectorClass;

        handleProperties(ctType, detectorClass);

        handleAttachments(ctType, detectorClass);
        if (ctType.getQualifiedName().contains("$")) {

            detectorClass.setInnerClass(true);
        }

        processMethods(ctType);
    }

    @Override
    public boolean isToBeProcessed(CtClass candidate) {
        return super.isToBeProcessed(candidate) && !(candidate instanceof CtInterfaceImpl);
    }

    @Override
    public void processMethods(CtClass ctClass) {

        MethodProcessor methodProcessor = new MethodProcessor();
        ConstructorProcessor constructorProcessor = new ConstructorProcessor();
        for (Object o : ctClass.getMethods()) {

            methodProcessor.process((CtMethod) o);
        }
        CtConstructor ctConstructor;
        for (Object o : ctClass.getConstructors()) {

            ctConstructor = (CtConstructor) o;
            constructorProcessor.process(ctConstructor);
        }

    }

    @Override
    public void handleAttachments(CtClass ctClass, DetectorClass detectorClass) {
        if (ctClass.getSuperclass() != null) {

            detectorClass.setParentName(ctClass.getSuperclass().getQualifiedName());
        }
        for (CtTypeReference<?> ctTypeReference : ctClass.getSuperInterfaces()) {

            detectorClass.getInterfacesNames().add(ctTypeReference.getQualifiedName());
        }

        String modifierText;
        DetectorVariable detectorVariable;
        DetectorModifiers detectorModifiers1;
        boolean isStatic;
        for (CtField<?> ctField : (List<CtField>) ctClass.getFields()) {

            modifierText = ctField.getVisibility() == null ? "null" : ctField.getVisibility().toString();
            detectorModifiers1 = DataConverter.convertTextToModifier(modifierText);
            if (detectorModifiers1 == null) {
                detectorModifiers1 = DetectorModifiers.DEFAULT;
            }
            detectorVariable = DetectorVariable.createDetectorVariable(ctField.getSimpleName(), ctField.getType().getQualifiedName(), detectorModifiers1, detectorClass);
            isStatic = false;
            for (ModifierKind modifierKind : ctField.getModifiers()) {
                if (modifierKind.toString().toLowerCase().equals("static")) {
                    isStatic = true;
                    break;
                }
            }
            detectorVariable.setStatic(isStatic);
        }

    }

    @Override
    public void handleProperties(CtClass ctClass, DetectorClass detectorClass) {

        Integer doi = 0;
        boolean isApplication = false;
        boolean isContentProvider = false;
        boolean isAsyncTask = false;
        boolean isService = false;
        boolean isView = false;
        boolean isActivity = false;
        boolean isBroadcastReceiver = false;
        boolean isInterface = ctClass.isInterface();
        boolean isStatic = false;
        for (ModifierKind modifierKind : ctClass.getModifiers()) {
            if (modifierKind.toString().toLowerCase().equals("static")) {
                isStatic = true;
                break;
            }
        }

        CtTypeReference reference = findSuperClass(ctClass, doi);


        if (reference != null) {

            try {
                Class myRealClass = classloader.loadClass(reference.getQualifiedName());

                while (myRealClass.getSuperclass() != null) {
                    doi++;

                    if (myRealClass.getSimpleName().endsWith("Activity")) {
                        isActivity = true;
                        break;
                    } else if (myRealClass.getSimpleName().endsWith("ContentProvider")) {
                        isContentProvider = true;
                        break;
                    } else if (myRealClass.getSimpleName().endsWith("AsyncTask")) {
                        isAsyncTask = true;
                        break;
                    } else if (myRealClass.getSimpleName().endsWith("View")) {
                        isView = true;
                        break;
                    } else if (myRealClass.getSimpleName().endsWith("BroadcastReceiver")) {
                        isBroadcastReceiver = true;
                        break;
                    } else if (myRealClass.getSimpleName().endsWith("Service")) {
                        isService = true;
                        break;
                    } else if (myRealClass.getSimpleName().endsWith("Application")) {
                        isApplication = true;
                        break;
                    }
                    myRealClass = myRealClass.getSuperclass();
                }
            } catch (ClassNotFoundException | NoClassDefFoundError e) {
                logger.warn("Class Not Found or Definition Not Found ; message : " + e.getLocalizedMessage());
                logger.debug("switching to heuristic mode");
                if (ctClass.getSimpleName().endsWith("Activity")||ctClass.getSuperclass().getQualifiedName().endsWith("Activity")) {
                    isActivity = true;
                } else if (ctClass.getSimpleName().endsWith("ContentProvider")||ctClass.getSuperclass().getQualifiedName().endsWith("ContentProvider")) {
                    isContentProvider = true;
                } else if (ctClass.getSimpleName().endsWith("AsyncTask")||ctClass.getSuperclass().getQualifiedName().endsWith("AsyncTask")) {
                    isAsyncTask = true;
                } else if (ctClass.getSimpleName().endsWith("View")||ctClass.getSuperclass().getQualifiedName().endsWith("View")) {
                    isView = true;
                } else if (ctClass.getSimpleName().endsWith("BroadcastReceiver")||ctClass.getSuperclass().getQualifiedName().endsWith("BroadcastReceiver")) {
                    isBroadcastReceiver = true;
                } else if (ctClass.getSimpleName().endsWith("Service")||ctClass.getSuperclass().getQualifiedName().endsWith("Service")) {
                    isService = true;
                } else if (ctClass.getSimpleName().endsWith("Application")||ctClass.getSuperclass().getQualifiedName().endsWith("Application")) {
                    isApplication = true;
                }
            }
        }

        detectorClass.setInterface(isInterface);
        detectorClass.setActivity(isActivity);
        detectorClass.setStatic(isStatic);
        detectorClass.setAsyncTask(isAsyncTask);
        detectorClass.setContentProvider(isContentProvider);
        detectorClass.setBroadcastReceiver(isBroadcastReceiver);
        detectorClass.setService(isService);
        detectorClass.setView(isView);
        detectorClass.setApplication(isApplication);
        detectorClass.setDepthOfInheritance(doi);


    }


}
