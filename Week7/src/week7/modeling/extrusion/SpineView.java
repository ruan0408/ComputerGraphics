package week7.modeling.extrusion;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.List;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;

import week7.modeling.extrusion.Point;

/**
 * A simple 2D view of the spine 
 *
 * @author malcolmr
 */
public class SpineView implements GLEventListener, MouseMotionListener {

    private static final int ROTATION_SCALE = 1;
    private ExtrusionExample myExtrusion;
    private java.awt.Point myMousePoint;
    private int myRotateX = 0;
    private int myRotateY = 0;

    public SpineView(ExtrusionExample extrusion) {
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
        gl.glRotated(myRotateX, 1, 0, 0);
        gl.glRotated(myRotateY, 0, 1, 0);

        List<Point> spine = myExtrusion.getSpine();
        if (spine != null) {
            gl.glColor4d(0, 0, 0, 1);
            gl.glBegin(GL2.GL_LINE_STRIP);
            for (Point p : spine) {
                p.draw(gl);
            }
            gl.glEnd();
        }
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width,
            int height) {

        GL2 gl = drawable.getGL().getGL2();

        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        
        gl.glOrtho(-2, 2, -2, 2, -4, 4);

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        myMousePoint = e.getPoint();        
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        java.awt.Point p = e.getPoint();

        if (myMousePoint != null) {
            int dx = p.x - myMousePoint.x;
            int dy = p.y - myMousePoint.y;

            // Note: dragging in the x dir rotates about y
            //       dragging in the y dir rotates about x
            myRotateY += dx * ROTATION_SCALE;
            myRotateX += dy * ROTATION_SCALE;

        }
        myMousePoint = p;
    }

    
}
