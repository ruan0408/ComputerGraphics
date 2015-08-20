import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLCanvas;
import javax.media.opengl.awt.GLJPanel;
import javax.media.opengl.glu.GLU;
import javax.swing.JFrame;

import com.jogamp.opengl.util.FPSAnimator;

/**
 * Demonstrates how providing different types of normals
 * can give the illusion of curves or edges.
 * 
 * Change the line from 
 * render(drawable,5,32,true);
 * 
 * to
 * render(drawable,5,32,false);
 * 
 * to see the difference.
 */

public class Cylinder implements GLEventListener , KeyListener{
	private boolean faceNormals = true;
	private static int angle = 0;
	private static int slices = 32;
	
	
    public static void main(String[] args) {
        // initialisation
        GLProfile glp = GLProfile.getDefault();
        GLCapabilities caps = new GLCapabilities(glp);
        
        // create a panel to draw on
        GLJPanel panel = new GLJPanel(caps);

        // put it in a JFrame
        final JFrame jframe = new JFrame("Rotations");
        jframe.setSize(400, 400);
        jframe.add(panel);
        jframe.setVisible(true);

        // Catch window closing events and quit             
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Cylinder c = new Cylinder();
        // add a GL Event listener to handle rendering
        panel.addGLEventListener(c);
        
        panel.addKeyListener(c);
        panel.setFocusable(true);   
        
        
      
        
        // NEW: add an animator to create display events at 60 FPS
        FPSAnimator animator = new FPSAnimator(60);
        animator.add(panel);
        animator.start();
    }

    
    /**
     * Render the cylinder in an OpenGL context.
     * 
     */
    public void render(GLAutoDrawable drawable, int height, int slices,boolean cylinder) {
    	GL2 gl = drawable.getGL().getGL2();
    	gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);

    	gl.glColor3f(1,0,0);
    
    	gl.glRotated(angle,1,0,0);
    	
    	double z1 = 0;
    	double z2 = -height;
    	
    	gl.glBegin(GL2.GL_TRIANGLE_FAN);{
    	
    		 gl.glNormal3d(0,0,1);
    		 gl.glVertex3d(0,0,z1);
    		 double angleStep = 2*Math.PI/slices;
             for (int i = 0; i <= slices ; i++){//slices; i++) {
                 double a0 = i * angleStep;
                 double a1 = ((i+1) % slices) * angleStep;
                 
                 //Calculate vertices for the quad
                 double x0 = Math.cos(a0);
                 double y0 = Math.sin(a0);

                gl.glVertex3d(x0,y0,z1);
             }
                 
                 
    	}gl.glEnd();
    	
    	gl.glBegin(GL2.GL_TRIANGLE_FAN);{
        	
   		 gl.glNormal3d(0,0,-1);
   		 gl.glVertex3d(0,0,z2);
   		 double angleStep = 2*Math.PI/slices;
            for (int i = 0; i <= slices ; i++){//slices; i++) {
                double a0 = i * angleStep;
               
                
                //Calculate vertices for the quad
                double x0 = Math.cos(a0);
                double y0 = Math.sin(a0);

               gl.glVertex3d(x0,y0,z2);
            }
                
                
   	}gl.glEnd();
   	
    	gl.glBegin(GL2.GL_QUADS);
        {
            double angleStep = 2*Math.PI/slices;
            for (int i = 0; i <= slices ; i++){//slices; i++) {
                double a0 = i * angleStep;
                double a1 = ((i+1) % slices) * angleStep;
                
                //Calculate vertices for the quad
                double x0 = Math.cos(a0);
                double y0 = Math.sin(a0);

                double x1 = Math.cos(a1);
                double y1 = Math.sin(a1);
                //Calculation for face normal for each quad
                //                     (x0,y0,z2)
                //                     ^
                //                     |  u = (0,0,z2-z1) 
                //                     |
                //                     | 
                //(x1,y1,z1)<--------(x0,y0,z1)
                //v = (x1-x0,y1-y0,0)  
                //                     
                //                     
                //                       
                //                    
                //
                // u x v gives us the un-normalised normal
                // u = (0,     0,   z2-z1)
                // v = (x1-x0,y1-y0,0) 
                
                
                //If we want it to be smooth like a cylinder
                //use different normals for each different x and y
                if(cylinder){
                    gl.glNormal3d(x0, y0, 0);
                }else{
                    //Use the face normal for all 4 vertices in the quad.
                	gl.glNormal3d(-(z2-z1)*(y1-y0),(x1-x0)*(z2-z1),0);
                }
                
                             
                gl.glVertex3d(x0, y0, z1);
                gl.glVertex3d(x0, y0, z2);  
                
                //If we want it to be smooth like a cylinder
                //use different normals for each different x and y
                if(cylinder)
                    gl.glNormal3d(x1, y1, 0);
                
                gl.glVertex3d(x1, y1, z2);
                gl.glVertex3d(x1, y1, z1);               
               
               
            }

        }
        gl.glEnd();
    }

    @Override
    public void display(GLAutoDrawable drawable) {
    	 GL2 gl = drawable.getGL().getGL2();
         gl.glMatrixMode(GL2.GL_MODELVIEW);
         gl.glLoadIdentity();
         gl.glTranslated(0, 0, -4);
        
         render(drawable,2,slices,!faceNormals);
         
            
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
    }

    @Override
    public void init(GLAutoDrawable drawable) {
    	GL2 gl = drawable.getGL().getGL2();
    	gl.glEnable(GL2.GL_DEPTH_TEST);
    	
        // enable lighting
        gl.glEnable(GL2.GL_LIGHTING);
        // turn on a light. Use default settings.
        gl.glEnable(GL2.GL_LIGHT0);
        
        // normalise normals (!)
        // this is necessary to make lighting work properly
        gl.glEnable(GL2.GL_NORMALIZE);
       
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int w, int h) {
    	 GL2 gl = drawable.getGL().getGL2();
         gl.glMatrixMode(GL2.GL_PROJECTION);
         gl.glLoadIdentity();
     
       
         gl.glOrtho(-3, 3, -3, 3, 1, 10);
         
    }
    
	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		switch (e.getKeyCode()) {
		  
		 case KeyEvent.VK_SPACE:
		          faceNormals = !faceNormals;
				
				  break;
				 
				  
		 case KeyEvent.VK_DOWN:
		       
			  angle = (angle - 10) % 360;
			  break;
	     case KeyEvent.VK_UP:
		     
			  angle = (angle + 10) % 360;
			  break;	
			  
			  
			 case KeyEvent.VK_RIGHT:
			       
				  if(slices <100) slices++;
				  break;
		     case KeyEvent.VK_LEFT:
			     
				  if(slices > 5) slices--;
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
