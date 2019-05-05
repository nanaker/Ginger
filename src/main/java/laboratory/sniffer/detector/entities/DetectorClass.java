

package laboratory.sniffer.detector.entities;

import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtType;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;


public class DetectorClass extends Entity{
    private DetectorApp detectorApp;
    private DetectorClass parent;
    private CtType classe;
    //parent name to cover library case
    private String parentName;
    private int children;
    private int complexity;
    private Set<DetectorClass> coupled;
    private Set<DetectorMethod> detectorMethods;
    private Set<DetectorVariable> detectorVariables;
    private Set<DetectorClass> interfaces;
    private DetectorModifiers modifier;
    private boolean isInterface;
    private boolean isStatic;
    private boolean isActivity;
    private boolean isBroadcastReceiver;
    private boolean isService;
    private boolean isContentProvider;
    private boolean isView;
    private boolean isAsyncTask;
    private boolean isApplication;
    private boolean isInnerClass;
    private int depthOfInheritance;
    private ArrayList<String> interfacesNames;
    private String path;

    public CtType getClasse() {
        return classe;
    }

    public String getPath() {
        return path;
    }

    public int getComplexity() {
        return complexity;
    }

    public DetectorModifiers getModifier() {
        return modifier;
    }

    public Set<DetectorVariable> getDetectorVariables() {
        return detectorVariables;
    }

    public Set<DetectorMethod> getDetectorMethods() {
        return detectorMethods;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    private DetectorClass(String name, DetectorApp detectorApp, DetectorModifiers modifier) {
        this.setName(name);
        this.detectorApp = detectorApp;
        this.children = 0;
        this.detectorMethods = new HashSet<>();
        this.detectorVariables = new HashSet<>();
        this.coupled = new HashSet<>();
        this.interfaces = new HashSet<>();
        this.modifier = modifier;
        this.isInterface=false;
        this.isStatic=false;
        this.isActivity=false;
        this.isApplication=false;
        this.isAsyncTask=false;
        this.isService=false;
        this.isContentProvider=false;
        this.isBroadcastReceiver=false;
        this.isInnerClass=false;
        this.isView=false;
        this.depthOfInheritance=0;
        this.interfacesNames=new ArrayList<>();
        this.complexity=0;
    }

    private DetectorClass(String name, DetectorApp detectorApp, DetectorModifiers modifier, String path,CtType classe) {
        this.setName(name);
        this.classe=classe;
        this.path=path;
        this.detectorApp = detectorApp;
        this.children = 0;
        this.detectorMethods = new HashSet<>();
        this.detectorVariables = new HashSet<>();
        this.coupled = new HashSet<>();
        this.interfaces = new HashSet<>();
        this.modifier = modifier;
        this.isInterface=false;
        this.isStatic=false;
        this.isActivity=false;
        this.isApplication=false;
        this.isAsyncTask=false;
        this.isService=false;
        this.isContentProvider=false;
        this.isBroadcastReceiver=false;
        this.isInnerClass=false;
        this.isView=false;
        this.depthOfInheritance=0;
        this.interfacesNames=new ArrayList<>();
        this.complexity=0;
    }


    public static DetectorClass createDetectorClass(String name, DetectorApp detectorApp, DetectorModifiers modifier) {
        DetectorClass detectorClass = new DetectorClass(name, detectorApp, modifier);
        detectorApp.addDetectorClass(detectorClass);
        return detectorClass;
    }

    public static DetectorClass createDetectorClass(String name, DetectorApp detectorApp, DetectorModifiers modifier, String path,CtType classe) {
        DetectorClass detectorClass = new DetectorClass(name, detectorApp, modifier, path,classe);
        detectorApp.addDetectorClass(detectorClass);

        return detectorClass;
    }

    public DetectorClass getParent() {
        return parent;
    }

    public Set<DetectorClass> getInterfaces(){ return interfaces;}

    public void setParent(DetectorClass parent) {
        this.parent = parent;
    }

    public void addDetectorMethod(DetectorMethod detectorMethod){
        detectorMethods.add(detectorMethod);
    }

    public DetectorApp getDetectorApp() {
        return detectorApp;
    }

    public void setDetectorApp(DetectorApp detectorApp) {
        this.detectorApp = detectorApp;
    }


    public void addChild() { children += 1;}

    public int computeComplexity() {

        for(DetectorMethod detectorMethod : this.getDetectorMethods()){
            this.complexity+= detectorMethod.getComplexity();
        }
        return this.complexity;
    }

    public int getChildren() { return children; }

    public void coupledTo(DetectorClass detectorClass){ coupled.add(detectorClass);}

    public void implement(DetectorClass detectorClass){ interfaces.add(detectorClass);}

    public int getCouplingValue(){ return coupled.size();}

    public int computeLCOM(){
        Object methods[] = detectorMethods.toArray();
        int methodCount = methods.length;
        int haveFieldInCommon = 0;
        int noFieldInCommon  = 0;
        for(int i=0; i< methodCount;i++){
            for(int j=i+1; j < methodCount; j++){
                if( ((DetectorMethod) methods[i]).haveCommonFields((DetectorMethod) methods[j])){
                    haveFieldInCommon++;
                }else{
                    noFieldInCommon++;
                }
            }
        }
        int LCOM =  noFieldInCommon - haveFieldInCommon;
        return LCOM > 0 ? LCOM : 0;
    }

    /**
        Get the NPath complexity of the entire program
        The NPath complexity is just the combinatorial of the cyclomatic complexity
     **/
    public double computeNPathComplexity() {
        return Math.pow(2.0, (double) this.complexity);
    }

    public void addDetectorVariable(DetectorVariable detectorVariable) {
        detectorVariables.add(detectorVariable);
    }

    public DetectorVariable findVariable(String name){
        // First we are looking to the field declared by this class (any modifiers)
        for (DetectorVariable detectorVariable : detectorVariables){
            if (detectorVariable.getName().equals(name)) return detectorVariable;
        }
        //otherwise we return null
        return null;
    }

    public boolean isInterface() {
        return isInterface;
    }

    public void setInterface(boolean anInterface) {
        isInterface = anInterface;
    }

    public boolean isStatic() {
        return isStatic;
    }

    public void setStatic(boolean aStatic) {
        isStatic = aStatic;
    }

    public boolean isActivity() {
        return isActivity;
    }

    public void setActivity(boolean activity) {
        isActivity = activity;
    }

    public ArrayList<String> getInterfacesNames() {
        return interfacesNames;
    }

    public void setInterfacesNames(ArrayList<String> interfacesNames) {
        this.interfacesNames = interfacesNames;
    }

    public DetectorMethod getCalledDetectorMethod(String methodName){
        for(DetectorMethod detectorMethod : this.getDetectorMethods()){
            if(detectorMethod.getName().equals(methodName)){
                return detectorMethod;
            }
        }
        //TODO check the return type and modifier in the super classes
        DetectorMethod calledMethod=  DetectorMethod.createDetectorMethod(methodName, DetectorModifiers.PUBLIC,"Uknown",this);
        calledMethod.setOverride(true);
        return calledMethod;

    }

    public int getDepthOfInheritance() {
        return depthOfInheritance;
    }

    public void setDepthOfInheritance(int depthOfInheritance) {
        this.depthOfInheritance = depthOfInheritance;
    }

    public void setComplexity(int complexity) {
        this.complexity = complexity;
    }

    public void setChildren(int children) {
        this.children = children;
    }

    public Set<DetectorClass> getCoupled() {
        return coupled;
    }

    public void setCoupled(Set<DetectorClass> coupled) {
        this.coupled = coupled;
    }

    public void setDetectorMethods(Set<DetectorMethod> detectorMethods) {
        this.detectorMethods = detectorMethods;
    }

    public void setDetectorVariables(Set<DetectorVariable> detectorVariables) {
        this.detectorVariables = detectorVariables;
    }

    public void setInterfaces(Set<DetectorClass> interfaces) {
        this.interfaces = interfaces;
    }

    public void setModifier(DetectorModifiers modifier) {
        this.modifier = modifier;
    }

    public boolean isBroadcastReceiver() {
        return isBroadcastReceiver;
    }

    public void setBroadcastReceiver(boolean broadcastReceiver) {
        isBroadcastReceiver = broadcastReceiver;
    }

    public boolean isService() {
        return isService;
    }

    public void setService(boolean service) {
        isService = service;
    }

    public boolean isContentProvider() {
        return isContentProvider;
    }

    public void setContentProvider(boolean contentProvider) {
        isContentProvider = contentProvider;
    }

    public boolean isView() {
        return isView;
    }

    public void setView(boolean view) {
        isView = view;
    }

    public boolean isAsyncTask() {
        return isAsyncTask;
    }

    public void setAsyncTask(boolean asyncTask) {
        isAsyncTask = asyncTask;
    }

    public boolean isApplication() {
        return isApplication;
    }

    public void setApplication(boolean application) {
        isApplication = application;
    }

    public boolean isInnerClass() {
        return isInnerClass;
    }

    public void setInnerClass(boolean innerClass) {
        isInnerClass = innerClass;
    }
}
