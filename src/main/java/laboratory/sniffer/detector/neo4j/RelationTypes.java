

package laboratory.sniffer.detector.neo4j;

import org.neo4j.graphdb.RelationshipType;


public enum RelationTypes implements RelationshipType {
    APP_OWNS_CLASS,
    CLASS_OWNS_METHOD,
    CLASS_OWNS_VARIABLE,
    METHOD_OWNS_ARGUMENT,
    IMPLEMENTS,
    EXTENDS,
    CALLS,
    USES,
    APP_USES_LIBRARY
}
