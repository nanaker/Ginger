

package laboratory.sniffer.detector.neo4j;


public class IGSQuery extends Query {

    private IGSQuery(QueryEngine queryEngine) {
        super(queryEngine, "IGS");
    }

    public static IGSQuery createIGSQuery(QueryEngine queryEngine) {
        return new IGSQuery(queryEngine);
    }

    @Override
    protected String getQuery(boolean details) {
        String query = "MATCH (a:App)-[:APP_OWNS_CLASS]->(cl:Class)-[:CLASS_OWNS_METHOD]->(m1:Method)-[:CALLS]->(m2:Method) WHERE (m2.is_setter OR m2.is_getter) AND (cl)-[:CLASS_OWNS_METHOD]->(m2) SET a.has_IGS=true " +
                "RETURN a.commit_number as commit_number, m1.app_key as key";
        if (details) {
            query += ",m1.full_name + m2.full_name as instance, a.commit_status as commit_status ";
        } else {
            query += ",count(m1) as IGS";
        }
        return query;
    }
}
