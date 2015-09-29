package week7.modeling.extrusion;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;

import week7.modeling.extrusion.Polygon;

/**
 * A simple 2D view of the cross-section 
 *
 * @author malcolmr
 */
public class CrossSectionView implements GLEventListener {

    private ExtrusionExample myExtrusion;

    public CrossSectionView(ExtrusionExample extrusion) {
        myExtrusion = extrusion;
    }

    @Override
    public void init(GLAutoDrawable drawable) {
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();

        gl.glClearColor(1, 1, 1, 1);
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);

        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();

        Polygon crossSection = myExtrusion.getCrossSection();
        if (crossSection != null) {
            gl.glColor4d(0, 0, 0, 1);
            gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_LINE);
            crossSection.draw(gl);
        }
        gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL2.GL_FILL);
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width,
            int height) {
        
        GL2 gl = drawable.getGL().getGL2();

        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        
        gl.glOrtho(-2, 2, -2, 2, -2, 2);

    }

}
