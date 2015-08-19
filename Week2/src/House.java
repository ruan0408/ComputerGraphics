import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLJPanel;
import javax.media.opengl.glu.GLU;
import javax.swing.JFrame;

/**
 * A simple example of writing a JOGL program to draw a house and play around
 * with 2D transformations
 *
 * @author angf
 */

public class House implements GLEventListener {
    static public boolean DRAW_COORDINATES = true;  
	
    public static void main(String[] args) {
        // Initialise OpenGL
        GLProfile glp = GLProfile.getDefault();
        GLCapabilities caps = new GLCapabilities(glp);

        // Create a panel to draw on
        GLJPanel panel = new GLJPanel(caps);

        // Put it in a window
        final JFrame jframe = new JFrame("House Transformations");
        jframe.setSize(1200, 1200);
        jframe.add(panel);
        jframe.setVisible(true);
      
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
     
        // add a GL Event listener to handle rendering
        panel.addGLEventListener(new House());
    }

    @Override
    public void display(GLAutoDrawable drawable) {
       
        GL2 gl = drawable.getGL().getGL2();
       
        // clear the window
        gl.glClearColor(1,1,1,1);
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);

        //Load and initialise the model view matrix	   
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();
    	  
        gl.glColor3d(0, 0, 0);
        drawGrid(gl);
        gl.glLineWidth(3);
        
        //Draw the world co-ordinate frame
        drawCoordinateFrame(gl);
        
        //An example of using transformations
        //Try other combinations to see what they do
        gl.glTranslated(-1, 2, 0);
        gl.glRotated(-45,0,0,1);
        gl.glScaled(0.5,0.5,1);
        drawHouse(gl);
        drawCoordinateFrame(gl);

        
    }
    
    public void drawHouse(GL2 gl){
  	   
  	   //House
        gl.glBegin(GL2.GL_LINE_STRIP);
        {
            gl.glVertex3d(0, 0, 0); 
            gl.glVertex3d(1, 0, 0); 
            gl.glVertex3d(1, 1, 0); 
            gl.glVertex3d(0, 1, 0); 
            gl.glVertex3d(0, 0, 0); 
        }
        gl.glEnd();

        //Door
        gl.glBegin(GL2.GL_LINE_STRIP);
        {
            gl.glVertex3d(0.65, 0, 0); 
            gl.glVertex3d(0.85, 0, 0); 
            gl.glVertex3d(0.85, 0.5, 0); 
            gl.glVertex3d(0.65, 0.5, 0); 
            gl.glVertex3d(0.65, 0, 0); 
        }
        gl.glEnd();
        
        //Roof
        gl.glBegin(GL2.GL_LINE_STRIP);
        {
            gl.glVertex3d(0, 1, 0); 
            gl.glVertex3d(1, 1, 0); 
            gl.glVertex3d(0.5, 2, 0); 
            gl.glVertex3d(0, 1, 0); 
        }
        gl.glEnd();
  	    
     }

    @Override
    public void init(GLAutoDrawable drawable) {
       
    	
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
        // dispose of any allocated resources
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int w, int h) {
        
    	
   	    GL2 gl = drawable.getGL().getGL2();      
        
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();

        double aspect = 1.0 * w / h;
    
      
        GLU glu = new GLU();
        if(aspect >= 1){
            glu.gluOrtho2D(-4* aspect, 4* aspect, -4.0, 4.0);  // left, right, top, bottom
           
        } else {
        	glu.gluOrtho2D(-4, 4, -4.0/aspect, 4.0/aspect);  // left, right, top, bottom
        }
       
    }
    
  
    
static void drawCoordinateFrame(GL2 gl) {
        
        if (!DRAW_COORDINATES) {
            return;
        }
        
        float[] color = new float[4];
        gl.glGetFloatv(GL2.GL_CURRENT_COLOR, color, 0);
        float[] width = new float[1];
        gl.glGetFloatv(GL2.GL_LINE_WIDTH, width, 0);
            
        gl.glLineWidth(3);
                    
        // x axis in red
        gl.glColor3d(1.0, 0.0, 0.0);
        gl.glBegin(GL.GL_LINES);
        {
            gl.glVertex2d(0, 0);
            gl.glVertex2d(1, 0);
        }
        gl.glEnd();
        
        // y axis in green
        gl.glColor3d(0.0, 1.0, 0.0);
        gl.glBegin(GL.GL_LINES);
        {
            gl.glVertex2d(0, 0);
            gl.glVertex2d(0, 1);
        }
        gl.glEnd();
        
        gl.glColor4d(color[0], color[1], color[2], color[3]);
        gl.glLineWidth(width[0]);
        
    }
    
static void drawGrid(GL2 gl) {



	float[] color = new float[4];
	gl.glGetFloatv(GL2.GL_CURRENT_COLOR, color, 0);
	float[] width = new float[1];
	gl.glGetFloatv(GL2.GL_LINE_WIDTH, width, 0);

	gl.glLineWidth(1);


	gl.glColor3d(.9, .9, 0.9);
	gl.glBegin(GL.GL_LINES);
	{
		for(int i=0; i< 14; i++){
			gl.glVertex2d(i*1, 6.5);
			gl.glVertex2d(i*1, -6.5);
			gl.glVertex2d(-i*1, 6.5);
			gl.glVertex2d(-i*1, -6.5);
		}
	}
	gl.glEnd();

	gl.glColor3d(.9, .9, 0.9);
	gl.glBegin(GL.GL_LINES);
	{
		for(int i=0; i< 14; i++){
			gl.glVertex2d(6.5,i*1);
			gl.glVertex2d(-6.5,i*1);
			gl.glVertex2d(6.5,-i*1);
			gl.glVertex2d(-6.5,-i*1);
		}
	}
	gl.glEnd();


	gl.glColor4d(color[0], color[1], color[2], color[3]);
	gl.glLineWidth(width[0]);

}


}
