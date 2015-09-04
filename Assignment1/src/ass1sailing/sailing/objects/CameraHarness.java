package ass1sailing.sailing.objects;

import ass1.GameObject;

/**
 * COMMENT: Comment CameraHarness 
 *
 * @author malcolmr
 */
public class CameraHarness extends GameObject {

    private GameObject myTarget;

    // don't connect to the target
    // instead track the target without rotation or scaling
    public CameraHarness(GameObject target) {
        super(GameObject.ROOT);
        
        myTarget = target;
    }

    @Override
    public void update(double dt) {
        double[] p = myTarget.getGlobalPosition();
        setPosition(p[0], p[1]);
    }
    
}
