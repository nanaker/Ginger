

package laboratory.sniffer.detector.metrics;



import laboratory.sniffer.detector.entities.DetectorClass;

public class IsBroadcastReceiver extends UnaryMetric<Boolean> {

    private IsBroadcastReceiver(DetectorClass entity, boolean value) {
        this.value = value;
        this.entity = entity;
        this.name = "is_broadcast_receiver";
    }

    public static IsBroadcastReceiver createIsBroadcastReceiver(DetectorClass entity, boolean value) {
        IsBroadcastReceiver isBroadcastReceiver= new IsBroadcastReceiver(entity, value);
        isBroadcastReceiver.updateEntity();
        return isBroadcastReceiver;
    }
}
