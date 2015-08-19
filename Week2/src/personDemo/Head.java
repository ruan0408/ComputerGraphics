package personDemo;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;

/**
 * COMMENT: Comment Head 
 *
 * @author malcolmr
 */
public class Head implements Drawable {

    private static final int NPOINTS = 20;
    private static final double RADIUS = 3;

    @Override
    public void draw(GL2 gl) {

        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glPushMatrix();
        {
            gl.glScaled(RADIUS, RADIUS, 1);
            Person.drawCoordinateFrame(gl);

            gl.glBegin(GL2.GL_POLYGON);

            for (int i = 0; i < NPOINTS; i++) {
                double a = i * Math.PI * 2 / NPOINTS;

                double x = Math.cos(a);
                double y = Math.sin(a) + 1; // off centre

                gl.glVertex2d(x, y);
            }

            gl.glEnd();
        }
        gl.glPopMatrix();

    }

}
