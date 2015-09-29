package week8.advanced;


import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import javax.media.opengl.DebugGL2;
import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLJPanel;
import javax.media.opengl.glu.GLU;
import javax.media.opengl.glu.GLUquadric;
import javax.swing.JFrame;
import com.jogamp.opengl.util.FPSAnimator;
import com.jogamp.opengl.util.gl2.GLUT;

import week8.MyTexture;

//Use Sphere mapping. Opengl auto-generates texture co-ordinates
//as the texture is being reflected by the teapot.
//This uses sphere mapping which is not as nice as cube mapping
//Cube mapping requires 6 points of view instead of just 1 though.
//This implementation loads a texture map, a texture can also be generated
//on the fly with another render pass instead.
public class SphereMapping implements GLEventListener, KeyListener,MouseMotionListener {
	private double Xangle = 90;
    private double Yangle = 0;
	private double Zangle = 0;
	   
	private MyTexture myTextures[];
	
	private GLUquadric qobj; 

    int currText = 0;
	
    public static void main(String[] args) {
        // initialisation
        GLProfile glp = GLProfile.getDefault();
        GLCapabilities caps = new GLCapabilities(glp);
        
        // create a panel to draw on
        GLJPanel panel = new GLJPanel(caps);

        // put it in a JFrame
        final JFrame jframe = new JFrame("Texture Example");
        jframe.setSize(800, 800);
        jframe.add(panel);
        jframe.setVisible(true);

        // Catch window closing events and quit             
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        SphereMapping s = new SphereMapping();
        
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
    
    @Override
    public void display(GLAutoDrawable drawable) {
    	  
        GL2 gl = drawable.getGL().getGL2();
    	
    	//gl.glEnable(GL2.GL_DEPTH_TEST);
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
     
        GLU glu = new GLU();
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();
        gl.glTranslated(0,0,-7);
        // Map the launch texture onto a rectangle parallel to the xy-plane.
        gl.glRotated(Zangle, 0.0, 0.0, 1.0);
        gl.glRotated(Yangle, 0.0, 1.0, 0.0);
        gl.glRotated(Xangle, 1.0, 0.0, 0.0);
        GLUT glut = new GLUT();
        
        gl.glEnable(GL2.GL_TEXTURE_GEN_S);	
        gl.glEnable(GL2.GL_TEXTURE_GEN_T);	
        gl.glBindTexture(GL2.GL_TEXTURE_2D, myTextures[0].getTextureId());   
        // Specify how texture values combine with current surface color values.
    	gl.glTexEnvf(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE, GL2.GL_REPLACE); 
      
        gl.glFrontFace(GL2.GL_CW);
        glut.glutSolidTeapot(1);
        gl.glFrontFace(GL2.GL_CCW);
        gl.glDisable(GL2.GL_TEXTURE_GEN_S);
        gl.glDisable(GL2.GL_TEXTURE_GEN_T);
                

    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
    }

   
    
    @Override
    public void init(GLAutoDrawable drawable) {
    	drawable.setGL(new DebugGL2(drawable.getGL().getGL2()));
    	GL2 gl = drawable.getGL().getGL2();
    	gl.glClearColor(0f, 0.0f, 0.0f, 0.0f); 

    	gl.glEnable(GL2.GL_DEPTH_TEST); // Enable depth testing.

    	gl.glEnable(GL2.GL_LIGHTING);

    	// Light property vectors.
    	float lightAmb[] = { 0.0f, 0.0f, 0.0f, 1.0f };
    	float lightDifAndSpec[] = { 1.0f, 1.0f, 1.0f, 1.0f };
    	float lightPos[] = { 0.0f, 1.5f, 3.0f, 0.0f };
    	float globAmb[] = { 0.2f, 0.2f, 0.2f, 1.0f };

    	// Light properties.
    	gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, lightAmb,0);
    	gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, lightDifAndSpec,0);
    	gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_SPECULAR, lightDifAndSpec,0);
    	gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, lightPos,0);

    	gl.glEnable(GL2.GL_LIGHT0); // Enable particular light source.
    	gl.glLightModelfv(GL2.GL_LIGHT_MODEL_AMBIENT, globAmb,0); // Global ambient light.
    	gl.glLightModeli(GL2.GL_LIGHT_MODEL_TWO_SIDE, GL2.GL_TRUE); // Enable two-sided lighting.
    	gl.glLightModeli(GL2.GL_LIGHT_MODEL_COLOR_CONTROL, GL2.GL_SEPARATE_SPECULAR_COLOR);


    	// Material property vectors.

    	
    	float matSpec[] = { 1.0f, 1.0f, 1.0f, 1.0f };
    	float matShine[] = { 50.0f };

    	// Material property vectors.
    	float matAmbAndDif1[] = {1.0f, 1.0f, 1.0f, 1.0f};

    	// Material properties.
    	gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT_AND_DIFFUSE, matAmbAndDif1,0);
    	gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_SPECULAR, matSpec,0);
    	gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_SHININESS, matShine,0);
    	
    	// Turn on OpenGL texturing.
    	gl.glEnable(GL2.GL_TEXTURE_2D); 
    	myTextures = new MyTexture[1];
    	myTextures[0] = new MyTexture(gl,"kittens.jpg","jpg",true);  	

    	// Specify automatic texture generation for a sphere map.
    	gl.glTexGeni(GL2.GL_S, GL2.GL_TEXTURE_GEN_MODE, GL2.GL_SPHERE_MAP); 
    	gl.glTexGeni(GL2.GL_T, GL2.GL_TEXTURE_GEN_MODE, GL2.GL_SPHERE_MAP);

    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int w, int h) {
    	 GL2 gl = drawable.getGL().getGL2();
         gl.glMatrixMode(GL2.GL_PROJECTION);
         gl.glLoadIdentity();
       
         gl.glFrustum(-2.0, 2.0, -2.0, 2.0, 5.0, 100.0);
         gl.glMatrixMode(GL2.GL_MODELVIEW);
         gl.glLoadIdentity();
       
    }
    
    public void keyPressed(KeyEvent ev) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
				 switch (ev.getKeyCode()) {
			
			 case KeyEvent.VK_X:
				  if (ev.isShiftDown()) {
					  Xangle -= 5.0;
					  if (Xangle < 0.0) Xangle += 360.0;
				  } else {
					  Xangle += 5.0;
						 if (Xangle > 360.0) Xangle -= 360.0;
					 
				  }
		       
				 break;
			  case KeyEvent.VK_Y:
				  if (ev.isShiftDown()) {
					  Yangle -= 5.0;
						 if (Yangle < 0.0) Yangle += 360.0;
				  } else {
					  Yangle += 5.0;
						 if (Yangle > 360.0) Yangle -= 360.0;
				  }
				 break;		
			  case KeyEvent.VK_Z:
				  if (ev.isShiftDown()) {
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

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		

		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

    
}
