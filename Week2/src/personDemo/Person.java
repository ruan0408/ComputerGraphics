package personDemo;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;

/**
 * COMMENT: Comment Person 
 *
 * @author malcolmr
 */
public class Person implements Drawable, KeyListener {

    static public boolean DRAW_COORDINATES = false;  
    
    static private double TORSO_LENGTH = 12;
    private static final double SHOULDER_HEIGHT = TORSO_LENGTH * 3 / 4;

    private double[] myPosition;
    private double myRotation;
    private double myScale;

    private Arm myLeftArm;
    private Arm myRightArm;
    private Leg myLeftLeg;
    private Leg myRightLeg;
    private Head myHead;

    public Person(double x, double y, double rotation, double scale) {
        
        myPosition = new double[2];
        myPosition[0] = x;
        myPosition[1] = y;
        myRotation = rotation;
        myScale = scale;
        
        myLeftArm = new Arm(true);
        myRightArm = new Arm(false);
        myLeftLeg = new Leg(true);
        myRightLeg = new Leg(false);
        myHead = new Head();
    }

    public void draw(GL2 gl) {

        gl.glColor3d(0.0, 0.0, 0.0);
        
        gl.glPushMatrix();
        {
            // transform the coordinate frame to put the person in position
            
            

            gl.glTranslated(myPosition[0], myPosition[1], 0);
            gl.glRotated(myRotation, 0, 0, 1.0f);
            gl.glScaled(myScale, myScale, 1.0f);
            drawCoordinateFrame(gl);
            
            // draw torso

            gl.glBegin(GL.GL_LINES);
            {
                gl.glVertex2d(0, 0);
                gl.glVertex2d(0, TORSO_LENGTH);
            }
            gl.glEnd();

            // draw legs
            myLeftLeg.draw(gl);
            myRightLeg.draw(gl);
            
            // move coordinates to neck and draw the head
                        
            gl.glPushMatrix();
            {
                gl.glTranslated(0, TORSO_LENGTH, 0);
                myHead.draw(gl);
            }
            gl.glPopMatrix();
            
            // move coordinates to shoulders and draw the arms
            gl.glPushMatrix();
            {
                gl.glTranslated(0, SHOULDER_HEIGHT, 0);
                myLeftArm.draw(gl);
                myRightArm.draw(gl);
            }
            gl.glPopMatrix();
            
            
        }
        gl.glPopMatrix();
    }

    static void drawCoordinateFrame(GL2 gl) {
        
        if (!DRAW_COORDINATES) {
            return;
        }
        
        float[] color = new float[4];
        gl.glGetFloatv(GL2.GL_CURRENT_COLOR, color, 0);
        float[] width = new float[1];
        gl.glGetFloatv(GL2.GL_LINE_WIDTH, width, 0);
        
        gl.glLineWidth(3);
        
        // x axis in red
        gl.glColor3d(1.0, 0.0, 0.0);
        gl.glBegin(GL.GL_LINES);
        {
            gl.glVertex2d(0, 0);
            gl.glVertex2d(1, 0);
        }
        gl.glEnd();
        
        // y axis in green
        gl.glColor3d(0.0, 1.0, 0.0);
        gl.glBegin(GL.GL_LINES);
        {
            gl.glVertex2d(0, 0);
            gl.glVertex2d(0, 1);
        }
        gl.glEnd();

        gl.glColor4d(color[0], color[1], color[2], color[3]);
        gl.glLineWidth(width[0]);
        
    }
    
    
    
    @Override
    public void keyPressed(KeyEvent e) {
        
        switch(e.getKeyCode()) {
        
        case KeyEvent.VK_1:
            myLeftArm.rotateElbow(-5);
            break;
            
        case KeyEvent.VK_2:
            myLeftArm.rotateElbow(5);
            break;
            
        case KeyEvent.VK_Q:
            myLeftArm.rotateShoulder(-5);
            break;
        
        case KeyEvent.VK_W:
            myLeftArm.rotateShoulder(5);
            break;
        
        case KeyEvent.VK_A:
            myLeftLeg.rotateHip(5);
            break;
        
        case KeyEvent.VK_S:
            myLeftLeg.rotateHip(-5);
            break;

        case KeyEvent.VK_Z:
            myLeftLeg.rotateKnee(5);
            break;

        case KeyEvent.VK_X:
            myLeftLeg.rotateKnee(-5);
            break;

        case KeyEvent.VK_9:
            myRightArm.rotateElbow(5);
            break;
            
        case KeyEvent.VK_0:
            myRightArm.rotateElbow(-5);
            break;
            
        case KeyEvent.VK_I:
            myRightArm.rotateShoulder(5);
            break;
        
        case KeyEvent.VK_O:
            myRightArm.rotateShoulder(-5);
            break;
        
        case KeyEvent.VK_J:
            myRightLeg.rotateHip(-5);
            break;
        
        case KeyEvent.VK_K:
            myRightLeg.rotateHip(5);
            break;

        case KeyEvent.VK_N:
            myRightLeg.rotateKnee(-5);
            break;

        case KeyEvent.VK_M:
            myRightLeg.rotateKnee(5);
            break;
            
        case KeyEvent.VK_SPACE:
            DRAW_COORDINATES = !DRAW_COORDINATES;
            break;
          
        }
        
        
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
    

    @Override
    public void keyTyped(KeyEvent e) {
    }

    
}
