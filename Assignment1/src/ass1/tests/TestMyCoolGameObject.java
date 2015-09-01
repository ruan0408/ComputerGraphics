package ass1.tests;

import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLJPanel;
import javax.swing.JFrame;

import ass1.Camera;
import ass1.GameEngine;
import ass1.GameObject;
import ass1.MyCoolGameObject;
import com.jogamp.opengl.util.FPSAnimator;

/**
 * A simple class to view ass1.MyCoolGameObject
 *
 * @author angf
 */
public class TestMyCoolGameObject {

	public static void createTestObjects(){
		
		//Should look good when we create using the default constructor
		MyCoolGameObject cgo = new MyCoolGameObject();
		
		// Should not break if we apply transformations for example
		// If we uncommented these lines (or wrote other transformations) 
		// it should not break your object
		
		cgo.translate(-0.2,0.2);
		cgo.rotate(45);
		cgo.scale(0.25);		    
	}
   
    /**
     * A simple test for ass1.MyCoolGameObject
     * 
     * @param args
     */
    public static void main(String[] args) {
        // Initialise OpenGL
        GLProfile glprofile = GLProfile.getDefault();
        GLCapabilities glcapabilities = new GLCapabilities(glprofile);
        
        // create a GLJPanel to draw on
        GLJPanel panel = new GLJPanel(glcapabilities);

        // Create a camera
        Camera camera = new Camera(GameObject.ROOT);
        
        createTestObjects();
        
        // Add the game engine
        GameEngine engine = new GameEngine(camera);
        panel.addGLEventListener(engine);

        // Add an animator to call 'display' at 60fps        
        FPSAnimator animator = new FPSAnimator(60);
        animator.add(panel);
        animator.start();

        // Put it in a window
        JFrame jFrame = new JFrame("Test Shapes");
        jFrame.add(panel);
        jFrame.setSize(600, 600);
        jFrame.setVisible(true);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

}
