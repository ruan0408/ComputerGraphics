import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLJPanel;
import javax.swing.JFrame;

/**
 * Demonstrates that their is a front and back on a triangle
 * Uses line mode instead of polygon mode for the back of the triangle
 * Because we use a clock-wise winding order when specifying our triangle
 * vertices we see the back face of it - thus it is in line mode.
 *
 * @author angf
 */

public class TriangleWindingOrder implements GLEventListener {

    public static void main(String[] args) {
        // Initialise OpenGL
        GLProfile glp = GLProfile.getDefault();
        GLCapabilities caps = new GLCapabilities(glp);

        // Create a panel to draw on
        GLJPanel panel = new GLJPanel(caps);

        // Put it in a window
        final JFrame jframe = new JFrame("Triangle Front Back");
        jframe.setSize(300, 300);
        jframe.add(panel);
        jframe.setVisible(true);

        
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      

        // add a GL Event listener to handle rendering
        panel.addGLEventListener(new TriangleWindingOrder());
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        // redraw the scene

        GL2 gl = drawable.getGL().getGL2();
       
        // clear the window
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);

        //Polygon mode is GL2.GL_FILL by default
        //gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_LINE);
        //gl.glPolygonMode(GL2.GL_FRONT, GL2.GL_LINE);
        gl.glPolygonMode(GL2.GL_BACK, GL2.GL_LINE);

        // draw a triangle
        gl.glBegin(GL2.GL_TRIANGLES);
        {
            //This triangle is drawn clockwise,
            //so you are actually seeing the back of it
        	//since we set the GL_BACK to line mode it should
        	//not be filled.
            gl.glColor3f(1, 0, 0);
            gl.glVertex2d(-1, -1);

            gl.glColor3f(0, 1, 0);
            gl.glVertex2d(0, 1);

            gl.glColor3f(0, 0, 1);
            gl.glVertex2d(1, -1);
        }
        gl.glEnd();

        //set back to GL2_FILL so screen gets cleared properly
        //This is a bug workaround needed on only some systems
        gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL); 
    }

    @Override
    public void init(GLAutoDrawable drawable) {
        // do any initialisation
    	GL2 gl = drawable.getGL().getGL2();
    	gl.glClearColor(1.0f, 1.0f, 1.0f, 1f); // White Background
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
        // dispose of any allocated resources
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int w, int h) {
        // respond to the window being resized
    }

}
