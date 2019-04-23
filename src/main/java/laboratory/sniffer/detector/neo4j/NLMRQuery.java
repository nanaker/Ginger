

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
        String query = "MATCH (cl:Class)  WHERE exists(cl.is_activity)  " +
                "RETURN CASE WHEN (not (cl:Class)-[:CLASS_OWNS_METHOD]->(:Method { name: 'onLowMemory' })) = true " +
                "THEN false ELSE true  END AS has_onLowMemory," +
                "CASE WHEN (NOT (cl)-[:EXTENDS]->(:Class)) = true THEN false ELSE true  END AS extend_class" +
                ",cl.class_complexity as class_complexity ";
        if (details) {
            query += ",cl.name as full_name order by class_complexity  desc";
        } else {
            query += ",count(cl) as NLMR";
        }
        return query;
    }
}
