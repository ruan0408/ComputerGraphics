package week7.vbos;


import java.nio.FloatBuffer;
import java.util.Random;

import javax.media.opengl.DebugGL2;
import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLProfile;
import javax.media.opengl.TraceGL2;
import javax.media.opengl.awt.GLJPanel;
import javax.swing.JFrame;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.util.FPSAnimator;

public class CubeWrong implements GLEventListener{
	
	
	
	private int numVertices = 36; //(6 faces)(2 triangles/face)(3 vertices/triangle)
	private static final int DIMENSION = 4;
	
	private float points[] = new float [numVertices*DIMENSION];
	private float colors[] = new float [numVertices*DIMENSION];
	
	// Vertices of a unit cube centered at origin, sides aligned with axes
	private float vertices[][] = {
			{ -0.5f, -0.5f,  0.5f, 1.0f },
			{ -0.5f,  0.5f,  0.5f, 1.0f },
			{  0.5f,  0.5f,  0.5f, 1.0f },
			{  0.5f, -0.5f,  0.5f, 1.0f },
			{ -0.5f, -0.5f, -0.5f, 1.0f },
			{ -0.5f,  0.5f, -0.5f, 1.0f },
			{  0.5f,  0.5f, -0.5f, 1.0f },
			{  0.5f, -0.5f, -0.5f, 1.0f }
	};

	// RGBA colors
	private float vertex_colors[][] = {
			{ 0.0f, 0.0f, 0.0f, 1.0f },  // black
			{ 1.0f, 0.0f, 0.0f, 1.0f },  // red
			{ 1.0f, 1.0f, 0.0f, 1.0f },  // yellow
			{ 0.0f, 1.0f, 0.0f, 1.0f },  // green
			{ 0.0f, 0.0f, 1.0f, 1.0f },  // blue
			{ 1.0f, 0.0f, 1.0f, 1.0f },  // magenta
			{ 1.0f, 1.0f, 1.0f, 1.0f },  // white
			{ 0.0f, 1.0f, 1.0f, 1.0f }   // cyan
	};
		 
	
	 private FloatBuffer vertexBuffer; 
	 private FloatBuffer colorBuffer; 
	
	 //unique ids for the buffers we are going to use
	 private int buffer[] = new int[1]; 
    
	 private int index = 0;

	 private void setData(int n){
		    colors[index] = vertex_colors[n][0]; 
		    points[index] = vertices[n][0]; 
		    index++;
		    colors[index] = vertex_colors[n][1]; 
		    points[index] = vertices[n][1]; 
		    index++;
		    colors[index] = vertex_colors[n][2]; 
		    points[index] = vertices[n][2]; 
		    index++;
		    colors[index] = vertex_colors[n][3]; 
		    points[index] = vertices[n][3]; 
		    index++;
	 
	 }
	
	 // quad generates two triangles for each face and assigns colors
     //to the vertices	 
	 private void quad( int a, int b, int c, int d ) {
	     setData(a);
	     setData(b);
	     setData(c);
	     setData(a);
	     setData(c);
	     setData(d);
	}
	 
	// generate 12 triangles: 36 vertices and 36 colors
	 private void colorcube() {
	     quad( 1, 0, 3, 2 );
	     quad( 2, 3, 7, 6 );
	     quad( 3, 0, 4, 7 );
	     quad( 6, 5, 1, 2 );
	     quad( 4, 5, 6, 7 );
	     quad( 5, 4, 0, 1 );
	 }

	 
    public static void main(String[] args) {
        // initialisation
        GLProfile glp = GLProfile.getDefault();
        GLCapabilities caps = new GLCapabilities(glp);
        
        // create a panel to draw on
        GLJPanel panel = new GLJPanel(caps);

        // put it in a JFrame
        final JFrame jframe = new JFrame("Cube");
        jframe.setSize(800, 800);
        jframe.add(panel);
        jframe.setVisible(true);

        // Catch window closing events and quit             
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        CubeWrong s = new CubeWrong();
        
        // add a GL Event listener to handle rendering
        panel.addGLEventListener(s);
     
        panel.setFocusable(true);   
        
       
    }   
    
    @Override
    public void display(GLAutoDrawable drawable) {
    	GL2 gl = drawable.getGL().getGL2();

    	gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
    	
    	gl.glDrawArrays(GL2.GL_TRIANGLES,0,numVertices);      
    
    }

    
   
    @Override
    public void dispose(GLAutoDrawable drawable) {
    }

    
    
    @Override
    public void init(GLAutoDrawable drawable) {
    	
    	GL2 gl = drawable.getGL().getGL2();

    	colorcube();
    	vertexBuffer = Buffers.newDirectFloatBuffer(points);
    	colorBuffer = Buffers.newDirectFloatBuffer(colors);

    	gl.glEnable(GL2.GL_DEPTH_TEST);

    	gl.glGenBuffers(1, buffer,0); // Generate buffer ids.

    	// Bind vertex buffer and reserve space.
    	gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, buffer[0]);

    	gl.glBufferData(GL2.GL_ARRAY_BUFFER, points.length*Buffers.SIZEOF_FLOAT + colors.length*Buffers.SIZEOF_FLOAT, null, GL2.GL_STATIC_DRAW);

    	gl.glBufferSubData( GL2.GL_ARRAY_BUFFER, 0, points.length*Buffers.SIZEOF_FLOAT , vertexBuffer );
    	gl.glBufferSubData( GL2.GL_ARRAY_BUFFER, points.length*Buffers.SIZEOF_FLOAT, colors.length*Buffers.SIZEOF_FLOAT, colorBuffer );

    	gl.glEnableClientState(GL2.GL_VERTEX_ARRAY);

    	gl.glVertexPointer(3, GL2.GL_FLOAT, 0, 0);

    	gl.glEnableClientState(GL2.GL_COLOR_ARRAY);
    	gl.glColorPointer(3, GL2.GL_FLOAT, 0, points.length*Buffers.SIZEOF_FLOAT);
    	   
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int w, int h) {
    	
    }
}
