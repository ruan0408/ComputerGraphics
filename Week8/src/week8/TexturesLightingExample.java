package week8;

import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Hashtable;

import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLJPanel;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;

import com.jogamp.opengl.util.FPSAnimator;


/**
 * COMMENT: 
 *
 * @author malcolmr
 */
public class TexturesLightingExample extends JFrame implements KeyListener {

    // swing components
    private GLJPanel[] myPanels;
    private JSlider myAmbient;
    private JSlider myDiffuse;
    private JSlider mySpecular;
    private JSlider myPhong;
    private JCheckBox myModulate;
    private JCheckBox mySpecularSep;
    
    private Model myModel = Model.TEAPOT;
    private boolean mySmooth = false;
    
   
   
    private String textureNames[] = {"metal.jpg","eyeTextureNew2.jpg","bricks.png","BrightPurpleMarble.png","kittens.jpg","Lights.png","grass.bmp","tired-user.jpg"};
    private String textureExtensions[] = {"jpg","jpg","png","png","jpg","png","bmp","jpg"};
    private int curTex;
    
    public enum Model {
        TEAPOT, CUBE, SPHERE, TORUS, SPACESHIP
    }
    

    public TexturesLightingExample() {
        super("Textures and Lighting Example");
        init();
    }

    public Model getModel() {
        return myModel;
    }
    
    public boolean isSmooth() {
        return mySmooth ;
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
    
    public boolean getModulate() {
        return myModulate.isSelected();
    }
    
    public boolean getSpecularSep() {
        return mySpecularSep.isSelected();
    }
    
    public int getNumTextures(){
    	return textureNames.length;
    }
    
    public int getTexId(){
    	return curTex;
    }
    
    public String getTexName(int i){
    	return textureNames[i];
    	
    }
    public String getTexExtension(int i){
    	return textureExtensions[i];
    }
    
    public void init() {
        // initialisation
        GLProfile glp = GLProfile.getDefault();
        GLCapabilities caps = new GLCapabilities(glp);

        setSize(1800, 1200);
        
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

       TextureView view1 = new TextureView(this);
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
        myPhong = new JSlider(JSlider.VERTICAL, 10, 200, 10);
        myModulate = new JCheckBox("Modulate", true);
        mySpecularSep = new JCheckBox("Specular", false);
        
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
        JPanel checkPanel = new JPanel();
        checkPanel.setLayout(new GridLayout(2, 1));
        checkPanel.add(myModulate);
        checkPanel.add(mySpecularSep);
        
        panel.add(checkPanel);
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
            myModel = Model.CUBE;
            break;
            
        case KeyEvent.VK_3:
            myModel = Model.SPHERE;
            break;

        case KeyEvent.VK_4:
            myModel = Model.TORUS;
            break;
        case KeyEvent.VK_5:
            myModel = Model.SPACESHIP;
            break;
            
        case KeyEvent.VK_S:
            mySmooth = !mySmooth;
            break;
        case KeyEvent.VK_SPACE:
            curTex++ ;
            curTex = curTex%textureNames.length;
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
        TexturesLightingExample example = new TexturesLightingExample();
    }


}
