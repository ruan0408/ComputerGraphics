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
import javax.media.opengl.glu.GLU;
import javax.swing.JFrame;

import com.jogamp.opengl.util.FPSAnimator;

import week8Old.Shader.CompilationException;

/**
 * COMMENT: Comment Triangle 
 *
 * A very simple program to show how to use shaders.
 */
public class Wave implements GLEventListener {
	private long myTime;
	private long startTime;
	
	private int N = 64;
	
	private static final String VERTEX_SHADER = "src/week6/SimpleVertex.glsl";
	//private static final String VERTEX_SHADER = "src/week6/AnimatedVertex.glsl";
	private static final String FRAGMENT_SHADER = "src/week6/SimpleFragment.glsl";
       
	private int shaderprogram;
	
	
	private float data[][] = new float[N][N]; /* array of data heights */

	
	
    @Override
    public void init(GLAutoDrawable drawable) {
    	 GL2 gl = drawable.getGL().getGL2();
    	 try {
             shaderprogram = Shader.initShaders(gl,VERTEX_SHADER,FRAGMENT_SHADER);
         }
         catch (Exception e) {
             e.printStackTrace();
             System.exit(1);
         }
    	 startTime = System.currentTimeMillis();
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
        
        GLU glu = new GLU();
        glu.gluLookAt(2.0, 2.0, 2.0,0.5, 0.0, 0.5, 0.0, 1.0, 0.0);
           
        long time = System.currentTimeMillis();
        double dt = (time - myTime) / 1000.0;
        myTime = time;
        float elapsedTime = time - startTime;
        
        gl.glUniform1f(gl.glGetUniformLocation(shaderprogram, "time"),elapsedTime );
        
        gl.glColor3f(0.8f,0.4f,0.8f);
        mesh(gl);
             
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width,
            int height) {
    	
   	 GL2 gl = drawable.getGL().getGL2();
     gl.glMatrixMode(GL2.GL_PROJECTION);
     gl.glLoadIdentity();
     
    
    
     gl.glOrtho(-0.75,0.75,-0.75,0.75,-5.5,5.5);
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
        Wave triangle = new Wave();
        panel.addGLEventListener(triangle);
        
        // NEW: add an animator to create display events at 60 FPS
        FPSAnimator animator = new FPSAnimator(60);
        animator.add(panel);
        animator.start();

    }
    
    
    
    int Index = 0;
    void mesh(GL2 gl) {
    	gl.glBegin(GL2.GL_TRIANGLES);
    	
    	 for(int i=0;i<N;i++) for(int j=0;j<N;j++) data[i][j]=0.0f;
        int i,j;
        for(i=0; i<N; i++) for(j=0; j<N;j++)
        {
           gl.glVertex4d((float)i/N, data[i][j], (float)j/N, 1.0);
           gl.glVertex4d((float)i/N, data[i][j], (float)(j+1)/N, 1.0);
           gl.glVertex4d((float)(i+1)/N, data[i][j], (float)(j+1)/N, 1.0); 
           gl.glVertex4d((float)(i+1)/N, data[i][j], (float)(j+1)/N, 1.0); 
           gl.glVertex4d((float)(i+1)/N, data[i][j], (float)j/N, 1.0); 
           gl.glVertex4d((float)i/N, data[i][j], (float)j/N, 1.0); 
        }
        gl.glEnd();
    }
}
