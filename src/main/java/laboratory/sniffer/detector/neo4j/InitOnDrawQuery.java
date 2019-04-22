

package laboratory.sniffer.detector.neo4j;


public class InitOnDrawQuery extends Query {

    private InitOnDrawQuery(QueryEngine queryEngine) {
        super(queryEngine, "IOD");
    }

    public static InitOnDrawQuery createInitOnDrawQuery(QueryEngine queryEngine) {
        return new InitOnDrawQuery(queryEngine);
    }

    @Override
    protected String getQuery(boolean details) {
        String query = "MATCH (a:App)-[:APP_OWNS_CLASS]->(cl:Class{is_view:true})-[:CLASS_OWNS_METHOD]->(n:Method{name:'onDraw'})-[:CALLS]->({name:'<init>'})" +
                "return DISTINCT a.commit_number as commit_number,  n.app_key as key, cl.file_path as file_path ";
        if (details) {
            query += ",n.full_name as instance";
        } else {
            query += ",count(n) as IOD";
        }
        return query;
    }
}
