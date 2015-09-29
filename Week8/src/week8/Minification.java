package week8;


import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.nio.ByteBuffer;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLJPanel;
import javax.media.opengl.glu.GLU;
import javax.swing.JFrame;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.util.FPSAnimator;

/**
 * Guha, Sumanta (2014-08-06). Computer Graphics Through OpenGL: From Theory to Experiments, Second Edition 
 * Experimenter code.
 *
 * Adapted to jogl and modified by Angela Finlayson 
 * 
 * Shows the shimmering of the grass as we zoom in and out.
 * This is due to minification.
 * Turn on mipmapping by changing lines to
 * myTextures[0] = new MyTexture(gl,textureFileName1,textureExt1,true);
 * myTextures[1] = new MyTexture(gl,textureFileName2,textureExt2,true);
 */

public class Minification implements GLEventListener, KeyListener {
	
	
	private String textureFileName1 = "src/week8/grass.bmp";
	private String textureFileName2 = "src./week8/sky.bmp";
	private String textureExt1 = "bmp";
	private String textureExt2 = "bmp";
	private double d;
	
	
	
	private int imageSize = 64;
    private static final int rgba = 4;
    private ByteBuffer chessImageBuf = Buffers.newDirectByteBuffer(imageSize*imageSize* rgba);
    
	private MyTexture myTextures[];
	
	private void createChessboard()
	{
	   int i, j;
	   for (i = 0; i < imageSize; i++) 
	      for (j = 0; j < imageSize; j++) 
	         if ( ( ((i/8)%2 == 1) && ((j/8)%2 == 1) ) || ( !((i/8)%2 == 1) && !((j/8)%2 == 1) ) )
			 {
	        	 
	            chessImageBuf.put((byte)0x00); //R = 0
	            chessImageBuf.put((byte)0x00); //G = 0
	            chessImageBuf.put((byte)0x00); //B = 0
	            chessImageBuf.put((byte)0xFF); //A = 1
			 }
			 else
			 {	
				 
				 chessImageBuf.put((byte)0xFF); //R = 1
				 chessImageBuf.put((byte)0xFF); //G = 1
				 chessImageBuf.put((byte)0xFF); //B = 1
				 chessImageBuf.put((byte)0xFF); //A = 1
			 }
	         chessImageBuf.rewind();
	}
	
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

        Minification s = new Minification();
        
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
     
        GLU glu = new GLU();
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();
        
        glu.gluLookAt(0.0, 10.0, 15.0 + d, 0.0, 10.0, d, 0.0, 1.0, 0.0);
         
       // Specify how texture values combine with current surface color values.
    	gl.glTexEnvf(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE, GL2.GL_REPLACE);     

        // Map the grass texture onto a rectangle along the xz-plane.
        gl.glBindTexture(GL2.GL_TEXTURE_2D, myTextures[0].getTextureId());        
        gl.glBegin(GL2.GL_POLYGON);
           gl.glTexCoord2d(0.0, 0.0); gl.glVertex3d(-100.0, 0.0, 100.0);
           gl.glTexCoord2d(8.0, 0.0); gl.glVertex3d(100.0, 0.0, 100.0);
           gl.glTexCoord2d(8.0, 8.0); gl.glVertex3d(100.0, 0.0, -100.0);
           gl.glTexCoord2d(0.0, 8.0); gl.glVertex3d(-100.0, 0.0, -100.0);
        gl.glEnd();

        // Map the sky texture onto a rectangle parallel to the xy-plane.
        gl.glBindTexture(GL2.GL_TEXTURE_2D, myTextures[1].getTextureId());        
        gl.glBegin(GL2.GL_POLYGON);
           gl.glTexCoord2d(0.0, 0.0); gl.glVertex3d(-100.0, 0.0, -70.0);
           gl.glTexCoord2d(1, 0.0); gl.glVertex3d(100.0, 0.0, -70.0);
           gl.glTexCoord2d(1, 1); gl.glVertex3d(100.0, 120.0, -70.0);
           gl.glTexCoord2d(0.0, 1); gl.glVertex3d(-100.0, 120.0, -70.0);
        gl.glEnd();
        
     

    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
    }

   
    
    @Override
    public void init(GLAutoDrawable drawable) {
    	GL2 gl = drawable.getGL().getGL2();
    	gl.glClearColor(0.8f, 0.8f, 0.8f, 0.0f); 
    	
    	gl.glEnable(GL2.GL_DEPTH_TEST); // Enable depth testing.
    	
    	// Turn on OpenGL texturing.
    	gl.glEnable(GL2.GL_TEXTURE_2D);
    
    	
    	myTextures = new MyTexture[2];
    	//gl.glHint(GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL2.GL_DONT_CARE);
    	gl.glHint(GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL2.GL_NICEST);

    	// Generate procedural texture.
   
    	createChessboard();
    	
    	//Compare setting mipmap to true instead of false
    	//myTextures[0] = new MyTexture(gl,chessImageBuf,imageSize,true);
    	myTextures[0] = new MyTexture(gl,textureFileName1,textureExt1,false);
    	myTextures[1] = new MyTexture(gl,textureFileName2,textureExt2,false);
        
    	
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int w, int h) {
    	 GL2 gl = drawable.getGL().getGL2();
         gl.glMatrixMode(GL2.GL_PROJECTION);
         gl.glLoadIdentity();
       
         gl.glFrustum(-5.0, 5.0, -5.0, 5.0, 5.0, 200.0);
         gl.glMatrixMode(GL2.GL_MODELVIEW);
         gl.glLoadIdentity();
       
    }

    @Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		 switch (e.getKeyCode()) {
		
		 case KeyEvent.VK_UP:
			 if (d > -50.0) d -= 0.2;
		   
			 break;
		 case KeyEvent.VK_DOWN:
			 if (d < 200) d += 0.2;
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
