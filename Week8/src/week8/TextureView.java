package week8;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.List;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLProfile;
import javax.media.opengl.glu.GLU;
import javax.media.opengl.glu.GLUquadric;

import week4.SpaceShip;
import week7.modeling.extrusion.Polygon;

import com.jogamp.opengl.util.gl2.GLUT;

/**
 * 
 *
 * @author malcolmr
 */
public class TextureView implements GLEventListener, MouseMotionListener {

    private static final int ROTATION_SCALE = 1;
    private TexturesLightingExample myModel;
    private Point myMousePoint;
    private MyTexture myTextures[];

    // the camera angle
    private int myRotateCameraX = 0;
    private int myRotateCameraY = 0;

    // the light angle
    private int myRotateLightX = 0;
    private int myRotateLightY = 0;

   
    
    
    public TextureView(TexturesLightingExample model) {
        myModel = model;
    }

    @Override
    public void init(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();

        // enable depth testing
        gl.glEnable(GL.GL_DEPTH_TEST);

        // backface culling
        gl.glEnable(GL.GL_CULL_FACE);
        gl.glCullFace(GL.GL_BACK);
        
        // enable lighting, turn on light 0
        gl.glEnable(GL2.GL_LIGHTING);
        gl.glEnable(GL2.GL_LIGHT0);

        // normalise normals (!)
        // this is necessary to make lighting work properly
        gl.glEnable(GL2.GL_NORMALIZE);

        // enable texturing
        gl.glEnable(GL.GL_TEXTURE_2D);
        
        //Load textures
        myTextures = new MyTexture[myModel.getNumTextures()];
        for(int i=0; i < myModel.getNumTextures(); i++){
        	myTextures[i] = new MyTexture(gl,"src/week8/"+ myModel.getTexName(i), myModel.getTexExtension(i),true);
        }
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

        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();

        // set the lights
        
        setLighting(gl);

        // rotate the camera 
        
        gl.glRotated(myRotateCameraX, 1, 0, 0);
        gl.glRotated(myRotateCameraY, 0, 1, 0);
        
       
        // bind the texture
        gl.glBindTexture(GL.GL_TEXTURE_2D, myTextures[myModel.getTexId()].getTextureId());
        // use the texture to modulate diffuse and ambient lighting
        if(myModel.getModulate()){
        	gl.glTexEnvf(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE, GL2.GL_MODULATE);
        } else {
        	gl.glTexEnvf(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE, GL2.GL_REPLACE);
        }
        
        // draw the shape
        drawShape(gl);

    }

    private void drawShape(GL2 gl) {
        gl.glColor3d(0, 0, 0);

        GLUT glut = new GLUT();

        // Set the reflection coefficients all to 1
        // Normally these values would be tuned to get a particular appearance

        float[] rhoA = new float[] { 1.0f, 1.0f, 1.0f, 1.0f };
        float[] rhoD = new float[] { 1.0f, 1.0f, 1.0f, 1.0f };
        float[] rhoS = new float[] { 1.0f, 1.0f, 1.0f, 1.0f };

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
            gl.glFrontFace(GL2.GL_CW);
            glut.glutSolidTeapot(1);
            gl.glFrontFace(GL2.GL_CCW);
            break;

        case CUBE:
            drawCube(gl);
            break;

        case SPHERE:
        	GLU glu = new GLU();
            // the built in glut sphere does not have texture coordinates set
            //glut.glutSolidSphere(1, 20, 20);
            GLUquadric quadric = glu.gluNewQuadric();
            glu.gluQuadricTexture(quadric, true);
            glu.gluQuadricNormals(quadric, GLU.GLU_SMOOTH);
            glu.gluSphere(quadric, 1, 64, 64);
         
            break;

        case TORUS:
            // the built in glut torus does not have texture coordinates set
             glut.glutSolidTorus(0.5, 1.5, 20, 20);
        	
            break;

        case SPACESHIP:
        
            gl.glPushMatrix();
            gl.glScaled(0.5, 0.5, 0.5);
            SpaceShip.xwing(gl,32);
            gl.glPopMatrix();           
        }

    }

    private void drawCube(GL2 gl) {

        // bottom
        gl.glBegin(GL2.GL_POLYGON);
        {
            // set all four normals to the face normal
            gl.glNormal3d(0, -1, 0);            

            // set texture coordinates for each vertex
            gl.glTexCoord2d(0, 0);
            gl.glVertex3d(-1, -1, -1);
            gl.glTexCoord2d(1, 0);
            gl.glVertex3d(1, -1, -1);
            gl.glTexCoord2d(1, 1);
            gl.glVertex3d(1, -1, 1);
            gl.glTexCoord2d(0, 1);
            gl.glVertex3d(-1, -1, 1);
        }
        gl.glEnd();

        // top
        gl.glBegin(GL2.GL_POLYGON);
        {
            gl.glNormal3d(0, 1, 0);
            gl.glTexCoord2d(1, 1);
            gl.glVertex3d(1, 1, -1);
            gl.glTexCoord2d(0, 1);
            gl.glVertex3d(-1, 1, -1);
            gl.glTexCoord2d(0, 0);
            gl.glVertex3d(-1, 1, 1);
            gl.glTexCoord2d(1, 0);
            gl.glVertex3d(1, 1, 1);
        }
        gl.glEnd();

        // left
        gl.glBegin(GL2.GL_POLYGON);
        {
            gl.glNormal3d(-1, 0, 0);
            gl.glVertex3d(-1, 1, -1);
            gl.glVertex3d(-1, -1, -1);
            gl.glVertex3d(-1, -1, 1);
            gl.glVertex3d(-1, 1, 1);
        }
        gl.glEnd();

        // right
        gl.glBegin(GL2.GL_POLYGON);
        {
            gl.glNormal3d(1, 0, 0);
            gl.glVertex3d(1, -1, -1);
            gl.glVertex3d(1, 1, -1);
            gl.glVertex3d(1, 1, 1);
            gl.glVertex3d(1, -1, 1);
        }
        gl.glEnd();

        // back
        gl.glBegin(GL2.GL_POLYGON);
        {
            gl.glNormal3d(0, 0, -1);
            
            gl.glTexCoord2d(1,1);
            gl.glVertex3d(1, -1, -1);
            gl.glTexCoord2d(0,1);
            gl.glVertex3d(-1, -1, -1);
            gl.glTexCoord2d(0,0);
            gl.glVertex3d(-1, 1, -1);
            gl.glTexCoord2d(1,0);
            gl.glVertex3d(1, 1, -1);
        }
        gl.glEnd();

        // front
        gl.glBegin(GL2.GL_POLYGON);
        {
            gl.glNormal3d(0, 0, 1);
            gl.glTexCoord2d(0,0);
            gl.glVertex3d(-1, -1, 1);
            
            gl.glTexCoord2d(1,0);
            gl.glVertex3d(1, -1, 1);
            
            gl.glTexCoord2d(1,1);            
            gl.glVertex3d(1, 1, 1);
            
            gl.glTexCoord2d(0,1);
            gl.glVertex3d(-1, 1, 1);
        }
        gl.glEnd();

    }
    
    
    private void setLighting(GL2 gl) {
        gl.glShadeModel(myModel.isSmooth() ? GL2.GL_SMOOTH : GL2.GL_FLAT);

        // rotate the light
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glPushMatrix();
        gl.glRotated(myRotateLightX, 1, 0, 0);
        gl.glRotated(myRotateLightY, 0, 1, 0);

        float[] pos = new float[] { 0.0f, 0.0f, 4.0f, 1.0f };
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, pos, 0);
        gl.glPopMatrix();
        
        // set the intensities

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
        
        if(myModel.getSpecularSep()){
        	
        	gl.glLightModeli(GL2.GL_LIGHT_MODEL_COLOR_CONTROL, GL2.GL_SEPARATE_SPECULAR_COLOR);
        }else {
        	gl.glLightModeli(GL2.GL_LIGHT_MODEL_COLOR_CONTROL, GL2.GL_SINGLE_COLOR);
        }
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width,
            int height) {

        GL2 gl = drawable.getGL().getGL2();

        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();

        gl.glOrtho(-3, 3, -3, 3, -4, 4);

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
            if (e.getButton() == MouseEvent.MOUSE_FIRST || true) {
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
