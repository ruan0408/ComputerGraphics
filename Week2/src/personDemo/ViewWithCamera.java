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
 * COMMENT: Comment View 
 *
 * @author malcolmr
 */
public class ViewWithCamera implements GLEventListener {

    private Person myPerson;
    private Camera myCamera;
    private boolean myCameraEnabled;
    
    public ViewWithCamera(Person person, Camera camera) {
        myPerson = person;
        myCamera = camera;
    }
    
    public void setEnableCamera(boolean enable) {
        myCameraEnabled = enable;
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

        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();

        if (myCameraEnabled) {
            myCamera.setView(gl);
        }
        
        gl.glColor3d(0.0, 0.0, 0.0);
        myPerson.draw(gl);
        myCamera.draw(gl);
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width,
            int height) {
        
        GL2 gl = drawable.getGL().getGL2();

        myCamera.setAspect(1.0 * width / height);
        
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        
        // coordinate system (left, right, bottom, top)
        GLU glu = new GLU();
        
        if (myCameraEnabled) {
            glu.gluOrtho2D(-1, 1, -1, 1);
        }
        else {            
            glu.gluOrtho2D(-20, 20, -20, 20);
        }

    }

    public static void main(String[] args) {
        // Initialise OpenGL
        GLProfile glp = GLProfile.getDefault();
        GLCapabilities caps = new GLCapabilities(glp);

        // Create a panel to draw on
        GLJPanel panel1 = new GLJPanel(caps);
        GLJPanel panel2 = new GLJPanel(caps);

        final JFrame jframe = new JFrame("Model-View Transform Demo");        
        jframe.setSize(800, 400);
        jframe.setLayout(new GridLayout(1,2));
        jframe.add(panel1);
        jframe.add(panel2);
        jframe.setVisible(true);

        // Catch window closing events and quit             
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // add a GL Event listener to handle rendering
        Person person = new Person(0, 0, 0, 1);
        Camera camera = new Camera();
        
        ViewWithCamera view1 = new ViewWithCamera(person, camera);
        view1.setEnableCamera(true);
        panel1.addGLEventListener(view1);
        panel1.addKeyListener(person);
        panel1.addKeyListener(camera);
        panel1.setFocusable(true);
        
        ViewWithCamera view2 = new ViewWithCamera(person, camera);
        view2.setEnableCamera(false);
        panel2.addGLEventListener(view2);        
        
        // add an Animator
        FPSAnimator animator = new FPSAnimator(60);
        animator.add(panel1);
        animator.add(panel2);
        animator.start();
    }
}
