package personDemo;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;

/**
 * COMMENT: Comment Foot 
 *
 * @author malcolmr
 */
public class Foot implements Drawable {
    
    static final private double SCALE = 3;

    public Foot() {
    }

    @Override
    public void draw(GL2 gl) {
        
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glPushMatrix();
        {
        	
            gl.glScaled(SCALE, SCALE, 1);
            Person.drawCoordinateFrame(gl);
            
            gl.glBegin(GL2.GL_POLYGON);
            {
                gl.glVertex2d(0, 0);
                gl.glVertex2d(0, -1);
                gl.glVertex2d(2, -1);
            }
            gl.glEnd();
            
        }
        gl.glPopMatrix();
    }

}
