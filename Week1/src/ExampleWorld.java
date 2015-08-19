import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.media.opengl.DebugGL2;
import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLProfile;
import javax.media.opengl.TraceGL2;
import javax.media.opengl.awt.GLCanvas;
import javax.media.opengl.awt.GLJPanel;
import javax.media.opengl.glu.GLU;
import javax.swing.JFrame;

import com.jogamp.opengl.util.FPSAnimator;
import com.jogamp.opengl.util.awt.TextRenderer;
import com.jogamp.opengl.util.gl2.GLUT;



// Simple example showing how to draw simple shapes and wrap GL2 objects
// in debuggers and tracers
// Also show how the aspect ratio of the screen objects can be maintained
// and how this gradually can show more of the world. For example by dragging the 
// screen and making it bigger horizontally, the black oint that starts off screen
// becomes visible.

public class ExampleWorld implements GLEventListener {

   

    public static void main(String[] args) {
    	GLProfile glprofile = GLProfile.getDefault();
        GLCapabilities glcapabilities = new GLCapabilities( glprofile );
        GLJPanel glcanvas = new GLJPanel( glcapabilities );
        glcanvas.addGLEventListener(new ExampleWorld());
      
        JFrame jframe = new JFrame( "JOGL Demo" ); 
        // Catch window closing events and quit
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      
        jframe.add(glcanvas);
        jframe.setSize( 480, 480 );
        jframe.setVisible( true );
    	
    }
  

    @Override
    public void init(GLAutoDrawable drawable) {
    
    	//uncomment this line to get debugging and tracing 
    	drawable.setGL(new DebugGL2(new TraceGL2(drawable.getGL().getGL2(), System.err)));

       //OR uncomment this line to just get debugging
       //drawable.setGL(new DebugGL2(drawable.getGL().getGL2()));
        GL2 gl = drawable.getGL().getGL2();

        gl.glClearColor(1.0f, 1.0f, 1.0f, 1f); // White Background
    }
    
    @Override
    public void display(GLAutoDrawable drawable) {
    	//Always get a fresh one. Don't store them as fields
    	//as they may get destroyed and recreated.
//        GL2 gl = drawable.getGL().getGL2();
//
//        //Clear the color buffer - the graphics card memory
//        gl.glClear(GL2.GL_COLOR_BUFFER_BIT);
//        //gl.glClear(GL2.GL_COLOR); //A DELIBERATE ERROR TO SHOW USE OF DEBUGGL2
//                     
//    
//        gl.glPointSize(15); //setting point size must be done outside BEGIN and END
//                           //not all point sizes available on all systems.     
//      
//            
//        gl.glBegin(GL.GL_POINTS);
//        {   
//            //Red
//            gl.glColor3f(1, 0, 0);
//            gl.glVertex2d(0, 0);
// 
//            //Green
//            gl.glColor3f(0, 1, 0);
//            gl.glVertex2d(0.8, 0.8);
//           
//            //Blue
//            gl.glColor3f(0, 0, 1);
//            gl.glVertex2d(-0.8, 0);
//            
//            //Black - this point is off screen with
//            //our current default world window settings
//            gl.glColor3f(0, 0, 0);
//            gl.glVertex2d(2, 0);
//        }
//        gl.glEnd();
//       
//        gl.glBegin(GL2.GL_TRIANGLE_STRIP);
//        {   
//        	gl.glColor3f(0F, 1F, 0F);
//
//        	gl.glVertex2d(-0.8, -0.2);
//
//        	gl.glColor3f(0.9F, 0.5F, 0.9F);
//
//        	gl.glVertex2d(-0.8, -0.8);
//
//        	gl.glColor3f(1F, 1F, 0F);
//        	gl.glVertex2d(-0.5, -0.2);
//
//        	gl.glColor3f(0F, 0F, 0F);
//
//        	gl.glVertex2d(-0.5,-0.8);  
//                              
//        }
//        gl.glEnd();
      
    }

	@Override
	public void dispose(GLAutoDrawable drawable) {
		
	}

	//in pixels.
	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		System.out.println(x + " " + y + " " + width + " "  + height);
	   
	    
		GL2 gl = drawable.getGL().getGL2();
	    // calculate the aspect ratio of window
        double aspect = 1.0 * width / height;
        
        // in OpenGL terms, we are changing the 'projection'
        // this will be explained later
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();

        // Use the GLU library to compute the new projection
        GLU glu = new GLU();
      
        //We are wider than high!
        if(aspect >= 1){
        	 System.out.println("Showing world " + (-aspect) + " to " + (aspect) + " -1 to 1"  );
             glu.gluOrtho2D(-aspect, aspect, -1.0, 1.0);  // left, right, top, bottom
        } else {
        	System.out.println("Showing world from -1 to 1 " + (-1.0/aspect) + " to " + (1.0/aspect)  );
        	glu.gluOrtho2D(-1, 1, -1.0/aspect, 1.0/aspect);  // left, right, top, bottom
        }
	   
        
	    
	}
	
	    
}
