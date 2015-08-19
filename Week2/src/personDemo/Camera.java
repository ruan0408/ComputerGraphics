package personDemo;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.media.opengl.GL2;

/**
 * COMMENT: Comment Camera 
 *
 * @author malcolmr
 */
public class Camera implements Drawable, KeyListener {

    private double[] myPos;
    private double myAngle;
    private double myScale;
    private double myAspect;

    public Camera() {
        myPos = new double[2];
        myAngle = 0;
        myScale = 20.0;
        myAspect = 1.0;
    }

    public void setAspect(double aspect) {
        myAspect = aspect;
    }    

    public void draw(GL2 gl) {
        gl.glPushMatrix();
        {
            gl.glTranslated(myPos[0], myPos[1], 0);
            gl.glRotated(myAngle, 0, 0, 1);
            gl.glScaled(myScale * myAspect, myScale, 1);

            // draw dashed lines
            gl.glLineStipple(1, (short) 0xAAAA);

            gl.glBegin(GL2.GL_LINE_STRIP);
            {
                gl.glVertex2d(-1, -1);
                gl.glVertex2d(1, -1);
                gl.glVertex2d(1, 1);
                gl.glVertex2d(-1, 1);
                gl.glVertex2d(-1, -1);
            }
            gl.glEnd();

        }
        gl.glPopMatrix();
    }

    /**
     * Set the view transform
     * 
     * Note: this is the inverse of the model transform above
     * 
     * @param gl
     */
    public void setView(GL2 gl) {
        gl.glScaled(1 / (myScale * myAspect), 1 / myScale, 1);
        gl.glRotated(-myAngle, 0, 0, 1);
        gl.glTranslated(-myPos[0], -myPos[1], 0);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch(e.getKeyCode()) {
        case KeyEvent.VK_LEFT:
            if (e.isShiftDown()) {
                myAngle += 5;
            }
            else {
                myPos[0] -= 1;                
            }
            break;

        case KeyEvent.VK_RIGHT:
            if (e.isShiftDown()) {
                myAngle -= 5;
            }
            else {
                myPos[0] += 1;                
            }
            break;

        case KeyEvent.VK_DOWN:
            myPos[1] -= 1;
            break;

        case KeyEvent.VK_UP:
            myPos[1] += 1;
            break;
            
        case KeyEvent.VK_PAGE_UP:
            myScale *= 1.1;
            break;
            
        case KeyEvent.VK_PAGE_DOWN:
            myScale /= 1.1;
            break;
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

}
