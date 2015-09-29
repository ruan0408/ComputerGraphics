package week7.modeling;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLJPanel;
import javax.media.opengl.glu.GLU;
import javax.swing.JFrame;

import com.jogamp.opengl.util.FPSAnimator;

//Create a cone by making a circle out of triangles and then changing
//the z coordinate of the middle point
public class LitCone implements GLEventListener, MouseMotionListener{
	private double rotateX = 0;
    private double rotateY = 0;
    private Point myMousePoint = null;
    private static final int ROTATION_SCALE = 1;
    private static final int NUM_SLICES = 64;
	
    private double height = 2;
    private double radius = 0.5;



    public static void main(String[] args) {
        // initialisation
        GLProfile glp = GLProfile.getDefault();
        GLCapabilities caps = new GLCapabilities(glp);
        
        // create a panel to draw on
        GLJPanel panel = new GLJPanel(caps);

        // put it in a JFrame
        final JFrame jframe = new JFrame("Cylinder");
        jframe.setSize(800, 800);
        jframe.add(panel);
        jframe.setVisible(true);

        // Catch window closing events and quit             
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        LitCone s = new LitCone();
        
        // add a GL Event listener to handle rendering
        panel.addGLEventListener(s);
        panel.addMouseMotionListener(s);
        panel.setFocusable(true);   
        
        // NEW: add an animator to create display events at 60 FPS
        FPSAnimator animator = new FPSAnimator(60);
        animator.add(panel);
        animator.start();
    }   
    
    public void normalize(double v[])  
    {  
        double d = Math.sqrt(v[0]*v[0]+v[1]*v[1]+v[2]*v[2]);  
        if (d != 0.0) 
        {  
           v[0]/=d; 
           v[1]/=d;  
           v[2]/=d;  
        }  
    } 
    
    void normCrossProd(double v1[], double v2[], double out[])  
    {  
       out[0] = v1[1]*v2[2] - v1[2]*v2[1];  
       out[1] = v1[2]*v2[0] - v1[0]*v2[2];  
       out[2] = v1[0]*v2[1] - v1[1]*v2[0];  
       normalize(out);  
    } 
    
    private double getX(double t){
    	return radius* Math.cos(2 * Math.PI * t);
    	
    }
    
    private double getY(double t){
    	return radius* Math.sin(2 * Math.PI * t);
    	
    }
    
    @Override
    public void display(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glColor4f(0f,0f,0f,1f);
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
        gl.glLoadIdentity();

        GLU glu = new GLU();
        glu.gluLookAt(0.0, 0.0, 6.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0);
        gl.glRotated(rotateX, 1, 0, 0);
        gl.glRotated(rotateY, 0, 1, 0);
        
        gl.glPolygonMode(GL2.GL_FRONT_AND_BACK,GL2.GL_FILL);
        // Make the approximating triangular mesh.
        
        double tIncrement = 1.0/NUM_SLICES;
        for(int i = 0; i < NUM_SLICES; i++)
        {
        	
           double t = i*tIncrement;
           double nextT = (i+1) * tIncrement;
           gl.glBegin(GL2.GL_TRIANGLES);
           {       	   	  
                   double x = getX(t);
                   double y = getY(t);                
                   double x2 = getX(nextT);
                   double y2 = getY(nextT);
                   
                   //Formula for normal for vertices of cone will be (x,y,radius/height)
                   double nz = 1;
                  
                   if(height != 0)
                   nz = radius/height; 
                   double normLength = Math.sqrt(x*x+y*y+nz*nz);
                   double normLength2 = Math.sqrt(x2*x2+y2*y2+nz*nz);
                                           
                   gl.glNormal3d(x/normLength,y/normLength,nz/normLength); 
                   
                   gl.glVertex3d(0,0,height);       
                   gl.glVertex3d(x, y, 0);
                   gl.glNormal3d(x2/normLength2,y2/normLength2,nz/normLength2);
                   gl.glVertex3d(x2, y2, 0);
                   
            }
         
              
     	  }
          gl.glEnd();
          gl.glPolygonMode(GL2.GL_FRONT_AND_BACK,GL2.GL_FILL);
    }

    
   
    @Override
    public void dispose(GLAutoDrawable drawable) {
    }

    @Override
    public void init(GLAutoDrawable drawable) {
    	GL2 gl = drawable.getGL().getGL2();
    	
    	gl.glClearColor(1.0f, 1.0f, 1.0f, 0.0f);
    	gl.glEnable(GL2.GL_DEPTH_TEST); // Enable depth testing.

    
    	  
    	// Turn on OpenGL lighting.
    	gl.glEnable(GL2.GL_LIGHTING);

    	// Light property vectors.
    	float lightAmb[] = { 0.0f, 0.0f, 0.0f, 1.0f };
    	float lightDifAndSpec[] = { 1.0f, 1.0f, 1.0f, 1.0f };
    	float lightPos[] = { 0.0f, 1.5f, 3.0f, 0.0f };
    	float globAmb[] = { 0.2f, 0.2f, 0.2f, 1.0f };

    	// Light properties.
    	gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, lightAmb,0);
    	gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, lightDifAndSpec,0);
    	gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_SPECULAR, lightDifAndSpec,0);
    	gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, lightPos,0);

    	gl.glEnable(GL2.GL_LIGHT0); // Enable particular light source.
    	gl.glLightModelfv(GL2.GL_LIGHT_MODEL_AMBIENT, globAmb,0); // Global ambient light.
    	gl.glLightModeli(GL2.GL_LIGHT_MODEL_TWO_SIDE, GL2.GL_TRUE); // Enable two-sided lighting.
    	gl.glLightModeli(GL2.GL_LIGHT_MODEL_LOCAL_VIEWER, GL2.GL_TRUE); // Enable local viewpoint.

    	// Material property vectors.
    	float matAmbAndDif1[] = {0.9f, 0.0f, 0.0f, 1.0f};
    	float matAmbAndDif2[] = {0.0f, 0.9f, 0.0f, 1.0f};
    	float matSpec[] = { 1.0f, 1.0f, 1.0f, 1.0f };
    	float matShine[] = { 50.0f };

    	// Material properties.
    	gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT_AND_DIFFUSE, matAmbAndDif1,0);
    	gl.glMaterialfv(GL2.GL_BACK, GL2.GL_AMBIENT_AND_DIFFUSE, matAmbAndDif2,0);
    	gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_SPECULAR, matSpec,0);
    	gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_SHININESS, matShine,0);
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int w, int h) {
    	 GL2 gl = drawable.getGL().getGL2();
         gl.glMatrixMode(GL2.GL_PROJECTION);
         gl.glLoadIdentity();
         
         GLU glu = new GLU();
         glu.gluPerspective(60.0, (float)w/(float)h, 1.0, 50.0);
        
         gl.glMatrixMode(GL2.GL_MODELVIEW);   
    }

   
	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		Point p = e.getPoint();

        if (myMousePoint != null) {
            int dx = p.x - myMousePoint.x;
            int dy = p.y - myMousePoint.y;

            // Note: dragging in the x dir rotates about y
            //       dragging in the y dir rotates about x
            rotateY += dx * ROTATION_SCALE;
            rotateX += dy * ROTATION_SCALE;

        }
        //System.out.println(rotateY + " " + rotateX);
        myMousePoint = p;
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		 myMousePoint = e.getPoint();
		
	}
}
