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
import javax.swing.JFrame;

import com.jogamp.opengl.util.FPSAnimator;

/**
 * The Triangle example extended to add interaction.
 * Press the left and right arrow keys to control the animation.
 *
 * @author malcolmr
 */
public class Triangle3 implements GLEventListener, KeyListener {

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
        final JFrame jframe = new JFrame("Triangle 3");
        jframe.setSize(300, 300);
        jframe.add(panel);
        jframe.setVisible(true);

        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
       

        Triangle3 triangle3 = new Triangle3();
        // add a GL Event listener to handle rendering
        panel.addGLEventListener(triangle3);
        
        // NEW: add a key listener to respond to keypresses
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

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int w, int h) {
    }

    /**
     * update the model
     *
     */
    private void update() {
        theta += dTheta;
        s = Math.sin(theta);
        c = Math.cos(theta);
    }

    /**
     * re-render the scene
     * 
     * @param drawable
     */
    private void render(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();

        gl.glClear(GL.GL_COLOR_BUFFER_BIT);

            
        // draw a triangle filling the window
        gl.glBegin(GL.GL_TRIANGLES);
        gl.glColor3f(1, 0, 0);
        gl.glVertex2d(-c, -c);

        gl.glColor3f(0, 1, 0);
        gl.glVertex2d(0, c);

        gl.glColor3f(0, 0, 1);
        gl.glVertex2d(s, -s);

        gl.glColor3f(1, 0, 0);
        gl.glVertex2d(-c, -c);
        gl.glEnd();
    }

    /**
     * When a key is pressed, set the direction of the animation appropriately.
     * 
     * @see java.awt.event.KeyListener#keyPressed(java.awt.event.KeyEvent)
     */
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

    /**
     * When an arrow key is released, stop the animation.
     * 
     * @see java.awt.event.KeyListener#keyPressed(java.awt.event.KeyEvent)
     */
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
        // ignored
    }
}
