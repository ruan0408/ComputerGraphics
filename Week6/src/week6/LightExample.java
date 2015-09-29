package week6;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Hashtable;

import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLJPanel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;

import com.jogamp.opengl.util.FPSAnimator;


/**
 * COMMENT:  
 *
 * @author 
 */
public class LightExample extends JFrame implements KeyListener {

    // swing components
    private GLJPanel[] myPanels;
    private JSlider myAmbient;
    private JSlider myDiffuse;
    private JSlider mySpecular;
    private JSlider myPhong;
    
    private Model myModel = Model.TEAPOT;
    private Shading myShading = Shading.FLAT;
    private MyColor myColor = MyColor.WHITE;
    
    public enum Model {
        TEAPOT, SPHERE, CUBE, TORUS
    }

    public enum MyColor {
        WHITE, RED, BLUE, GREEN, GOLD, PURPLE
    }
    
    public enum Shading {
        FLAT, SMOOTH, PHONG
    }
    
    public LightExample() {
        super("Lighting example 2");
        init();
    }
    
    public Shading getShading(){
    	return myShading;
    }

    public Model getModel() {
        return myModel;
    }
    
 

    public float getAmbient() {
        return 0.1f * myAmbient.getValue();
    }

    public float getDiffuse() {
        return 0.1f * myDiffuse.getValue();
    }

    public float getSpecular() {
        return 0.1f * mySpecular.getValue();
    }
    
    public int getPhong() {
        return myPhong.getValue();
    }
    
    public float [] getMySpecular(){
    	float [] specular = {1f,1f,1f,1f};
    	if(myColor == MyColor.GOLD){
    		float [] goldSpecular = {0.628281f,0.555802f,0.366065f};
    		return goldSpecular;
    	}
    	
    	return specular;
    }
    
    public float[] getMyAmbient(){
    	
    	if(myColor == MyColor.GOLD){
    		float goldAmbient[] = {0.24725f,0.1995f,0.0745f};
    	    return goldAmbient;
    	}
    	return getMyDiffuse();
    	
    }
    public float [] getMyDiffuse(){
    	float [] diffuse = {0.5f,0.5f,0.5f,1f};
    	if(myColor == MyColor.RED){
    		diffuse[0] = 0.8f;
    		diffuse[1] = 0;
    		diffuse[2] = 0;
    	} else if (myColor == MyColor.GREEN){
    		diffuse[0] = 0;
    		diffuse[1] = 0.5f;
    		diffuse[2] = 0;
    	} else if (myColor == MyColor.BLUE){
    		diffuse[0] = 0;
    		diffuse[1] = 0;
    	} else if (myColor == MyColor.PURPLE){
    		float [] purple = {0.3f,0.1f,0.75f,1f};
    		return purple;
    	} else if (myColor == MyColor.GOLD){
    		float [] gold = {0.75164f,0.60648f,0.22648f};
    		return gold;
    	} 
    	return diffuse;
    }
    
    public void init() {
        // initialisation
        GLProfile glp = GLProfile.getDefault();
        GLCapabilities caps = new GLCapabilities(glp);

        setSize(1600, 800);
        
        initPanels(caps);

        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    
    private void initPanels(GLCapabilities caps) {
        // create a panel to draw on
        myPanels = new GLJPanel[1];
        myPanels[0] = new GLJPanel(caps);

        // create a panel of sliders
        JPanel panel = initSliders();

        setLayout(new GridLayout(1, 2));
        add(myPanels[0]);
        add(panel);

       LightingView view1 = new LightingView(this);
        myPanels[0].addGLEventListener(view1);
        myPanels[0].addMouseMotionListener(view1);
        myPanels[0].addKeyListener(this);
        myPanels[0].setFocusable(true);
        
        FPSAnimator animator = new FPSAnimator(60);
        animator.add(myPanels[0]);
        animator.start();
    }

    private JPanel initSliders() {
        Hashtable<Integer, JLabel> labels = new Hashtable<Integer, JLabel>();
        for (int i = 0; i <= 10; i++) {
            double x = i/10;
            labels.put(i, new JLabel(String.format("%.1f", x)));
        }

        myAmbient = new JSlider(JSlider.VERTICAL, 0, 10, 1);
        myDiffuse = new JSlider(JSlider.VERTICAL, 0, 10, 1);
        mySpecular = new JSlider(JSlider.VERTICAL, 0, 10, 1);
        myPhong = new JSlider(JSlider.VERTICAL, 0, 128, 8);
        
        myAmbient.setValue(0);
        myDiffuse.setValue(0);
        mySpecular.setValue(0);
        myPhong.setValue(10);
        
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 4));
        panel.add(myAmbient);
        panel.add(myDiffuse);
        panel.add(mySpecular);
        panel.add(myPhong);
        panel.add(new JLabel("Ambient"));
        panel.add(new JLabel("Diffuse"));
        panel.add(new JLabel("Specular"));
        panel.add(new JLabel("Phong"));
        return panel;
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
        case KeyEvent.VK_1:
            myModel = Model.TEAPOT;
            break;
            
        case KeyEvent.VK_2:
            myModel = Model.SPHERE;
            break;
            
        case KeyEvent.VK_3:
            myModel = Model.CUBE;
            break;

        case KeyEvent.VK_4:
            myModel = Model.TORUS;
            break;
        
            
        case KeyEvent.VK_S:
            myShading = myShading.values()[(myShading.ordinal() + 1)%3]; 
           
            break;
        case KeyEvent.VK_UP:
        	 myColor = myColor.values()[(myColor.ordinal() + 1)%6]; 
           
            break;
      
        default:
        	break;
            
        }
    }

    /**
     * Make sure the main panel keeps the focus so keypress events are handled
     *
     */
    public void focus() {
        myPanels[0].requestFocus();
    }

    public static void main(String[] args) {
        LightExample example = new LightExample();
    }


}
