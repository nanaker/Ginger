

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


public class HeavyAsyncTaskStepsQuery extends FuzzyQuery {
    protected static double high_cc = 3.5;
    protected static double veryHigh_cc = 5;
    protected static double high_noi = 17;
    protected static double veryHigh_noi = 26;

    private HeavyAsyncTaskStepsQuery(QueryEngine queryEngine) {
        super(queryEngine, "HAS_NO_FUZZY");
        fclFile = "D:\\pfe\\Projets\\sarra\\exportMobilesoft\\exportMSR\\SmellDetector\\fcl\\HeavySomething.fcl";
    }

    public static HeavyAsyncTaskStepsQuery createHeavyAsyncTaskStepsQuery(QueryEngine queryEngine) {
        return new HeavyAsyncTaskStepsQuery(queryEngine);
    }

    @Override
    protected String getQuery(boolean details) {
        String query = "MATCH (c:Class{is_async_task:true})-[:CLASS_OWNS_METHOD]->(m:Method) WHERE (m.name='onPreExecute' OR m.name='onProgressUpdate' OR m.name='onPostExecute') AND  m.number_of_instructions > 10 AND m.cyclomatic_complexity > 1 return m.app_key as app_key";
        if (details) {
            query += ",m.full_name as full_name";
        } else {
            query += ",count(m) as HAS";
        }
        return query;
    }

    public void executeFuzzy(boolean details) throws CypherException, IOException {
        Result result;
        try (Transaction ignored = graphDatabaseService.beginTx()) {
            String query = "MATCH (c:Class{is_async_task:true})-[:CLASS_OWNS_METHOD]->(m:Method) WHERE (m.name='onPreExecute' OR m.name='onProgressUpdate' OR m.name='onPostExecute') AND  m.number_of_instructions >" + high_noi + " AND m.cyclomatic_complexity > " + high_cc + " return m.app_key as app_key,m.cyclomatic_complexity as cyclomatic_complexity, m.number_of_instructions as number_of_instructions";
            if (details) {
                query += ",m.full_name as full_name";
            }
            result = graphDatabaseService.execute(query);
            List<String> columns = new ArrayList<>(result.columns());
            columns.add("fuzzy_value");
            int noi, cc;
            List<Map> fuzzyResult = new ArrayList<>();
            File fcf = new File(fclFile);
            //We look if the file is in a directory otherwise we look inside the jar
           FIS fis;
            if (fcf.exists() && !fcf.isDirectory()) {
                fis = FIS.load(fclFile, false);
            } else {
                fis = FIS.load(getClass().getResourceAsStream(fclFile), false);
            }
            FunctionBlock fb = fis.getFunctionBlock(null);
            while (result.hasNext()) {
                HashMap res = new HashMap(result.next());
                cc = (int) res.get("cyclomatic_complexity");
                noi = (int) res.get("number_of_instructions");
                if (cc >= veryHigh_cc && noi >= veryHigh_noi) {
                    res.put("fuzzy_value", 1);
                } else {
                    fb.setVariable("cyclomatic_complexity", cc);
                    fb.setVariable("number_of_instructions", noi);
                    fb.evaluate();
                    res.put("fuzzy_value", fb.getVariable("res").getValue());
                }
                fuzzyResult.add(res);
            }
            queryEngine.resultToCSV(fuzzyResult, columns, "_HAS.csv");
        }
    }


}
