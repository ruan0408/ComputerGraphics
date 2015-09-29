package week7.modeling;


import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
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

//Implements a surface of revolution
//Takes a 2D profile and sweeps it around the y-axis

public class RevProfile implements GLEventListener, MouseMotionListener,KeyListener{
	private double rotateX = 0;
    private double rotateY = 0;
    private Point myMousePoint = null;
    private static final int ROTATION_SCALE = 1;
   
	    
    public static void main(String[] args) {
        // initialisation
        GLProfile glp = GLProfile.getDefault();
        GLCapabilities caps = new GLCapabilities(glp);
        
        // create a panel to draw on
        GLJPanel panel = new GLJPanel(caps);

        // put it in a JFrame
        final JFrame jframe = new JFrame("Cylinder");
        jframe.setSize(1200, 1200);
        jframe.add(panel);
        jframe.setVisible(true);

        // Catch window closing events and quit             
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        RevProfile s = new RevProfile();
        
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
    
    double [] makeProfileNormals(double[] r, double []y){
    	double [] n = new double[r.length*2];
    	double[] v0 = new double[2];
    	double[] v1 = new double[2];
    	for(int i = 0; i < r.length; i++){
    	    if (i == 0) {
    			v0[0] = r[i];
    			v0[1] = y[i];
    			v1[0] = r[i+1];
    			v1[1] = y[i+1];
    		
    		} else if (i == r.length-1) {
    			v0[0] = r[i-1];
    			v0[1] = y[i-1];
    			v1[0] = r[i];
    			v1[1] = y[i];
    		} else {
    			v0[0] = r[i-1];
    			v0[1] = y[i-1];
    			v1[0] = r[i+1];
    			v1[1] = y[i+1];
    		}
    		
    		double dx = v1[0] - v0[0];
    		double dy = v1[1] - v0[1];
    		double mag = Math.sqrt(dx*dx + dy*dy);
    	    if(mag != 0){
    	   
    		   dx = dx/mag;
    		   dy = dy/mag;
    	    }
    		//create a normal from the tangent in 2D
    		n[i*2] = dy; 
    		n[i*2+1] = -dx;
    	}
    	return n;
    }
    
   
    
    
    
    //Like multilying by matrix
    //M = cos(theta)  0 sin(theta)   0
    //         0      1    0         0
    //    -sin(theta) 0 cos(theta)  0
    //        0       0    0        1
    
    @Override
    public void display(GLAutoDrawable drawable) {
    	
        GL2 gl = drawable.getGL().getGL2();
       
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
        
        int ang,i;  
        int delang = 5;  
        double x1,x2,z1,z2,x3,z3; 
        
        //Other profile curve uncomment and try - comment out martini glass profile instead
        //double r[] = {0.0,0.3,0.3,0.25,0.25,0.35,0.35,0.3,0.15,0.15,0.05,0.0}; 
        //double y[] = {0.0,0.0,0.05,0.1,0.3,0.35,0.45,0.5,0.5,0.4,0.3,0.3};
        
        //Martini glass
        double[] r = {0.26,0.26,0.09,0.07,0.04,0.02,0.02,0.05,0.14,0.41,0.4,0.11,0.04,0};       
        double[] y = {0,0,0.02,0.02,0.05,0.05,0.38,0.46,0.56,0.98,0.99,0.57,0.51,0.5};
        
        
        double[] n = makeProfileNormals(r,y);
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();
        //Set camera 
        gl.glTranslated(0,0,-3);
        gl.glRotated(rotateX, 1, 0, 0);
        gl.glRotated(rotateY, 0, 1, 0);
       //gl.glEnable(GL2.GL_CULL_FACE);
       //gl.glCullFace(GL2.GL_BACK);
        
        gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);
        
        for (i=0;i<r.length-1;i++) 
        { 
         gl.glBegin(GL2.GL_TRIANGLE_STRIP); 
          for(ang=0;ang<=360;ang+=delang)  
          {  
        	  
        	  
           x1=r[i]*Math.cos((double)ang*2.0*Math.PI/360.0); 
           x2=r[i+1]*Math.cos((double)ang*2.0*Math.PI/360.0); 
           z1=r[i]*Math.sin((double)ang*2.0*Math.PI/360.0);  
           z2=r[i+1]*Math.sin((double)ang*2.0*Math.PI/360.0);  
           
         
           double normal[] = new double[3];
           normal[0] = n[i*2]* Math.cos((double)ang*2.0*Math.PI/360.0); 
           normal[1] = n[i*2+1];
           normal[2] = n[i*2]* Math.sin((double)ang*2.0*Math.PI/360.0); 
           normalize(normal);    
       
           gl.glNormal3dv(normal,0);         
           gl.glVertex3d(x1,y[i],z1);
           normal[0] = n[(i+1)*2]* Math.cos((double)ang*2.0*Math.PI/360.0); 
           normal[1] = n[(i+1)*2+1];
           normal[2] = n[(i+1)*2]* Math.sin((double)ang*2.0*Math.PI/360.0); 
           
           normalize(normal);    
           gl.glNormal3dv(normal,0); 
           gl.glVertex3d(x2,y[i+1],z2); 
         
           }; 
         gl.glEnd();  
        }
        gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);       
    }

    
   
    @Override
    public void dispose(GLAutoDrawable drawable) {
    }

    @Override
    public void init(GLAutoDrawable drawable) {
    	GL2 gl = drawable.getGL().getGL2();

    	gl.glClearColor(1.0f, 1.0f, 1.0f, 0.0f);
    	gl.glEnable(GL2.GL_DEPTH_TEST); // Enable depth testing.

    	gl.glEnable(GL2.GL_LIGHTING); 
    	gl.glEnable(GL2.GL_LIGHT0); 

    	// material parameter set for metallic gold or brass 

    	float ambient[] = {0.33f, 0.22f, 0.03f, 1.0f}; 
    	float diffuse[] = {0.78f, 0.57f, 0.11f, 1.0f}; 
    	float specular[] = {0.99f, 0.91f, 0.81f, 1.0f}; 
    	float shininess = 27.8f;  	

    	gl.glLightModeli(GL2.GL_LIGHT_MODEL_TWO_SIDE, GL2.GL_TRUE);
    	gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_AMBIENT, ambient,0); 
    	gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_DIFFUSE, diffuse,0); 
    	gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_SPECULAR, specular,0); 
    	gl.glMaterialf(GL2.GL_FRONT_AND_BACK, GL2.GL_SHININESS, shininess); 
    	  
    } 	   
    	 

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int w, int h) {
    	 GL2 gl = drawable.getGL().getGL2();
         gl.glMatrixMode(GL2.GL_PROJECTION);
         gl.glLoadIdentity();
         
         GLU glu = new GLU();
         glu.gluPerspective(60.0, (float)w/(float)h, 1.0, 10.0);
         
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

	@Override
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
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
