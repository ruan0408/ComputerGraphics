package week9;



import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLJPanel;
import javax.swing.JFrame;

import com.jogamp.opengl.util.FPSAnimator;
import com.jogamp.opengl.util.gl2.GLUT;


//Demonstrates use of line anti-aliasing 
//and full screen multi-sampling
public class AntiAndMulti implements GLEventListener, KeyListener{
	
  
 
    private float width = 1.0f; // Line width.
    private double xDist = 0.0, yDist = 0.0, zDist = -15.0; // Distance scene translated.
    private boolean isAntialiased = false;   // Is antialiasing on?
    private boolean isMultisampled = false;; // Is multisampling on?
   
    private double Xangle = 0.0, Yangle = 0.0, Zangle = 0.0; // Angles to rotate hemisphere.
    private int sampleBuffers[] = new int[1]; // Number of sample buffers.
    private int numSamples[] = new int[1];
 
	    
    public static void main(String[] args) {
        // initialisation
        GLProfile glp = GLProfile.getDefault();
        GLCapabilities caps;
       
        //We are actually use non-default GLCapabilites
        caps = new GLCapabilities(glp);
        caps.setSampleBuffers(true);
        caps.setNumSamples(4);
        
        // create a panel to draw on
        GLJPanel panel = new GLJPanel(caps);

        // put it in a JFrame
        final JFrame jframe = new JFrame("Anti-aliasing");
        jframe.setSize(1200, 1200);
        jframe.add(panel);
        jframe.setVisible(true);

        // Catch window closing events and quit             
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        AntiAndMulti s = new AntiAndMulti();
        
        // add a GL Event listener to handle rendering
        panel.addGLEventListener(s);
       
        panel.addKeyListener(s);
        panel.setFocusable(true);   
        
        // NEW: add an animator to create display events at 60 FPS
        FPSAnimator animator = new FPSAnimator(60);
        animator.add(panel);
        animator.start();
    }   
    
 
    
    @Override
    public void display(GLAutoDrawable drawable) {
    	
        GL2 gl = drawable.getGL().getGL2();
       
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
       
        gl.glColor4d(0.0, 0.0, 0.0, 1);
        gl.glLoadIdentity(); 

        gl.glRasterPos3f(-4.5f, 4.5f, -5.1f); 
        // Antialiasing control.
       if (isAntialiased) 
        {
    	   gl.glEnable(GL2.GL_BLEND); // Enable blending.
    	   gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA); // Specify blending parameters.

    	   gl.glEnable(GL2.GL_LINE_SMOOTH); // Enable line antialiasing.
    	   gl.glHint(GL2.GL_LINE_SMOOTH_HINT, GL2.GL_NICEST); // Ask for best line antialiasing.
    	
        }
        else 
        {
     	  gl.glDisable(GL2.GL_BLEND); 
          gl.glDisable(GL2.GL_LINE_SMOOTH);
        }

    
        // Multisampling control.
        if (isMultisampled) 
        {
           gl.glEnable(GL2.GL_MULTISAMPLE); // Enable multisampling.
      
     	  
          gl.glGetIntegerv(GL2.GL_SAMPLES, numSamples,0);
          System.out.println("Num samples " + numSamples[0]);

        }
        else{
        
           gl.glDisable(GL2.GL_MULTISAMPLE);
        
        }

        // Commands to move the scene.
        gl.glTranslated(xDist, yDist, zDist);
        gl.glRotated(Zangle, 0.0, 0.0, 1.0);
        gl.glRotated(Yangle, 0.0, 1.0, 0.0);
        gl.glRotated(Xangle, 1.0, 0.0, 0.0);

      

        // Draw a line segment.
        gl.glColor4d(1.0, 0.0, 0.0, 1);
        gl.glLineWidth(width);
        gl.glBegin(GL2.GL_LINES);
           gl.glVertex3d(-10.0, 0.0, 0.0);
           gl.glVertex3d(10.0, 0.0, 0.0);
        gl.glEnd();
        
        gl.glColor4d(0.0, 0.0, 0.0, 1);
        // Draw a wire cube reference frame.
        gl.glLineWidth(width);
        GLUT glut = new GLUT();
        glut.glutWireCube(7.0f);
        
        gl.glDisable(GL2.GL_BLEND); 
        
       
        // Draw two adjacent triangles.
        gl.glBegin(GL2.GL_TRIANGLES);
           gl.glColor4d(0.0, 0.0, 1.0, 0.8);
           gl.glVertex3d(6.0, 1.0, 0.0);
           gl.glVertex3d(6.0, 8.0, 0.0);
           gl.glVertex3d(12.0, 1.0, 0.0);

           gl.glColor4d(1.0, 1.0, 0.0, 0.8);
           gl.glVertex3d(12.0, 8.0, 0.0);
           gl.glVertex3d(12.0, 1.0, 0.0);
           gl.glVertex3d(6.0, 8.0, 0.0);
        gl.glEnd();
        
        
      

          
    }

    
   
    @Override
    public void dispose(GLAutoDrawable drawable) {
    }

    @Override
    public void init(GLAutoDrawable drawable) {
    	GL2 gl = drawable.getGL().getGL2();

    	gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f); 
    	  
    } 	   
    	 

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int w, int h) {
    	 GL2 gl = drawable.getGL().getGL2();
         gl.glMatrixMode(GL2.GL_PROJECTION);
         gl.glLoadIdentity();       
         gl.glFrustum(-5.0f, 5.0f, -5.0f, 5.0f, 5.0f, 100.0f);         
         gl.glMatrixMode(GL2.GL_MODELVIEW);        
       
    }

 	

	@Override
	public void keyPressed(KeyEvent e) {
		 switch (e.getKeyCode()) {
		 case KeyEvent.VK_A:
		
			 isAntialiased = !isAntialiased;
			 System.out.println(isAntialiased);
			 break;
		 case KeyEvent.VK_M:			
			 isMultisampled = !isMultisampled;
			 System.out.println(isMultisampled);
			 break;
		 case KeyEvent.VK_L:
			 if(e.isShiftDown()) {
				 width++;
			 } else {
				 width--;
			 }
			 break;
		
		 case KeyEvent.VK_X:
				if(e.isShiftDown()) {
					 Xangle += 5.0;
					 if (Xangle > 360.0) Xangle -= 360.0;
				  } else {
					  Xangle -= 5.0;
						 if (Xangle < 0.0) Xangle += 360.0;
				  }
			 break;
		 case KeyEvent.VK_Y:
				if(e.isShiftDown()) {
					   Yangle -= 5.0;
						 if (Yangle < 0.0) Yangle += 360.0;
				  } else {
					  Yangle += 5.0;
						 if (Yangle > 360.0) Yangle -= 360.0;
				  }
			 break;
		
		 case KeyEvent.VK_Z:
				if(e.isShiftDown()) {
					   Zangle -= 5.0;
						 if (Zangle < 0.0) Zangle += 360.0;
				  } else {
					  Zangle += 5.0;
						 if (Zangle > 360.0) Zangle -= 360.0;
				  }
			 break;
		
	    
	   
		 default:
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
