package week8.advanced;


import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;

import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLJPanel;
import javax.media.opengl.glu.GLU;

import javax.swing.JFrame;

import com.jogamp.opengl.util.FPSAnimator;

import com.jogamp.opengl.util.gl2.GLUT;


/**
 * Guha, Sumanta (2014-08-06). Computer Graphics Through OpenGL: From Theory to Experiments, Second Edition 
 * Experimenter code.
 *
 * Adapted to jogl and modified by Angela Finlayson 
 * 
 * Shadow Mapping. This example does calculations assuming the light is like a spotlight.
 */


public class ShadowMapping implements GLEventListener, KeyListener {

	// Begin globals. 
	private double latAngle = 0.0; // Latitudinal angle.
	private double longAngle = 0.0; // Longitudinal angle.
	private boolean isAnimate = false; // Animated?
	

	private int winWth, winHgt; // OpenGL window sizes.

	private int[] shadowMapTex = new int[1]; // Shadow map texture.
	private int shadowMapTexWth = 512; // Shadow map texture width.
	private int shadowMapTexHgt = 512; // Shadow map texture height.

	// Light properties.
	
	private double lightFovy  = 120.0; // gluPerspective from light: field of view angle.
	private double lightAspect  = 1.0; // gluPerspective from light: aspect ratio.
	private double lightNearPlane  = 1.0; // gluPerspective from light: near plane.
	private double lightFarPlane  = 35.0; // gluPerspective from light: far plane.
	private float lightPos[] = { 0.0f, 30.0f, 0.0f }; // Light position.
	private float lightLookAt[]  = { 0.0f, 0.0f, 0.0f }; // gluLookAt from light: point looked at (i.e., center).
	private float lightUp[]  = { 1.0f, 0.0f, 0.0f }; // gluLookAt from light: up direction.
	private float lightAmb[] = { 0.0f, 0.0f, 0.0f, 1.0f }; // Light ambient values.
	private float lightDifAndSpec[] = { 1.0f, 1.0f, 1.0f, 1.0f }; // Light diffuse and specular values.
	private float globAmb[] = { 0.3f, 0.3f, 0.3f, 1.0f }; // Global ambient values.

	// Camera properties.
	private double cameraFovy  = 90.0; // gluPerspective from camera: field of view angle.
	private double cameraNearPlane  = 5.0; // gluPerspective from camera: near plane.
	private double cameraFarPlane  = 100.0; // gluPerspective from camera: far plane.
	private double cameraPos[]  = { 0.0, 36.0, 7.0 }; // Camera position.
	private double cameraLookAt[]  = { 0.0, 0.0, 0.0 }; // gluLookAt from camera: point looked at (i.e., center).
	private double cameraUp[]  = { 0.0, 1.0, 0.0 }; // gluLookAt from camear: up direction.

	// Material properties.
	float matSpec[] = { 1.0f, 1.0f, 1.0f, 1.0f }; // Material specular values.
	float matShine[] = { 50.0f }; // Material shininess.

	// Matrices (4x4 matrices each written in a 1-dim array in column-major order).
	private float lightProjMat[] = new float[16]; // Light's projection transformation matrix.
	private float lightViewMat[] = new float[16]; // Light's viewing transformation matrix.  
	private float cameraProjMat[] = new float[16]; // Camera's projection transformation matrix.  
	private float cameraViewMat[] = new float[16]; // Camera's viewing transformation matrix.  
	private float texMat[] = new float[16]; // Texture matrix.

	
	
	// Draw ball flying around torus.
	void drawFlyingBallAndTorus(GL2 gl)
	{
	   GLUT glut = new GLUT();
	   gl.glShadeModel(GL2.GL_SMOOTH); 

	   gl.glPushMatrix();{

		   gl.glTranslated(0.0, 10.0, 0.0);
		   gl.glRotated(90.0, 1.0, 0.0, 0.0);

		   // Fixed torus.
		   gl.glColor3d(0.0, 1.0, 0.0);
		   glut.glutSolidTorus(2.0, 12.0, 80, 80);

		   // Begin revolving ball.
		   gl.glRotated(longAngle, 0.0, 0.0, 1.0);

		   gl.glTranslated(12.0, 0.0, 0.0);
		   gl.glRotated(latAngle, 0.0, 1.0, 0.0);
		   gl.glTranslated(-12.0, 0.0, 0.0);

		   gl.glTranslated(20.0, 0.0, 0.0);

		   gl.glColor3d(0.0, 0.0, 1.0);
		   glut.glutSolidSphere(2.0, 20, 20);
		   // End revolving ball.

	   }gl.glPopMatrix();
	}
	
	
   
    public static void main(String[] args) {
        // initialisation
        GLProfile glp = GLProfile.getDefault();
        GLCapabilities caps = new GLCapabilities(glp);
        
        // create a panel to draw on
        GLJPanel panel = new GLJPanel(caps);

        // put it in a JFrame
        final JFrame jframe = new JFrame("Shadow Mapping");
        jframe.setSize(800, 800);
        jframe.add(panel);
        jframe.setVisible(true);

        // Catch window closing events and quit             
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        ShadowMapping s = new ShadowMapping();
        
        // add a GL Event listener to handle rendering
        panel.addGLEventListener(s);
        panel.addKeyListener(s);
        panel.setFocusable(true);   
        
        // NEW: add an animator to create display events at 60 FPS
        FPSAnimator animator = new FPSAnimator(60);
        animator.add(panel);
        animator.start();
    } 
    
    // Draw checkered floor.
    void drawCheckeredFloor(GL2 gl)
    {  
       gl.glShadeModel(GL2.GL_FLAT); // Flat shading to get the checkered pattern.
       int i = 0;

       gl.glPushMatrix();

       for(double z = 50.0; z > -50.0; z -= 5.0)
       {
          gl.glBegin(GL2.GL_TRIANGLE_STRIP);
          for(double x = -50.0; x < 50.0; x += 5.0)
    	  {
             if (i % 2 == 0) gl.glColor3d(0.0, 0.5, 0.5);  
             else gl.glColor3d(1.0, 1.0, 1.0); 
             gl.glNormal3d(0.0, 1.0, 0.0);
    		 gl.glVertex3d(x, 0.0, z - 5.0);
    	     gl.glVertex3d(x, 0.0, z);
    		 i++;
    	  }
          gl.glEnd();
    	  i++;
       }

       gl.glPopMatrix();
    }
    
    
    // Timer function.
    void update(boolean value)
    {
       if (isAnimate) 
       {
          latAngle += 5.0;
    	  if (latAngle > 360.0) latAngle -= 360.0;
          longAngle += 1.0;
    	  if (longAngle > 360.0) longAngle -= 360.0;
       }
    }
    
    @Override
    public void display(GLAutoDrawable drawable) {
    	  
        GL2 gl = drawable.getGL().getGL2();
    	
        // FIRST PASS: Draw the scene from the light's viewpoint
        // and capture the z-buffer in the shadow map texture.
        
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);	

        // Load the light's projection transformation matrix.
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadMatrixf(lightProjMat,0);

        // Load the light's viewing transformation matrix.
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadMatrixf(lightViewMat,0);

        // Create viewport same size as the shadow map texture.
        gl.glViewport(0, 0, shadowMapTexWth, shadowMapTexHgt);

        // Disable writes to the color buffer so that the scene is not rendered to the window.
        gl.glColorMask(false,false,false,false);
     	
        // Cull front faces so that only depths of back faces are written to the z-buffer
        // (so that front faces, which should be visible, win the depth competition in their corresponding pixels).
        // This is to stop precision problems but only works for closed objects.
        gl.glCullFace(GL2.GL_FRONT);

        // Draw the scene.
        drawCheckeredFloor(gl);
        drawFlyingBallAndTorus(gl);

        // Capture the depth buffer in the shadow map texture.
        gl.glBindTexture(GL2.GL_TEXTURE_2D, shadowMapTex[0]);
        gl.glCopyTexImage2D(GL2.GL_TEXTURE_2D, 0, GL2.GL_DEPTH_COMPONENT, 0, 0, shadowMapTexWth, shadowMapTexHgt, 0 );

        // SECOND PASS: Draw the scene from the camera's viewpoint using only
        // global ambient (dim) light, so the whole scene is drawn shadowed.

        gl.glClear(GL2.GL_DEPTH_BUFFER_BIT);

        // Load the camera's projection transformation matrix.
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadMatrixf(cameraProjMat,0);
     	
        // Load the camera's viewing transformation matrix.
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadMatrixf(cameraViewMat,0);

        // Create viewport same size as the OpenGL window.
        gl.glViewport(0, 0, winWth, winHgt);

        // Cull back faces (not visible to the camera).
        gl.glCullFace(GL2.GL_BACK);

        // Allow the scene to be rendered.
        gl.glColorMask(true, true, true, true);

        // Enable lighting. This enables ONLY global ambient light as LIGHT0 is NOT enabled.
        //However, in order that the curved surfaces in shadow do not appear unnaturally flat, 
        // we could use a dim diffuse light source. 
        gl.glEnable(GL2.GL_LIGHTING);

        // Draw the scene.
         drawCheckeredFloor(gl);
         drawFlyingBallAndTorus(gl);
        
        // THIRD PASS: Draw the scene from the camera's viewpoint, 
        // but draw only non-shadowed parts and use bright light.
        
        // LIGHT0 throws a bright light.
        gl.glEnable(GL2.GL_LIGHT0);
        
        // Compute and save the texture matrix.
        // Computations are done in the modelview matrix stack.
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glPushMatrix();
        gl.glLoadIdentity();

        // These two transformations generate the bias matrix B.
        gl.glTranslated(0.5, 0.5, 0.5);
        gl.glScaled(0.5, 0.5, 0.5);
        
        gl.glMultMatrixf(lightProjMat,0); // B x P_light
        gl.glMultMatrixf(lightViewMat,0); // B x P_light x V_light
        
        // Save the current modelview matrix.
        gl.glGetFloatv(GL2.GL_MODELVIEW_MATRIX, texMat,0);  
        
        gl.glPopMatrix();

        // List the rows of the texture matrix noting that it is stored in texMat 
        // in column-major order.
        float texMatRow0[] = { texMat[0], texMat[4], texMat[8], texMat[12] };
        float texMatRow1[] = { texMat[1], texMat[5], texMat[9], texMat[13] };
        float texMatRow2[] = { texMat[2], texMat[6], texMat[10], texMat[14] };
        float texMatRow3[] = { texMat[3], texMat[7], texMat[11], texMat[15] };

        // Generate texture coordinates equal to 
        // B x P_light x V_light x (V_camera)^{-1) x vertex
        gl.glEnable(GL2.GL_TEXTURE_GEN_S);
        gl.glTexGeni(GL2.GL_S, GL2.GL_TEXTURE_GEN_MODE, GL2.GL_EYE_LINEAR);
        gl.glTexGenfv(GL2.GL_S, GL2.GL_EYE_PLANE, texMatRow0,0);

        gl.glEnable(GL2.GL_TEXTURE_GEN_T);
        gl.glTexGeni(GL2.GL_T, GL2.GL_TEXTURE_GEN_MODE, GL2.GL_EYE_LINEAR);
        gl.glTexGenfv(GL2.GL_T, GL2.GL_EYE_PLANE, texMatRow1,0);

        gl.glEnable(GL2.GL_TEXTURE_GEN_R);
        gl.glTexGeni(GL2.GL_R, GL2.GL_TEXTURE_GEN_MODE, GL2.GL_EYE_LINEAR);
        gl.glTexGenfv(GL2.GL_R, GL2.GL_EYE_PLANE, texMatRow2,0);

        gl.glEnable(GL2.GL_TEXTURE_GEN_Q);
        gl.glTexGeni(GL2.GL_Q, GL2.GL_TEXTURE_GEN_MODE, GL2.GL_EYE_LINEAR);
        gl.glTexGenfv(GL2.GL_Q, GL2.GL_EYE_PLANE, texMatRow3,0);

        // Activate the shadow map texture.
        gl.glBindTexture(GL2.GL_TEXTURE_2D, shadowMapTex[0]);

        // Compare the r-coordinate to the corresponding value stored in the shadow map texture.
        gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_COMPARE_MODE, GL2.GL_COMPARE_R_TO_TEXTURE);

        // Comparison should be true if r <= texture, i.e., if the fragment is not shadowed.
        gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_COMPARE_FUNC, GL2.GL_LEQUAL);

        // Set shadow comparison to generate an intensity value 
        // (0 for failed comparisons, 1 for successful).
        gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_DEPTH_TEXTURE_MODE, GL2.GL_INTENSITY);

        //Any fragments which "fail" the comparison (R > D) will generate an alpha value of 0, 
        //and any which pass will have alpha of 1. By using the alpha test, 
        //we can discard any fragments which should be shadowed. 
        // Set alpha test to discard failed comparisons, i.e., shadowed fragments.
        gl.glEnable(GL2.GL_ALPHA_TEST);
        gl.glAlphaFunc(GL2.GL_GREATER, 0.5f);

        // Draw the scene.
        drawCheckeredFloor(gl);
        drawFlyingBallAndTorus(gl);
        
        // Restore states.
        gl.glDisable(GL2.GL_TEXTURE_GEN_S);
        gl.glDisable(GL2.GL_TEXTURE_GEN_T);
        gl.glDisable(GL2.GL_TEXTURE_GEN_R);
        gl.glDisable(GL2.GL_TEXTURE_GEN_Q);
        gl.glDisable(GL2.GL_LIGHTING);
        gl.glDisable(GL2.GL_LIGHT0);
        gl.glDisable(GL2.GL_ALPHA_TEST);
     	
        // FOURTH PASS: Draw a red sphere at the light's position. 
        gl.glTranslatef(lightPos[0], lightPos[1], lightPos[2]);
        gl.glColor3d(1.0, 0.0, 0.0);
        GLUT glut = new GLUT();
        glut.glutWireSphere(0.2, 10, 10);    	
      
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
    }

   
    
    @Override
    public void init(GLAutoDrawable drawable) {
    	GL2 gl = drawable.getGL().getGL2();
    	
    	gl.glMatrixMode(GL2.GL_MODELVIEW);
    	gl.glLoadIdentity();

    	gl.glClearColor(1.0f, 1.0f, 1.0f, 0.0f); 

    	// Need to set the depth test to <=, rather than the default <, so that the brightly lit
    	// regions in the third drawing pass can overwrite their dark versions from the second.
    	gl.glDepthFunc(GL2.GL_LEQUAL);
    	gl.glEnable(GL2.GL_DEPTH_TEST);

    	//Create the shadow map (depth) texture.
    	gl.glGenTextures(1, shadowMapTex,0);
    	gl.glBindTexture(GL2.GL_TEXTURE_2D, shadowMapTex[0]);
    	gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_S, GL2.GL_CLAMP);
    	gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_T, GL2.GL_CLAMP);
    	gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_NEAREST);
    	gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_NEAREST);
    	gl.glTexImage2D(GL2.GL_TEXTURE_2D, 0, GL2.GL_DEPTH_COMPONENT, shadowMapTexWth, shadowMapTexHgt, 0,
    			GL2.GL_DEPTH_COMPONENT, GL2.GL_UNSIGNED_BYTE, null);

    	// Local light source properties.
    	gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, lightAmb,0);
    	gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, lightDifAndSpec,0);
    	gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_SPECULAR, lightDifAndSpec,0);
    	gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, lightPos,0);

    	// Global ambient light.
    	gl.glLightModelfv(GL2.GL_LIGHT_MODEL_AMBIENT, globAmb,0); 

    	// Material properties.
    	gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, matSpec,0);
    	gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SHININESS, matShine,0);

    	// Enable color material mode.
    	gl.glEnable(GL2.GL_COLOR_MATERIAL); 
    	gl.glColorMaterial(GL2.GL_FRONT, GL2.GL_AMBIENT_AND_DIFFUSE); 

    	// Enable texturing.
    	gl.glEnable(GL2.GL_TEXTURE_2D);

    	// Enable face culling.
    	gl.glEnable(GL2.GL_CULL_FACE);

    	// Compute and save matrices.
    	// All computations, even projection matrix, are done in the modelview matrix stack.
    	// We are just using it as a matrix calculator :)
    	gl.glPushMatrix();

    	// Camera's projection transformation matrix.  
    	gl.glLoadIdentity();
    	GLU glu = new GLU();
    	glu.gluPerspective(cameraFovy, (float)winWth/winHgt, cameraNearPlane, cameraFarPlane);
    	gl.glGetFloatv(GL2.GL_MODELVIEW_MATRIX, cameraProjMat,0);

    	// Camera's viewing transformation matrix.  
    	gl.glLoadIdentity();
    	glu.gluLookAt(cameraPos[0], cameraPos[1], cameraPos[2], cameraLookAt[0], cameraLookAt[1], cameraLookAt[2],
    			cameraUp[0], cameraUp[1], cameraUp[2]);
    	gl.glGetFloatv(GL2.GL_MODELVIEW_MATRIX, cameraViewMat,0);

    	// Light's projection transformation matrix.  
    	gl.glLoadIdentity();
    	glu.gluPerspective(lightFovy, lightAspect, lightNearPlane, lightFarPlane);
    	gl.glGetFloatv(GL2.GL_MODELVIEW_MATRIX, lightProjMat,0);

    	// Light's viewing transformation matrix.  
    	gl.glLoadIdentity();
    	glu.gluLookAt(lightPos[0], lightPos[1], lightPos[2], lightLookAt[0], lightLookAt[1], lightLookAt[2],
    			lightUp[0], lightUp[1], lightUp[2]);
    	gl.glGetFloatv(GL2.GL_MODELVIEW_MATRIX, lightViewMat,0);

    	gl.glPopMatrix();    

    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int w, int h) {
   
    	GL2 gl = drawable.getGL().getGL2();
   
         // Update the new window dimensions.
         winWth = w; 
         winHgt=h;

         // Update the camera's projection matrix by recomputing it (using the modelview matrix stack).
         gl.glPushMatrix();
         gl.glLoadIdentity();
         GLU glu = new GLU();
         glu.gluPerspective(cameraFovy, (float)winWth/winHgt, cameraNearPlane, cameraFarPlane);
         gl.glGetFloatv(GL2.GL_MODELVIEW_MATRIX, cameraProjMat,0);
         gl.glPopMatrix();
       
    }

    @Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		 switch (e.getKeyCode()) {
		 case KeyEvent.VK_SPACE:
		
			   if (isAnimate) isAnimate = false;
				 else 
				 {
			        isAnimate = true; 
					update(true);
				 }
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
