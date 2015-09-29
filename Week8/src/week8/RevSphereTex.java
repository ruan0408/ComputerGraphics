package week8;


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

public class RevSphereTex implements GLEventListener, MouseMotionListener, KeyListener{
	private double rotateX = 0;
    private double rotateY = 0;
    private Point myMousePoint = null;
    private static final int ROTATION_SCALE = 1;
    private int maxStacks = 20;
    private int maxSlices = 24;
    private int numStacks = 1;
    private int numSlices = 1;
    
    private String textureFileName1 = "src/week8/eyeTextureNew2.jpg";
   
    private String textureExt1 = "bmp";
    private MyTexture myTextures[];
	    
    public static void main(String[] args) {
        // initialisation
        GLProfile glp = GLProfile.getDefault();
        GLCapabilities caps = new GLCapabilities(glp);
        
        // create a panel to draw on
        GLJPanel panel = new GLJPanel(caps);

        // put it in a JFrame
        final JFrame jframe = new JFrame("Sphere");
        jframe.setSize(1200, 1200);
        jframe.add(panel);
        jframe.setVisible(true);

        // Catch window closing events and quit             
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        RevSphereTex s = new RevSphereTex();
        
        // add a GL Event listener to handle rendering
        panel.addGLEventListener(s);
        panel.addMouseMotionListener(s);
        panel.setFocusable(true);   
        
        panel.addGLEventListener(s);
        panel.addKeyListener(s);
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
    
   
    
    double r(double t){
    	double x  = Math.cos(2 * Math.PI * t);
        return x;
    }
    
    double getY(double t){
    	
    	double y  = Math.sin(2 * Math.PI * t);
        return y;
    }
    
    @Override
    public void display(GLAutoDrawable drawable) {
    	
    	GL2 gl = drawable.getGL().getGL2();

    	gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);

    
    	gl.glMatrixMode(GL2.GL_MODELVIEW);
    	gl.glLoadIdentity();
    	//Set camera 
    	gl.glTranslated(0,0,-3);
    	gl.glRotated(rotateX, 1, 0, 0);
    	gl.glRotated(rotateY, 0, 1, 0);
    	//gl.glEnable(GL2.GL_CULL_FACE);
    	// gl.glCullFace(GL2.GL_BACK);
        gl.glBindTexture(GL2.GL_TEXTURE_2D, myTextures[0].getTextureId());
    	gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);
    	drawObject(gl);
    	gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);       
    }
    
    
    public void drawObject(GL2 gl){
    	double deltaT;
    
    	
    	deltaT = 0.5/maxStacks;
    	int ang;  
    	int delang = 360/maxSlices;
    	double x1,x2,z1,z2,y1,y2;
    	double radius = 0.5;
    	for (int i = 0; i < numStacks; i++) 
    	{ 
    		double t = -0.25 + i*deltaT;
    		
    		gl.glBegin(GL2.GL_TRIANGLE_STRIP); 
    		for(int j = 0; j <= numSlices; j++)  
    		{  
    			ang = j*delang;
    			x1=radius * r(t)*Math.cos((double)ang*2.0*Math.PI/360.0); 
    			x2=radius * r(t+deltaT)*Math.cos((double)ang*2.0*Math.PI/360.0); 
    			y1 = radius * getY(t);

    			z1=radius * r(t)*Math.sin((double)ang*2.0*Math.PI/360.0);  
    			z2= radius * r(t+deltaT)*Math.sin((double)ang*2.0*Math.PI/360.0);  
    			y2 = radius * getY(t+deltaT);

    			double normal[] = {x1,y1,z1};


    			normalize(normal);    

    			gl.glNormal3dv(normal,0);  
    			double tCoord = 1.0/numStacks * i; //Or * 2 to repeat label
    			double sCoord = 1.0/numSlices * j;
    			gl.glTexCoord2d(sCoord,tCoord);
    			gl.glVertex3d(x1,y1,z1);
    			normal[0] = x2;
    			normal[1] = y2;
    			normal[2] = z2;

    			
    			normalize(normal);    
    			gl.glNormal3dv(normal,0); 
    			tCoord = 1.0/numStacks * (i+1); //Or * 2 to repeat label
    			gl.glTexCoord2d(sCoord,tCoord);
    			gl.glVertex3d(x2,y2,z2); 

    		}; 
    		gl.glEnd();  
    	}
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
    	
    	myTextures = new MyTexture[1];
    	myTextures[0] = new MyTexture(gl,textureFileName1,textureExt1,true);
    

    	// Specify how texture values combine with current surface color values.
    	gl.glTexEnvf(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE, GL2.GL_REPLACE); 

    	// Turn on OpenGL texturing.
    	gl.glEnable(GL2.GL_TEXTURE_2D); 

    	  
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
	public void keyPressed(KeyEvent ev) {
		
		switch (ev.getKeyCode()) {
		case KeyEvent.VK_RIGHT:

			if (numSlices < maxSlices ) numSlices++;

			break;		
		case KeyEvent.VK_LEFT:

			if (numSlices > 1) numSlices--;

			break;	
		case KeyEvent.VK_UP:

			if (numStacks < maxStacks ) numStacks++;

			break;	
		case KeyEvent.VK_DOWN:

			if (numStacks > 1) numStacks--;

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
