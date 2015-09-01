import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;


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
 * Adapted from Guha, Sumanta (2014-08-06). Computer Graphics Through OpenGL: From Theory to Experiments, Second Edition 
 * Experimenter code.
 *
 * 
 */

public class SphereInABoxMyLighting implements GLEventListener, KeyListener,MouseMotionListener {
	private double rotateX = 0;
    private double rotateY = 0;
    private Point myMousePoint = null;
    private static final int ROTATION_SCALE = 1;
	
		
	private static int step = 0; // Steps in open/closing the box lid.

	
	// Box vertex co-ordinate vectors. 
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

        SphereInABoxMyLighting s = new SphereInABoxMyLighting();
        
        // add a GL Event listener to handle rendering
        panel.addGLEventListener(s);
        panel.addKeyListener(s);
        panel.addMouseMotionListener(s);
        panel.setFocusable(true);   
        
        // NEW: add an animator to create display events at 60 FPS
        FPSAnimator animator = new FPSAnimator(60);
        animator.add(panel);
        animator.start();
    }

    public void setUpLighting(GL2 gl) {
    	
    	
    	// Light property vectors.
    	float lightPos[] = { 0.0f, 1.5f, 3.0f, 1.0f };
    	float lightAmb[] = { 0.0f, 0.0f, 0.0f, 1.0f };
    	float lightDifAndSpec[] = { 1.0f, 1.0f, 1.0f, 1.0f };
    	float globAmb[] = { 0.2f, 0.2f, 0.2f, 1.0f };
    	
    	gl.glEnable(GL2.GL_LIGHT0); // Enable particular light source.
    	
    	// Set light properties.
    	gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, lightAmb,0);
    	gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, lightDifAndSpec,0);
    	gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_SPECULAR, lightDifAndSpec,0);
    	gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, lightPos,0);
    	
    	   	
    	gl.glLightModelfv(GL2.GL_LIGHT_MODEL_AMBIENT, globAmb,0); // Global ambient light.
    	gl.glLightModeli(GL2.GL_LIGHT_MODEL_TWO_SIDE, GL2.GL_TRUE); // Enable two-sided lighting.
    }
    
    @Override
    public void display(GLAutoDrawable drawable) {
    	  
        GL2 gl = drawable.getGL().getGL2();
       
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
     
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();

        setUpLighting(gl);
        
        GLU glu = new GLU();
        // Position the camera for viewing.
        glu.gluLookAt(0.0, 3.0, 3.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0);
        gl.glRotated(rotateX, 1, 0, 0);
        gl.glRotated(rotateY, 0, 1, 0);
        
        
        //setUpLighting(gl);
        
        // Material property vectors.
        float matAmbAndDif1[] = {0.9f, 0.0f, 0.0f, 1.0f};
        float matAmbAndDif2[] = {0.0f, 0.5f, 0.0f, 1.0f};
        float matAmbAndDif3[] = {0.9f, 0, 0.9f, 1.0f};
        float matSpec1[] = {0.2f, 0.2f, 0.2f, 1f};
        float matSpec2[] = {0.0f, 0.0f, 0.0f, 1.0f};
        float matShine[] = {150.0f};
  
        // Material properties of the box.
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT_AND_DIFFUSE, matAmbAndDif1,0);
        gl.glMaterialfv(GL2.GL_BACK, GL2.GL_AMBIENT_AND_DIFFUSE, matAmbAndDif3,0);
        gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_SPECULAR, matSpec2,0);
        gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_SHININESS, matShine,0);

      
       
        
        //gl.glEnable(GL2.GL_CULL_FACE);
        //gl.glCullFace(GL2.GL_FRONT);
        //gl.glCullFace(GL2.GL_BACK);
        
        
        gl.glBegin(GL2.GL_QUADS);{
           //Draw 5 faces of box excluding the top. (each with 4 vertices)
           for(int i=0; i< 20; i++){
        	  int index = quadIndices[i];  
        	
        	  gl.glNormal3dv(faceNormals,(i/4)*3);

        	  gl.glVertex3dv(vertices,index*3);
        	   
           }
        }gl.glEnd();
        
        // The rotated lid (top side) of the box. 
        gl.glPushMatrix(); 
        gl.glTranslated(0.0, 1.0, -1.0);
        gl.glRotated(step, -1.0, 0.0, 0.0);
        gl.glTranslated(0.0, -1.0, 1.0);
        //setUpLighting(gl);
        gl.glBegin(GL2.GL_QUADS);{
            for(int i=20; i< 24; i++){
            	int index = quadIndices[i]; 

            	gl.glNormal3dv(faceNormals,(i/4)*3);

            	gl.glVertex3dv(vertices,index*3);
         	   
            }
         }gl.glEnd();
        gl.glPopMatrix();

        // Material properties of the sphere 
        // The sphere is closed and the interior/back faces are never seen.
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT_AND_DIFFUSE, matAmbAndDif2,0);
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, matSpec1,0);
        
        // Cull the back faces of the sphere.
        //gl.glEnable(GL2.GL_CULL_FACE);
        //gl.glCullFace(GL2.GL_BACK);

        // Sphere.
        GLUT glut = new GLUT();
        glut.glutSolidSphere(1.0, 40, 40);
        
        //setUpLighting(gl);
        //gl.glDisable(GL2.GL_CULL_FACE);
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
    }

    @Override
    public void init(GLAutoDrawable drawable) {
    	GL2 gl = drawable.getGL().getGL2();
    	
    	gl.glClearColor(1.0f, 1.0f, 1.0f, 0.0f);
    	gl.glEnable(GL2.GL_DEPTH_TEST); // Enable depth testing.
    	 // Turn on OpenGL lighting.
    	gl.glEnable(GL2.GL_LIGHTING);
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int w, int h) {
    	 GL2 gl = drawable.getGL().getGL2();
         gl.glMatrixMode(GL2.GL_PROJECTION);
         gl.glLoadIdentity();
         
         GLU glu = new GLU();
        
         glu.gluPerspective(60.0, (float)w/(float)h, 1.0, 20.0);
         
    }

    @Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		 switch (e.getKeyCode()) {
		 case KeyEvent.VK_UP:
			 if (step < 180) step++;
			 break;
		 case KeyEvent.VK_DOWN:
			 if (step > 0) step--;;
			 break;
		 default:
			 break;
		 }
		
    }

	@Override
	public void keyReleased(KeyEvent arg0) {
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		Point p = e.getPoint();

        if (myMousePoint != null) {
            int dx = p.x - myMousePoint.x;
            int dy = p.y - myMousePoint.y;

            // Note: dragging in the x dir rotates about y
            //       dragging in the y dir rotates about x
            rotateY += dx * ROTATION_SCALE;
            rotateX += dy * ROTATION_SCALE;

        }
        
        myMousePoint = p;
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		 myMousePoint = e.getPoint();
	}
 
}
