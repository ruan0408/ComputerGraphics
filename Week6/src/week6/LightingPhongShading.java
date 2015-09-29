package week6;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GL2ES2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLJPanel;
import javax.media.opengl.glu.GLU;
import javax.swing.JFrame;

import week6.Shader.CompilationException;

import com.jogamp.opengl.util.FPSAnimator;
import com.jogamp.opengl.util.gl2.GLUT;

/**
 * 
 * Demonstration of using shaders for Phong Per Fragment Specular lighting.
 * You can move the light with the arrows
 * You can turn off and on phong shading and just use normal fixed pipeline garoud smooth shading  or flat shading by pressing
 * the space bar
 * Press P to increase phong exponent and p to decrease it
 * d, s, m control the amount of diffuse and specular and ambient
 * Press 1, 2 ,3 4 to control object (teapot, cube,sphere, torus)
 */

public class LightingPhongShading implements GLEventListener, KeyListener {
	
	private Shading shading = Shading.FLAT; 
	private int useBlinn = 1;
	private boolean light1On = false; // Blue light on?
	private boolean light0On = true;
	private float erosion = -0.5f;
	private float f = 50;
	private float d = 1.0f; // Diffuse and  white light intensity.
	private float s = 1.0f; // Specular white light intensity.
	private float m = 0.2f; // Global ambient white light intensity.
	private float a = 0.2f; //  Ambient white light intensity.
	private float xAngle = 0.0f, yAngle = 0.0f; // Rotation angles of white light.
	private static final String VERTEX_SHADER[] = {"src/week6/garoudVertex.glsl",
		                                           "src/week6/Phongvertex.glsl",
		                                           "src/week6/Phongvertex.glsl",
		                                           "src/week6/Phongvertex.glsl"
	};
	private static final String FRAGMENT_SHADER[] = {"src/week6/garoudFragment.glsl",
		                                             "src/week6/Phongfragment.glsl",
		                                             "src/week6/PhongMultifragment.glsl",
		                                             "src/week6/PhongDiscardfragment.glsl"
	};
	private int shaderprogram[] = new int[4];
	private Model myModel = Model.TEAPOT;
	
	public enum Shading {
		FLAT, SMOOTH, SMOOTH_SHADER, PHONG_FRAGMENT, PHONG_MULTILIGHT_FRAGMENT,PHONG_DISCARD  
	};
	
	public enum Model {
		TEAPOT, CUBE, SPHERE, TORUS, BUNNY
	};

	
    public static void main(String[] args) {
        // initialisation
        GLProfile glp = GLProfile.getDefault();
        GLCapabilities caps = new GLCapabilities(glp);
        
        // create a panel to draw on
        GLJPanel panel = new GLJPanel(caps);

        // put it in a JFrame
        final JFrame jframe = new JFrame("Phong Per Fragment Shading");
        jframe.setSize(800, 800);
        jframe.add(panel);
        jframe.setVisible(true);

        // Catch window closing events and quit             
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        LightingPhongShading s = new LightingPhongShading();
        
        // add a GL Event listener to handle rendering
        panel.addGLEventListener(s);
        panel.addKeyListener(s);
        panel.setFocusable(true);   
        
        // NEW: add an animator to create display events at 60 FPS
        FPSAnimator animator = new FPSAnimator(60);
        animator.add(panel);
        animator.start();
    }   
    
    @Override
    public void display(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        if(shading == Shading.FLAT){
			gl.glShadeModel(GL2.GL_FLAT);
			gl.glUseProgram(0);
		} else if (shading == Shading.SMOOTH){
			gl.glShadeModel(GL2.GL_SMOOTH);
			gl.glUseProgram(0);
		} else {
    	   int index = shading.ordinal() - 2; 
    	   System.out.println("Shading " + shading);
    	   System.out.println("Use Blinn " + useBlinn);
           gl.glUseProgram(shaderprogram[index]);
           gl.glUniform1f(gl.glGetUniformLocation(shaderprogram[index],"limit"),erosion);
           gl.glUniform1i(gl.glGetUniformLocation(shaderprogram[index],"useBlinn"),useBlinn);
		}
        	 
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
     
        // Light property vectors.
        float lightAmb[] = { a, a, a, 1.0f };
        float lightDif[] = { d, d, d, 1.0f };
        float lightSpec[] = { s, s, s, 1.0f };
        float lightPos0[] = { 0.0f, 0.0f, 3.0f, 1 }; //positional light
        float globAmb[] = { m, m, m, 1.0f };
        gl.glLightModeli(GL2.GL_LIGHT_MODEL_LOCAL_VIEWER, GL2.GL_TRUE); // Enable local viewpoint
        
        // Light0 properties.
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, lightAmb,0);
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, lightDif,0);
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_SPECULAR, lightSpec,0); 
       
        // Light1 properties.
        float lightDifAndSpec1[] = { 0.0f, 0.0f, 1.0f, 1.0f };
        gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_AMBIENT, lightAmb,0);
        gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_DIFFUSE, lightDifAndSpec1,0);
        gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_SPECULAR, lightDifAndSpec1,0);
        
        gl.glLightModelfv(GL2.GL_LIGHT_MODEL_AMBIENT, globAmb,0); // Global ambient light.
       

        // Turn lightson.
        if(light0On){
        	gl.glEnable(GL2.GL_LIGHT0); 
        } else {
        	
        	gl.glDisable(GL2.GL_LIGHT0);
        }
        if(light1On){
        	gl.glEnable(GL2.GL_LIGHT1);
        } else {
        	gl.glDisable(GL2.GL_LIGHT1);
        }
        
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();
        
       
        GLU glu = new GLU();
        glu.gluLookAt(0.0, 3.0, 5.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0);

        // Draw light source spheres

        // Light0 positioned and sphere positioned
        gl.glPushMatrix();
        gl.glRotated(xAngle, 1.0, 0.0, 0.0); // Rotation about x-axis.
        gl.glRotated(yAngle, 0.0, 1.0, 0.0); // Rotation about z-axis.
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, lightPos0,0);
        gl.glTranslatef(lightPos0[0], lightPos0[1], lightPos0[2]);
        gl.glColor3f(d, d, d); 
        GLUT glut = new GLUT();
        float emmL[] = {1.0f, 1.0f, 1.0f, 1.0f};
        float matAmbAndDifL[] = {0.0f, 0.0f, 0.0f, 1.0f};
        float matSpecL[] = { 0.0f, 0.0f, 0,0f, 1.0f };
        float matShineL[] = { f };

        // Material properties of sphere that represents the light
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT_AND_DIFFUSE, matAmbAndDifL,0);
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, matSpecL,0);
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SHININESS, matShineL,0);
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_EMISSION, emmL,0);
        if (light0On)
        glut.glutSolidSphere(0.05, 8, 8); // Sphere at positional light source.
        gl.glPopMatrix();
      
        
        //Just for an example, instead of using emissive light we
        //Disable lighting temporarily so we can see the actual sphere
        //representing light 2
        
        float lightPos1[] = { 1.0f, 2.0f, 0.0f, 1.0f };
        gl.glDisable(GL2.GL_LIGHTING);
        // Light1 and its sphere positioned.
        gl.glPushMatrix();{
        	gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_POSITION, lightPos1,0);
        	gl.glTranslated(lightPos1[0], lightPos1[1], lightPos1[2]);
        	gl.glColor3f(0.0f, 0.0f, 1.0f); 
        	if (light1On) glut.glutWireSphere(0.05, 8, 8);
        }gl.glPopMatrix();
        //Enable it again for the rest of scene
        gl.glEnable(GL2.GL_LIGHTING);


        float matAmbAndDif[] = {0.5f, 0.0f, 0.0f, 1.0f};
        float matSpec[] = { 1.0f, 1.0f, 1,0f, 1.0f };
        float matShine[] = { f };
        float emm[] = {0.0f, 0.0f, 0.0f, 1.0f};
 	   // Material properties of teapot
 	   gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT_AND_DIFFUSE, matAmbAndDif,0);
 	   gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, matSpec,0);
 	   gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SHININESS, matShine,0);
 	   gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_EMISSION, emm,0);
      
 	   if(myModel == Model.TEAPOT){
 		   gl.glFrontFace(GL2.GL_CW);
 		   glut.glutSolidTeapot(1.5);
 		   gl.glFrontFace(GL2.GL_CCW);
 	   } else if (myModel == Model.CUBE){

 		   glut.glutSolidCube(2f);


 	   } else if (myModel == Model.SPHERE){

 		   glut.glutSolidSphere(1.5, 20, 20);


 	   } else if (myModel == Model.TORUS){

 		   glut.glutSolidTorus(0.5, 1.5, 20, 20);

 	   }  else if (myModel == Model.BUNNY){
 		   try{
 			   gl.glPushMatrix();
 			   gl.glScaled(2.5,2.5,2.5);
 		     Bunny.gen3DObjectList(gl) ;
 		    gl.glPopMatrix();
 		   } catch(IOException e){
 			   System.err.println("Could not open bunny.txt. Here is a donut instead");
 			  glut.glutSolidTorus(0.5, 1.5, 20, 20);
 			
 		   }
	   } 

    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
    }

    @Override
    public void init(GLAutoDrawable drawable) {
    	GL2 gl = drawable.getGL().getGL2();
    	
    	
    	gl.glEnable(GL2.GL_DEPTH_TEST); // Enable depth testing.

    	gl.glEnable(GL2.GL_LIGHTING);

    	   
    	// init shaders
        try {
            initShaders(gl);
        }
        catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

    	
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int w, int h) {
    	 GL2 gl = drawable.getGL().getGL2();
         gl.glMatrixMode(GL2.GL_PROJECTION);
         gl.glLoadIdentity();
         
         GLU glu = new GLU();
         glu.gluPerspective(60.0, (float)w/(float)h, 1.0, 20.0);
         //gl.glOrtho(-4,4,-4,4,1,10);
         gl.glMatrixMode(GL2.GL_MODELVIEW);
    }

    private void initShaders(GL2 gl) throws Exception {
        for(int i=0; i < shaderprogram.length; i++){
        	
        
    	     shaderprogram[i] = initShader(gl,i);
        }
    }
    private int initShader(GL2 gl, int index) throws Exception {
        Shader vertexShader = new Shader(GL2.GL_VERTEX_SHADER, new File(VERTEX_SHADER[index]));
        vertexShader.compile(gl);
        Shader fragmentShader = new Shader(GL2.GL_FRAGMENT_SHADER, new File(FRAGMENT_SHADER[index]));
        fragmentShader.compile(gl);
        
        //Each shaderProgram must have
        //one vertex shader and one fragment shader.
        int shaderprogram = gl.glCreateProgram();
        gl.glAttachShader(shaderprogram, vertexShader.getID());
        gl.glAttachShader(shaderprogram, fragmentShader.getID());
        
        
        gl.glLinkProgram(shaderprogram);
        
        
        int[] error = new int[2];
        gl.glGetProgramiv(shaderprogram, GL2.GL_LINK_STATUS, error, 0);
        if (error[0] != GL2.GL_TRUE) {
        	int[] logLength = new int[1];
            gl.glGetProgramiv(shaderprogram, GL2ES2.GL_INFO_LOG_LENGTH, logLength, 0);

            byte[] log = new byte[logLength[0]];
            gl.glGetProgramInfoLog(shaderprogram, logLength[0], (int[]) null, 0, log, 0);

            System.out.printf("Failed to compile shader! %s\n", new String(log));
            throw new CompilationException("Error compiling the shader: "
                    + new String(log));
        }
        
        gl.glValidateProgram(shaderprogram);
        
        gl.glGetProgramiv(shaderprogram, GL2.GL_VALIDATE_STATUS, error, 0);
        if (error[0] != GL2.GL_TRUE) {
        	System.out.printf("Failed to validate shader!\n");
        	throw new Exception("program failed to validate");
        }

        return shaderprogram;

    }
    
    
    @Override
	public void keyPressed(KeyEvent ev) {
		 switch (ev.getKeyCode()) {
		 case KeyEvent.VK_E:
			 if (ev.isShiftDown()) {
			     erosion-= 0.01;	 
			 } else {
	    		erosion+= 0.01;
			 }		
			 System.out.println(erosion);
	         break;
		 case KeyEvent.VK_W:			
	    		light0On = !light0On;

	    		break;
	    	case KeyEvent.VK_B:
	    		light1On = !light1On;

	    		break;
	    	case KeyEvent.VK_L:
	    		if(useBlinn == 0) useBlinn = 1;
	    		else useBlinn = 0;

	    		break;
	    	
		  case KeyEvent.VK_D:
			  if (ev.isShiftDown()) {
				  if (d < 1.0) d += 0.05;
			  } else {
				  if (d > 0.0) d -= 0.05;
			  }
	        
			 break;
		  case KeyEvent.VK_M:
			  if (ev.isShiftDown()) {
				  if (m < 1.0) m += 0.05;
			  } else {
				  if (m > 0.0) m -= 0.05;
			  }
			 break;	
		  case KeyEvent.VK_S:
			  if (ev.isShiftDown()) {
				  if (s < 1.0) s += 0.05;
			  } else {
				  if (s > 0.0) s -= 0.05;
			  }
			 break;	
		  case KeyEvent.VK_F:
			  if (ev.isShiftDown()) {
				  if (f < 128) f += 2;
			  } else {
				  if (f > 0.0) f -= 2;
			  }
			 break;		
		  case KeyEvent.VK_UP:
			  xAngle--;
		      if (xAngle < 0.0) xAngle += 360.0;
			  break;
		  case KeyEvent.VK_DOWN:
			  xAngle++;
		      if (xAngle > 360.0) xAngle -= 360.0;
			  break;
		  case KeyEvent.VK_LEFT:
			  yAngle--;
		      if (yAngle < 0.0) yAngle += 360.0;
		      break;
		  case KeyEvent.VK_RIGHT:
			  yAngle++;
		      if (yAngle > 360.0) yAngle -= 360.0;
		      break;
		  case KeyEvent.VK_SPACE:
			  shading = shading.values()[(shading.ordinal() + 1)%6]; 
		      break;
		  case KeyEvent.VK_1:
	            myModel = Model.TEAPOT;
	            break;
	            
		  case KeyEvent.VK_2:
			  myModel = Model.CUBE;
			  break;

		  case KeyEvent.VK_3:
			  myModel = Model.SPHERE;
			  break;

		  case KeyEvent.VK_4:
			  myModel = Model.TORUS;
			  break;
			  
		  case KeyEvent.VK_5:
			  myModel = Model.BUNNY;
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
