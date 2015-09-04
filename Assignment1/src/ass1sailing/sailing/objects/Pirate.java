package ass1sailing.sailing.objects;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import ass1sailing.org.json.JSONObject;

import ass1.GameObject;
import ass1.MathUtil;
import ass1.Mouse;
import ass1.PolygonalGameObject;


/**
 * COMMENT: Comment Boat 
 *
 * @author malcolmr
 */
public class Pirate extends PolygonalGameObject {

    // 
    //  (-1.0, 0.7) +---------------+\ (1.0, 0.6)
    //              |                  \
    //              |      + (0,0)      + (1.5, 0)
    //              |                  /
    // (-1.0, -0.7) +---------------+/ (1.0, -0.6)
    //


    private static final double[] POLYGON = { 1.5, 0, 1, 0.6, -1, 0.7, -1,
            -0.7, 1, -0.6 };
    private static final double[] LINE_COLOR = { 0.5f, 0.3f, 0.0f, 1.0f };
    private static final double[] FILL_COLOR = { 0.5f, 0.4f, 0.25f, 1.0f };
    
    private static final int LEFT_BUTTON = MouseEvent.BUTTON1;
    private static final int RIGHT_BUTTON = MouseEvent.BUTTON3;

    private final Sail myFrontSail;
    private final Sail myBackSail;

    private double myTurningSpeed = 90;
    private double mySpeed = 5;
    private double myRadius = 1;

    private final Cannon[] myPortCannon;
    private final Cannon[] myStarboardCannon;
    
    public Pirate(double x, double y, double angle) {
        super(GameObject.ROOT, POLYGON, FILL_COLOR, LINE_COLOR);

        myPortCannon = new Cannon[2];
        myStarboardCannon = new Cannon[2];
        
        myPortCannon[0] = new Cannon(this, 0.5, 0.625, 90, 0.2, LEFT_BUTTON);
        myPortCannon[1] = new Cannon(this, -0.5, 0.675, 90, 0.2, LEFT_BUTTON);
        myStarboardCannon[0] = new Cannon(this, 0.5, -0.625, -90, 0.2, RIGHT_BUTTON);
        myStarboardCannon[1] = new Cannon(this, -0.5, -0.675, -90, 0.2, RIGHT_BUTTON);

        // hide them
//        myPortCannon[0].show(false);
//        myPortCannon[1].show(false);
//        myStarboardCannon[0].show(false);
//        myStarboardCannon[1].show(false);

        myFrontSail = new Sail(this, 0.5f, 0, 0, 0.75f);
        myBackSail = new Sail(this, -0.5f, 0, 0, 1.0f);               

        setPosition(x, y);
        setRotation(angle);
    }

    /**
     * COMMENT: update
     * 
     */
    @Override
    public void update(double dt) {
        move(dt);
    }

    private void move(double dt) {
        double[] target = Mouse.theMouse.getPosition();
        double[] pos = getGlobalPosition();
        
        double dx = target[0] - pos[0];
        double dy = target[1] - pos[1];
        double dd = Math.sqrt(dx * dx + dy * dy);

        // only move if the mouse is outside our close vicinty (no turning on the spot)
        
        if (dd > myRadius) {

            double heading = getGlobalRotation();
            double angle = Math.toDegrees(Math.atan2(dy, dx));
 
            angle = MathUtil.normaliseAngle(angle - heading);

            myFrontSail.setAngle(angle);
            myBackSail.setAngle(angle);
            
            // turn towards the mouse at a fixed rate

            double maxTurn = dt * myTurningSpeed;
            angle = MathUtil.clamp(angle, -maxTurn, maxTurn);
            rotate(angle);
           
            double distance = dt * mySpeed;
            distance = MathUtil.clamp(distance, 0, dd);

            double theta = Math.toRadians(getRotation());
            translate(distance * Math.cos(theta), distance * Math.sin(theta));                 
        }
    }
    
    /**
     * COMMENT: fromJSON
     * 
     * @param jsonPlayer
     * @return
     */
    public static Pirate fromJSON(JSONObject json) {
        double x = json.getDouble("x");
        double y = json.getDouble("y");
        double angle = json.getDouble("angle");
        
        return new Pirate(x, y, angle);
    }


}
