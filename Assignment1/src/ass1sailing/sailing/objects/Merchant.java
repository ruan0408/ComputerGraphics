package ass1sailing.sailing.objects;

import ass1sailing.org.json.JSONObject;

import ass1.GameObject;
import ass1.PolygonalGameObject;




/**
 * COMMENT: Comment Boat 
 *
 * @author malcolmr
 */
public class Merchant extends PolygonalGameObject {

    // 
    //  (-1.0, 0.7) +---------------+\ (1.0, 0.6)
    //              |                  \
    //              |      + (0,0)      + (1.5, 0)
    //              |                  /
    // (-1.0, -0.7) +---------------+/ (1.0, -0.6)
    //


    private static final double[] POLYGON = { 1.5f, 0, 1, 0.6f, -1, 0.7f, -1,
            -0.7f, 1, -0.6f };
    private static final double[] LINE_COLOR = { 0.5f, 0.3f, 0.0f, 1.0f };
    private static final double[] FILL_COLOR = { 0.5f, 0.4f, 0.25f, 1.0f };
    
    private final Sail mySail;
    
    public Merchant(double x, double y, double angle) {
        super(GameObject.ROOT, POLYGON, FILL_COLOR, LINE_COLOR);

        mySail = new Sail(this, 0, 0, 0, 1.0f);

        setPosition(x, y);
        setRotation(angle);
    }

    /**
     * COMMENT: fromJSON
     * 
     * @param jsonObject
     * @return
     */
    public static Merchant fromJSON(JSONObject json) {
        double x = json.getDouble("x");
        double y = json.getDouble("y");
        double angle = json.getDouble("angle");
        
        return new Merchant(x, y, angle);
    }



}
