import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLJPanel;
import javax.media.opengl.glu.GLU;
import javax.swing.JFrame;

import com.jogamp.opengl.util.FPSAnimator;


public class MouseClickDemo implements MouseListener, GLEventListener {
     private static GLJPanel panel;
     private int x;
     private int y;
    
     
	 public static void main(String[] args) {
	        // Initialise OpenGL
	        GLProfile glp = GLProfile.getDefault();
	        GLCapabilities caps = new GLCapabilities(glp);
	        // Create a panel to draw on
	        panel = new GLJPanel(caps);

	        // Put it in a window
	        final JFrame jframe = new JFrame("Dots");
	        jframe.setSize(500, 500);
	        jframe.add(panel);
	        jframe.setVisible(true);

	        // Catch window closing events and quit
	        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        // add a GL Event listener to handle rendering
	        MouseClickDemo d = new MouseClickDemo();
	        panel.setFocusable(true);
	        panel.addGLEventListener(d);
	        panel.addMouseListener(d);
	       
	     
	}

	@Override
	public void display(GLAutoDrawable drawable) {
		
		GL2 gl = drawable.getGL().getGL2();
		double [] points = MouseUtils.computeMousePosition(gl,x,y);

		System.out.println("Screen Coords " + x + " " + y);
		System.out.println("World Coords " + points[0] + " " + points[1]);
		
		// clear the window
		gl.glClearColor(1,1,1,0);
		gl.glClear(GL.GL_COLOR_BUFFER_BIT);
		gl.glPointSize(10); //must be done outside BEGIN and END

		
		gl.glColor3f(1,0,0);
		gl.glBegin(GL.GL_POINTS);{
			gl.glVertex2d(points[0],points[1]);
			//gl.glVertex2d(x,y); //wrong

		}gl.glEnd();
        
	}

	@Override
	public void dispose(GLAutoDrawable arg0) {

	}

	@Override
	public void init(GLAutoDrawable arg0) {

	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int w,
			int h) {
		GL2 gl = drawable.getGL().getGL2();
    	gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        GLU glu = new GLU();
        glu.gluOrtho2D(-10, 10,-10,10);
	}

	@Override
	public void mousePressed(MouseEvent e) {
		//Not in world co-ordinates - screen co-ordinates.
				
		if(e.getButton() == MouseEvent.BUTTON1){
			
		    x = e.getX();
		    y = e.getY();
		  		
		}
	    panel.display();	
	}
  
	
	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {

	}

	@Override
	public void mouseClicked(MouseEvent e) {

	}

	@Override
	public void mouseReleased(MouseEvent e) {

	}

	
}
