package week8;


import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.media.opengl.DebugGL2;
import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLProfile;
import javax.media.opengl.TraceGL2;
import javax.media.opengl.awt.GLJPanel;
import javax.media.opengl.glu.GLU;
import javax.swing.JFrame;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.util.FPSAnimator;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import week6.Shader;
import week7Old.MyTexture;


/**
 * This program that allows the user to press the space bar and cycle through different textures
 * Shows how to map texture co-ordinates to a simple rectangle.
 * Two procedures are loaded from files, two are generated procedurally
 * @author Angela
 *
 */


public class TextureExample1VBO implements GLEventListener, KeyListener{
	private static final String VERTEX_SHADER = "src/week8/PhongVertexTex.glsl";
    private static final String FRAGMENT_SHADER = "src/week8/PhongFragmentTex.glsl";
	
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
    
    int texUnitLoc;
    int texUnitLoc2;
    
    private int shaderprogram;
    
    int bufferIds[];
    
	MyTexture myTextures[];
	private int currIndex = 0; // Currently displayed texture index
	
	/*
	 gl.glTexCoord2d(0, 0.0); gl.glVertex3d(-10.0, 0.0, 0.0);
     gl.glTexCoord2d(1, 0.0); gl.glVertex3d(10.0, 0.0, 0.0);
     gl.glTexCoord2d(1, 1); gl.glVertex3d(10.0, 10.0, 0.0);
     gl.glTexCoord2d(0, 1.0); gl.glVertex3d(-10.0, 10.0, 0.0);
	*/
	// Vertex co-ordinate vectors.
	private float vertices[] =  
		{	
			-10,0,0,1,
			10,0,0,1,
			10,10,0,1,
			-10,10,0,1
		};

	private float texCoords[] =  
		{	0,0,1,0,1,1,0,1

		};

	private FloatBuffer vertexBuffer = Buffers.newDirectFloatBuffer(vertices);
	private FloatBuffer texBuffer = Buffers.newDirectFloatBuffer(texCoords);

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
	        	 
	            chessImageBuf.put((byte)0x00); //R
	            chessImageBuf.put((byte)0x00); //G
	            chessImageBuf.put((byte)0x00); //B
	            chessImageBuf.put((byte)0xFF); //A
			 }
			 else
			 {	
				 
				 chessImageBuf.put((byte)0xFF); //R
				 chessImageBuf.put((byte)0xFF); //G
				 chessImageBuf.put((byte)0xFF); //B
				 chessImageBuf.put((byte)0xFF); //A
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

        TextureExample1VBO s = new TextureExample1VBO();
        
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

    	gl.glUseProgram(shaderprogram);
    	gl.glUniform1i(texUnitLoc , 0);
    	

    	// Set current texture
    	gl.glActiveTexture(GL2.GL_TEXTURE0);
    	gl.glBindTexture(GL2.GL_TEXTURE_2D, myTextures[currIndex].getTextureId());        
    	      
    	//Set wrap mode for texture in S direction
    	gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_S, GL2.GL_REPEAT); 
    	//Set wrap mode for texture in T direction
    	gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_T, GL2.GL_CLAMP_TO_EDGE);

    	gl.glEnableClientState(GL2.GL_VERTEX_ARRAY);
    	gl.glEnableClientState(GL2.GL_TEXTURE_COORD_ARRAY);

    	gl.glBindBuffer(GL.GL_ARRAY_BUFFER,bufferIds[0]);
    	// Specify locations for the co-ordinates and color arrays.
    	gl.glVertexPointer(4, GL.GL_FLOAT, 0, 0); //last num is the offset
    	gl.glTexCoordPointer(2, GL.GL_FLOAT, 0, vertices.length*4);

    	gl.glDrawArrays(GL2.GL_QUADS,0,4);
    	gl.glBindBuffer(GL.GL_ARRAY_BUFFER,0);
    	gl.glDisableClientState(GL2.GL_VERTEX_ARRAY);
    	gl.glDisableClientState(GL2.GL_TEXTURE_COORD_ARRAY);

    	//Specify current texture to be 0 - no texture
    	gl.glBindTexture(GL2.GL_TEXTURE_2D, 0);     
    	gl.glUseProgram(0);
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
    	
    	drawable.setGL(new DebugGL2( drawable.getGL().getGL2()));
    	GL2 gl = drawable.getGL().getGL2();
    	gl.glClearColor(0.8f, 0.8f, 0.8f, 0.0f); 
    	
    	gl.glEnable(GL2.GL_DEPTH_TEST); // Enable depth testing.
    	
    	// Turn on OpenGL texturing.
    	gl.glEnable(GL2.GL_TEXTURE_2D);
   
    	//Load in textures from files
    	myTextures = new MyTexture[NUM_TEXTURES];
    	myTextures[0] = new MyTexture(gl,textureFileName1,textureExt1);
    	myTextures[1] = new MyTexture(gl,textureFileName2,textureExt2);

    	// Generate procedural texture.
    	createRandomTex();
    	createChessboard();
    	
    	//Load procedural textures
    	myTextures[2] = new MyTexture(gl,chessImageBuf,imageSize);
    	myTextures[3] = new MyTexture(gl,randomImageBuf,imageSize);
        
    	// Specify how texture values combine with current surface color values.
        // Replace just covers the underlying polygon, ignoring the underlying material
    	//Change GL2.GL_REPLACE to GL2.GL_MODULATE to see the difference 
    	gl.glTexEnvf(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE, GL2.GL_REPLACE); 
    
        
   	 try {
   		 shaderprogram = Shader.initShaders(gl,VERTEX_SHADER,FRAGMENT_SHADER);
   		
   		 
        }
        catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    	texUnitLoc = gl.glGetUniformLocation(shaderprogram,"texUnit1");
    	texUnitLoc2 = gl.glGetUniformLocation(shaderprogram,"texUnit2");
    	
    	
    	 bufferIds = new int[1];
          gl.glGenBuffers(1,bufferIds,0);
          //would use GL.GL_ELEMENT_ARRAY_BUFFER for indices
     	 //Say that is the current array buffer
          gl.glBindBuffer(GL.GL_ARRAY_BUFFER,bufferIds[0]);
          //Is 4 right?
          //just ask for the space.
     	 gl.glBufferData(GL.GL_ARRAY_BUFFER,vertices.length*4+texCoords.length*4,null,GL2.GL_STATIC_DRAW);
     	 gl.glBufferSubData(GL.GL_ARRAY_BUFFER,0,vertices.length*4,vertexBuffer);
     	 gl.glBufferSubData(GL.GL_ARRAY_BUFFER,vertices.length*4,texCoords.length*4,texBuffer);
     	 
     	 
     	 
     	 // Enable two vertex arrays: co-ordinates and color.
     	   gl.glEnableClientState(GL2.GL_VERTEX_ARRAY);
     	   gl.glEnableClientState(GL2.GL_TEXTURE_COORD_ARRAY);

     	   // Specify locations for the co-ordinates and color arrays.
     	   gl.glVertexPointer(4, GL.GL_FLOAT, 0, 0); //last num is the offset
     	   gl.glTexCoordPointer(2, GL.GL_FLOAT, 0, vertices.length*4);
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
