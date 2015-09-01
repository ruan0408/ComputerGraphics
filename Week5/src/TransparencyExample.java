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



// A simple example of transparency and blending

public class TransparencyExample implements GLEventListener {
	
  
    public static void main(String[] args) {
        // initialisation
        GLProfile glp = GLProfile.getDefault();
        GLCapabilities caps = new GLCapabilities(glp);
        
        // create a panel to draw on
        GLJPanel panel = new GLJPanel(caps);

        // put it in a JFrame
        final JFrame jframe = new JFrame("My First 3D Example");
        jframe.setSize(300, 300);
        jframe.add(panel);
        jframe.setVisible(true);
       
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        panel.addGLEventListener(new TransparencyExample());
              
    }

    
    
   
    @Override
    public void display(GLAutoDrawable drawable) {
    	GL2 gl = drawable.getGL().getGL2();


    	gl.glMatrixMode(GL2.GL_MODELVIEW);
    	gl.glLoadIdentity();

        //Move camera back a little
    	gl.glTranslated(0,0,-1);
    	
    	//Forgetting to clear the depth buffer can cause problems 
    	//such as empty black screens.
    	gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
    	
    	gl.glBegin(GL2.GL_TRIANGLES);{
    		
    		 //Need to draw transparent one last
    	    gl.glColor4f(1f,0f,0f,0.75f); //try with 0.25
            //CCW ordering of vertices
    	    double p0[] = {1,0,-1}; 
    	    double p1[] = {0,1,-1};
    	    double p2[] = {-1,0,-1};
    	
    	    //Can pass in an array and an offset into the array
    	    gl.glVertex3dv(p0,0);
    	    gl.glVertex3dv(p1,0);
    	    gl.glVertex3dv(p2,0);
    		
    	    //Opaque one (at back) must be drawn before
            //transparent one in front   	   
    	    gl.glColor4f(0,1,0,1f);
    	    gl.glVertex3d(2,0,-2);
    	    gl.glVertex3d(1,1,-2);
    	    gl.glVertex3d(0,0,-2);
    	    
    	   
    	}gl.glEnd();
       	   
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
    	
    	gl.glClearColor(1,1, 1, 1);
    	//Turn on for transparency and blending
    	gl.glBlendFunc(GL2.GL_SRC_ALPHA,GL2.GL_ONE_MINUS_SRC_ALPHA);
    	gl.glEnable(GL2.GL_BLEND);

    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int w, int h) {
    	 GL2 gl = drawable.getGL().getGL2();
         gl.glMatrixMode(GL2.GL_PROJECTION);
         gl.glLoadIdentity();        
         gl.glFrustum(-1,1,-1,1,1,10);
              
    }  
   
}
