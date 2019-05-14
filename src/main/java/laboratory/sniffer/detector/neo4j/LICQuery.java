

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
        String query = "MATCH (cl:Class) WHERE  exists(cl.is_inner_class) RETURN CASE WHEN cl.is_static = true THEN true ELSE false END AS is_static ,cl.is_enum as is_enum ,CASE WHEN (not (cl:Class)-[:CLASS_OWNS_METHOD]->(:Method)-[:USES]->(:Variable{is_static:false})) = true THEN false ELSE true END as uses_variables, CASE WHEN (not  (cl:Class)-[:CLASS_OWNS_METHOD]->(:Method)-[:CALLS]->(:Method{is_static:false}))= true THEN false ELSE true END as call_method,cl.class_complexity as class_complexity   ";
        if (details) {
            query += ",cl.name as full_name order by class_complexity  desc";
        } else {
            query += ",count(cl) as LIC";
        }
        return query;
    }


}
