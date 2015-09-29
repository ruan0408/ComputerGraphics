package week8;

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



/**
 * An example of multi-texturing
 *
 */


public class MultiTexture implements GLEventListener, KeyListener,MouseMotionListener {

	private float alpha = 0.5f;                  // Interpolation parameter.
	private float constantColor[] = new float[4]; // Texture environment color.

	
	//Texture file information
	private String TEX_0 = "src/week8/metal.jpg";
	private String TEX_1 = "src/week8/Lights.png";
	private String TEX_F_0 = ".jpg";
	private String TEX_F_1 = ".png";
	
	//Texture data
	private MyTexture myTextures[] = new MyTexture[2];
	
	
	
    public static void main(String[] args) {
        
        GLProfile glp = GLProfile.getDefault();
        GLCapabilities caps = new GLCapabilities(glp);
        
        // create a panel to draw on
        GLJPanel panel = new GLJPanel(caps);

        // put it in a JFrame
        final JFrame jframe = new JFrame("Multi-Texture Example");
        jframe.setSize(600, 600);
        jframe.add(panel);
        jframe.setVisible(true);

        // Catch window closing events and quit             
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        MultiTexture s = new MultiTexture();
        
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
    public void init(GLAutoDrawable drawable) {
    	GL2 gl = drawable.getGL().getGL2();
    	gl.glClearColor(0f, 0.0f, 0.0f, 0.0f); 
    	gl.glEnable(GL2.GL_DEPTH_TEST); 
    	
    	
        myTextures[0] = new MyTexture(gl,TEX_0,TEX_F_0,false);
        myTextures[1] = new MyTexture(gl,TEX_1,TEX_F_1,false);
    	    	
    	// Select texture unit 0 as the currently active texture unit and specify its texture states.
    	gl.glActiveTexture(GL2.GL_TEXTURE0); 	
    	gl.glEnable(GL2.GL_TEXTURE_2D);
    	gl.glBindTexture(GL2.GL_TEXTURE_2D, myTextures[0].getTextureId());  
    	gl.glTexEnvi(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE, GL2.GL_REPLACE); 

    	// Select texture unit 1 as the currently active texture unit and specify its texture states.
    	gl.glActiveTexture(GL2.GL_TEXTURE1);
    	gl.glEnable(GL2.GL_TEXTURE_2D);
    	gl.glBindTexture(GL2.GL_TEXTURE_2D, myTextures[1].getTextureId());
    	   	
    	// Unit 1 COMBINES with unit 0 in a manner
    	// specified by the combiner function.
    	gl.glTexEnvi(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE, GL2.GL_COMBINE); 
    	
    	// The COMBINER FUNCTION is specified to be interpolation between RGB values of Arg0 and Arg1 
    	// according to the formula: Arg0 * Arg2 + Arg1 * (1-Arg2)
    	gl.glTexEnvi(GL2.GL_TEXTURE_ENV, GL2.GL_COMBINE_RGB, GL2.GL_INTERPOLATE); 

    	// Set of statements specifying the interpolation combiner's arguments.
    	gl.glTexEnvi(GL2.GL_TEXTURE_ENV, GL2.GL_SRC0_RGB, GL2.GL_PREVIOUS); // Texture combiner's zeroth source's RGB are from previous texture. 
    	gl.glTexEnvi(GL2.GL_TEXTURE_ENV, GL2.GL_SRC1_RGB, GL2.GL_TEXTURE); // Texture combiner's first source's RGB are from current texture.
    	gl.glTexEnvi(GL2.GL_TEXTURE_ENV, GL2.GL_SRC2_ALPHA, GL2.GL_CONSTANT); // Texture combiner's second source's alpha is from constant environment color.
    	gl.glTexEnvi(GL2.GL_TEXTURE_ENV, GL2.GL_OPERAND0_RGB, GL2.GL_SRC_COLOR); // Arg0's RGB values are zeroth source's color.
    	gl.glTexEnvi(GL2.GL_TEXTURE_ENV, GL2.GL_OPERAND1_RGB, GL2.GL_SRC_COLOR); // Arg1's RGB values are first source's color.
    	gl.glTexEnvi(GL2.GL_TEXTURE_ENV, GL2.GL_OPERAND2_ALPHA, GL2.GL_SRC_ALPHA); // Arg2 is second source's alpha.
  
    	
    }

    
    @Override
    public void display(GLAutoDrawable drawable) {
    	  
        GL2 gl = drawable.getGL().getGL2();
    	
  	
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
     
        GLU glu = new GLU();
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();
            
        glu.gluLookAt(0.0, 0.0, 20.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0);
        
        // Select texture unit 0 as the currently active texture unit and specify its texture states.
    	gl.glActiveTexture(GL2.GL_TEXTURE0); 	
    	gl.glEnable(GL2.GL_TEXTURE_2D);
    	gl.glBindTexture(GL2.GL_TEXTURE_2D, myTextures[0].getTextureId());
  

    	// Select texture unit 1 as the currently active texture unit and specify its texture states.
    	gl.glActiveTexture(GL2.GL_TEXTURE1);
    	gl.glEnable(GL2.GL_TEXTURE_2D);
    	gl.glBindTexture(GL2.GL_TEXTURE_2D, myTextures[1].getTextureId());
        
        // Set the interpolation parameter, which is the alpha of environment color (i.e., Arg 2 in setup).
        constantColor[3] = alpha; 
        // Specify the constant texture environment color.        
        gl.glTexEnvfv(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_COLOR, constantColor,0); 
        
        
        //Draw the polygon
        gl.glBegin(GL2.GL_POLYGON);
       
        gl.glMultiTexCoord2d(GL2.GL_TEXTURE0, 0.0, 0.0); 
        gl.glMultiTexCoord2d(GL2.GL_TEXTURE1, 0.0, 0.0);  
        gl.glVertex3d(-20.0, -20.0, 0.0);

        gl.glMultiTexCoord2d(GL2.GL_TEXTURE0, 1.0, 0.0); 
        gl.glMultiTexCoord2d(GL2.GL_TEXTURE1, 2.0, 0.0);       
        gl.glVertex3d(20.0, -20.0, 0.0);
      
        gl.glMultiTexCoord2d(GL2.GL_TEXTURE0, 1.0, 1.0); 
        gl.glMultiTexCoord2d(GL2.GL_TEXTURE1, 2.0, 2.0); 
     	gl.glVertex3d(20.0, 20.0, 0.0);

        gl.glMultiTexCoord2d(GL2.GL_TEXTURE0, 0.0, 1.0); 
        gl.glMultiTexCoord2d(GL2.GL_TEXTURE1, 0.0, 2.0); 
     	gl.glVertex3d(-20.0, 20.0, 0.0);
        gl.glEnd();
        

    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
    }

   
    
    
    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int w, int h) {
    	 GL2 gl = drawable.getGL().getGL2();
         gl.glMatrixMode(GL2.GL_PROJECTION);
         gl.glLoadIdentity();
       
         gl.glFrustum(-5.0, 5.0, -5.0, 5.0, 5.0, 100.0);
         gl.glMatrixMode(GL2.GL_MODELVIEW);
         gl.glLoadIdentity();
       
    }

    @Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		 switch (e.getKeyCode()) {
		
		 case KeyEvent.VK_UP:
			   if (alpha < 1.0) alpha += 0.01;
		     
			   break;
		 
		   
		 case KeyEvent.VK_DOWN:
			 if (alpha > 0.0) alpha -= 0.01;
			 
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
		Point p = e.getPoint();

		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		 //myMousePoint = e.getPoint();
	}

    
}
