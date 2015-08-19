import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

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

/**
 * An example of non-uniform scaling followed by rotation with a moving camera
 * Use left, right, up, down arrows to move the camera
 * Hold down the shift key and use left and right arrows to rotate.
 * Hold down PgUp and PgDown to scale
 * 
 * @author angf
 */

public class RectScaleRotateCamera implements GLEventListener, KeyListener{
    static public boolean DRAW_COORDINATES = true;  
	private double theta = 45;
	
	private double[] cameraPos = {0,0};
	private double cameraAngle = 0;
	private double cameraScale = 1;
	
    public static void main(String[] args) {
        // Initialise OpenGL
        GLProfile glp = GLProfile.getDefault();
        GLCapabilities caps = new GLCapabilities(glp);

        // Create a panel to draw on
        GLJPanel panel = new GLJPanel(caps);

        // Put it in a window
        final JFrame jframe = new JFrame("House Scale then Rotations");
        jframe.setSize(800, 800);
        jframe.add(panel);
        jframe.setVisible(true);

        
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        RectScaleRotateCamera r = new RectScaleRotateCamera();

        // add a GL Event listener to handle rendering
        panel.addGLEventListener(r);
        
        
        panel.addKeyListener(r);
        panel.setFocusable(true);
        
        // NEW: add an animator to create display events at 60 FPS
        FPSAnimator animator = new FPSAnimator(60);
        animator.add(panel);
        animator.start();
    }

    @Override
    public void display(GLAutoDrawable drawable) {
       
        GL2 gl = drawable.getGL().getGL2();
       
        // clear the window
        gl.glClearColor(0,0,0,1);
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);
        theta+= 0.2;
        
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();
        setCameraView(gl);
           
        //Do model transformations
        gl.glRotated(theta,0,0,1);
        gl.glScaled(1,3,1);    	     
	   
 	    drawSquare(gl,0,0);
 	    drawCoordinateFrame(gl);
              
    }
    
    public void setCameraView(GL2 gl) {
        gl.glScaled(1 / (cameraScale), 1 / cameraScale, 1);
        gl.glRotated(-cameraAngle, 0, 0, 1);
        gl.glTranslated(-cameraPos[0], -cameraPos[1], 0);
    }
  
  
    
    public void drawSquare(GL2 gl, int xPos, int yPos){
	    gl.glColor3d(1.0,1,1);
 	    
 	    gl.glBegin(GL2.GL_POLYGON);{
 	    	gl.glVertex2d(xPos,yPos);
 	    	gl.glVertex2d(xPos + 1, yPos);
 	    	gl.glVertex2d(xPos + 1, yPos + 1);
 	    	gl.glVertex2d(xPos, yPos + 1);
 	    } gl.glEnd();	  	    
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
    	 GL2 gl = drawable.getGL().getGL2();
         
       
         // in OpenGL terms, we are changing the 'projection'
         // this will be explained later
         gl.glMatrixMode(GL2.GL_PROJECTION);
         gl.glLoadIdentity();

         // Use the GLU library to compute the new projection
         GLU glu = new GLU();
     
         glu.gluOrtho2D(-8, 8, -8, 8.0);  // left, right, top, bottom
       
    }
    
    
static void drawCoordinateFrame(GL2 gl) {
        
        if (!DRAW_COORDINATES) {
            return;
        }
        
        float[] color = new float[4];
        gl.glGetFloatv(GL2.GL_CURRENT_COLOR, color, 0);
        float[] width = new float[1];
        gl.glGetFloatv(GL2.GL_LINE_WIDTH, width, 0);
        
        gl.glLineWidth(3);
        
        // x axis in red
        gl.glColor3d(1.0, 0.0, 0.0);
        gl.glBegin(GL.GL_LINES);
        {
            gl.glVertex2d(0, 0);
            gl.glVertex2d(1, 0);
        }
        gl.glEnd();
        
        // y axis in green
        gl.glColor3d(0.0, 1.0, 0.0);
        gl.glBegin(GL.GL_LINES);
        {
            gl.glVertex2d(0, 0);
            gl.glVertex2d(0, 1);
        }
        gl.glEnd();

        gl.glColor4d(color[0], color[1], color[2], color[3]);
        gl.glLineWidth(width[0]);
        
    }

@Override
public void keyPressed(KeyEvent e) {
    switch(e.getKeyCode()) {
    case KeyEvent.VK_LEFT:
        if (e.isShiftDown()) {
            cameraAngle += 5;
        }
        else {
            cameraPos[0] -= 1;                
        }
        break;

    case KeyEvent.VK_RIGHT:
        if (e.isShiftDown()) {
            cameraAngle -= 5;
        }
        else {
            cameraPos[0] += 1;                
        }
        break;

    case KeyEvent.VK_DOWN:
        cameraPos[1] -= 1;
        break;

    case KeyEvent.VK_UP:
        cameraPos[1] += 1;
        break;
        
    case KeyEvent.VK_PAGE_UP:
        cameraScale *= 1.1;
        break;
        
    case KeyEvent.VK_PAGE_DOWN:
        cameraScale /= 1.1;
        break;
    }

}

@Override
public void keyReleased(KeyEvent arg0) {
	// TODO Auto-generated method stub
	
}

@Override
public void keyTyped(KeyEvent arg0) {
	// TODO Auto-generated method stub
	
}
    

}
