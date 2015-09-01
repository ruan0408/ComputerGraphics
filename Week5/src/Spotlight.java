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
import com.jogamp.opengl.util.gl2.GLUT;

/**
 * Guha, Sumanta (2014-08-06). Computer Graphics Through OpenGL: From Theory to Experiments, Second Edition 
 * Experimenter code.
 *
 * Adapted to jogl and modified by Angela Finlayson 
 * Check out the keyPressed function to see what keys to press to change the settings :)
 */

public class Spotlight implements GLEventListener, KeyListener {
	
	private float spotAngle = 10.0f; // Spotlight cone half-angle.
	private float spotDirection[] = {0.0f, -1.0f, 0.0f}; // Spotlight direction.
	private float spotExponent = 2.0f; // Spotlight exponent = attenuation factor.
	private double xMove = 0.0, zMove = 0.0; // Movement components.
	private float spotPos = 3;
	
	
    public static void main(String[] args) {
        // initialisation
        GLProfile glp = GLProfile.getDefault();
        GLCapabilities caps = new GLCapabilities(glp);
        
        // create a panel to draw on
        GLJPanel panel = new GLJPanel(caps);

        // put it in a JFrame
        final JFrame jframe = new JFrame("Spotlight");
        jframe.setSize(800, 800);
        jframe.add(panel);
        jframe.setVisible(true);

        // Catch window closing events and quit             
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Spotlight s = new Spotlight();
        
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
        GLUT glut = new GLUT();
        
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();
        
        GLU glu = new GLU();
        glu.gluLookAt (0.0, 4.0, 6.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0);
      
        gl.glPushMatrix();{
        	gl.glTranslated(xMove, 0, zMove); // Move the spotlight.
        	float lightPos[] = {0.0f, spotPos, 0.0f, 1.0f}; // Spotlight position.
        	gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, lightPos,0);  

        	// Draw the spotlight cone in wireframe after disabling lighting
        	// This is needed as there will be no light on the wireframe but we want to see it
        	// to help us understand what is happening with the spotlight settings.
        	gl.glPushMatrix();{
        		gl.glDisable(GL2.GL_LIGHTING);
        		gl.glRotated(-90.0, 1.0, 0.0, 0.0);
        		gl.glColor3f(1.0f, 1.0f, 1.0f);       		
        		glut.glutWireCone(3.0 * Math.tan( spotAngle/180.0 * Math.PI ), 3.0, 20, 20);
        		gl.glEnable(GL2.GL_LIGHTING);
        	}gl.glPopMatrix();

        	// Spotlight properties including position.

        	gl.glLightf(GL2.GL_LIGHT0, GL2.GL_SPOT_CUTOFF, spotAngle);
        	gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_SPOT_DIRECTION, spotDirection,0);    
        	gl.glLightf(GL2.GL_LIGHT0, GL2.GL_SPOT_EXPONENT, spotExponent);

        }gl.glPopMatrix();

        // Draw 10 x 10 array of multi-colored spheres.
        int i,j;
        for (i = 0; i < 9; i++)
           for (j = 0; j < 9; j++)
           {
     	     gl.glPushMatrix();{
     	    	 gl.glTranslated(-4.0+i, 0.0, -4.0+j);

     	    	 // Ambient and diffuse colors of the spheres specified to alternate.
     	    	 if ((i+j)%3 == 0) gl.glColor4f(1.0f, 0.0f, 0.0f, 1.0f);
     	    	 else if ((i+j)%3 == 1) gl.glColor4f(0.0f, 1.0f, 0.0f, 1.0f);
     	    	 else gl.glColor4f(0.0f, 0.0f, 1.0f, 1.0f);

     	    	 glut.glutSolidSphere (0.5, 20, 16);
     	     }gl.glPopMatrix(); 
     	  }
        
        gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);

    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
    }

    @Override
    public void init(GLAutoDrawable drawable) {
    	GL2 gl = drawable.getGL().getGL2();

    	//gl.glClearColor(1.0f, 1.0f, 1.0f, 0.0f);
    	gl.glEnable(GL2.GL_DEPTH_TEST); // Enable depth testing.

    	// Turn on OpenGL lighting.
    	gl.glEnable(GL2.GL_LIGHTING);

    	// Light property vectors.
    	float lightAmb[] = {0.0f, 0.0f, 0.0f, 1.0f};
    	float lightDifAndSpec[] = {1.0f, 1.0f, 1.0f, 1.0f};
    	float globAmb[] = {0.05f, 0.05f, 0.05f, 1.0f};

    	// Light properties.
    	gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, lightAmb,0);
    	gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, lightDifAndSpec,0);
    	gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_SPECULAR, lightDifAndSpec,0);

    	gl.glEnable(GL2.GL_LIGHT0); // Enable particular light source.
    	gl.glLightModelfv(GL2.GL_LIGHT_MODEL_AMBIENT, globAmb,0); // Global ambient light.
    	gl.glLightModeli(GL2.GL_LIGHT_MODEL_LOCAL_VIEWER, GL2.GL_TRUE); // Enable local viewpoint.

    	// Material property vectors.
    	float matSpec[] = {1.0f, 1.0f, 1.0f, 1.0f};
    	float matShine[] = {50.0f};

    	// Material properties shared by all the spheres.
    	gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, matSpec,0);
    	gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SHININESS, matShine,0);

    	// Cull back faces.
    	gl.glEnable(GL2.GL_CULL_FACE);
    	gl.glCullFace(GL2.GL_BACK);

    	// Enable color material mode:
    	// The ambient and diffuse color of the front faces will track the color set by glColor().
    	gl.glEnable(GL2.GL_COLOR_MATERIAL); 
    	gl.glColorMaterial(GL2.GL_FRONT, GL2.GL_AMBIENT_AND_DIFFUSE);
    
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int w, int h) {
    	 GL2 gl = drawable.getGL().getGL2();
         gl.glMatrixMode(GL2.GL_PROJECTION);
         gl.glLoadIdentity();
         
         GLU glu = new GLU();
         
         glu.gluPerspective(60.0, (float)w/(float)h, 1.0, 20.0);        
         
         gl.glMatrixMode(GL2.GL_MODELVIEW);
         
    }

    @Override
	public void keyPressed(KeyEvent ev) {
    	switch (ev.getKeyCode()) {
    	case KeyEvent.VK_S:

    		if (ev.isShiftDown()) {
    			spotExponent += 0.2;
    		}else{
    			if (spotExponent > 0.0) spotExponent -= 0.2;
    		}
    		break;
    	case KeyEvent.VK_PAGE_DOWN:

    		if (spotAngle > 0.0) spotAngle -= 1.0;
    		break;
    	case KeyEvent.VK_PAGE_UP:

    		if (spotAngle < 90.0) spotAngle += 1.0;

    		break;
    	case KeyEvent.VK_UP:

    		if (zMove > -4.0) zMove -= 0.1;
    		break;
    	case KeyEvent.VK_DOWN:

    		if (zMove < 4.0) zMove += 0.1;
    		break;
    	case KeyEvent.VK_LEFT:

    		if (xMove > -4.0) xMove -= 0.1;
    		break;
    	case KeyEvent.VK_RIGHT:

    		if (xMove < 4.0) xMove += 0.1;
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
