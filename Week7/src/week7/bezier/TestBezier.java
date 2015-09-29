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


/** A class that demonstrates bezier curves 
 * 
 *  The up,down,left,arrows move the current control point
 *  The space bar changes to the next control point
 * @author Angela
 *
 */


public class TestBezier implements GLEventListener, KeyListener{
	private int numPoints = 10;
	private int currentPoint =0;
	private boolean lineMode = true;



	// Control points.
	private double controlPoints[] = 
		{
			0, 0.5,
			3, 0.5,
			3, 2.5,
			0, 2.5
		};

	// Control point values stored unchanged for use on reset.
	private double originalControlPoints[] = 
		{
			0, 0.5,
			3, 0.5,
			3, 2.5,
			0, 2.5	
		};


	// Routine to restore control points to original values.
	private void restoreControlPoints()
	{
		int i;
		for (i=0; i<controlPoints.length; i++){
			controlPoints[i] = originalControlPoints[i];
		}
	}
	
	
    public static void main(String[] args) {
        // initialisation
        GLProfile glp = GLProfile.getDefault();
        GLCapabilities caps;
        caps = new GLCapabilities(glp);
    
        // create a panel to draw on
        GLJPanel panel = new GLJPanel(caps);

        // put it in a JFrame
        final JFrame jframe = new JFrame("Bezier Curve");
        jframe.setSize(600, 600);
        jframe.add(panel);
        jframe.setVisible(true);

        // Catch window closing events and quit             
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        TestBezier s = new TestBezier();
        
        // add a GL Event listener to handle rendering
        panel.addGLEventListener(s);
       
        panel.addKeyListener(s);
        panel.setFocusable(true);   
        
        // NEW: add an animator to create display events at 60 FPS
        FPSAnimator animator = new FPSAnimator(60);
        animator.add(panel);
        animator.start();
    }   
    
 
  
    @Override
    public void display(GLAutoDrawable drawable) {
    	
        GL2 gl = drawable.getGL().getGL2();
       
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
       
        gl.glColor4d(0.0, 0.0, 0.0, 1);
        gl.glLoadIdentity(); 
        CubicBezierSpline curve  = new CubicBezierSpline(controlPoints);
      
        
   
        gl.glPointSize(10);
        
        gl.glBegin(GL2.GL_POINTS);{
        	gl.glColor3d(1, 0, 0);
        	gl.glVertex2dv(curve.controlPoint(0),0);
        	gl.glColor3d(0, 1, 1);
        	gl.glVertex2dv(curve.controlPoint(1),0);
        	gl.glColor3d(0, 1, 0);
        	gl.glVertex2dv(curve.controlPoint(2),0);
        	gl.glColor3d(0,0,0);
        	gl.glVertex2dv(curve.controlPoint(3),0);     	
        }gl.glEnd();
        
        //Draw the bezier curve.
        //We are just using the values as x and y values
        //and using a z of 0. 
        
        gl.glPointSize(5);
        
        if(lineMode)
        	gl.glBegin(GL2.GL_LINE_STRIP);
        else
        	gl.glBegin(GL2.GL_POINTS);

        gl.glColor3d(0, 0, 0);

        double tIncrement = 1.0/numPoints;

        for(int i = 0; i < numPoints; i++){        		
        	double t = i*tIncrement;      		
        	gl.glVertex2dv(curve.point(t),0);
        }
        //Connect to the final point - we just get the final control 
        //point as getting the the road.point at road.size() gives us
        //an error
        gl.glVertex2dv(curve.controlPoint(3),0);

        gl.glEnd();

  	 
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
		 case KeyEvent.VK_R:

			 restoreControlPoints();
			 break;
		 case KeyEvent.VK_SPACE:			
			 currentPoint++;
			 currentPoint = currentPoint%4;
			 System.out.println(currentPoint);
			 break;
		 case KeyEvent.VK_LEFT:
			 controlPoints[currentPoint*2]-=0.1;
			 break;

		 case KeyEvent.VK_RIGHT:
			 controlPoints[currentPoint*2]+=0.1;
			 break;

		 case KeyEvent.VK_DOWN:
			 controlPoints[currentPoint*2+1]-=0.1;
			 break;

		 case KeyEvent.VK_UP:
			 controlPoints[currentPoint*2+1]+=0.1;
			 break;
		 case KeyEvent.VK_L:			
			 lineMode = !lineMode;
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
