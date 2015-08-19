import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.media.opengl.*;
import javax.media.opengl.awt.GLJPanel;
import javax.media.opengl.glu.GLU;

import com.jogamp.opengl.util.FPSAnimator;



/**
 * A 2D OpenGL animation that demonstrates the use of glPushMatrix and glPopMatrix to
 * implement hierarchical modeling.  
 */


public class Sun implements GLEventListener {
  
 
   //This program would be better off restructured as an oo scene graph type approach
   //where each sun would have its own pos, theta and scale.
   
   private double pos = -4;  //The position of one of the suns
   private double theta = 0; //The angle of one of the suns.
	
   public static void main(String[] args) {
       // Initialise OpenGL
       GLProfile glp = GLProfile.getDefault();
       GLCapabilities caps = new GLCapabilities(glp);

       // Create a panel to draw on
       GLJPanel panel = new GLJPanel(caps);

       // Put it in a window
       final JFrame jframe = new JFrame("House Transformations");
       jframe.setSize(400, 400);
       jframe.add(panel);
       jframe.setVisible(true);
     
       jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    
       // add a GL Event listener to handle rendering
       panel.addGLEventListener(new Sun());
       
       // NEW: add an animator to create display events at 60 FPS
       FPSAnimator animator = new FPSAnimator(60);
       animator.add(panel);
       animator.start();
   }

   /**
    * This method is called when the GLJPanel is created.  It initializes
    * the GL context.  Here, it sets the clear color to be sky blue.
    */
   public void init(GLAutoDrawable drawable) {
      GL2 gl = drawable.getGL().getGL2();
      gl.glClearColor(0.6f, 0.9f, 1f, 1);
      
   }
   
   /**
    * This method is called when the GLJPanel needs to be redrawn.
    * It draws the current frame of the animation.
    */
   public void display(GLAutoDrawable drawable) {
      GL2 gl = drawable.getGL().getGL2();
      gl.glClear(GL.GL_COLOR_BUFFER_BIT); // Fills the scene with blue.

      gl.glMatrixMode(GL2.GL_MODELVIEW);
      gl.glLoadIdentity();
      update(); //Update the models
     
      draw(gl); //Draw the models
   
   }
   
   public void draw(GL2 gl){
      drawSun(gl);
	   
      gl.glPushMatrix();
          gl.glTranslated(2,2.5,0);
          //This suns angle depends on the value of theta
          gl.glRotated(theta,0,0,1);
          drawSun(gl);
      gl.glPopMatrix();
           
     
      gl.glPushMatrix(); 
         //This suns position depends on the value of pos
         gl.glTranslated(pos,-3,0);
         gl.glScaled(0.5, 0.5, 1);
         drawSun(gl);
      gl.glPopMatrix();
   }

  
   
   /**
    * Draw a sun with radius 0.5 centered at (0,0).  There are also 13 rays which
    * extend outside from the sun for another 0.25 units.
    */
   private void drawSun(GL2 gl) {
      gl.glColor3f(1,1,0);
      gl.glPushMatrix(); 
      for (int i = 0; i < 13; i++) { // Draw 13 rays, with different rotations.
         gl.glRotatef( 360f / 13, 0, 0, 1 ); // Note that the rotations accumulate!
         drawLine(gl,0.75);
      }
      gl.glPopMatrix();
      drawCircle(gl, 0.5);      
   }
   
   /**
    * Draw a 32-sided regular polygon as an approximation for a circular circle.
    * 
    */
   private void drawCircle(GL2 gl, double radius) {
    
	   	 gl.glBegin(GL2.GL_TRIANGLE_FAN);
	  		  gl.glVertex2d(0, 0); //The centre of the circle
              for (int d = 0; d <= 32; d++) {
                  double angle = 2*Math.PI/32 * d;
                  gl.glVertex2d( radius*Math.cos(angle), radius*Math.sin(angle));
              }
          gl.glEnd();    
   }
   
   //Draw a line from 0,0 to size
   private void drawLine(GL2 gl, double size) {
       gl.glBegin(GL2.GL_LINES);{
    	   gl.glVertex2d(0,0);
    	   gl.glVertex2d(size,0);
       }
       gl.glEnd();
   }

   //Update the model of the suns positions and rotations
   private void update() {	      
      pos = pos + 0.01;
      if(pos > 5) pos = -4;
      theta = theta + 0.5;
   }

   public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
	      GL2 gl = drawable.getGL().getGL2();
	      // The next three lines set up the coordinates system.
	      gl.glMatrixMode(GL2.GL_PROJECTION);
	      gl.glLoadIdentity();
	      GLU glu = new GLU();
	      glu.gluOrtho2D(-4, 4, -4, 4);
   }

  

   @Override
   public void dispose(GLAutoDrawable arg0) {
	   // TODO Auto-generated method stub

   }
}
