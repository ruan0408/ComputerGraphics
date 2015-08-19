import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

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
 * The triangle example extends to maintain the aspect ratio when the window is reshaped
 *
 * @author malcolmr
 */
public class Triangle4 implements GLEventListener, KeyListener {

    private double dTheta = 0;
    private double theta = 0;
    private double s = 0;
    private double c = 0;

    public static void main(String[] args) {
        // initialisation
        GLProfile glp = GLProfile.getDefault();
        GLCapabilities caps = new GLCapabilities(glp);
        
        // create a panel to draw on
        GLJPanel panel = new GLJPanel(caps);

        // put it in a JFrame
        final JFrame jframe = new JFrame("Triangle 4");
        jframe.setSize(300, 300);
        jframe.add(panel);
        jframe.setVisible(true);

       
        
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Triangle4 triangle3 = new Triangle4();
        // add a GL Event listener to handle rendering
        panel.addGLEventListener(triangle3);
        
        // add a key listener to respond to keypresses
        panel.addKeyListener(triangle3);
        // the panel needs to be focusable to get key events
        panel.setFocusable(true);   
        
        // add an animator to create display events at 60 FPS
        FPSAnimator animator = new FPSAnimator(60);
        animator.add(panel);
        animator.start();
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        // Split into update and render
        update();
        render(drawable);
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
    }

    @Override
    public void init(GLAutoDrawable drawable) {
    }

    /**
     * NEW: adjust the shape of the world window when the viewport is changed 
     * 
     * @see javax.media.opengl.GLEventListener#reshape(javax.media.opengl.GLAutoDrawable, int, int, int, int)
     */
    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int w, int h) {
        GL2 gl = drawable.getGL().getGL2();
        
        // calculate the aspect ratio of window
        double aspect = 1.0 * w / h;
        
        // in OpenGL terms, we are changing the 'projection'
        // this will be explained later
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();

        // Use the GLU library to compute the new projection
        GLU glu = new GLU();

        if(aspect >= 1){
       
            glu.gluOrtho2D(-aspect, aspect, -1.0, 1.0);  // left, right, top, bottom
        } else {
        	
        	glu.gluOrtho2D(-1, 1, -1.0/aspect, 1.0/aspect);  // left, right, top, bottom
        }
    }

    private void update() {
        // update the model
        theta += dTheta;
        s = Math.sin(theta);
        c = Math.cos(theta);
    }

    private void render(GLAutoDrawable drawable) {
        // re-render the scene
        GL2 gl = drawable.getGL().getGL2();

        gl.glClear(GL.GL_COLOR_BUFFER_BIT);

        gl.glPointSize(4.0f);

        // draw a triangle filling the window
        gl.glBegin(GL.GL_TRIANGLES);
        {	gl.glColor3f(1, 0, 0);
        	gl.glVertex2d(-c, -c);
        	gl.glColor3f(0, 1, 0);
        	gl.glVertex2d(0, c);
        	gl.glColor3f(0, 0, 1);
        	gl.glVertex2d(s, -s);
        }
        gl.glEnd();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {

        case KeyEvent.VK_LEFT:
            dTheta = -0.1; 
            break;

        case KeyEvent.VK_RIGHT:
            dTheta = 0.1; 
            break;
                
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {

        case KeyEvent.VK_LEFT:
        case KeyEvent.VK_RIGHT:
            dTheta = 0; 
            break;
                
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }
}
