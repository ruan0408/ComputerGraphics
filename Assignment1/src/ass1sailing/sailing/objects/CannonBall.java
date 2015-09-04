package ass1sailing.sailing.objects;

import ass1.GameObject;
import ass1.MathUtil;
import ass1.PolygonalGameObject;


/**
 * COMMENT: Comment Sail 
 *
 * @author malcolmr
 */
public class CannonBall extends PolygonalGameObject {

    private static final double[] LINE_COLOR = { 0.0f, 0.0f, 0.0f, 1.0f };
    private static final double[] FILL_COLOR = { 0.1f, 0.1f, 0.1f, 1.0f };
    private static final int POINTS = 8;

    private static final double LIFETIME = 1.0;
    private static final double SPEED = 10.0;
        
    private double myLifetime;
    private double[] myMomentum;
    
    public CannonBall(GameObject parent, double x, double y, double angle, double scale) {
        super(parent, makePolygon(), FILL_COLOR, LINE_COLOR);
        
        setPosition(x, y);
        setRotation(angle);
        setScale(scale);
        
        myLifetime = LIFETIME;
        myMomentum = new double[2];
    }

    /**
     * Make an approximate circle
     * 
     * @return
     */
    private static double[] makePolygon() {
        double[] polygon = new double[POINTS*2];
        
        for (int i = 0; i < POINTS; i++) {
            double a = i * Math.PI * 2.0 / POINTS;
            polygon[2*i] = Math.cos(a);
            polygon[2*i+1] = Math.sin(a);
        }
        
        return polygon;
    }

    /**
     * Use this method to give the cannonball some initial momentum.
     *  
     * This is used to match the ball's initial speed to the ship's
     * even though it is no longer attached to the ship's coordinate frame. 
     * 
     * COMMENT: setMomentum
     * 
     * @param vx
     * @param vy
     */
    public void setMomentum(double vx, double vy) {
        myMomentum[0] = vx;
        myMomentum[1] = vy;
    }

    @Override
    public void update(double dt) {
        // the ball disappears when the lifetimer runs out
        myLifetime -= dt;
        
        if (myLifetime <= 0) {
            destroy();
        }
        else {
            double d = SPEED * dt;
            double angle = Math.toRadians(getRotation());
            translate(d * Math.cos(angle), d * Math.sin(angle));
            
            // add momentum term
            translate(dt * myMomentum[0], dt * myMomentum[1]);
        }
    }

    
    
}
