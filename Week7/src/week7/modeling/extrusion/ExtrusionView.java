package week7.modeling.extrusion;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.List;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;

import week7.modeling.extrusion.Polygon;

/**
 * COMMENT: Comment CrossSectionView 
 *
 * @author malcolmr
 */
public class ExtrusionView implements GLEventListener, MouseMotionListener {

    private static final int ROTATION_SCALE = 1;
    private ExtrusionExample myExtrusion;
    private Point myMousePoint;
    private int myRotateX = 0;
    private int myRotateY = 0;

    public ExtrusionView(ExtrusionExample extrusion) {
        myExtrusion = extrusion;
    }

    @Override
    public void init(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();

        // enable depth testing
        gl.glEnable(GL.GL_DEPTH_TEST);
        
        // enable lighting
        gl.glEnable(GL2.GL_LIGHTING);
        
        // normalise normals (!)
        // this is necessary to make lighting work properly
        gl.glEnable(GL2.GL_NORMALIZE);
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
   
        // clear the window and the depth buffer 
        gl.glClearColor(1, 1, 1, 1);
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

        // turn on a light
        gl.glEnable(GL2.GL_LIGHT0);
       
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();
        gl.glRotated(myRotateX, 1, 0, 0);
        gl.glRotated(myRotateY, 0, 1, 0);

        // draw the mesh as a sequence of polygons
        List<Polygon> mesh = myExtrusion.getMesh();
        if (mesh != null) {
            gl.glColor4d(0, 0, 0, 1);
            gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);
            
            for (Polygon p : mesh) {
                p.draw(gl);                
            }
        }
     
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width,
            int height) {
        
        GL2 gl = drawable.getGL().getGL2();

        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        
        gl.glOrtho(-3, 3, -3, 3, -4, 4);

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        myMousePoint = e.getPoint();        
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        Point p = e.getPoint();

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
