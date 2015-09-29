package week8;


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



import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.util.FPSAnimator;

import java.nio.ByteBuffer;

import week8.MyTexture;




/**
 * This program that allows the user to press the space bar and cycle through different textures
 * Shows how to map texture co-ordinates to a simple rectangle.
 * Two procedures are loaded from files, two are generated procedurally
 * @author Angela
 *
 */


public class TextureExample1 implements GLEventListener, KeyListener{
    private final int NUM_TEXTURES = 4;
	private int imageSize = 64;
    private static final int rgba = 4;
    
    //Buffers for the procedural textures
    private ByteBuffer chessImageBuf = Buffers.newDirectByteBuffer(imageSize*imageSize* rgba);
    private ByteBuffer randomImageBuf = Buffers.newDirectByteBuffer(imageSize*imageSize* rgba);
    private String textureFileName1 = "src/week8/kittens.jpg";
    private String textureFileName2 = "src/week8/canLabel.bmp"; 
    private String textureExt1 = "jpg";
    private String textureExt2 = "bmp";
    
    private boolean modulate = false;
    private boolean clamp_S = false;
    private boolean clamp_T = false;
    

	private MyTexture myTextures[];
	private int currIndex = 0; // Currently displayed texture index

	//Creates a random Texture. Each pixel has random R,G,B value
	//And an alpha value of 255. Pixels values go from 0..255 (not 0..1 like opengl settings)
	private void createRandomTex(){

		int i, j;
		for(i=0; i<imageSize; i++)
		{
			for(j=0; j<imageSize; j++)
			{

				randomImageBuf.put((byte)(255 * Math.random())); //R
				randomImageBuf.put((byte)(255 * Math.random())); //G
				randomImageBuf.put((byte)(255 * Math.random())); //B
				randomImageBuf.put((byte)0xFF); //A
			}
		}
		 randomImageBuf.rewind();
	}

	// Create 64 x 64 RGBA image of a chessboard.
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

        TextureExample1 s = new TextureExample1();
        
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
        glu.gluLookAt(0.0, 0.0, 20.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0);
               

    	if(modulate){
    		gl.glTexEnvf(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE, GL2.GL_MODULATE); 
    	} else {
    		gl.glTexEnvf(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE, GL2.GL_REPLACE); 
    	}
        
         	          
        //Set wrap mode for texture in S direction
        if(clamp_S){
        	//gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_S, GL2.GL_CLAMP_TO_EDGE);     
        	float color[] = { 1.0f, 0.0f, 0.0f, 1.0f };
        	gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_S, GL2.GL_CLAMP_TO_BORDER); 
        	gl.glTexParameterfv(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_BORDER_COLOR, color,0);
        } else {
        	 gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_S, GL2.GL_REPEAT);  
        }
        
        if(clamp_T){
        	gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_T, GL2.GL_CLAMP_TO_EDGE);      	
        } else {
        	 gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_T, GL2.GL_REPEAT);  
        }
       
    
        // Set current texture
        gl.glBindTexture(GL2.GL_TEXTURE_2D, myTextures[currIndex].getTextureId());   
        
        gl.glBegin(GL2.GL_POLYGON);{
           gl.glColor4d(0,1,0,1);
                  
          
           gl.glTexCoord2d(0, 0.0); gl.glVertex3d(-10.0, 0.0, 0.0);
           gl.glTexCoord2d(1, 0.0); gl.glVertex3d(10.0, 0.0, 0.0);
           gl.glTexCoord2d(1, 1); gl.glVertex3d(10.0, 10.0, 0.0);
           gl.glTexCoord2d(0, 1.0); gl.glVertex3d(-10.0, 10.0, 0.0);
                      
        }gl.glEnd();
        
        //Specify current texture to be 0 - no texture
        gl.glBindTexture(GL2.GL_TEXTURE_2D, 0);     
       
        gl.glBegin(GL2.GL_POLYGON);{
            gl.glColor4d(1,0,0,1);
        
            //gl.glTexCoord2d(-1, 0); 
            gl.glVertex3d(-10.0, -10.0, 0.0);
            //gl.glTexCoord2d(2, 0.0); 
            gl.glVertex3d(10.0, -10.0, 0.0);     
            //gl.glTexCoord2d(0.5, 2.0); 
            gl.glVertex3d(0.0, -5.0, 0.0);
                                  
         }gl.glEnd();
        
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
   
    	//Load in textures from files
    	myTextures = new MyTexture[NUM_TEXTURES];
    	myTextures[0] = new MyTexture(gl,textureFileName1,textureExt1,true);
    	myTextures[1] = new MyTexture(gl,textureFileName2,textureExt2,true);

    	// Generate procedural texture.
    	createRandomTex();
    	createChessboard();
    	
    	//Load procedural textures
    	myTextures[2] = new MyTexture(gl,chessImageBuf,imageSize,true);
    	myTextures[3] = new MyTexture(gl,randomImageBuf,imageSize,true);
        
     
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int w, int h) {
    	 GL2 gl = drawable.getGL().getGL2();
         gl.glMatrixMode(GL2.GL_PROJECTION);
         gl.glLoadIdentity();
         
         gl.glFrustum(-3.0, 3.0, -3.0, 3.0, 5.0, 100.0);
         gl.glMatrixMode(GL2.GL_MODELVIEW);
         gl.glLoadIdentity();
       
    }

    @Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		 switch (e.getKeyCode()) {
		 case KeyEvent.VK_SPACE:
			 currIndex++;
			 currIndex = currIndex%NUM_TEXTURES;
			 break;
		 case KeyEvent.VK_M:
			 modulate = !modulate;
			 
			 break;
		 case KeyEvent.VK_S:
			 clamp_S = !clamp_S;
			 
			 break;
		 case KeyEvent.VK_T:
			 clamp_T = !clamp_T;		 
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
