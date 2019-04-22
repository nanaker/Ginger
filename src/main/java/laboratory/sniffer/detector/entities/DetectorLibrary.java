package laboratory.sniffer.detector.entities;


public class DetectorLibrary extends Entity {

    DetectorApp detectorApp;

    private DetectorLibrary(String name, DetectorApp detectorApp) {
        this.name=name;
        this.detectorApp = detectorApp;
    }

    public static DetectorLibrary createDetectorLibrary(String name, DetectorApp detectorApp){
        DetectorLibrary detectorLibrary =new DetectorLibrary(name, detectorApp);
        detectorApp.addDetectorLibrary(detectorLibrary);
        return detectorLibrary;
    }


    public DetectorApp getDetectorApp() {
        return detectorApp;
    }

    public void setDetectorApp(DetectorApp detectorApp) {
        this.detectorApp = detectorApp;
    }
}
