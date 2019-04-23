

package laboratory.sniffer.detector.neo4j;

import org.neo4j.cypher.CypherException;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;

import java.io.IOException;
import java.util.List;
import java.util.Map;


public abstract class Query {
    protected QueryEngine queryEngine;
    protected GraphDatabaseService graphDatabaseService;
    protected String smellName;

    public Query(QueryEngine queryEngine, String smellName) {
        this.queryEngine = queryEngine;
        this.smellName = smellName;
        graphDatabaseService = queryEngine.getGraphDatabaseService();
    }

    /**
     * Generate query to execute.
     *
     * @param details The specific query.
     * @return The query String.
     */
    protected abstract String getQuery(boolean details);

    public void execute(boolean details) throws CypherException, IOException {
        List<Map<String, Object>> result = fetchResult(details);
        queryEngine.resultToCSV(result,  "prediction/"+smellName + "_prediction.csv");

    }

    public final List<Map<String, Object>> fetchResult(boolean details, boolean orderByCommit) throws CypherException {
        List<Map<String, Object>> result;
        try (Transaction ignored = graphDatabaseService.beginTx()) {
            String query = getQuery(details);
            if (orderByCommit) {
                query += " ORDER BY commit_number";
            }
            result = queryEngine.toMap(graphDatabaseService.execute(query));
            ignored.success();
        }
        return result;
    }

    public final List<Map<String, Object>> fetchResult(boolean details) {
        return fetchResult(details, false);
    }


    /**
     * Return a stream to the neo4j result via the object {@link Result}.
     * This stream has to be fully consumed or closed to avoid any leak!
     *
     * @param details Export smell details.
     * @param orderByCommit Order by commit number.
     * @return The {@link Result}.
     * @throws CypherException If anything goes wrong.
     */
    public Result streamResult(boolean details, boolean orderByCommit) throws CypherException {
        String query = getQuery(details);
        if (orderByCommit) {
            query += " ORDER BY commit_number";
        }
        return graphDatabaseService.execute(query);
    }

    public Result streamResult(boolean details) throws CypherException {
        return streamResult(details, false);
    }

    public String getSmellName() {
        return smellName;
    }
}
