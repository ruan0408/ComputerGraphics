package ass1sailing.sailing.objects;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import ass1sailing.sailing.SailingGame;

import ass1.GameObject;
import ass1.Mouse;
import ass1.PolygonalGameObject;

/**
 * COMMENT: Comment Sail 
 *
 * @author malcolmr
 */
public class Cannon extends PolygonalGameObject {

    static final double[] POLYGON = { 0, 1, -0.87f, 0.5f, -0.87f, -0.5f, 0, -1,
            2, -0.67f, 2, 0.67f };
    static final double[] LINE_COLOR = { 0.0f, 0.0f, 0.0f, 1.0f };
    static final double[] FILL_COLOR = { 0.1f, 0.1f, 0.1f, 1.0f };
    private int myButton;

    private double[] myPrevPos;
    
    public Cannon(GameObject parent, double x, double y, double angle,
            double scale, int button) {
        super(parent, POLYGON, FILL_COLOR, LINE_COLOR);

        myButton = button;

        setPosition(x, y);
        setRotation(angle);
        setScale(scale);
        
        myPrevPos = getGlobalPosition();
    }

    public void update(double dt) {

        double[] position = getGlobalPosition();

        // fire the cannon
        if (Mouse.theMouse.wasPressed(myButton)) {
            // create a ball at the cannon's origin
            CannonBall ball = new CannonBall(this, 0, 0, 0, 1);

            // detach it from the cannon
            ball.setParent(ROOT);
            
            // add momentum to match the ship's speed
            double vx = (position[0] - myPrevPos[0]) / dt;
            double vy = (position[1] - myPrevPos[1]) / dt;

            ball.setMomentum(vx, vy);
        }
        
        myPrevPos = position;
    }


}
