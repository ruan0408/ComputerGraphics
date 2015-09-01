import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GL2ES2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;
import javax.swing.SwingUtilities;

//import week7.extrusion.Polygon;
//import week8.Shader;
//import week8.Shader.CompilationException;

import com.jogamp.opengl.util.gl2.GLUT;


/**
 * COMMENT: 
 *
 * @author 
 */
public class LightingView implements GLEventListener, MouseMotionListener{
	
    private static final int ROTATION_SCALE = 1;
    private LightExample myModel;
    private Point myMousePoint;
 
    // the camera angle
    private double myRotateCameraX = 0;
    private double myRotateCameraY = 0;

    // the light angle
    private double myRotateLightX = 0;
    private double myRotateLightY = 0;
   

    public LightingView(LightExample model) {
        myModel = model;
    }

    @Override
    public void init(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();

        // enable depth testing
        gl.glEnable(GL.GL_DEPTH_TEST);

        // enable lighting, turn on light 0
        gl.glEnable(GL2.GL_LIGHTING);
        gl.glEnable(GL2.GL_LIGHT0);

        // normalise normals (!)
        // this is necessary to make lighting work properly
        gl.glEnable(GL2.GL_NORMALIZE);

    }

    
    
    @Override
    public void dispose(GLAutoDrawable drawable) {
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();

        // clear the window and depth buffer
        gl.glClearColor(0, 0, 0, 1);
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

        if(myModel.getShading() == LightExample.Shading.FLAT){
       		gl.glShadeModel( GL2.GL_FLAT);
       	} else {
       		gl.glShadeModel( GL2.GL_SMOOTH);
       	}
        	
       
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();

        // set the lights
       
        setLighting(gl);
      
        
        // rotate the camera 
        //
        gl.glTranslated(0,0,-5);
        gl.glRotated(myRotateCameraX, 1, 0, 0);
        gl.glRotated(myRotateCameraY, 0, 1, 0);
       
       
       
        drawShape(gl);

    }

    private void drawShape(GL2 gl) {
        gl.glColor3d(0, 0, 0);

        GLUT glut = new GLUT();

        // Set the reflection coefficients all to 1
        // Normally these values would be tuned to get a particular appearance

        float[] rhoA = myModel.getMyAmbient();    //new float[] { 1.0f, 1.0f, 1.0f, 1.0f };
        float[] rhoD = myModel.getMyDiffuse();   //new float[] { 1.0f, 1.0f, 1.0f, 1.0f };
        float[] rhoS = myModel.getMySpecular();  //new float[] { 1.0f, 1.0f, 1.0f, 1.0f };

        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT, rhoA, 0);
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_DIFFUSE, rhoD, 0);
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, rhoS, 0);

        // Set the shininess (i.e. the Phong exponent)
        
        int phong = myModel.getPhong();
        gl.glMaterialf(GL2.GL_FRONT, GL2.GL_SHININESS, phong);

        // Draw the model
        
        gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL2.GL_FILL);
      
        switch (myModel.getModel()) {
        
        
        case TEAPOT:
            // the builtin teapot is back-to-front
            // https://developer.apple.com/library/mac/documentation/Darwin/Reference/ManPages/man3/glutSolidTeapot.3.html
        	//gl.glUniform3f(gl.glGetUniformLocation(shaderprogram,"color"),0.0f,1.0f,0.0f);
        	
           //gl.glEnable(GL2.GL_CULL_FACE);
           //gl.glCullFace(GL2.GL_BACK);
        	gl.glFrontFace(GL2.GL_CW);
            glut.glutSolidTeapot(1);
            gl.glFrontFace(GL2.GL_CCW);
            break;

        case CUBE:
            glut.glutSolidCube(1.5f);
            break;

        case SPHERE:
            glut.glutSolidSphere(1, 20, 20);
            break;

        case TORUS:
            glut.glutSolidTorus(0.5, 1, 20, 20);
            break;       

        }
    }

    private void setLighting(GL2 gl) {
    	

        // rotate the light
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        
        gl.glPushMatrix();
        gl.glLoadIdentity();
        gl.glRotated(myRotateLightX, 1, 0, 0);
        gl.glRotated(myRotateLightY, 0, 1, 0);
      
       
        float[] pos = new float[] { 0.0f, 0.0f, 7.0f, 1.0f };
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, pos, 0);
        gl.glPopMatrix();
        
        float ambient = myModel.getAmbient();
        float diffuse = myModel.getDiffuse();
        float specular = myModel.getSpecular();

        float[] a = new float[4];
        a[0] = a[1] = a[2] = ambient;
        a[3] = 1.0f;
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, a, 0);

        float[] d = new float[4];
        d[0] = d[1] = d[2] = diffuse;
        d[3] = 1.0f;
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, d, 0);

        float[] s = new float[4];
        s[0] = s[1] = s[2] = specular;
        s[3] = 1.0f;
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_SPECULAR, s, 0);
       
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width,
            int height) {

        GL2 gl = drawable.getGL().getGL2();

        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();

        
        GLU glu = new GLU();
        glu.gluPerspective(60.0, (float)width/(float)height, 1.0, 20.0);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        myModel.focus();
        myMousePoint = e.getPoint();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
       
    	myModel.focus();
        Point p = e.getPoint();
        
        if (myMousePoint != null) {
            int dx = p.x - myMousePoint.x;
            int dy = p.y - myMousePoint.y;

            // Note: dragging in the x dir rotates about y
            //       dragging in the y dir rotates about x
        
            if (SwingUtilities.isLeftMouseButton(e) ) {
                // button 1 moves the camera
                myRotateCameraY += dx * ROTATION_SCALE;
                myRotateCameraX += dy * ROTATION_SCALE;
            }
            else {
                // other buttons move the light
                myRotateLightY += dx * ROTATION_SCALE;
                myRotateLightX += dy * ROTATION_SCALE;
            }

        }
      
        myMousePoint = p;
    }

	

}
