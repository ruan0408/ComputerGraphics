package week8;


import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLJPanel;
import javax.media.opengl.glu.GLU;
import javax.swing.JFrame;

import com.jogamp.opengl.util.FPSAnimator;


//Texturing a cylinder.

//Control orientation with x, shift x, y, shift y, z, shift Z
public class TexCylinder implements GLEventListener, KeyListener{
	private double Xangle = 0;
    private double Yangle = 0;
	private double Zangle = 0;
  

    private static final int SLICES = 32;
    
    private String textureFileName1 = "src/week8/canTop.bmp";
    private String textureFileName2 = "src/week8/canLabel.bmp";
    private String textureExt1 = "bmp";
    private String textureExt2 = "bmp";
    
	private MyTexture myTextures[];

    public static void main(String[] args) {
        // initialisation
        GLProfile glp = GLProfile.getDefault();
        GLCapabilities caps = new GLCapabilities(glp);
        
        // create a panel to draw on
        GLJPanel panel = new GLJPanel(caps);

        // put it in a JFrame
        final JFrame jframe = new JFrame("Cylinder");
        jframe.setSize(800, 800);
        jframe.add(panel);
        jframe.setVisible(true);

        // Catch window closing events and quit             
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        TexCylinder s = new TexCylinder();
        
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
    	// re-render the scene
    	//drawable.setGL(new DebugGL2(drawable.getGL().getGL2()));
        GL2 gl = drawable.getGL().getGL2();
        
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
        gl.glLoadIdentity();

        GLU glu = new GLU();
       
     
        glu.gluLookAt(0.0, 0.0, 3.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0);
       
        gl.glTranslated(0, 0, -3);
        // Commands to turn the cylinder.
        gl.glRotated(Zangle, 0.0, 0.0, 1.0);
        gl.glRotated(Yangle, 0.0, 1.0, 0.0);
        gl.glRotated(Xangle, 1.0, 0.0, 0.0);
        gl.glTranslated(0, 0, 3);
      
        gl.glBindTexture(GL2.GL_TEXTURE_2D, myTextures[1].getTextureId());
        double angleIncrement = (Math.PI * 2.0) / SLICES;
        double zFront = -1;
        double zBack = -3;
        gl.glBegin(GL2.GL_QUAD_STRIP);{      
	        for(int i=0; i<= SLICES; i++){
	        	double angle0 = i*angleIncrement;
	        	double angle1 = (i+1)*angleIncrement;
	        	double xPos0 = Math.cos(angle0);
	        	double yPos0 = Math.sin(angle0);
	        	double sCoord = 1.0/SLICES * i; //Or * 2 to repeat label
	        	

	        	gl.glNormal3d(xPos0, yPos0, 0);
	        	gl.glTexCoord2d(sCoord,1);
	        	gl.glVertex3d(xPos0,yPos0,zFront);
	        	gl.glTexCoord2d(sCoord,0);
	        	gl.glVertex3d(xPos0,yPos0,zBack);	        	
	        	
	        }
        }gl.glEnd();    
        
        
       //Draw the top of the cylinder with the canTop.bmp texture
        gl.glBindTexture(GL2.GL_TEXTURE_2D, myTextures[0].getTextureId());
        gl.glBegin(GL2.GL_POLYGON);{
       
        for(int i = 0; i < SLICES; i++)
        {
        	double angle0 = i*angleIncrement;
        
     	    gl.glNormal3d(0.0, 0.0, 1);
     	    gl.glTexCoord2d(0.5+0.5*Math.cos(angle0),0.5+0.5*Math.sin(angle0));
     	    gl.glVertex3d(Math.cos(angle0), Math.sin(angle0),zFront);
        }
        }gl.glEnd();
        
        
        //Draw the bottom of the cylinder also with the canTop.bmp texture :)
        //just for demonstration.
        gl.glBegin(GL2.GL_POLYGON);{
           
            for(int i = 0; i < SLICES; i++)
            {
            	double angle0 = -i*angleIncrement;
            	
         	    gl.glNormal3d(0.0, 0.0, -1);
         	 
         	    gl.glTexCoord2d(0.5+0.5*Math.cos(angle0),0.5+0.5*Math.sin(angle0));
         	    gl.glVertex3d(Math.cos(angle0), Math.sin(angle0),zBack);
            }
            }gl.glEnd();
        
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

    	float matAmbAndDif2[] = {0.0f, 0.9f, 0.0f, 1.0f};
    	float matSpec[] = { 1.0f, 1.0f, 1.0f, 1.0f };
    	float matShine[] = { 50.0f };

    	// Material property vectors.
    	float matAmbAndDif1[] = {1.0f, 1.0f, 1.0f, 1.0f};

    	// Material properties.
    	gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT_AND_DIFFUSE, matAmbAndDif1,0);
    	gl.glMaterialfv(GL2.GL_BACK, GL2.GL_AMBIENT_AND_DIFFUSE, matAmbAndDif2,0);
    	gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_SPECULAR, matSpec,0);
    	gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_SHININESS, matShine,0);

    	// Create texture ids. 

    	myTextures = new MyTexture[2];
    	myTextures[0] = new MyTexture(gl,textureFileName1,textureExt1,true);
    	myTextures[1] = new MyTexture(gl,textureFileName2,textureExt2,true);

    	// Specify how texture values combine with current surface color values.
    	gl.glTexEnvf(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE, GL2.GL_MODULATE); 

    	// Turn on OpenGL texturing.
    	gl.glEnable(GL2.GL_TEXTURE_2D); 

    	   
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int w, int h) {
    	 GL2 gl = drawable.getGL().getGL2();
         gl.glMatrixMode(GL2.GL_PROJECTION);
         gl.glLoadIdentity();
         
         GLU glu = new GLU();
         glu.gluPerspective(90.0, (float)w/(float)h, 1.0, 50.0);
             
         gl.glMatrixMode(GL2.GL_MODELVIEW);
              
    }


	@Override
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
	public void keyReleased(KeyEvent ev) {
		
	}
		
    
    

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
