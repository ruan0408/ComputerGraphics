import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.nio.Buffer;
import java.nio.FloatBuffer;

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
 * COMMENT: Comment ZFightingExample 
 *
 * @author malcolmr
 */
public class ZFightingExample extends JFrame implements GLEventListener, KeyListener, MouseMotionListener {

    private static final int ROTATION_SCALE = 1;
    private double myRotateX = 0;
    private double myRotateY = 0;
    private Point myMousePoint;
	   

    public ZFightingExample() {
        super("Z Fighting Example");
    }
    
    public void run() {
        GLProfile glp = GLProfile.getDefault();
        GLCapabilities caps = new GLCapabilities(glp);

        GLJPanel panel = new GLJPanel();

        panel.addGLEventListener(this);
        panel.addKeyListener(this);
        panel.addMouseMotionListener(this);
        panel.setFocusable(true);
        panel.requestFocus();

        // Add an animator to call 'display' at 60fps        
        FPSAnimator animator = new FPSAnimator(60);
        animator.add(panel);
        animator.start();

        getContentPane().add(panel);
        setSize(800, 600);        
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);        
    }

    @Override
    public void init(GLAutoDrawable drawable) {

        GL2 gl = drawable.getGL().getGL2();
        
        gl.glEnable(GL2.GL_DEPTH_TEST);
               
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
        
    }

    @Override
    public void display(GLAutoDrawable drawable) {

        GL2 gl = drawable.getGL().getGL2();
     	
        gl.glClearColor(1, 1, 1, 1);
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);

        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();
        
        gl.glRotated(myRotateX, 1, 0, 0);
        gl.glRotated(myRotateY, 0, 1, 0);
        
        gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);
      
        gl.glColor3d(1.0, 0.0, 0.0);
        gl.glBegin(GL2.GL_QUADS);
        {
            gl.glVertex3d(-1.5,-2, -2);
            gl.glVertex3d(0.5,-2, -2);
            gl.glVertex3d(0.5,2, -2);
            gl.glVertex3d(-1.5,2, -2);
        }
        gl.glEnd();
        

        //enable polygon offset for filled polygons       
       // gl.glEnable(GL2.GL_POLYGON_OFFSET_FILL);
        //push this polygon to the front a little
        //gl.glPolygonOffset(-1,-1); 
        //push to the back a little
        //gl.glPolygonOffset(1,1); 
        gl.glColor3d(0.0, 0.0, 1.0);
        gl.glBegin(GL2.GL_QUADS);
        {
            gl.glVertex3d(-0.5,-2, -2);
            gl.glVertex3d(1.5,-2, -2);
            gl.glVertex3d(1.5,2, -2);
            gl.glVertex3d(-0.5,2, -2);
        }
        gl.glEnd();
        
        //If you do not turn this off again it will not work!
        //gl.glDisable(GL2.GL_POLYGON_OFFSET_FILL);
        
    }

    /**
     * @see javax.media.opengl.GLEventListener#reshape(javax.media.opengl.GLAutoDrawable, int, int, int, int)
     */
    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width,
            int height) {
    	
    	  GL2 gl = drawable.getGL().getGL2();
    	   gl.glMatrixMode(GL2.GL_PROJECTION);
           gl.glLoadIdentity();
        	GLU glu = new GLU();
        	glu.gluPerspective(60, 1, 1, 8);       
     
       
        
    }

    public static void main(String[] args) {
        ZFightingExample example = new ZFightingExample();
        example.run();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        myMousePoint = e.getPoint();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        Point p = e.getPoint();

        if (myMousePoint != null) {
            int dx = p.x - myMousePoint.x;
            int dy = p.y - myMousePoint.y;

            // Note: dragging in the x dir rotates about y
            //       dragging in the y dir rotates about x
            myRotateY += dx * ROTATION_SCALE;
            myRotateX += dy * ROTATION_SCALE;

        }
        myMousePoint = p;
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
    	
      
            
    
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
