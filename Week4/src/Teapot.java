import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLJPanel;
import javax.swing.JFrame;

import com.jogamp.opengl.util.FPSAnimator;

import TeapotView.View;

/**
 * COMMENT: Comment Teapot 
 *
 * @author malcolmr
 */
public class Teapot implements MouseMotionListener, KeyListener {

    private static final int ROTATION_SCALE = 1;
    private static GLJPanel[] myPanels;
    private static JFrame myFrame;
    private double myRotateX = 0;
    private double myRotateY = 0;
    private double myScale = 0.5;
    private double myDistance = 1;

    private Point myMousePoint = null;
    private boolean myWireframe = true;
    private boolean myLights = false;
    private boolean mySmooth = false;
    private boolean myPerspective = false;
    private double myFOV = 60;
    
    static public enum Type {
        TEAPOT, CUBE, SPHERE, TORUS, TETRAHEDRON, ICOSAHEDRON, CYLINDER , CONE, SPACESHIP;
    }

    private Type myType = Type.TEAPOT;

    public double getDistance() {
        return myDistance;
    }

    public double getRotateX() {
        return myRotateX;
    }

    public double getRotateY() {
        return myRotateY;
    }

    public double getScale() {
        return myScale;
    }

    public Type getType() {
        return myType;
    }

    public boolean isWireframe() {
        return myWireframe;
    }

    public boolean isLit() {
        return myLights;
    }

    public boolean isSmooth() {
        return mySmooth;
    }

    public boolean isPerspective() {
        return myPerspective;
    }

    public double getFOV() {
        return myFOV;
    }
    
    public static void main(String[] args) {
        // initialisation
        GLProfile glp = GLProfile.getDefault();
        GLCapabilities caps = new GLCapabilities(glp);
        
        // create a panel to draw on
        myPanels = new GLJPanel[4];
        myPanels[0] = new GLJPanel(caps);
        myPanels[1] = new GLJPanel(caps);
        myPanels[2] = new GLJPanel(caps);
        myPanels[3] = new GLJPanel(caps);

        myFrame = new JFrame("Teapot");
        myFrame.setSize(800, 800);
        myFrame.setLayout(new GridLayout(2, 2));
        myFrame.add(myPanels[0]);
        myFrame.add(myPanels[1]);
        myFrame.add(myPanels[2]);
        myFrame.add(myPanels[3]);
        myFrame.setVisible(true);
        myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Teapot teapot = new Teapot();
        myPanels[0].addMouseMotionListener(teapot);
        myPanels[0].addKeyListener(teapot);
        myPanels[0].setFocusable(true);

        TeapotView view1 = new TeapotView(teapot, false, View.FREE);
        myPanels[0].addGLEventListener(view1);

        myPanels[1].addMouseMotionListener(teapot);
        myPanels[1].setFocusable(false);
        TeapotView view2 = new TeapotView(teapot, true, View.BACKWARDS);
        myPanels[1].addGLEventListener(view2);

        TeapotView view3 = new TeapotView(teapot, true, View.DOWN);
        myPanels[2].addGLEventListener(view3);

        TeapotView view4 = new TeapotView(teapot, true, View.RIGHT);
        myPanels[3].addGLEventListener(view4);

        FPSAnimator animator = new FPSAnimator(60);
        animator.add(myPanels[0]);
        animator.add(myPanels[1]);
        animator.add(myPanels[2]);
        animator.add(myPanels[3]);
        animator.start();
    }

    private void reshape() {
        // force reshape() call on all panels
        
        Dimension size = myFrame.getSize();
        
        int w = size.width / 2;
        int h = size.height / 2;
        
        for (int i = 0; i < myPanels.length; i++) {
            myPanels[i].setSize(w,h);
        }
    }
    
    /**
     * @see java.awt.event.MouseMotionListener#mouseDragged(java.awt.event.MouseEvent)
     */
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
        System.out.println("Rotate x: y " + myRotateX + " " + myRotateY);
        myMousePoint = p;
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        myMousePoint = e.getPoint();
    }

    /**
    * @see java.awt.event.KeyListener#keyTyped(java.awt.event.KeyEvent)
    */
    @Override
    public void keyTyped(KeyEvent e) {

    }

    /**
     * @see java.awt.event.KeyListener#keyPressed(java.awt.event.KeyEvent)
     */
    @Override
    public void keyPressed(KeyEvent e) {

    }

    /**
     * @see java.awt.event.KeyListener#keyReleased(java.awt.event.KeyEvent)
     */
    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
        case KeyEvent.VK_SPACE:
            myWireframe = !myWireframe;
            break;

        case KeyEvent.VK_L:
            myLights = !myLights;
            break;

        case KeyEvent.VK_S:
            mySmooth = !mySmooth;
            break;

        case KeyEvent.VK_P:
            myPerspective = !myPerspective;
            reshape();
            break;

        case KeyEvent.VK_1:
            myType = Type.TEAPOT;
            break;

        case KeyEvent.VK_2:
            myType = Type.CUBE;
            break;

        case KeyEvent.VK_3:
            myType = Type.SPHERE;
            break;

        case KeyEvent.VK_4:
            myType = Type.TORUS;
            break;
        case KeyEvent.VK_5:
            myType = Type.TETRAHEDRON;
            break;
        case KeyEvent.VK_6:
            myType = Type.ICOSAHEDRON;
            break;
        case KeyEvent.VK_7:
            myType = Type.CYLINDER;
            break;
        case KeyEvent.VK_8:
            myType = Type.CONE;
            break;
        case KeyEvent.VK_9:
            myType = Type.SPACESHIP;
            break;

        case KeyEvent.VK_LEFT:
            myScale *= 1.1;
            System.out.println("myScale " + myScale);
            break;

        case KeyEvent.VK_RIGHT:
            myScale /= 1.1;
            System.out.println("myScale " + myScale);
            break;

        case KeyEvent.VK_UP:
        	
            myDistance -= 0.1;
            System.out.println("distance " + myDistance);
            break;

        case KeyEvent.VK_DOWN:
        	 
            myDistance += 0.1;
            System.out.println("distance " + myDistance);
            break;

        case KeyEvent.VK_PAGE_UP:
            myFOV += 5;
            System.out.println("fov " + myFOV);
            reshape();
            break;

        case KeyEvent.VK_PAGE_DOWN:
            myFOV -= 5;
            System.out.println("fov " + myFOV);
            reshape();
            break;

        }

    }

}
