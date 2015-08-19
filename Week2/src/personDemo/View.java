package personDemo;

import java.awt.GridLayout;

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

/**
 * COMMENT: Displays and allows the user to interactively animate a person.
 *
 * @author malcolmr
 */

public class View implements GLEventListener {

    private Person myPerson;
        
    public View(Person person) {
        myPerson = person;
    }

    @Override
    public void init(GLAutoDrawable drawable) {
        
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        
        gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);
        
        gl.glColor3d(0.0, 0.0, 0.0);

        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();
                
        myPerson.draw(gl);        
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width,
            int height) {
        
        GL2 gl = drawable.getGL().getGL2();

        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();

        // coordinate system (left, right, bottom, top)
        GLU glu = new GLU();
        glu.gluOrtho2D(-20, 20, -20, 20);

    }

    public static void main(String[] args) {
        // Initialise OpenGL
        GLProfile glp = GLProfile.getDefault();
        GLCapabilities caps = new GLCapabilities(glp);

        // Create a panel to draw on
        GLJPanel panel1 = new GLJPanel(caps);

        final JFrame jframe = new JFrame("Model Transform Demo");        
        jframe.setSize(400, 400);
        jframe.setLayout(new GridLayout(1,2));
        jframe.add(panel1);
        jframe.setVisible(true);

        // Catch window closing events and quit             
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // add a GL Event listener to handle rendering
        Person person = new Person(0, 0, 0, 1);
        View view = new View(person);
        panel1.addGLEventListener(view);
        panel1.addKeyListener(person);
        panel1.setFocusable(true);
                
        // add an Animator
        FPSAnimator animator = new FPSAnimator(60);
        animator.add(panel1);
        animator.start();
    }
}
