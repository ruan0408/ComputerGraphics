package ass1sailing.sailing.objects;

import ass1.GameObject;
import ass1.PolygonalGameObject;
import ass1sailing.org.json.JSONArray;
import ass1sailing.org.json.JSONObject;


/**
 * COMMENT: Comment Sail 
 *
 * @author malcolmr
 */
public class Island extends PolygonalGameObject {

    private static final double[] LINE_COLOR = { 0.0f, 0.5f, 0.0f, 1.0f };
    private static final double[] FILL_COLOR = { 0.0f, 0.9f, 0.0f, 1.0f };

    public Island(double x, double y, double[] polygon) {
        super(GameObject.ROOT, polygon, FILL_COLOR, LINE_COLOR);

        setPosition(x, y);
    }

    public static Island fromJSON(JSONObject json) {

        double x = json.getDouble("x");
        double y = json.getDouble("y");
        JSONArray points = json.getJSONArray("polygon");

        double[] polygon = new double[points.length()];
        for (int i = 0; i < points.length(); i++) {
            polygon[i] = points.getDouble(i);
        }

        return new Island(x, y, polygon);
    }

}
