

package laboratory.sniffer.detector.neo4j;


public class InvalidateWithoutRectQuery extends Query {

    private InvalidateWithoutRectQuery(QueryEngine queryEngine) {
        super(queryEngine, "IWR");
    }

    public static InvalidateWithoutRectQuery createInvalidateWithoutRectQuery(QueryEngine queryEngine) {
        return new InvalidateWithoutRectQuery(queryEngine);
    }

    @Override
    protected String getQuery(boolean details) {
        String query = "MATCH (a:App)-[:APP_OWNS_CLASS]->(cl:Class{is_view:true})-[:CLASS_OWNS_METHOD]->(n:Method{name:'onDraw'})-[:CALLS]->(e:ExternalMethod{name:'invalidate'}) " +
                "WHERE NOT (e)-[:METHOD_OWNS_ARGUMENT]->(:ExternalArgument) " +
                "return DISTINCT a.commit_number as commit_number, n.app_key as key, cl.file_path as file_path";
        if (details) {
            query += ",n.full_name as instance";
        } else {
            query += ",count(n) as IWR";
        }
        return query;
    }
}