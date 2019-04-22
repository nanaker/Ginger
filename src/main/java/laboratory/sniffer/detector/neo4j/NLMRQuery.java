

package laboratory.sniffer.detector.neo4j;

public class NLMRQuery extends Query {

    private NLMRQuery(QueryEngine queryEngine) {
        super(queryEngine, "NLMR");
    }

    public static NLMRQuery createNLMRQuery(QueryEngine queryEngine) {
        return new NLMRQuery(queryEngine);
    }

    @Override
    protected String getQuery(boolean details) {
        String query = "MATCH (a:App)-[:APP_OWNS_CLASS]->(cl:Class) WHERE exists(cl.is_activity) AND NOT (cl:Class)-[:CLASS_OWNS_METHOD]->(:Method { name: 'onLowMemory' }) AND NOT (cl)-[:EXTENDS]->(:Class) " +
                "RETURN DISTINCT a.commit_number as commit_number, cl.app_key as key, cl.file_path as file_path";
        if (details) {
            query += ",cl.name as instance";
        } else {
            query += ",count(cl) as NLMR";
        }
        return query;
    }
}
