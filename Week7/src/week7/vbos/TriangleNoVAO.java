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
import com.jogamp.opengl.util.gl2.GLUT;

import week6.Shader;
import week8Old.Shader.CompilationException;

/**
 * COMMENT: Comment Triangle  and an annulus
 *
 * 
 */
public class TriangleNoVAO implements GLEventListener {
	


	 // Vertex co-ordinate vectors.
	 private float positionsAnnulus[] =  
		 {	
			 3.0f, 3.0f, 0.0f,
			 1.0f, 1.0f, 0.0f, 
			 7.0f, 3.0f, 0.0f,
			 9.0f, 1.0f, 0.0f,
			 7.0f, 7.0f, 0.0f,
			 9.0f, 9.0f, 0.0f,
			 3.0f, 7.0f, 0.0f,
			 1.0f, 9.0f, 0.0f
		 };

	 // Vertex color vectors.
	 private float colorsAnnulus[] =  
		 {
			 0.0f, 0.0f, 0.0f,
			 1.0f, 0.0f, 0.0f,
			 0.0f, 1.0f, 0.0f,
			 0.0f, 0.0f, 1.0f,
			 1.0f, 1.0f, 0.0f,
			 1.0f, 0.0f, 1.0f,
			 0.0f, 1.0f, 1.0f,
			 1.0f, 0.0f, 0.0f
		 };

	 // Triangle strip vertex indices in order.
	 static short indicesAnnulus[] = {0, 1, 2, 3, 4, 5, 6, 7, 0, 1};



	 float positions[] = {0,1,-1, -1,-1,-1,
			 1,-1,-1, 0, 2,-4,
			 -2,-2,-4, 2,-2,-4};
	 float colors[] = {1,0,0, 0,1,0,
			 1,1,1, 0,0,0,
			 0,0,1, 1,1,0}; 

	 short indexes[] = {0,1,5,3,4,2};
	 
	//This is not a VBO, just a java container.
	 private FloatBuffer  posData= Buffers.newDirectFloatBuffer(positions);
	 private FloatBuffer colorData = Buffers.newDirectFloatBuffer(colors);
	 private ShortBuffer indexData = Buffers.newDirectShortBuffer(indexes);
	 
	 
	 private FloatBuffer  posDataAnnulus= Buffers.newDirectFloatBuffer(positionsAnnulus);
	 private FloatBuffer colorDataAnnulus = Buffers.newDirectFloatBuffer(colorsAnnulus);
	 private ShortBuffer indexDataAnnulus = Buffers.newDirectShortBuffer(indicesAnnulus);
	
	
	 private int bufferIds[] = new int[4];
	
    @Override
    public void init(GLAutoDrawable drawable) {
    	 GL2 gl = drawable.getGL().getGL2();
    	 gl.glEnable(GL2.GL_DEPTH_TEST); // Enable depth testing.
    	 
         gl.glGenBuffers(4,bufferIds,0);
       
         gl.glBindBuffer(GL.GL_ARRAY_BUFFER,bufferIds[0]);
      
         gl.glBufferData(GL2.GL_ARRAY_BUFFER,      
        	        positions.length *Float.BYTES +  
        	        colors.length*Float.BYTES,
        	        null, GL2.GL_STATIC_DRAW);


         gl.glBufferSubData(GL2.GL_ARRAY_BUFFER, 0,
        		 positions.length*Float.BYTES,posData);

         gl.glBufferSubData(GL2.GL_ARRAY_BUFFER,
        		 positions.length*Float.BYTES, 
        		 colors.length*Float.BYTES,colorData);

         gl.glBindBuffer(GL2.GL_ELEMENT_ARRAY_BUFFER, bufferIds[1]);

         gl.glBufferData(GL2.GL_ELEMENT_ARRAY_BUFFER,      
     	        indexes.length *Short.BYTES,
     	        indexData, GL2.GL_STATIC_DRAW);
         
         
         /* Annulus data */
         gl.glBindBuffer(GL.GL_ARRAY_BUFFER,bufferIds[2]);
       
         gl.glBufferData(GL2.GL_ARRAY_BUFFER,      
        	        positionsAnnulus.length *Float.BYTES +  
        	        colorsAnnulus.length*Float.BYTES,
        	        null, GL2.GL_STATIC_DRAW);


         gl.glBufferSubData(GL2.GL_ARRAY_BUFFER, 0,
        		 positionsAnnulus.length*Float.BYTES,posDataAnnulus);

         gl.glBufferSubData(GL2.GL_ARRAY_BUFFER,
        		 positionsAnnulus.length*Float.BYTES, 
        		 colorsAnnulus.length*Float.BYTES,colorDataAnnulus);

         gl.glBindBuffer(GL2.GL_ELEMENT_ARRAY_BUFFER, bufferIds[3]);

         gl.glBufferData(GL2.GL_ELEMENT_ARRAY_BUFFER,      
     	        indicesAnnulus.length *Short.BYTES,
     	        indexDataAnnulus, GL2.GL_STATIC_DRAW);
    	  

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


    	gl.glBindBuffer(GL.GL_ARRAY_BUFFER,bufferIds[0]);

    	// Enable two vertex arrays: co-ordinates and color.
    	gl.glEnableClientState(GL2.GL_VERTEX_ARRAY);
    	gl.glEnableClientState(GL2.GL_COLOR_ARRAY);

    	// Specify locations for the co-ordinates and color arrays.
    	gl.glVertexPointer(3, GL.GL_FLOAT, 0, 0); //last num is the offset
    	gl.glColorPointer(3, GL.GL_FLOAT, 0, positions.length*Float.BYTES);

    	gl.glBindBuffer(GL2.GL_ELEMENT_ARRAY_BUFFER, bufferIds[1]);


    	gl.glDrawElements(GL2.GL_TRIANGLES, 6, GL2.GL_UNSIGNED_SHORT,0); 


    	gl.glBindBuffer(GL.GL_ARRAY_BUFFER,bufferIds[2]);

    	// Enable two vertex arrays: co-ordinates and color.
    	gl.glEnableClientState(GL2.GL_VERTEX_ARRAY);
    	gl.glEnableClientState(GL2.GL_COLOR_ARRAY);

    	// Specify locations for the co-ordinates and color arrays.
    	gl.glVertexPointer(3, GL.GL_FLOAT, 0, 0); //last num is the offset
    	gl.glColorPointer(3, GL.GL_FLOAT, 0, positionsAnnulus.length*Float.BYTES);

    	gl.glBindBuffer(GL2.GL_ELEMENT_ARRAY_BUFFER, bufferIds[3]);
    	gl.glDrawElements(GL2.GL_TRIANGLE_STRIP, 10, GL2.GL_UNSIGNED_SHORT, 0); 

    	gl.glTranslated(-10, 0, 0);
    	gl.glDrawElements(GL2.GL_TRIANGLE_STRIP, 10, GL2.GL_UNSIGNED_SHORT, 0); 
      	      	
  
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width,
            int height) {
    	GL2 gl = drawable.getGL().getGL2();
    	gl.glMatrixMode(GL2.GL_PROJECTION);
    	gl.glLoadIdentity();

    	//Sometimes good to use glOrtho for developing and debugging   	
    	gl.glOrtho(-10, 10, -10, 10, 0, 20);
        
    }

    public static void main(String[] args) {
        
        // Initialise OpenGL
        GLProfile glp = GLProfile.getDefault();
        GLCapabilities caps = new GLCapabilities(glp);

        // Create a panel to draw on
        GLJPanel panel = new GLJPanel(caps);

        final JFrame jframe = new JFrame("Triangle VAO");        
        jframe.setSize(300, 300);
        jframe.add(panel);
        jframe.setVisible(true);

        // Catch window closing events and quit             
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // add a GL Event listener to handle rendering
        TriangleNoVAO triangle = new TriangleNoVAO();
        panel.addGLEventListener(triangle);
        
        // NEW: add an animator to create display events at 60 FPS
        FPSAnimator animator = new FPSAnimator(60);
        animator.add(panel);
        animator.start();

    }
    
    
}
