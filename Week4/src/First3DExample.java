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

public class First3DExample implements GLEventListener {
	
  
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
        
        panel.addGLEventListener(new First3DExample());
              
    }

    
    
    
    @Override
    public void display(GLAutoDrawable drawable) {
    	GL2 gl = drawable.getGL().getGL2();

    	printMatrix(gl,"Proj");
    	gl.glMatrixMode(GL2.GL_MODELVIEW);
    	gl.glLoadIdentity();

    	
    	
    	//Forgetting to clear the depth buffer can cause problems 
    	//such as empty black screens.
    	gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
    	
    	gl.glBegin(GL2.GL_TRIANGLES);{
    
    		//red triangle at front
    	    gl.glColor3f(1f,0f,0f);
    	    //CCW ordering of vertices
    	    double p0[] = {1,0,-2}; 
    	    double p1[] = {0,1,-2};
    	    double p2[] = {-1,0,-2};
    	
    	    //Can pass in an array and an offset into the array
    	    gl.glVertex3dv(p0,0);
    	    gl.glVertex3dv(p1,0);
    	    gl.glVertex3dv(p2,0);

    	
    	    //blue triangle at back.
    		gl.glColor3f(0,0,1);
    		gl.glVertex3d(2,0,-3);
    		gl.glVertex3d(1,1,-3);
    		gl.glVertex3d(0,0,-3);

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
    	//gl.glEnable(GL2.GL_DEPTH_TEST);
    	
    	
    	
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int w, int h) {
    	 GL2 gl = drawable.getGL().getGL2();
         gl.glMatrixMode(GL2.GL_PROJECTION);
         gl.glLoadIdentity();  
         
         //You can use an orthographic camera
         gl.glOrtho(-2, 2, -2, 2, 1, 20);
         GLU glu = new GLU();
         
         //Or a perspective camera
         //You can use either gluPerspective 
         //Or glFrustum to do this.
         //glu.gluPerspective(60,1,1,20);
         //gl.glFrustum(-0.6,0.6,-0.6,0.6,1,20);
         //glu.gluPerspective(120,1,1,20);
        // gl.glFrustum(-2,2,-2,2,1,20);
         
    }  
    
    static void printMatrix(GL2 gl,String message){
   	 double[] curmat = new double[16];

   	   // Get the current matrix on the MODELVIEW stack
   	 gl.glGetDoublev(GL2.GL_PROJECTION_MATRIX, curmat, 0);

   	   // Note: If you want to multiply the matrix 'curmat' onto the
   	   // matrix stack at a later time, just use this:
   	   //      gl.glMultMatrixd(curmat, 0);

   	   // Print out the contents of this matrix in OpenGL format
   	   System.out.println(message);
   	   for (int row = 0; row < 4; row++)
   	       for (int col = 0; col < 4; col++)
   		   // OpenGL uses column-major order for storage
   		   System.out.format("%7.3f%c", curmat[row+col*4], col==3 ? '\n':' ');
   	   
   	  
   		  

   }

   
}
