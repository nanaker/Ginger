

package laboratory.sniffer.detector.neo4j;


public class LICQuery extends Query {

    private LICQuery(QueryEngine queryEngine) {
        super(queryEngine, "LIC");
    }

    public static LICQuery createLICQuery(QueryEngine queryEngine) {
        return new LICQuery(queryEngine);
    }


    @Override
    protected String getQuery(boolean details) {
        String query = "MATCH (a:App)-[:APP_OWNS_CLASS]->(cl:Class) WHERE exists(cl.is_inner_class) AND NOT exists(cl.is_static) " +
                "RETURN DISTINCT a.commit_number as commit_number, cl.app_key as key, cl.file_path as file_path";
        if (details) {
            query += ",cl.name as instance";
        } else {
            query += ",count(cl) as LIC";
        }
        return query;
    }


}
