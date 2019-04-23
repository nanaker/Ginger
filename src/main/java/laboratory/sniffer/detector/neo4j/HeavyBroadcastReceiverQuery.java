

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


public class HeavyBroadcastReceiverQuery extends Query {


    private HeavyBroadcastReceiverQuery(QueryEngine queryEngine) {
        super(queryEngine, "HBR");
    }

    public static HeavyBroadcastReceiverQuery createHeavyBroadcastReceiverQuery(QueryEngine queryEngine) {
        return new HeavyBroadcastReceiverQuery(queryEngine);
    }

    @Override
    protected String getQuery(boolean details) {
        String query = "MATCH (c:Class{is_broadcast_receiver:true})-[:CLASS_OWNS_METHOD]->(m:Method)  " +
                "return m.cyclomatic_complexity as cyclomatic_complexity, " +
                "m.number_of_instructions as number_of_instructions ,(m.name='onReceive')as has_methode_onReceive";
        if (details) {
            query += ",m.full_name as full_name order by cyclomatic_complexity desc";
        } else {
            query += ",count(m) as HBR";
        }
        return query;
    }




}
