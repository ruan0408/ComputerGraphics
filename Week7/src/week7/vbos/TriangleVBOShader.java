package week7.vbos;




import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;

import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLJPanel;
import javax.swing.JFrame;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.util.FPSAnimator;

import week7.vbos.Shader;


/**
 * COMMENT: Comment Triangle 
 *
 * 
 */

public class TriangleVBOShader implements GLEventListener {
	
	
	private float positions[] =  {0,1,-1, 
			-1,-1,-1,
			1,-1,-1, 
			0, 2,-4,
			-2,-2,-4, 
			2,-2,-4};

	//There should be a matching entry in this array for each entry in
	//the positions array
	private float colors[] =     {1,0,0, 
			0,1,0,
			1,1,1,
			0,0,0,
			0,0,1, 
			1,1,0}; 

	//Best to use smallest data type possible for indexes 
	//We could even use byte here...
	private short indexes[] = {0,1,5,3,4,2};

	//These are not vertex buffer objects, they are just java containers
	private FloatBuffer  posData= Buffers.newDirectFloatBuffer(positions);
	private FloatBuffer colorData = Buffers.newDirectFloatBuffer(colors);
	private ShortBuffer indexData = Buffers.newDirectShortBuffer(indexes);

	//We will be using 2 vertex buffer objects
	private int bufferIds[] = new int[2];
	
	
	 private static final String VERTEX_SHADER = "src/week7/vbos/AttributeVertex.glsl";
	 private static final String FRAGMENT_SHADER = "src/week7/vbos/AttributeFragment.glsl";
	 
	 private int shaderprogram;
	 
	 
    @Override
    public void init(GLAutoDrawable drawable) {
    	 GL2 gl = drawable.getGL().getGL2();
    	 gl.glEnable(GL2.GL_DEPTH_TEST); // Enable depth testing.
    	 
    	 
    	//Generate 2 VBO buffer and get their IDs
         gl.glGenBuffers(2,bufferIds,0);
        
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
         
         
         //Now for the element array
         //Element arrays hold indexes to an array buffer
         gl.glBindBuffer(GL2.GL_ELEMENT_ARRAY_BUFFER, bufferIds[1]);

         //We can load it all at once this time since there are not
         //two separate parts like there was with color and position.
         gl.glBufferData(GL2.GL_ELEMENT_ARRAY_BUFFER,      
     	        indexes.length *Short.BYTES,
     	        indexData, GL2.GL_STATIC_DRAW);
    	    	 
    	 try {
    		 shaderprogram = Shader.initShaders(gl,VERTEX_SHADER,FRAGMENT_SHADER);
   		    		 
         }
         catch (Exception e) {
             e.printStackTrace();
             System.exit(1);
         }

    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void display(GLAutoDrawable drawable) {

        GL2 gl = drawable.getGL().getGL2();
        
     
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
       
       
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();
        
        //Use the shader
        gl.glUseProgram(shaderprogram);
        gl.glBindBuffer(GL.GL_ARRAY_BUFFER,bufferIds[0]);
           
        int vertexColLoc = gl.glGetAttribLocation(shaderprogram,"vertexCol");
        int vertexPosLoc = gl.glGetAttribLocation(shaderprogram,"vertexPos");
               
   	    // Specify locations for the co-ordinates and color arrays.
        gl.glEnableVertexAttribArray(vertexPosLoc);
        gl.glEnableVertexAttribArray(vertexColLoc);
   	   	gl.glVertexAttribPointer(vertexPosLoc,3, GL.GL_FLOAT, false,0, 0); //last num is the offset
   	   	gl.glVertexAttribPointer(vertexColLoc,3, GL.GL_FLOAT, false,0, positions.length*Float.BYTES);
       
    	
   	    gl.glBindBuffer(GL2.GL_ELEMENT_ARRAY_BUFFER, bufferIds[1]);
   	 
   	
    	gl.glDrawElements(GL2.GL_TRIANGLES, 6, GL2.GL_UNSIGNED_SHORT,0);    
    	
    	
    	gl.glUseProgram(0);
    	   
     	//Un-bind the buffer. 
     	//This is not needed in this simple example but good practice
         gl.glBindBuffer(GL.GL_ARRAY_BUFFER,0);
         gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER,0);
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
        TriangleVBOShader triangle = new TriangleVBOShader();
        panel.addGLEventListener(triangle);
        
        // NEW: add an animator to create display events at 60 FPS
        FPSAnimator animator = new FPSAnimator(60);
        animator.add(panel);
        animator.start();

    }
    
    
}
