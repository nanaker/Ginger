

package laboratory.sniffer.detector.neo4j;


public class UnsuitedLRUCacheSizeQuery extends Query {

    private UnsuitedLRUCacheSizeQuery(QueryEngine queryEngine) {
        super(queryEngine, "UCS");
    }

    public static UnsuitedLRUCacheSizeQuery createUnsuitedLRUCacheSizeQuery(QueryEngine queryEngine) {
        return new UnsuitedLRUCacheSizeQuery(queryEngine);
    }

    @Override
    protected String getQuery(boolean details) {
        String query = "Match (a:App)-[:APP_OWNS_CLASS]->(cl:Class)-[:CLASS_OWNS_METHOD]->(m:Method)-[:CALLS]->(e:ExternalMethod {full_name:'<init>#android.util.LruCache'}) WHERE NOT (m)-[:CALLS]->(:ExternalMethod {full_name:'getMemoryClass#android.app.ActivityManager'})  " +
                "return DISTINCT a.commit_number as commit_number, m.app_key as key, cl.file_path as file_path";
        if (details) {
            query += ",m.full_name as instance";
        } else {
            query += ",count(m) as UCS";
        }
        return query;
    }
}
