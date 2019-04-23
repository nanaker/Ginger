

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
        String query = "MATCH (m1:Method) RETURN  m1.number_of_callers>0 as number_of_callers_not_null," +
                "CASE WHEN m1.is_init = true THEN true ELSE false END as is_init," +
                "CASE WHEN m1.is_static = true THEN true ELSE false END  as is_static," +
                "CASE WHEN m1.is_override = true THEN true ELSE false END   as is_override," +
                "CASE WHEN (not (m1)-[:USES]->(:Variable)) = true THEN false ELSE true END as uses_variables, " +
                "CASE WHEN (not (m1)-[:CALLS]->(:ExternalMethod)) = true THEN false ELSE true END  as call_external_methode," +
                "m1.cyclomatic_complexity as cyclomatic_complexity";
        if (details) {
            query += ",m1.full_name as full_name order by cyclomatic_complexity desc";
        } else {
            query += ",count(m1) as MIM";
        }
        return query;
    }
}
