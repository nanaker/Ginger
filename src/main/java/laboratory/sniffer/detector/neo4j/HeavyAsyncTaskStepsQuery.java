

package laboratory.sniffer.detector.neo4j;

import net.sourceforge.jFuzzyLogic.FIS;
import net.sourceforge.jFuzzyLogic.FunctionBlock;
import org.neo4j.cypher.CypherException;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class HeavyAsyncTaskStepsQuery extends Query {


    private HeavyAsyncTaskStepsQuery(QueryEngine queryEngine) {
        super(queryEngine, "HAS");

    }

    public static HeavyAsyncTaskStepsQuery createHeavyAsyncTaskStepsQuery(QueryEngine queryEngine) {
        return new HeavyAsyncTaskStepsQuery(queryEngine);
    }

    @Override
    protected String getQuery(boolean details) {
        String query = "MATCH (c:Class{parent_name:'android.os.AsyncTask'})-[:CLASS_OWNS_METHOD]->(m:Method)   " +
                "return m.cyclomatic_complexity as cyclomatic_complexity, " +
                "m.number_of_instructions as number_of_instructions , " +
                "(m.name='onPreExecute' OR m.name='onProgressUpdate' OR m.name='onPostExecute' ) as has_method";
        if (details) {
            query += ",m.full_name as full_name order by cyclomatic_complexity desc";
        } else {
            query += ",count(m) as HAS";
        }
        return query;
    }




}
