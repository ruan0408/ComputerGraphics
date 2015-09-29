package week7.modeling;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLJPanel;
import javax.media.opengl.glu.GLU;
import javax.swing.JFrame;

import com.jogamp.opengl.util.FPSAnimator;




//Extruding a polygon along the z-axis.
//This simple example draws an arrow at z = -1, an arrow at z = -4 
//and connects all the points on the front and back face using quads
public class ArrowExtrusion implements GLEventListener, MouseMotionListener{
	private double rotateX = 0;
    private double rotateY = 0;
    private Point myMousePoint = null;
    private static final int ROTATION_SCALE = 1;
    
    //x y profile of arrow in CCW order
	double vertices[][] = {{0,0},
						   {0.5,0.3}, 
						   {0.2,0.3},
						   {0.2,1}, 
						   {-0.2,1},
						   {-0.2,0.3},
						   {-0.5,0.3}
			};
	
	//When we want to draw the actual faces of the polygon
	//we must break it up into 2 parts since it is concave
	int faces[][] = {{0,1,6},    //indexes of the points in the vertices array for the triangle part
			         {2,3,4,5}}; //indexs for the quad part
	
					  
	
    public static void main(String[] args) {
        // initialisation
        GLProfile glp = GLProfile.getDefault();
        GLCapabilities caps = new GLCapabilities(glp);
        
        // create a panel to draw on
        GLJPanel panel = new GLJPanel(caps);

        // put it in a JFrame
        final JFrame jframe = new JFrame("Arrow Extrusion");
        jframe.setSize(800, 800);
        jframe.add(panel);
        jframe.setVisible(true);

        // Catch window closing events and quit             
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        ArrowExtrusion s = new ArrowExtrusion();
        
        // add a GL Event listener to handle rendering
        panel.addGLEventListener(s);
        panel.addMouseMotionListener(s);
        panel.setFocusable(true);   
        
        // NEW: add an animator to create display events at 60 FPS
        FPSAnimator animator = new FPSAnimator(60);
        animator.add(panel);
        animator.start();
    }   
    
  
    /* 
     * Some maths utility functions
     * 
     */
    
    double [] cross(double u [], double v[]){
    	double crossProduct[] = new double[3];
    	crossProduct[0] = u[1]*v[2] - u[2]*v[1];
    	crossProduct[1] = u[2]*v[0] - u[0]*v[2];
    	crossProduct[2] = u[0]*v[1] - u[1]*v[0];
    
    	return crossProduct;
    }
    
    //Find normal for planar polygon
    public double[] normal(double[] p0, double p1[], double p2[]){
    	double [] u = {p1[0] - p0[0], p1[1] - p0[1], p1[2] - p0[2]};
    	double [] v = {p2[0] - p0[0], p2[1] - p0[1], p2[2] - p0[2]};
    	double [] normal = cross(u,v);
    	return normalise(normal);
    }
    
   
    double [] normalise(double [] n){
    	double  mag = getMagnitude(n);
    	double norm[] = {n[0]/mag,n[1]/mag,n[2]/mag};
    	return norm;
    }
    
    double getMagnitude(double [] n){
    	double mag = n[0]*n[0] + n[1]*n[1] + n[2]*n[2];
    	mag = Math.sqrt(mag);
    	return mag;
    }
    
    public static double[] multiply(double[][] m, double[] v) {
      
        double[] u = new double[4];

        for (int i = 0; i < 4; i++) {
            u[i] = 0;
            for (int j = 0; j < 4; j++) {
                u[i] += m[i][j] * v[j];
            }
        }

        return u;
    }
    
    @Override
    public void display(GLAutoDrawable drawable) {
    	  
        GL2 gl = drawable.getGL().getGL2();
       
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
        gl.glLoadIdentity();
        
        // Material property vectors.
        float matAmbAndDif1[] = {0.7f, 0.2f, 0.7f, 1.0f};
        float matAmbAndDif2[] = {0f, 1f, 0f, 1.0f};
        float matSpec1[] = {0.2f, 0.2f, 0.2f, 1f};
    
        float matShine[] = {150.0f};
        
        //Set front and back to have different colors to make debugging easier
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT_AND_DIFFUSE, matAmbAndDif1,0);
        gl.glMaterialfv(GL2.GL_BACK, GL2.GL_AMBIENT_AND_DIFFUSE, matAmbAndDif2,0);
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, matSpec1,0);
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SHININESS, matShine,0);
        
        
        
        GLU glu = new GLU();
        glu.gluLookAt(0.0, 0.0, 7.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0);
        gl.glRotated(rotateX, 1, 0, 0);
        gl.glRotated(rotateY, 0, 1, 0);
      
        //The front of our arrow extrusion will be on the z=-1 plane
        //The back will be on the z = -2 plane
        double zFront = -1;
        double zBack = -2;
        
        
        //Draw the front face
	    for(int j=0; j < faces.length; j++){
	        gl.glBegin(GL2.GL_POLYGON);{
	        	for(int i = 0 ;i< faces[j].length; i++){
	        		gl.glNormal3d(0,0,1);
	        		gl.glVertex3d(vertices[faces[j][i]][0],vertices[faces[j][i]][1],zFront);       		
	        	}	            
	        }gl.glEnd();	        
	    }
	    
	        
	    //Draw the back face 
	    for(int j = 0; j < faces.length; j++ ){
	    	gl.glBegin(GL2.GL_POLYGON);{
                //Draw face in reverse order so CWW order since it is facing the back
	    		for(int i = faces[j].length -1; i>=0; i--){
	    			gl.glNormal3d(0,0,-1);
	    			gl.glVertex3d(vertices[faces[j][i]][0],vertices[faces[j][i]][1],zBack);       		
	    		}

	    	}gl.glEnd();

	    }
        
        
           
        //We will go through each point in the arrow,
        //create a copy of the point at zBack instead of zFront
        //and connect it to its neighbouring point and transformed neighbouring point
        //using quads
	    /*
        gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);
        
        gl.glBegin(GL2.GL_QUADS);{
	    	for(int i=0; i< vertices.length; i++){
	    		//The final point needs to be connected back to point at index 0
	    		int i1 = (i+1)%vertices.length;


	    		double p0[] = {vertices[i][0],vertices[i][1],zFront,1};	        		
	    		double p1[] = {vertices[i][0],vertices[i][1],zBack,1};	

	    		double p2[] = {vertices[i1][0],vertices[i1][1],zFront,1};
	    		double p3[] = {vertices[i1][0],vertices[i1][1],zBack,1};


	    		//We only want flat shading so we just use one normal per face
	    		//Since our quads are planar, using simple method with 3 points is ok
	    		gl.glNormal3dv(normal(p0,p1,p2),0);
	    		gl.glVertex3dv(p0,0);	        		
	    		gl.glVertex3dv(p1,0);
	    		gl.glVertex3dv(p3,0);	  				          
	    		gl.glVertex3dv(p2,0);	
	    		
	        				             		
	        }
	        		
	    }gl.glEnd();
           
	    
	    */
	    
	    gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);
    }

    
   
    @Override
    public void dispose(GLAutoDrawable drawable) {
    }

    @Override
    public void init(GLAutoDrawable drawable) {
    	GL2 gl = drawable.getGL().getGL2();
    	
    	
    	gl.glEnable(GL2.GL_DEPTH_TEST); // Enable depth testing.

    	gl.glEnable(GL2.GL_LIGHTING);
    	gl.glEnable(GL2.GL_LIGHT0);
    	gl.glLightModeli(GL2.GL_LIGHT_MODEL_TWO_SIDE, GL2.GL_TRUE);
    	 
    	gl.glEnable(GL2.GL_NORMALIZE);
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int w, int h) {
    	GL2 gl = drawable.getGL().getGL2();
    	gl.glMatrixMode(GL2.GL_PROJECTION);
    	gl.glLoadIdentity();

    	//Sometimes good to use glOrtho for developing and debugging   	
    	gl.glOrtho(-4, 4, -4, 4, 0, 20);
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
        
        myMousePoint = p;
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		 myMousePoint = e.getPoint();
		
	}
}
