

package laboratory.sniffer.detector.neo4j;


public class MIMQuery extends Query {

    private MIMQuery(QueryEngine queryEngine) {
        super(queryEngine, "MIM");
    }

    public static MIMQuery createMIMQuery(QueryEngine queryEngine) {
        return new MIMQuery(queryEngine);
    }

    @Override
    protected String getQuery(boolean details) {
        String query = "MATCH (a:App)-[:APP_OWNS_CLASS]->(cl:Class)-[:CLASS_OWNS_METHOD]->(m1:Method) " +
                "WHERE m1.number_of_callers>0 " +
                "AND NOT exists(m1.is_static) " +
                "AND NOT exists(m1.is_override) " +
                "AND NOT (m1)-[:USES]->(:Variable) " +
                "AND NOT (m1)-[:CALLS]->(:ExternalMethod) " +
                "AND NOT (m1)-[:CALLS]->(:Method) " +
                "AND NOT exists(m1.is_init) " +
                "AND NOT exists(cl.is_interface) " +
                "RETURN DISTINCT a.commit_number as commit_number, m1.app_key as key, cl.file_path as file_path";
        if (details) {
            query += ",m1.full_name as instance";
        } else {
            query += ",count(m1) as MIM";
        }
        return query;
    }
}
