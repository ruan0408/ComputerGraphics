import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

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

// A example illustrating using vertex normals by averaging face normals to get a smooth surface

public class Normals implements GLEventListener, KeyListener  {
	
	private boolean faceNormals = true;
  
    public static void main(String[] args) {
        // initialisation
        GLProfile glp = GLProfile.getDefault();
        GLCapabilities caps = new GLCapabilities(glp);
        
        // create a panel to draw on
        GLJPanel panel = new GLJPanel(caps);

        // put it in a JFrame
        final JFrame jframe = new JFrame("Face Normals vs Vertex Normals");
        jframe.setSize(800, 800);
        jframe.add(panel);
        jframe.setVisible(true);
       
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Normals n = new Normals();
        panel.addGLEventListener(n);
        panel.addKeyListener(n);
        panel.setFocusable(true);   
        
        
        // NEW: add an animator to create display events at 60 FPS
        FPSAnimator animator = new FPSAnimator(60);
        animator.add(panel);
        animator.start();
    }
    
    /* 
     * Some maths utility functions
     * 
     */

    double getMagnitude(double [] n){
    	double mag = n[0]*n[0] + n[1]*n[1] + n[2]*n[2];
    	mag = Math.sqrt(mag);
    	return mag;
    }
    
    double [] normalise(double [] n){
    	double  mag = getMagnitude(n);
    	double norm[] = {n[0]/mag,n[1]/mag,n[2]/mag};
    	return norm;
    }
    
    double [] cross(double u [], double v[]){
    	double crossProduct[] = new double[3];
    	crossProduct[0] = u[1]*v[2] - u[2]*v[1];
    	crossProduct[1] = u[2]*v[0] - u[0]*v[2];
    	crossProduct[2] = u[0]*v[1] - u[1]*v[0];
    	//System.out.println("CP " + crossProduct[0] + " " +  crossProduct[1] + " " +  crossProduct[2]);
    	return crossProduct;
    }
    
    double [] getNormal(double[] p0, double[] p1, double[] p2){
    	double u[] = {p1[0] - p0[0], p1[1] - p0[1], p1[2] - p0[2]};
    	double v[] = {p2[0] - p0[0], p2[1] - p0[1], p2[2] - p0[2]};
    	
    	return cross(u,v);
    	
    }
    
   
    @Override
    public void display(GLAutoDrawable drawable) {
    	GL2 gl = drawable.getGL().getGL2();


    	gl.glMatrixMode(GL2.GL_MODELVIEW);
    	gl.glLoadIdentity();

    	//You can use glu look at or normal transformations
    	//to position the camera.
    	
    	
    	//Forgetting to clear the depth buffer can cause problems 
    	//such as empty black screens.
    	gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
    	gl.glColor4d(1,1,1,1);
    
    	//Note: we are not just using the same normal for every vertex
    	//in the same face.
    	//We are using vertex normals. Each vertex has its own
    	//normal that has every time, regardless of what quad it is 
    	//a part of.
    	    	
       
    	float red[] = {1f, 0.0f, 0.0f, 1.0f};
    
    	gl.glBegin(GL2.GL_QUADS);{
    		//6 points in space
    		double p0[] = {0.2,0,-2.3};
    		double p1[] = {0.2,1,-2.3};
    		double p2[] = {0,1,-2};
    		double p3[] = {0,0,-2};
    		double p4[] = {-0.2,1,-2};
    		double p5[] = {-0.2,0,-2}; 
    		                             
    		//Normal for the face of the first quad p0,p1,p2,p3
    		double [] n1 = getNormal(p0,p1,p2);
    		
    		//Normal for the face of the second quad p3,p2,p4,p5
    		double [] n2 = getNormal(p3,p2,p4);
    		
    		
    		
    		//Average of the the two face normals 
    		//(we don't bother dividing by 2 when taking the average as it gets normalised anyway    		
    		double [] n3 = {n1[0] + n2[0],n1[1] + n2[1], n1[2] + n2[2]};
    		
    		n1 = normalise(n1);
      	    n2 = normalise(n2);
    	    n3 = normalise(n3);
    	
    		
    	
    		gl.glNormal3dv(n1,0);
    		gl.glVertex3dv(p0,0);
    		gl.glVertex3dv(p1,0);
    		gl.glVertex3dv(p2,0);
    		gl.glVertex3dv(p3,0);
    		  	   		
    		
    		    
    	    gl.glNormal3dv(n2,0);
    		
    		gl.glVertex3dv(p3,0);
    		gl.glVertex3dv(p2,0);   		
    		gl.glVertex3dv(p4,0);
    		gl.glVertex3dv(p5,0);
    		

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
    	   	
    	
    	
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int w, int h) {
    	 GL2 gl = drawable.getGL().getGL2();
         gl.glMatrixMode(GL2.GL_PROJECTION);
         gl.glLoadIdentity();      
         
         gl.glOrtho(-1.5,1.5,-1.5,1.5,1,8);       
    }

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		switch (e.getKeyCode()) {
		  
		 case KeyEvent.VK_SPACE:
		          faceNormals = !faceNormals;
				
				  break;
		
		 default:
			 break;
		 }
		System.out.println("face normals " + faceNormals);
		
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}  
    
   
   
}
