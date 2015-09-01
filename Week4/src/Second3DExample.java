import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLJPanel;
import javax.media.opengl.glu.GLU;
import javax.swing.JFrame;

import com.jogamp.opengl.util.FPSAnimator;
import com.jogamp.opengl.util.gl2.GLUT;


// A simple 3D example used in lectures to show
// some of the important 3D settings and issues

public class Second3DExample implements GLEventListener {

    public static void main(String[] args) {
        // initialisation
        GLProfile glp = GLProfile.getDefault();
        GLCapabilities caps = new GLCapabilities(glp);
        
        // create a panel to draw on
        GLJPanel panel = new GLJPanel(caps);

        // put it in a JFrame
        final JFrame jframe = new JFrame("My Second 3D Example");
        jframe.setSize(300, 300);
        jframe.add(panel);
        jframe.setVisible(true);
       
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        panel.addGLEventListener(new Second3DExample());
    }

    //BACKFACE - cull - color
    //DEPTH
    //LIGHTS!
    //PERSPECTIVE
    @Override
    public void display(GLAutoDrawable drawable) {
    	GL2 gl = drawable.getGL().getGL2();

    	gl.glMatrixMode(GL2.GL_MODELVIEW);
    	gl.glLoadIdentity();

    	//Forgetting to clear the depth buffer can cause problems 
    	//such as empty black screens.
    	gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
        gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL2.GL_LINE);
    	gl.glColor3f(0,1,0);
    	GLUT glut = new GLUT();
    	gl.glTranslated(0,0,-3);
    	glut.glutSolidTeapot(1);
    	gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL2.GL_FILL);
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
    }

    @Override
    public void init(GLAutoDrawable drawable) {
    	GL2 gl = drawable.getGL().getGL2();
    	
    	//If you do not add this line
    	//opengl will draw things in the order you
    	//draw them in your program
    	gl.glEnable(GL2.GL_DEPTH_TEST);
    	
    	//By enabling lighting, color is worked out differently.
        gl.glEnable(GL2.GL_LIGHTING);
    	
    	//When you enable lighting you must still actually
    	//turn on a light such as this default light.
    	gl.glEnable(GL2.GL_LIGHT0);
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int w, int h) {
    	 GL2 gl = drawable.getGL().getGL2();
         gl.glMatrixMode(GL2.GL_PROJECTION);
         gl.glLoadIdentity();  
         
         //You can use an orthographic camera
        GLU glu = new GLU();
        glu.gluPerspective(60,1,1,20);
        //gl.glOrtho(-2, 2, -2, 2, 0, 20);
    }
}
