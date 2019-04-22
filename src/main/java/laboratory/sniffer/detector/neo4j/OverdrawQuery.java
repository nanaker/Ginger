

package laboratory.sniffer.detector.neo4j;


public class OverdrawQuery extends Query {

    private OverdrawQuery(QueryEngine queryEngine) {
        super(queryEngine, "UIO");
    }

    public static OverdrawQuery createOverdrawQuery(QueryEngine queryEngine) {
        return new OverdrawQuery(queryEngine);
    }

    @Override
    protected String getQuery(boolean details) {
        String query = "MATCH (a:App)-[:APP_OWNS_CLASS]->(cl:Class{is_view:true})-[:CLASS_OWNS_METHOD]->(n:Method{name:\"onDraw\"})-[:METHOD_OWNS_ARGUMENT]->(:Argument{position:0,name:\"android.graphics.Canvas\"}) \n" +
                "WHERE NOT (n)-[:CALLS]->(:ExternalMethod{full_name:\"clipRect#android.graphics.Canvas\"}) AND NOT (n)-[:CALLS]->(:ExternalMethod{full_name:\"quickReject#android.graphics.Canvas\"})\n" +
                "  RETURN DISTINCT a.commit_number as commit_number, n.app_key as key, cl.file_path as file_path";
        if (details) {
            query += ", n.full_name as instance";
        } else {
            query += ",count(n) as UIO";
        }
        return query;
    }
}
