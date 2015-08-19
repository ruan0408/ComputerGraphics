import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLJPanel;
import javax.swing.JFrame;



/**
 * A simple example of writing a JOGL program that draws a circle in a window.
 *
 * @author angf
 */

public class CircleTriangleFan implements GLEventListener {

	
    public static void main(String[] args) {
        // Initialise OpenGL
        GLProfile glp = GLProfile.getDefault();
        GLCapabilities caps = new GLCapabilities(glp);

        // Create a panel to draw on
        GLJPanel panel = new GLJPanel(caps);

        // Put it in a window
        final JFrame jframe = new JFrame("Circle Triangle Strip");
        jframe.setSize(300, 300);
        jframe.add(panel);
        jframe.setVisible(true);

        
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      

        // add a GL Event listener to handle rendering
        panel.addGLEventListener(new CircleTriangleFan());
    }
  

    @Override
    public void display(GLAutoDrawable drawable) {

        int numVertices = 20; //try 4
        double radius = 1;
        GL2 gl = drawable.getGL().getGL2();
       
        // clear the window
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);

        gl.glColor3f(0, 0, 0); //set pen color to black
    
        // approximate a circle with triangle fans
        gl.glBegin(GL2.GL_TRIANGLE_FAN);
        {        	
        	gl.glVertex2d(0, 0); //The centre of the circle
            double angle = 0;
            double angleIncrement = 2*Math.PI/numVertices;
            for(int i=0; i <= numVertices; i++){
            	angle = i* angleIncrement;
            	double x = radius * Math.cos(angle);
            	double y = radius * Math.sin(angle);
            
            	gl.glVertex2d(x, y);
            
            }
        }
        gl.glEnd();
      
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
