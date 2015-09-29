package week7.revision;

import java.awt.BorderLayout;
import java.io.IOException;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLJPanel;
import javax.media.opengl.glu.GLU;
import javax.swing.JFrame;

import week6.Shader;

import com.jogamp.opengl.util.FPSAnimator;
import com.jogamp.opengl.util.gl2.GLUT;

/**
 * COMMENT: 
 * @author 
 */
public class DrivingGame extends JFrame implements GLEventListener {

	private static final String VERTEX_SHADER = "src/week7/revision/PhongVertex.glsl";
    private static final String FRAGMENT_SHADER = "src/week7/revision/PhongFragment.glsl";
    private int shaderprogram;
    
    
    private static final double CAMERA_DIST = 5;
    private static final float FOV = 90;
    private Car myCar;
    private Floor myFloor;

    
    
    public DrivingGame() {
        super("Driving Game!");   
        
        myFloor = new Floor(20, 10);
        myCar = new Car();
    }
    
    private void run() {
        GLProfile glprofile = GLProfile.getDefault();
        GLCapabilities glcapabilities = new GLCapabilities(glprofile);
        GLJPanel panel = new GLJPanel(glcapabilities);

        panel.addGLEventListener(this);
         
        FPSAnimator animator = new FPSAnimator(60);
        animator.add(panel);
        animator.start();

        getContentPane().add(panel, BorderLayout.CENTER);
        setSize(800, 800);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);        
    }
    
    
    
    @Override
    public void init(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        
        try {
   		 	shaderprogram = Shader.initShaders(gl,VERTEX_SHADER,FRAGMENT_SHADER);   		 
        }
        catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        
        gl.glEnable(GL2.GL_DEPTH_TEST);
        gl.glEnable(GL2.GL_LIGHTING);
        
       
    }
    
    public void setLighting(GL2 gl){
    	 gl.glEnable(GL2.GL_LIGHT0);
    	 float lightPos[] = {0,1,0,0}; //directional light
    	 float globAmb[] = {1f,1f,1f};
    	 gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, lightPos,0);
    	 gl.glLightModelfv(GL2.GL_LIGHT_MODEL_AMBIENT, globAmb,0); // Global ambient light.
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glUseProgram(shaderprogram);
        gl.glClearColor(0.4f, 0.4f, 1, 1);
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
        
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();
       
       //gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_LINE);
        gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);
        gl.glColor4d(0,0,0,1);
        
        //gl.glRotated(-30, 1, 0, 0);
        //gl.glTranslated(0,-4, -CAMERA_DIST);
        GLU glu = new GLU();
        //glu.gluLookAt(0,2,CAMERA_DIST,0,0,0,0,1,0);
        gl.glRotated(22,1,0,0);
        gl.glTranslated(0,-2,-CAMERA_DIST);
        setLighting(gl);
        myFloor.draw(gl);
        gl.glPushMatrix();
            gl.glTranslated(-1,0,1); 
            gl.glRotated(-90, 0, 1, 0);   
            gl.glScaled(0.75,0.75,0.75);
            myCar.draw(gl);
        gl.glPopMatrix();
        
        
        float[] difColor = {0.5f, 0.3f, 0.6f, 1};
        float[] ambColor = {0.1f, 0.01f, 0.1f, 1}; 
        gl.glPushAttrib(GL2.GL_LIGHTING_BIT);
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_DIFFUSE, difColor, 0);
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT, ambColor, 0);
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, difColor, 0);
        gl.glMaterialf(GL2.GL_FRONT, GL2.GL_SHININESS, 120);
        try{
        	gl.glTranslated(3.5, 0, -1);
            gl.glScaled(2,2,2);
            Triceratops.drawObject(gl);
        } catch(IOException e){
        	GLUT glut = new GLUT();
        	glut.glutSolidSphere(0.5,10,10);
        }
        gl.glPopAttrib();
        gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width,
            int height) {
        GL2 gl = drawable.getGL().getGL2();
        
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        
//        gl.glOrtho(-8, 8, -8, 8, -8, 8);
        
        double aspect = 1.0 * width / height;
        
        GLU glu = new GLU();
        glu.gluPerspective(FOV, aspect, 1, CAMERA_DIST + 4);
        
    }

    public static void main(String[] args) {
        DrivingGame game = new DrivingGame();
        
        game.run();
    }


}
