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

public class LightAndMaterial implements GLEventListener, KeyListener {
	
	private boolean light0On = false; // White light on?
	private boolean light1On = false; // Blue light on?
	private float d = 1.0f; // Diffuse and specular white light intensity.
	private float m = 0.2f; // Global ambient white light intensity.
	private int localViewer = 0; // Local viewpoint?
	private int p = 1; // Positional light?
	private float xAngle = 0.0f, yAngle = 0.0f; // Rotation angles of white light.

	
    public static void main(String[] args) {
        // initialisation
        GLProfile glp = GLProfile.getDefault();
        GLCapabilities caps = new GLCapabilities(glp);
        
        // create a panel to draw on
        GLJPanel panel = new GLJPanel(caps);

        // put it in a JFrame
        final JFrame jframe = new JFrame("Sphere In a Box");
        jframe.setSize(800, 800);
        jframe.add(panel);
        jframe.setVisible(true);

        // Catch window closing events and quit             
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        LightAndMaterial s = new LightAndMaterial();
        
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
       
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
     
        gl.glClear (GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();

        GLUT glut = new GLUT();
        GLU glu = new GLU();
        glu.gluLookAt(0.0, 3.0, 5.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0);
        
        // Light property vectors.
        float lightAmb[] = { 0.0f, 0.0f, 0.0f, 1.0f };
        float lightDifAndSpec0[] = { d, d, d, 1.0f };
        float lightPos0[] = { 0.0f, 0.0f, 3.0f, p };
        float lightDifAndSpec1[] = { 0.0f, 0.0f, 1.0f, 1.0f };
        float lightPos1[] = { 1.0f, 2.0f, 0.0f, 1.0f };
        float globAmb[] = { m, m, m, 1.0f };

        // Light0 properties.
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, lightAmb,0);
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, lightDifAndSpec0,0);
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_SPECULAR, lightDifAndSpec0,0);

        // Light1 properties.
        gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_AMBIENT, lightAmb,0);
        gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_DIFFUSE, lightDifAndSpec1,0);
        gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_SPECULAR, lightDifAndSpec1,0);

        gl.glLightModelfv(GL2.GL_LIGHT_MODEL_AMBIENT, globAmb,0); // Global ambient light.
        gl.glLightModeli(GL2.GL_LIGHT_MODEL_LOCAL_VIEWER, localViewer); // Enable local viewpoint

        // Turn lights off/on.
        if (light0On) gl.glEnable(GL2.GL_LIGHT0); else gl.glDisable(GL2.GL_LIGHT0);
        if (light1On) gl.glEnable(GL2.GL_LIGHT1); else gl.glDisable(GL2.GL_LIGHT1);

        // Light0 positioned and sphere positioned in case of positional light
        // and arrow in case of directional light.
        gl.glPushMatrix();{
        	gl.glRotated(xAngle, 1.0, 0.0, 0.0); // Rotation about x-axis.
        	gl.glRotated(yAngle, 0.0, 1.0, 0.0); // Rotation about z-axis.
        	gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, lightPos0,0);
        	gl.glTranslatef(lightPos0[0], lightPos0[1], lightPos0[2]);
        	gl.glColor3f(d, d, d); 

        	float emmL[] = {1.0f, 1.0f, 1.0f, 1.0f};
        	float matAmbAndDifL[] = {0.0f, 0.0f, 0.0f, 1.0f};
        	float matSpecL[] = { 0.0f, 0.0f, 0.0f, 1.0f };
        	float matShineL[] = { 50.0f };

        	// Material properties of sphere.
        	gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT_AND_DIFFUSE, matAmbAndDifL,0);
        	gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, matSpecL,0);
        	gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SHININESS, matShineL,0);
        	
        	//Since the actual sphere/arrow representing the light will not necessarily be hit
        	//by any light and we want to, see it, we give it an emissive property.
        	//The other alternative would be to temporarily turn off lighting.
        	//We do this for the other light to give an example of both ways
        	gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_EMISSION, emmL,0);
        	if (light0On) 
        	{
        		if (p == 1) glut.glutSolidSphere(0.05, 8, 8); // Sphere at positional light source.
        		else // Arrow pointing along incoming directional light.
        		{
        			gl.glLineWidth(3.0f);
        			gl.glBegin(GL2.GL_LINES);
        			gl.glVertex3d(0.0, 0.0, 0.25);
        			gl.glVertex3d(0.0, 0.0, -0.25);
        			gl.glVertex3d(0.0, 0.0, -0.25);
        			gl.glVertex3d(0.05, 0.0, -0.2);
        			gl.glVertex3d(0.0, 0.0, -0.25);
        			gl.glVertex3d(-0.05, 0.0, -0.2);
        			gl.glEnd();
        			gl.glLineWidth(1.0f);
        		}
        	}
        }gl.glPopMatrix();

        //Just for an example, instead of using emissive light we
        //Disable lighting temporarily so we can see the actual sphere
        //representing light 2
        gl.glDisable(GL2.GL_LIGHTING);
        // Light1 and its sphere positioned.
        gl.glPushMatrix();{
        	gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_POSITION, lightPos1,0);
        	gl.glTranslated(lightPos1[0], lightPos1[1], lightPos1[2]);
        	gl.glColor3f(0.0f, 0.0f, 1.0f); 
        	if (light1On) glut.glutWireSphere(0.05, 8, 8);
        }gl.glPopMatrix();
        //Enable it again for the rest of scene
        gl.glEnable(GL2.GL_LIGHTING);

        float matAmbAndDif[] = {1.0f, 0.0f, 0.0f, 1.0f};
        float matSpec[] = { 1.0f, 1.0f, 1.0f, 1.0f };
        float matShine[] = { 50.0f };
        float emm[] = {0.0f, 0.0f, 0.0f, 1.0f};
        // Material properties of sphere.
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT_AND_DIFFUSE, matAmbAndDif,0);
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, matSpec,0);
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SHININESS, matShine,0);
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_EMISSION, emm,0);
        // Sphere.
        gl.glFrontFace(GL2.GL_CW);
        glut.glutSolidTeapot(1.5);
        gl.glFrontFace(GL2.GL_CCW);


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

    	gl.glEnable(GL2.GL_LIGHTING);

    	// Cull back faces.
    	gl.glEnable(GL2.GL_CULL_FACE);
    	gl.glCullFace(GL2.GL_BACK);
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
    	case KeyEvent.VK_W:			
    		light0On = !light0On;

    		break;
    	case KeyEvent.VK_B:
    		light1On = !light1On;

    		break;
    	case KeyEvent.VK_L:
    		if(localViewer == 1) localViewer = 0;
    		else localViewer = 1;
    		break;
    	case KeyEvent.VK_P:
    		//Positional vs directional
    		if(p == 1) p = 0;
    		else p = 1;

    		break;
    	case KeyEvent.VK_D:
    		if (ev.isShiftDown()) {
    			if (d < 1.0) d += 0.05;
    		} else {
    			if (d > 0.0) d -= 0.05;
    		}

    		break;
    	case KeyEvent.VK_M:
    		if (ev.isShiftDown()) {
    			if (m < 1.0) m += 0.05;
    		} else {
    			if (m > 0.0) m -= 0.05;
    		}
    		break;		
    	case KeyEvent.VK_UP:
    		xAngle--;
    		if (xAngle < 0.0) xAngle += 360.0;
    		break;
    	case KeyEvent.VK_DOWN:
    		xAngle++;
    		if (xAngle > 360.0) xAngle -= 360.0;
    		break;
    	case KeyEvent.VK_LEFT:
    		yAngle--;
    		if (yAngle < 0.0) yAngle += 360.0;
    		break;
    	case KeyEvent.VK_RIGHT:
    		yAngle++;
    		if (yAngle > 360.0) yAngle -= 360.0;
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
