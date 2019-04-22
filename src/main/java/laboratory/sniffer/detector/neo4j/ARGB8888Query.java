

package laboratory.sniffer.detector.neo4j;


public class ARGB8888Query extends Query {

    private ARGB8888Query(QueryEngine queryEngine) {
        super(queryEngine, "ARGB8888");
    }

    public static ARGB8888Query createARGB8888Query(QueryEngine queryEngine) {
        return new ARGB8888Query(queryEngine);
    }

    @Override
    protected String getQuery(boolean details) {
        String query = "MATCH (e: ExternalArgument) WHERE exists(e.is_argb_8888) RETURN e";
        if (details) {
            query += ", count(e) as ARGB8888";
        }
        return query;
    }

}
