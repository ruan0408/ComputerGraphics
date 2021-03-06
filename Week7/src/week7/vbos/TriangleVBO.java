package week7.vbos;



import java.io.File;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GL2ES2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLJPanel;
import javax.swing.JFrame;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.util.FPSAnimator;

import week6.Shader;
import week8Old.Shader.CompilationException;

/**
 * COMMENT: Comment Triangle 
 *
 * ////////////////////////////////////////////////////////////////

 */
public class TriangleVBO implements GLEventListener {
	

	float positions[] =  {0,1,-1, 
			-1,-1,-1,
			1,-1,-1, 
			0, 2,-4,
			-2,-2,-4, 
			2,-2,-4};

	//There should be a matching entry in this array for each entry in
	//the positions array
	float colors[] =     {1,0,0, 
			0,1,0,
			1,1,1,
			0,0,0,
			0,0,1, 
			1,1,0}; 

	

	//These are not vertex buffer objects, they are just java containers
	private FloatBuffer  posData= Buffers.newDirectFloatBuffer(positions);
	private FloatBuffer colorData = Buffers.newDirectFloatBuffer(colors);
	

	//We will be using 1 vertex buffer object
	private int bufferIds[] = new int[1];
	
    @Override
    public void init(GLAutoDrawable drawable) {
    	 GL2 gl = drawable.getGL().getGL2();
    	 gl.glEnable(GL2.GL_DEPTH_TEST); // Enable depth testing.
    	    	 
    	 //Generate 1 VBO buffer and get its ID
         gl.glGenBuffers(1,bufferIds,0);
        
    	 //This buffer is now the current array buffer
         //array buffers hold vertex attribute data
         gl.glBindBuffer(GL.GL_ARRAY_BUFFER,bufferIds[0]);
      
         //This is just setting aside enough empty space
         //for all our data
         gl.glBufferData(GL2.GL_ARRAY_BUFFER,    //Type of buffer  
        	        positions.length * Float.BYTES +  colors.length* Float.BYTES, //size needed
        	        null,    //We are not actually loading data here yet
        	        GL2.GL_STATIC_DRAW); //We expect once we load this data we will not modify it


         //Actually load the positions data
         gl.glBufferSubData(GL2.GL_ARRAY_BUFFER, 0, //From byte offset 0
        		 positions.length*Float.BYTES,posData);

         //Actually load the color data
         gl.glBufferSubData(GL2.GL_ARRAY_BUFFER,
        		 positions.length*Float.BYTES,  //Load after the position data
        		 colors.length*Float.BYTES,colorData);
         
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
    	GL2 gl = drawable.getGL().getGL2();
        gl.glDeleteBuffers(1,bufferIds,0);
    }

    @Override
    public void display(GLAutoDrawable drawable) {

    	GL2 gl = drawable.getGL().getGL2();


    	gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);

    	gl.glMatrixMode(GL2.GL_MODELVIEW);
    	gl.glLoadIdentity();

    	//Bind the buffer we want to use
    	gl.glBindBuffer(GL.GL_ARRAY_BUFFER,bufferIds[0]);

    	// Enable two vertex arrays: coordinates and color.
    	//To tell the graphics pipeline that we want it to use our vertex position and color data
    	gl.glEnableClientState(GL2.GL_VERTEX_ARRAY);
    	gl.glEnableClientState(GL2.GL_COLOR_ARRAY);

   	   // This tells OpenGL the locations for the co-ordinates and color arrays.
   	   gl.glVertexPointer(3, //3 coordinates per vertex 
   			              GL.GL_FLOAT, //each co-ordinate is a float 
   			              0, //There are no gaps in data between co-ordinates 
   			              0); //Co-ordinates are at the start of the current array buffer
   	   gl.glColorPointer(3, GL.GL_FLOAT, 0, 
   			             positions.length*Float.BYTES); //colors are found after the position
   	   												    //co-ordinates in the current array buffer
    	
   	   //Draw triangles, using 6 vertices, starting at vertex index 0 
   	   gl.glDrawArrays(GL2.GL_TRIANGLES,0,6);      
        	
   	   //Disable these. Not needed in this example, but good practice.
   	   gl.glDisableClientState(GL2.GL_VERTEX_ARRAY);
   	   gl.glDisableClientState(GL2.GL_COLOR_ARRAY);
   	   
    	//Unbind the buffer. 
    	//This is not needed in this simple example but good practice
        gl.glBindBuffer(GL.GL_ARRAY_BUFFER,0);
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width,
            int height) {
    	GL2 gl = drawable.getGL().getGL2();
    	gl.glMatrixMode(GL2.GL_PROJECTION);
    	gl.glLoadIdentity();

    	//Sometimes good to use glOrtho for developing and debugging   	
    	gl.glOrtho(-4, 4, -4, 4, 0, 20);
        
    }

    public static void main(String[] args) {
        
        // Initialise OpenGL
        GLProfile glp = GLProfile.getDefault();
        GLCapabilities caps = new GLCapabilities(glp);

        // Create a panel to draw on
        GLJPanel panel = new GLJPanel(caps);

        final JFrame jframe = new JFrame("Triangle");        
        jframe.setSize(300, 300);
        jframe.add(panel);
        jframe.setVisible(true);

        // Catch window closing events and quit             
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // add a GL Event listener to handle rendering
        TriangleVBO triangle = new TriangleVBO();
        panel.addGLEventListener(triangle);
        
        // NEW: add an animator to create display events at 60 FPS
        FPSAnimator animator = new FPSAnimator(60);
        animator.add(panel);
        animator.start();

    }
    
    
}
