

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


public class CCQuery extends FuzzyQuery {
    protected static double high = 28;
    protected static double veryHigh = 43;

    private CCQuery(QueryEngine queryEngine) {
        super(queryEngine, "CC_NO_FUZZY");
        fclFile = "/ComplexClass.fcl";
    }

    public static CCQuery createCCQuery(QueryEngine queryEngine) {
        return new CCQuery(queryEngine);
    }

    @Override
    protected String getQuery(boolean details) {
        String query = "MATCH (cl:Class) WHERE cl.class_complexity > " + veryHigh + " RETURN cl.app_key as app_key";
        if (details) {
            query += ",cl.name as full_name";
        } else {
            query += ",count(cl) as CC";
        }
        return query;
    }

    public void executeFuzzy(boolean details) throws CypherException, IOException {
        Result result;
        try (Transaction ignored = graphDatabaseService.beginTx()) {
            String query = "MATCH (cl:Class) WHERE cl.class_complexity > " + high + " RETURN cl.app_key as app_key, cl.class_complexity as class_complexity";
            if (details) {
                query += ",cl.name as full_name";
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
                cc = (int) res.get("class_complexity");
                if (cc >= veryHigh) {
                    res.put("fuzzy_value", 1);
                } else {
                    fb.setVariable("class_complexity", cc);
                    fb.evaluate();
                    res.put("fuzzy_value", fb.getVariable("res").getValue());
                }
                fuzzyResult.add(res);
            }
            queryEngine.resultToCSV(fuzzyResult, columns, "_CC.csv");
        }
    }


}
