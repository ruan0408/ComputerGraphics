package personDemo;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;

/**
 * COMMENT: Comment Leg 
 *
 * @author malcolmr
 */
public class Leg implements Drawable {

    private static final double THIGH_LENGTH = 6.0;
    private static final double SHIN_LENGTH = 6.0;    

    private boolean myLeft;
    private double myHipAngle;
    private double myKneeAngle;
    private Foot myFoot;
    
    public Leg(boolean left) {
        myLeft = left;        
        myFoot = new Foot();
        
        myHipAngle = 45;
        myKneeAngle = -45;
    }

    public void rotateHip(double angle) {
        myHipAngle += angle;
    }
    
    public void rotateKnee(double angle) {
        myKneeAngle += angle;
    }
    
    /**
     * @see examples.week02.Drawable#draw(GL2)
     */
    @Override
    public void draw(GL2 gl) {

        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glPushMatrix();
        {
            gl.glScaled(myLeft ? -1 : 1, 1, 1);  // flip after rotating
            gl.glRotated(myHipAngle, 0, 0, 1.0);
            Person.drawCoordinateFrame(gl);

            gl.glBegin(GL.GL_LINES);
            {
                gl.glVertex2d(0, 0);
                gl.glVertex2d(0, -THIGH_LENGTH);
            }
            gl.glEnd();
            
            // move the coordinate frame to draw the forearm
            gl.glTranslated(0, -THIGH_LENGTH, 0);
            gl.glRotated(myKneeAngle, 0, 0, 1.0f);
            Person.drawCoordinateFrame(gl);

            gl.glBegin(GL.GL_LINES);
            {
                gl.glVertex2d(0, 0);
                gl.glVertex2d(0, -SHIN_LENGTH);
            }
            gl.glEnd();
            
            // move the coordinate frame to draw the hand
            gl.glTranslated(0, -SHIN_LENGTH, 0);
            myFoot.draw(gl);
            
        }
        gl.glPopMatrix();
    }

}
