package ass1sailing.sailing.objects;

import ass1.GameObject;
import ass1.Mouse;
import ass1.PolygonalGameObject;

/**
 * A simple cursor class for testing the Mouse class 
 *
 * @author malcolmr
 */
public class Cursor extends PolygonalGameObject {

    private static final double[] LINE_COLOR = { 0, 0, 0, 1 };
    private static final double[] FILL_COLOR = { 1, 1, 1, 1 };
    private static final double[] POLYGON = { -1, -1, -1, 1, 1, 1, 1, -1 };

    public Cursor() {
        super(GameObject.ROOT, POLYGON, FILL_COLOR, LINE_COLOR);
    }

    @Override
    public void update(double dt) {
        double[] p = Mouse.theMouse.getPosition();
        setPosition(p[0], p[1]);
    }

    
    
}
