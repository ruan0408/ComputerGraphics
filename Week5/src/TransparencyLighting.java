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


// A simple 3D example used in lectures to show
// some of the important 3D settings and issues

public class TransparencyLighting implements GLEventListener {
	
  
    public static void main(String[] args) {
        // initialisation
        GLProfile glp = GLProfile.getDefault();
        GLCapabilities caps = new GLCapabilities(glp);
        
        // create a panel to draw on
        GLJPanel panel = new GLJPanel(caps);

        // put it in a JFrame
        final JFrame jframe = new JFrame("Transparency Lighting");
        jframe.setSize(300, 300);
        jframe.add(panel);
        jframe.setVisible(true);
       
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        panel.addGLEventListener(new TransparencyLighting());
              
    }

    
   
    @Override
    public void display(GLAutoDrawable drawable) {
    	GL2 gl = drawable.getGL().getGL2();


    	gl.glMatrixMode(GL2.GL_MODELVIEW);
    	gl.glLoadIdentity();

    
    	//to position the camera.
    	
    	gl.glTranslated(0,0,-1);
    	
    	//Forgetting to clear the depth buffer can cause problems 
    	//such as empty black screens.
    	gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
    	 float matAmbAndDifR[] = {0.9f, 0.0f, 0.0f, 0.75f};
         float matAmbAndDifG[] = {0.0f, 0.5f, 0.0f, 1.0f};
      
         
    	gl.glBegin(GL2.GL_TRIANGLES);{
    		
    		
    		//One in drawn first and in front has alpha
    	    //gl.glColor4f(1f,0f,0f,1f);
    	    //CCW ordering of vertices
    	    double p0[] = {1,0,-1}; 
    	    double p1[] = {0,1,-1};
    	    double p2[] = {-1,0,-1};
    	   
        
    	   
            gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT_AND_DIFFUSE, matAmbAndDifG,0);
           
    	    //gl.glColor4f(0,1,0,1f);
    		gl.glVertex3d(2,0,-2);
    		gl.glVertex3d(1,1,-2);
    		gl.glVertex3d(0,0,-2);
    		
    		gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_AMBIENT_AND_DIFFUSE, matAmbAndDifR,0);
    		gl.glVertex3dv(p0,0);
       	    gl.glVertex3dv(p1,0);
       	    gl.glVertex3dv(p2,0);

    	}gl.glEnd();
    	
    	gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
    }

    @Override
    public void init(GLAutoDrawable drawable) {
    	GL2 gl = drawable.getGL().getGL2();
    	
    	//If you do not add this line
    	//opengl will draw things in the order you
    	//draw them in your program
    	gl.glEnable(GL2.GL_DEPTH_TEST);
    	
    	//By enabling lighting, color is worked out differently.
    	gl.glEnable(GL2.GL_LIGHTING);
    	
    	//When you enable lighting you must still actually
    	//turn on a light such as this default light.
    	gl.glEnable(GL2.GL_LIGHT0); 
    	// Light property vectors.
    	float lightAmb[] = { 0.0f, 0.0f, 0.0f, 1.0f };
    	float lightDifAndSpec[] = { 1.0f, 1.0f, 1.0f, 1.0f };
    	//float lightPos[] = { 0.0f, 1.5f, 3.0f, 1.0f };
    	float globAmb[] = { 0.2f, 0.2f, 0.2f, 1.0f };
    	//float globAmb[] = {0.5f,0.5f,0.5f,1f};
    	// Light properties.
    	gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, lightAmb,0);
    	gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, lightDifAndSpec,0);
    	gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_SPECULAR, lightDifAndSpec,0);
    	//gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, lightPos,0);
    	
    	gl.glBlendFunc(GL2.GL_SRC_ALPHA,GL2.GL_ONE_MINUS_SRC_ALPHA);
    	gl.glEnable(GL2.GL_BLEND);

    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int w, int h) {
    	 GL2 gl = drawable.getGL().getGL2();
         gl.glMatrixMode(GL2.GL_PROJECTION);
         gl.glLoadIdentity();  
         
         //You can use an orthographic camera
         //gl.glOrtho(-2, 2, -2, 2, 0, 20); 
         GLU glu = new GLU();
         
         //Or a perspective camera
         //You can use either gluPerspective 
         //Or glFrustum to do this.
         //glu.gluPerspective(60,1,1,20);
         gl.glFrustum(-1,1,-1,1,1,20);
              
    }  
   
}
