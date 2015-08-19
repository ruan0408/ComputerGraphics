package personDemo;

import javax.media.opengl.GL2;

/**
 * COMMENT: Comment Hand 
 *
 * @author malcolmr
 */
public class Hand implements Drawable {

    static final private double SCALE = 1.5;

    @Override
    public void draw(GL2 gl) {

        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glPushMatrix();
        {
            gl.glScaled(SCALE, SCALE, 1);
            Person.drawCoordinateFrame(gl);
            
            gl.glBegin(GL2.GL_POLYGON);
            {
                double y = -Math.sqrt(3);
                gl.glVertex2d(0, 0);
                gl.glVertex2d(-1, y);
                gl.glVertex2d(1, y);
            }
            gl.glEnd();

        }
        gl.glPopMatrix();

    }

}
