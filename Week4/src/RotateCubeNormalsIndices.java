import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLCanvas;
import javax.media.opengl.awt.GLJPanel;
import javax.media.opengl.glu.GLU;
import javax.swing.JFrame;

import com.jogamp.opengl.util.FPSAnimator;
import com.jogamp.opengl.util.gl2.GLUT;

/**
 * 
 *  Simple demo to illustrate 3D rotations.
 *  Change the angle of rotation by pressing
 *  up and down arrows.
 *  
 *  Modify the code to rotate around different axes
 *  gl.glRotated ( angle, 1,0, 0);
 * 
 */

public class RotateCubeNormalsIndices implements GLEventListener, KeyListener {
	
	
	// cube vertex co-ordinates
		private static double vertices[] = 
		{
			1.0, -1.0, 1.0, 
			1.0, 1.0, 1.0, 
			1.0, 1.0, -1.0, 
			1.0, -1.0, -1.0, 
			-1.0, -1.0, 1.0, 
			-1.0, 1.0, 1.0, 
			-1.0, 1.0, -1.0, 
			-1.0, -1.0, -1.0
		};

		// Vertex indices of each box side, 6 groups of 4.
		private static int quadIndices[] = 
		{
		    3, 2, 1, 0, //right face
		    7, 6, 2, 3, //back face
		    4, 5, 6, 7, //left face
			0, 1, 5, 4, //front face
			4, 7, 3, 0, //bottom face
			6, 5, 1, 2  //top face
		};

		
		//Face normals = normalized unit vector pointing in direction of face
		static double faceNormals[] = {
			 1, 0, 0,  //right face
			 0, 0,-1,  //back face
			-1, 0, 0,  //left face
			 0, 0, 1,  //front face
			 0,-1, 0,  //bottom face
		     0, 1, 0   //top face
		};
		
	
	private static int angle = 0;
	
    public static void main(String[] args) {
        // initialisation
        GLProfile glp = GLProfile.getDefault();
        GLCapabilities caps = new GLCapabilities(glp);
        
        // create a panel to draw on
        GLJPanel panel = new GLJPanel(caps);

        // put it in a JFrame
        final JFrame jframe = new JFrame("Rotations");
        jframe.setSize(300, 300);
        jframe.add(panel);
        jframe.setVisible(true);

        // Catch window closing events and quit             
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        RotateCubeNormalsIndices p = new RotateCubeNormalsIndices();
        // add a GL Event listener to handle rendering
        panel.addGLEventListener(p);
        // NEW: add a key listener to respond to keypresses
        panel.addKeyListener(p);
        // the panel needs to be focusable to get key events
        panel.setFocusable(true);   
        
        // NEW: add an animator to create display events at 60 FPS
        FPSAnimator animator = new FPSAnimator(60);
        animator.add(panel);
        animator.start();
    }
    
    @Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		 switch (e.getKeyCode()) {
  
		 case KeyEvent.VK_UP:
		       
				  angle = (angle + 10) % 360;
				  break;
		 case KeyEvent.VK_DOWN:
			     
				  angle = (angle - 10) % 360;
				  break;		
		 default:
			 break;
		 }
		 System.out.println(angle);
    }
    
    @Override
    public void display(GLAutoDrawable drawable) {
    	
    	GL2 gl = drawable.getGL().getGL2();

    	gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
    	
    	gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();
       
        //set up camera
        //gl.glTranslated(0,0,-3);
        
        GLU glu = new GLU();
        //glu.gluLookAt(0, 0, 3, 0, 0, 0, 0, 1, 0);
        glu.gluLookAt(0, 2, 5, 0, 0, 0, 0, 1, 0);
        
    	gl.glColor3f(1, 1, 1);  	
        
    
        //do rotation on cube
        gl.glRotated ( angle, 1,1, 1);  //Axis  (1,1,1)
   
    	drawCube(gl);
    
    	 
    }
    
    private void drawCube(GL2 gl){
    	 gl.glBegin(GL2.GL_QUADS);{
         	//Draw 6 faces of box (each with v vertices
         	for(int i=0; i< 24; i++){
         		int index = quadIndices[i];  
         		gl.glNormal3dv(faceNormals,(i/4)*3);
         		gl.glVertex3dv(vertices,index*3);
         	}
         }gl.glEnd();
    	   
    }
    

    @Override
    public void dispose(GLAutoDrawable drawable) {
    }

    @Override
    public void init(GLAutoDrawable drawable) {
    	GL2 gl = drawable.getGL().getGL2();
    	gl.glEnable(GL2.GL_DEPTH_TEST);
    	  
    	// enable lighting
        gl.glEnable(GL2.GL_LIGHTING);
        //Turn on default light
        gl.glEnable(GL2.GL_LIGHT0);
        
        // normalise normals (!)
        // this is necessary to make lighting work properly
        //gl.glEnable(GL2.GL_NORMALIZE);
        
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int w, int h) {
    	 GL2 gl = drawable.getGL().getGL2();
    
         gl.glMatrixMode(GL2.GL_PROJECTION);
         gl.glLoadIdentity();
        
         gl.glOrtho(-2,2,-2,2,1,10);
        
        
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
