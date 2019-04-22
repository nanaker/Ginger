

package laboratory.sniffer.detector.neo4j;


public class TrackingHardwareIdQuery extends Query {

    private TrackingHardwareIdQuery(QueryEngine queryEngine) {
        super(queryEngine, "THI");
    }

    public static TrackingHardwareIdQuery createTrackingHardwareIdQuery(QueryEngine queryEngine) {
        return new TrackingHardwareIdQuery(queryEngine);
    }

    @Override
    protected String getQuery(boolean details) {
        String query = "MATCH (m1:Method)-[:CALLS]->(:ExternalMethod { full_name:'getDeviceId#android.telephony.TelephonyManager'}) RETURN m1.app_key as app_key";
        if (details) {
            query += ",m1.full_name as full_name";
        } else {
            query += ",count(m1) as THI";
        }
        return query;
    }
}
