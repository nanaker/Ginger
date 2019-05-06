package laboratory.sniffer.detector.neo4j;

import org.neo4j.graphdb.GraphDatabaseService;

public class Pretraitement {
    private QueryEngine queryEngine;
    private GraphDatabaseService graphDatabaseService;
    private String query;

    public void setQuery(String query) {
        this.query = query;
    }

    public Pretraitement(QueryEngine queryEngine, String query)
    {
        this.queryEngine=queryEngine;
        this.query=query;
        this.graphDatabaseService=queryEngine.getGraphDatabaseService();
    }

    public void exec(){

        queryEngine.toMap(graphDatabaseService.execute(query));
    }
}
