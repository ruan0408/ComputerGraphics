package ass1sailing.sailing.objects;

import ass1.GameObject;
import ass1.MathUtil;
import ass1.PolygonalGameObject;



/**
 * COMMENT: Comment Sail 
 *
 * @author malcolmr
 */
public class Sail extends PolygonalGameObject {

    private static final double[] POLYGON = { 0.5f, 0, 0, 1.5f, 0, -1.5f };
    private static final double[] LINE_COLOR = { 1.0f, 1.0f, 1.0f, 1.0f };
    private static final double[] FILL_COLOR = { 1.0f, 1.0f, 1.0f, 0.75f };

    private double myMaxAngle = 15;
    
    public Sail(GameObject parent, double x, double y, double angle, double scale) {
        super(parent, POLYGON, FILL_COLOR, LINE_COLOR);

        setPosition(x, y);
        setRotation(angle);
        setScale(scale);
    }
    
    public void setAngle(double angle) {
        setRotation(MathUtil.clamp(angle, -myMaxAngle, myMaxAngle));
    }
    
    
}
