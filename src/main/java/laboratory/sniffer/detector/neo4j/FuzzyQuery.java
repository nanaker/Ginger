

package laboratory.sniffer.detector.neo4j;

import org.neo4j.cypher.CypherException;

import java.io.IOException;


public abstract class FuzzyQuery extends Query {
    protected String fclFile;

    public FuzzyQuery(QueryEngine queryEngine, String smellName) {
        super(queryEngine, smellName);
    }

    public abstract void executeFuzzy(boolean details) throws CypherException, IOException;
}
