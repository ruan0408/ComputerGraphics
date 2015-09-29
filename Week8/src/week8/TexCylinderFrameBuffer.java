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


//Draws a scene to the framebuffer, creates a texture from it
//Clears the framebuffer and renders a scene using the texture!

public class TexCylinderFrameBuffer implements GLEventListener, KeyListener{
	private int width;
	private int height;
	private double pos = -4;
	private double theta = 0;
			
	private double Xangle = 0;
    private double Yangle = 0;
	private double Zangle = 0;
  
    private static final int SLICES = 32;
    
    private int textures[];

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

        TexCylinderFrameBuffer s = new TexCylinderFrameBuffer();
        
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

    	//Set it up to render texture scene into buffer
    	
    	gl.glDisable(GL2.GL_LIGHTING);
    	gl.glDisable(GL2.GL_TEXTURE_2D);
    	draw2DFrame(gl);
    	
    	gl.glEnable(GL2.GL_TEXTURE_2D);
    	gl.glEnable(GL2.GL_LIGHTING);
    	
    	//Get the information about the dimensions
    	int[] viewPort = new int[4];  // The current viewport; x and y will be 0.
    	gl.glGetIntegerv(GL2.GL_VIEWPORT, viewPort, 0);
    	int textureWidth = viewPort[2];  // The width of the texture.
    	int textureHeight = viewPort[3]; // The height of the texture.
    	
	        
		/* Grab the image from the color buffer for use as a 2D texture. */
		gl.glBindTexture(GL2.GL_TEXTURE_2D, textures[0]); 
		gl.glCopyTexImage2D(GL2.GL_TEXTURE_2D, 0, GL2.GL_RGBA, 
				0, 0, textureWidth, textureHeight, 0);
		
		gl.glClearColor(0f, 0f, 0f, 1f);
	
		//Now render the actual cylinder
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);

        //Need to set the camera 
        //to render the cylinder
        setCamera(gl);
        
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();

        GLU glu = new GLU();
         
        glu.gluLookAt(0.0, 0.0, 3.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0);
        gl.glTranslated(0, 0, -3);
        // Commands to turn the cylinder.
        gl.glRotated(Zangle, 0.0, 0.0, 1.0);
        gl.glRotated(Yangle, 0.0, 1.0, 0.0);
        gl.glRotated(Xangle, 1.0, 0.0, 0.0);

        //Use the texture we created from the framebuffer
        gl.glBindTexture(GL2.GL_TEXTURE_2D, textures[0]); 
        gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_LINEAR);
       
        double angleIncrement = (Math.PI * 2.0) / SLICES;
        double zFront = -1;
        double zBack = -3;
        gl.glBegin(GL2.GL_QUAD_STRIP);{      
	        for(int i=0; i<= SLICES; i++){
	        	double angle0 = i*angleIncrement;
	        	double angle1 = (i+1)*angleIncrement;
	        	double xPos0 = Math.cos(angle0);
	        	double yPos0 = Math.sin(angle0);
	        	double sCoord = 1.0/SLICES * i;
	        

	        	gl.glNormal3d(xPos0, yPos0, 0);
	        	gl.glTexCoord2d(sCoord,0);
	        	gl.glVertex3d(xPos0,yPos0,zFront);
	        	gl.glTexCoord2d(sCoord,1);
	        	gl.glVertex3d(xPos0,yPos0,zBack);	        	
	        	
	        }
        }gl.glEnd();    
           	  
  
        gl.glBegin(GL2.GL_POLYGON);{
       
        for(int i = 0; i < SLICES; i++)
        {
        	double angle0 = i*angleIncrement;
        	
     	    gl.glNormal3d(0.0, 0.0, 1);
     	    gl.glTexCoord2d(0.5+0.5*Math.cos(angle0),0.5+0.5*Math.sin(angle0));
     	    gl.glVertex3d(Math.cos(angle0), Math.sin(angle0),zFront);
        }
        }gl.glEnd();
        
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

    //Draw a scene with a sun into the frame buffer
    //this does not end up getting displayed on viewport
    //but gets used as a texture.
    public void draw2DFrame(GL2 gl){
    	
    	update();
    	
    	
		gl.glClearColor(0.5f, 0.5f, 1, 1);
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT|GL2.GL_DEPTH_BUFFER_BIT); // Fills the scene with blue.
		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glOrtho(0, 7, -1, 4, -1, 1);
		
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glLoadIdentity();
  
  	   
        gl.glPushMatrix();
        gl.glTranslated(2,2.5,0);
        gl.glRotated(theta,0,0,1);
        drawSun(gl);
        gl.glPopMatrix();
            
     }
    
    
    /**
     * Draw a sun with radius 0.5 centered at (0,0).  There are also 13 rays which
     * extend outside from the sun for another 0.25 units.
     */
    private void drawSun(GL2 gl) {
       gl.glColor4f(1,1,0,1);
       for (int i = 0; i < 13; i++) { // Draw 13 rays, with different rotations.
          gl.glRotatef( 360f / 13, 0, 0, 1 ); // Note that the rotations accumulate!
          drawLine(gl,0.75);
       }
       drawCircle(gl, 0.5);
       
    }
    
    /**
     * Draw a 32-sided regular polygon as an approximation for a circular circle.
     * 
     */
    private void drawCircle(GL2 gl, double radius) {
     
 	  gl.glBegin(GL2.GL_POLYGON);
       for (int d = 0; d < 32; d++) {
          double angle = 2*Math.PI/32 * d;
          gl.glVertex2d( radius*Math.cos(angle), radius*Math.sin(angle));
       }
       gl.glEnd();
      
      
      
    }
    
    private void drawLine(GL2 gl, double size) {
        gl.glBegin(GL2.GL_LINES);{
     	   gl.glVertex2d(0,0);
     	   gl.glVertex2d(size,0);
        }gl.glEnd();
 	     
    }
    @Override
    public void dispose(GLAutoDrawable drawable) {
    }

    @Override
    public void init(GLAutoDrawable drawable) {
    	GL2 gl = drawable.getGL().getGL2();
    	gl.glEnable(GL2.GL_DEPTH_TEST);
    
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
    	textures = new int[1];
    	gl.glGenTextures(1, textures,0);

    	   
    }
    
    private void setCamera(GL2 gl){
    	 gl.glMatrixMode(GL2.GL_PROJECTION);
         gl.glLoadIdentity();
         
         GLU glu = new GLU();
         glu.gluPerspective(70.0, (float)width/(float)height, 1.0, 50.0);
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int w, int h) {
    	 GL2 gl = drawable.getGL().getGL2();
       
         width = w;
         height = h;
         
         setCamera(gl);      
         gl.glMatrixMode(GL2.GL_MODELVIEW);
              
    }

    //Update model of sun used in texture
    private void update() {	      
        pos = pos + 0.1;
        if(pos > 5) pos = -4;
        theta = theta + 0.5;
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
