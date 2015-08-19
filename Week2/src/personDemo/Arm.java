package personDemo;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;

/**
 * COMMENT: Comment Arm 
 *
 * @author malcolmr
 */
public class Arm implements Drawable {

    private static final double UPPER_ARM_LENGTH = 6.0;
    private static final double FORE_ARM_LENGTH = 6.0;

    private boolean myLeft;
    private double myShoulderAngle;
    private double myElbowAngle;
    private Hand myHand;

    public Arm(boolean left) {
        myLeft = left;
        myHand = new Hand();
        
        myShoulderAngle = 45;
        myElbowAngle = 45;
    }

    public void rotateShoulder(double angle) {
        myShoulderAngle += angle;
    }

    public void rotateElbow(double angle) {
        myElbowAngle += angle;
    }

    /**
     * @see examples.week02.Drawable#draw(GL2)
     */
    @Override
    public void draw(GL2 gl) {

        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glPushMatrix();
        {
            // rotate first then flip left to right if necessary
            gl.glScaled(myLeft ? -1 : 1, 1, 1);
            gl.glRotated(myShoulderAngle, 0, 0, 1.0);
            Person.drawCoordinateFrame(gl);

            gl.glBegin(GL.GL_LINES);
            {
                gl.glVertex2d(0, 0);
                gl.glVertex2d(0, -UPPER_ARM_LENGTH);
            }
            gl.glEnd();
            
            // move the coordinate frame to draw the forearm
            gl.glTranslated(0, -UPPER_ARM_LENGTH, 0);
            gl.glRotated(myElbowAngle, 0, 0, 1.0f);
           
            Person.drawCoordinateFrame(gl);

            gl.glBegin(GL.GL_LINES);
            {
                gl.glVertex2d(0, 0);
                gl.glVertex2d(0, -FORE_ARM_LENGTH);
            }
            gl.glEnd();
            
            // move the coordinate frame to draw the hand
            gl.glTranslated(0, -FORE_ARM_LENGTH, 0);
            myHand.draw(gl);
            
        }
        gl.glPopMatrix();
    }

}
