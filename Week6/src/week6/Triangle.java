package week6;



import java.io.File;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GL2ES2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLJPanel;
import javax.swing.JFrame;

import com.jogamp.opengl.util.FPSAnimator;

import week8Old.Shader.CompilationException;

/**
 * COMMENT: Comment Triangle 
 *
 * A very simple program to show how to use shaders.
 */
public class Triangle implements GLEventListener {
	 private long myTime;
	
	
	
	private static final String VERTEX_SHADER = "src/week6/BasicVertex.glsl";
    private static final String FRAGMENT_SHADER = "src/week6/BasicFragment.glsl";
    
    // private static final String VERTEX_SHADER2 = "src/week6/SimpleVertex.glsl";
  	//private static final String FRAGMENT_SHADER2 = "src/week6/SimpleFragment.glsl";
  	
  	private static final String VERTEX_SHADER2 = "src/week6/TestVertex.glsl";
   	private static final String FRAGMENT_SHADER2 = "src/week6/TestFragment.glsl";
       
	private int shaderprogram;
	
	private int shaderprogram2;
	
    @Override
    public void init(GLAutoDrawable drawable) {
    	 GL2 gl = drawable.getGL().getGL2();
    	 try {
    		 shaderprogram = Shader.initShaders(gl,VERTEX_SHADER,FRAGMENT_SHADER);
    		 shaderprogram2 = Shader.initShaders(gl,VERTEX_SHADER2,FRAGMENT_SHADER2);
    		 
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
        
        //If we turn on flat then we do not get interpolated color values.
        //gl.glShadeModel(GL2.GL_FLAT); 
       
        gl.glUseProgram(shaderprogram);
      
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);
       
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();
           
    
        gl.glBegin(GL2.GL_TRIANGLES);
        {
        	       	        	
        	gl.glColor4f(1,0,0,1);
            gl.glVertex2d(-1, -1);
                
            gl.glColor4f(0,1,0,1);          
            gl.glVertex2d(0, -1);
            
            gl.glColor4f(0,0,1,1);
            gl.glVertex2d(0, 1);
            
        }
        gl.glEnd();
        
        gl.glUseProgram(shaderprogram2);
        gl.glBegin(GL2.GL_TRIANGLES);
        {
        	       	        	
        	gl.glColor4f(1,0,0,1);
            gl.glVertex2d(0, -1);
                
            gl.glColor4f(0,1,0,1);          
            gl.glVertex2d(1, -1);
            
            gl.glColor4f(0,0,1,1);
            gl.glVertex2d(1, 1);
            
        }
        gl.glEnd();
        
     
       /*
        gl.glBegin(GL2.GL_TRIANGLES);
        {
        	gl.glColor4f(0,1,0,1);          
            gl.glVertex2d(1, -1);
        	
        	gl.glColor4f(1,0,0,1);
            gl.glVertex2d(0, -1);
                                               
            gl.glColor4f(0,0,1,1);
            gl.glVertex2d(1, 1);
            
        }
        gl.glEnd();
          */   
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width,
            int height) {
        // TODO Auto-generated method stub
        
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
        Triangle triangle = new Triangle();
        panel.addGLEventListener(triangle);
        
        // NEW: add an animator to create display events at 60 FPS
        FPSAnimator animator = new FPSAnimator(60);
        animator.add(panel);
        animator.start();

    }
    
    
}
