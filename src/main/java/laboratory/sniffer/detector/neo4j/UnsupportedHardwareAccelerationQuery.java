

package laboratory.sniffer.detector.neo4j;


public class UnsupportedHardwareAccelerationQuery extends Query {

    private UnsupportedHardwareAccelerationQuery(QueryEngine queryEngine) {
        super(queryEngine, "UHA");
    }

    public static UnsupportedHardwareAccelerationQuery createUnsupportedHardwareAccelerationQuery(QueryEngine queryEngine) {
        return new UnsupportedHardwareAccelerationQuery(queryEngine);
    }


    @Override
    protected String getQuery(boolean details) {
        String[] uhas = {
                "drawPicture#android.graphics.Canvas",
                "drawVertices#android.graphics.Canvas",
                "drawPosText#android.graphics.Canvas",
                "drawTextOnPath#android.graphics.Canvas",
                "drawPath#android.graphics.Canvas",
                "setLinearText#android.graphics.Paint",
                "setMaskFilter#android.graphics.Paint",
                "setPathEffect#android.graphics.Paint",
                "setRasterizer#android.graphics.Paint",
                "setSubpixelText#android.graphics.Paint"
        };
        StringBuilder query = new StringBuilder("MATCH (a:App)-[:APP_OWNS_CLASS]->(cl:Class)-[:CLASS_OWNS_METHOD]->(m:Method)-[:CALLS]->(e:ExternalMethod) WHERE e.full_name='" + uhas[0] + "'");
        for (int i = 1; i < uhas.length; i++) {
            query.append(" OR e.full_name='").append(uhas[i]).append("' ");
        }
        query.append(" return DISTINCT a.commit_number as commit_number, m.app_key as key, cl.file_path as file_path");
        if (details) {
            query.append(",m.full_name as instance");
        } else {
            query.append(",count(m) as UHA");
        }
        return query.toString();
    }
}
