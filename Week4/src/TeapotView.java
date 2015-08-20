import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;
import javax.media.opengl.glu.GLUquadric;



import com.jogamp.opengl.util.gl2.GLUT;

/**
 * COMMENT: Comment Teapot 
 *
 * @author malcolmr
 */
public class TeapotView implements GLEventListener {

    private Teapot myTeapot;
    private boolean myShowCamera;
    public enum View {FREE, BACKWARDS, DOWN, RIGHT};    
    final private View myView;

    public TeapotView(Teapot teapot, boolean showCamera, View view) {
        myTeapot = teapot;
        myShowCamera = showCamera;
        myView = view;        
    }

    @Override
    public void init(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        GLUT glut = new GLUT();

       
        // enable depth testing
        gl.glEnable(GL.GL_DEPTH_TEST);
        
        // enable lighting
        gl.glEnable(GL2.GL_LIGHTING);
        
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

        // clear both the colour buffer and the depth buffer
        gl.glClearColor(1, 1, 1, 1);       
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();

        if (myShowCamera) {
            switch (myView) {
            case FREE:
            case BACKWARDS:
                break;
                
            case DOWN:
                gl.glRotated(90, 1, 0, 0);
                break;
                
            case RIGHT:
                gl.glRotated(90, 0, 1, 0);
                break;
            }
            
            drawCamera(gl);
        }
        else {
            // apply view transform
            double scale = myTeapot.getScale();
            gl.glTranslated(0, 0, -myTeapot.getDistance());
            gl.glScaled(scale, scale, scale);
            gl.glRotated(myTeapot.getRotateX(), 1, 0, 0);
            gl.glRotated(myTeapot.getRotateY(), 0, 1, 0);
        }        

        drawShape(gl);
        
    }

    private void drawShape(GL2 gl) {
        gl.glColor3d(0, 0, 0);

        GLUT glut = new GLUT();

        if (myTeapot.isLit()) {
            // turn on a light
            gl.glEnable(GL2.GL_LIGHT0);
        }
        else {
            gl.glDisable(GL2.GL_LIGHT0);   
        }

        if (myTeapot.isWireframe()) {
            gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL2.GL_LINE);
        }
        else {
            gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL2.GL_FILL);            
        }
        
        gl.glShadeModel(myTeapot.isSmooth() ? GL2.GL_SMOOTH : GL2.GL_FLAT);
        
        switch (myTeapot.getType()) {

        case TEAPOT:
            if (myTeapot.isWireframe()) {
                glut.glutWireTeapot(1);
            }
            else {
                glut.glutSolidTeapot(1);
            }
            break;

        case CUBE:
            if (myTeapot.isWireframe()) {
                glut.glutWireCube(1);
            }
            else {
                glut.glutSolidCube(1);
            }
            break;

        case SPHERE:
            if (myTeapot.isWireframe()) {
                glut.glutWireSphere(1, 20, 20);
            }
            else {
                glut.glutSolidSphere(1, 20, 20);
            }
            break;

        case TORUS:
            if (myTeapot.isWireframe()) {
                glut.glutWireTorus(0.5, 1.5, 20, 20);
            }
            else {
                glut.glutSolidTorus(0.5, 1.5, 20, 20);
            }
            break;
            
        case TETRAHEDRON:
            if (myTeapot.isWireframe()) {
                glut.glutWireTetrahedron();
            }
            else {
                glut.glutSolidTetrahedron();
            }
            break;
            
        case ICOSAHEDRON:
            if (myTeapot.isWireframe()) {
                glut.glutWireIcosahedron();
            }
            else {
                glut.glutSolidIcosahedron();
            }
            break;
            
        case CYLINDER:
        	GLU glu = new GLU();
        	GLUquadric quadric = glu.gluNewQuadric();
        			
           
            if (myTeapot.isWireframe()) {
            	
                glu.gluQuadricDrawStyle(quadric,GLU.GLU_LINE);
            }
          
            glu.gluCylinder(quadric, 0.5, 0.5, 2, 10, 10);
            break;
            
        case CONE:
            if (myTeapot.isWireframe()) {
                glut.glutWireCone(1, 1.5, 20, 20);
            }
            else {
                glut.glutSolidCone(1, 1.5, 20, 20);
            }
            break;

        case SPACESHIP:
        	
        	
            if (myTeapot.isWireframe()) {
            	 gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_LINE);
           	 
                
            }
            else {
            	 gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);
                
            }
            gl.glPushMatrix();
            gl.glScaled(0.5, 0.5, 0.5);
            SpaceShip.xwing(gl,32);
            gl.glPopMatrix();           
        }
        	//bug fixes
        gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL2.GL_FILL);
    }

    private void drawCamera(GL2 gl) {
        

        // don't light the gizmo
        gl.glDisable(GL2.GL_LIGHT0);

        // rotate the gizmo the inverse of the view transform
        gl.glPushMatrix();
        gl.glRotated(-myTeapot.getRotateX(), 1, 0, 0);
        gl.glRotated(-myTeapot.getRotateY(), 0, 1, 0);
        double scale = 1 / myTeapot.getScale();
        gl.glScaled(scale, scale, scale);
        gl.glTranslated(0, 0, myTeapot.getDistance());


        GLUT glut = new GLUT();
        // draw wireframe
        gl.glColor3d(0, 0, 0);
        gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_LINE);       

        if (myTeapot.isPerspective()) {
            // perspective view volume

            double d = 2.0 * Math.tan(Math.toRadians(myTeapot.getFOV()) / 2);
            double d2 = 4.0 * Math.tan(Math.toRadians(myTeapot.getFOV()) / 2);

            // draw the frustum
            gl.glBegin(GL2.GL_QUAD_STRIP);
            {
                gl.glVertex3d(d, d, -2);
                gl.glVertex3d(-d, d, -2);

                gl.glVertex3d(d, -d, -2);
                gl.glVertex3d(-d, -d, -2);

                gl.glVertex3d(d2, -d2, -4);
                gl.glVertex3d(-d2, -d2, -4);

                gl.glVertex3d(d2, d2, -4);
                gl.glVertex3d(-d2, d2, -4);

                gl.glVertex3d(d, d, -2);
                gl.glVertex3d(-d, d, -2);

            }
            gl.glEnd();
            
        }
        else {
            // orthographic view volume 
            gl.glTranslated(0, 0, -1);
            glut.glutWireCube(2);
        }
        
        gl.glPopMatrix();

        
    }
    
    /**
     * @see javax.media.opengl.GLEventListener#reshape(javax.media.opengl.GLAutoDrawable, int, int, int, int)
     */
    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width,
            int height) {

        GL2 gl = drawable.getGL().getGL2();
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();

        if (myShowCamera) {
            // a bigger view so we can see the camera
            gl.glOrtho(-4, 4, -4, 4, -4, 4);
        }
        else {
            if (myTeapot.isPerspective()) {
                GLU glu = new GLU();
                // field of view, aspect, near, far                
                glu.gluPerspective(myTeapot.getFOV(), 1.0, 2, 4);
            }
            else {   
                gl.glOrtho(-1, 1, -1, 1, 0, 2);
            }
        }

    }

}
