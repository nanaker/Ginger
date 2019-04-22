

package laboratory.sniffer.detector.neo4j;


public class HashMapUsageQuery extends Query {

    private HashMapUsageQuery(QueryEngine queryEngine) {
        super(queryEngine, "HMU");
    }

    public static HashMapUsageQuery createHashMapUsageQuery(QueryEngine queryEngine) {
        return new HashMapUsageQuery(queryEngine);
    }

    @Override
    protected String getQuery(boolean details) {
        String query = "MATCH (a:App)-[:APP_OWNS_CLASS]->(cl:Class)-[:CLASS_OWNS_METHOD]->(m:Method)-[:CALLS]->(e:ExternalMethod{full_name:'<init>#java.util.HashMap'})  " +
                "return DISTINCT a.commit_number as commit_number, m.app_key as key, cl.file_path as file_path";
        if (details) {
            query += ",m.full_name as instance";
        } else {
            query += ", count(m) as HMU";
        }
        return query;
    }

}
