

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


public class LMQuery extends FuzzyQuery {
    protected static double high = 17;
    protected static double veryHigh = 26;

    private LMQuery(QueryEngine queryEngine) {
        super(queryEngine, "LM_NO_FUZZY");
        fclFile = "/LongMethod.fcl";
    }

    public static LMQuery createLMQuery(QueryEngine queryEngine) {
        return new LMQuery(queryEngine);
    }

    @Override
    protected String getQuery(boolean details) {
        String query = "MATCH (m:Method) WHERE m.number_of_instructions >" + veryHigh + " RETURN m.app_key as app_key";
        if (details) {
            query += ",m.full_name as full_name ";
        } else {
            query += ",count(m) as LM";
        }
        return query;
    }

    public void executeFuzzy(boolean details) throws CypherException, IOException {
        Result result;
        try (Transaction ignored = graphDatabaseService.beginTx()) {
            String query = "MATCH (m:Method) WHERE m.number_of_instructions >" + high + " RETURN m.app_key as app_key,m.number_of_instructions as number_of_instructions";
            if (details) {
                query += ",m.full_name as full_name";
            }
            result = graphDatabaseService.execute(query);
            List<String> columns = new ArrayList<>(result.columns());
            columns.add("fuzzy_value");
            int cc;
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
                cc = (int) res.get("number_of_instructions");
                if (cc >= veryHigh) {
                    res.put("fuzzy_value", 1);
                } else {
                    fb.setVariable("number_of_instructions", cc);
                    fb.evaluate();
                    res.put("fuzzy_value", fb.getVariable("res").getValue());
                }
                fuzzyResult.add(res);
            }
            queryEngine.resultToCSV(fuzzyResult, columns, "_LM.csv");
        }
    }
}
