package week7.bezier;



import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLJPanel;
import javax.swing.JFrame;

import com.jogamp.opengl.util.FPSAnimator;


/** 
 * @author Angela
 *
 */


public class Interpolate implements GLEventListener, KeyListener{
	private int numPoints = 10;
	private int currentPoint =0;
	private boolean lineMode = true;


	// End points.
	private double endPoints[] = 
		{
			0, 0.5,
			0, 2.5
		};
	
	
    public static void main(String[] args) {
        // initialisation
        GLProfile glp = GLProfile.getDefault();
        GLCapabilities caps;
        caps = new GLCapabilities(glp);
    
        // create a panel to draw on
        GLJPanel panel = new GLJPanel(caps);

        // put it in a JFrame
        final JFrame jframe = new JFrame("Interpolate");
        jframe.setSize(600, 600);
        jframe.add(panel);
        jframe.setVisible(true);

        // Catch window closing events and quit             
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Interpolate s = new Interpolate();
        
        // add a GL Event listener to handle rendering
        panel.addGLEventListener(s);
       
        panel.addKeyListener(s);
        panel.setFocusable(true);   
        
        // NEW: add an animator to create display events at 60 FPS
        FPSAnimator animator = new FPSAnimator(60);
        animator.add(panel);
        animator.start();
    }   
    
    
    
    private double getX(double t){
    	return (1 - t) * endPoints[0] + t * endPoints[2];
    }
    
    private double getY(double t){
    	return (1 - t) * endPoints[1] + t * endPoints[3];    	
    }
    
    @Override
    public void display(GLAutoDrawable drawable) {
    	
        GL2 gl = drawable.getGL().getGL2();
       
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
       
        gl.glColor4d(0.0, 0.0, 0.0, 1);
        gl.glLoadIdentity(); 
      
         
        gl.glPointSize(10);
        
        gl.glBegin(GL2.GL_POINTS);{
        	gl.glColor3d(1, 0, 0);
        	gl.glVertex2dv(endPoints,0);
        
        	gl.glColor3d(0,0,0);
        	gl.glVertex2dv(endPoints,2);     	
        }gl.glEnd();
        
        
        gl.glPointSize(5);
         
      
        if(lineMode)
        	gl.glBegin(GL2.GL_LINE_STRIP);
        else
        	gl.glBegin(GL2.GL_POINTS);
        {
        	gl.glColor3d(0, 0, 0);
        	double tIncrement = 1.0/numPoints;

        	for(int i = 0; i <= numPoints; i++){        		
        		double t = i*tIncrement;
        		double x  = getX(t);
        		double y  = getY(t);

        		gl.glVertex2d(x,y);
        	}
        }gl.glEnd();	 
    }
    
    @Override
    public void dispose(GLAutoDrawable drawable) {
    }

    @Override
    public void init(GLAutoDrawable drawable) {
    	GL2 gl = drawable.getGL().getGL2();

    	gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f); 
    	  
    } 	   
    	 

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int w, int h) {
    	 GL2 gl = drawable.getGL().getGL2();
         gl.glMatrixMode(GL2.GL_PROJECTION);
         gl.glLoadIdentity();       
         gl.glOrtho(-10.0f, 10.0f, -10.0f, 10.0f, -1.0f, 1.0f);         
         gl.glMatrixMode(GL2.GL_MODELVIEW);        
    }

 	

	@Override
	public void keyPressed(KeyEvent e) {
		 switch (e.getKeyCode()) {

		 case KeyEvent.VK_N:
			 if (e.isShiftDown()) {
				 if(numPoints < 10000)
					 numPoints++;
			 } else{
				 if(numPoints > 4)
					 numPoints--;

			 }
			 break;

		 case KeyEvent.VK_SPACE:			
			 currentPoint++;
			 currentPoint = currentPoint%2;

			 break;
		 case KeyEvent.VK_L:			
			 lineMode = !lineMode;
			 break;
		 case KeyEvent.VK_LEFT:
			 endPoints[currentPoint*2]-=0.1;
			 break;

		 case KeyEvent.VK_RIGHT:
			 endPoints[currentPoint*2]+=0.1;
			 break;

		 case KeyEvent.VK_DOWN:
			 endPoints[currentPoint*2+1]-=0.1;
			 break;

		 case KeyEvent.VK_UP:
			 endPoints[currentPoint*2+1]+=0.1;
			 break;

		 default:
			 break;
		 }
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
