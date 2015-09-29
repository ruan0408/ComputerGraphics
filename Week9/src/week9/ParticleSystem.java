package week9;


import java.awt.event.*;

import java.util.Random;

import javax.swing.*;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLException;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLCanvas;
import javax.media.opengl.awt.GLJPanel;
import javax.media.opengl.glu.GLU;
import com.jogamp.opengl.util.FPSAnimator;


import static java.awt.event.KeyEvent.VK_T;
import static javax.media.opengl.GL.*;  // GL constants
import static javax.media.opengl.GL2.*; // GL2 constants


/**
 * Taken from NeHe Lesson #19a: Fireworks
 */
public class ParticleSystem implements GLEventListener, KeyListener {
   
   //private GLU glu;  // for the GL Utility
   
   private static final int MAX_PARTICLES = 1000; // max number of particles
   private Particle[] particles = new Particle[MAX_PARTICLES];

   private static boolean enabledBurst = false;
   
   // Pull forces in each direction
   private static float gravityY = -0.0008f; // gravity

   // Global speed for all the particles
   private static float speedYGlobal = 0.1f;
   private static float z = -40.0f; //zOffset
   private static float y = 5.0f;   //yOffset

   // Texture applied over the shape
   private MyTexture myTextures[];
   private String textureFileName = "src/week9/star.bmp";
   private String textureExt = ".bmp";

   
   
   /** The entry main() method */
   public static void main(String[] args) {
	   // initialisation
       GLProfile glp = GLProfile.getDefault();
       GLCapabilities caps = new GLCapabilities(glp);
       
       // create a panel to draw on
       GLCanvas panel = new GLCanvas(caps);
       //GLJPanel panel = new GLJPanel(caps);
       
       // put it in a JFrame
       final JFrame jframe = new JFrame("Particle System");
       jframe.setSize(600, 600);
       jframe.add(panel);
       jframe.setVisible(true);

       // Catch window closing events and quit             
       jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

       ParticleSystem s = new ParticleSystem();
       
       // add a GL Event listener to handle rendering
       panel.addGLEventListener(s);
       panel.addKeyListener(s);
       panel.setFocusable(true);   
       
       // NEW: add an animator to create display events at 60 FPS
       FPSAnimator animator = new FPSAnimator(60,true);
       animator.add(panel);
       animator.start();
   }
   
 

  
   @Override
   public void init(GLAutoDrawable drawable) {
      GL2 gl = drawable.getGL().getGL2();    
                  
      gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f); 
  
      gl.glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST); 
      

      // Enable Blending 
      gl.glEnable(GL_BLEND);      
      //Creates an additive blend, which looks spectacular on a black background
      gl.glBlendFunc(GL_SRC_ALPHA, GL_ONE);
     
      
      //Load the texture image
      try {
     	 myTextures = new MyTexture[1];
      	 myTextures[0] = new MyTexture(gl,textureFileName,textureExt,false);
      	//gl.glTexEnvi(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE, GL2.GL_REPLACE);
      } catch (GLException e) {
         e.printStackTrace();
      } 
     
      gl.glEnable(GL_TEXTURE_2D);
        
      // Initialize the particles
      for (int i = 0; i < MAX_PARTICLES; i++) {
         particles[i] = new Particle();
      }
   }

   /**
    * Call-back handler for window re-size event. Also called when the drawable is 
    * first set to visible.
    */
   @Override
   public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
      GL2 gl = drawable.getGL().getGL2();  // get the OpenGL 2 graphics context

      if (height == 0) height = 1;   // prevent divide by zero
      float aspect = (float)width / height;

      // Setup perspective projection, with aspect ratio matches viewport
      gl.glMatrixMode(GL_PROJECTION);  // choose projection matrix
      gl.glLoadIdentity();             // reset projection matrix
      GLU glu = new GLU();
      glu.gluPerspective(45.0, aspect, 0.1, 100.0); // fovy, aspect, zNear, zFar      
      
   }

   
   @Override
   public void display(GLAutoDrawable drawable) {
      GL2 gl = drawable.getGL().getGL2();  // get the OpenGL 2 graphics context
      
     
      
      gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear color and depth buffers
     
      gl.glMatrixMode(GL_MODELVIEW);
      gl.glLoadIdentity();  

      // Render the particles
      for (int i = 0; i < MAX_PARTICLES; i++) {
         if (particles[i].active) {
            // Draw the particle using our RGB values, fade the particle based on it's life
        	
            gl.glColor4f(particles[i].r, particles[i].g, particles[i].b, particles[i].life);
         
         
            
            gl.glBindTexture(GL2.GL_TEXTURE_2D, myTextures[0].getTextureId()); 
        
            
            gl.glBegin(GL_TRIANGLE_STRIP); // build quad from a triangle strip

            float px = particles[i].x;
            float py = particles[i].y + y;
            float pz = particles[i].z + z;

            gl.glTexCoord2d(1, 1);
            gl.glVertex3f(px + 0.5f, py + 0.5f, pz); // Top Right
            gl.glTexCoord2d(0, 1);
            gl.glVertex3f(px - 0.5f, py + 0.5f, pz); // Top Left
            gl.glTexCoord2d(1, 0);
            gl.glVertex3f(px + 0.5f, py - 0.5f, pz); // Bottom Right
            gl.glTexCoord2d(0, 0);
            gl.glVertex3f(px - 0.5f, py - 0.5f, pz); // Bottom Left
            gl.glEnd();

            // Move the particle
            particles[i].x += particles[i].speedX;
            particles[i].y += particles[i].speedY;
            particles[i].z += particles[i].speedZ;

            //particles[i].life -= 0.01;
            
            // Apply the gravity force on y-axis
            particles[i].speedY += gravityY;
            
            if (enabledBurst) {
               particles[i].burst();
            }
         }
      }
      if (enabledBurst) enabledBurst = false;
   }

   /** 
    * Called back before the OpenGL context is destroyed. Release resource such as buffers. 
    */
   @Override
   public void dispose(GLAutoDrawable drawable) { }

   // Particle (inner class)
   class Particle {
      boolean active; // always active in this program
      float life;     // life time
      float fade;     // fading speed, which reduces the life time
      float r, g, b;  // color
      float x, y, z;  // position
      float speedX, speedY, speedZ; // speed in the direction

      private final float[][] colors = {    // rainbow of 12 colors
            { 1.0f, 0.5f, 0.5f }, { 1.0f, 0.75f, 0.5f }, { 1.0f, 1.0f, 0.5f },
            { 0.75f, 1.0f, 0.5f }, { 0.5f, 1.0f, 0.5f }, { 0.5f, 1.0f, 0.75f },
            { 0.5f, 1.0f, 1.0f }, { 0.5f, 0.75f, 1.0f }, { 0.5f, 0.5f, 1.0f },
            { 0.75f, 0.5f, 1.0f }, { 1.0f, 0.5f, 1.0f }, { 1.0f, 0.5f, 0.75f } };

      private Random rand = new Random();

      // Constructor
      public Particle() {
         active = true;
         burst();
      }

      public void burst() {
         life = 1.0f;

         // Set a random fade speed value between 0.003 and 0.103
         fade = rand.nextInt(100) / 1000.0f + 0.003f;

         // Set the initial position
         x = y = z = 0.0f;
         
         // Generate a random speed and direction in polar coordinate, then resolve
         // them into x and y.
         float maxSpeed = 0.1f;
         float speed = 0.02f + (rand.nextFloat() - 0.5f) * maxSpeed; 
         float angle = (float)Math.toRadians(rand.nextInt(360));

         speedX = speed * (float)Math.cos(angle);
         speedY = speed * (float)Math.sin(angle) + speedYGlobal;
         speedZ = (rand.nextFloat() - 0.5f) * maxSpeed;

         int colorIndex = (int)(((speed - 0.02f) + maxSpeed) / (maxSpeed * 2) * colors.length) % colors.length;
         // Pick a random color
         r = colors[colorIndex][0];
         g = colors[colorIndex][1];
         b = colors[colorIndex][2];
      }
   }

   @Override
   public void keyPressed(KeyEvent e) {
      switch (e.getKeyCode()) {
         case VK_T:
            if (!enabledBurst) enabledBurst = true;
            break;
      }
   }

   @Override
   public void keyReleased(KeyEvent e) {}

   @Override
   public void keyTyped(KeyEvent e) {}
}
