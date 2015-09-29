package week7.revision;

import javax.media.opengl.GL2;

/**
 * COMMENT: Comment Wheel 
 *
 * @author malcolmr
 */
public class Wheel {
    
    private static final int CIRCLE_STEPS = 10;
    
    private double myZ;
    private double myX;
    private double myAngle;
    
    public Wheel(double x, double z, double angle) {
        myX = x;
        myZ = z;
        myAngle = angle;
    }

    /**
     * COMMENT: draw
     * 
     * @param gl
     */
    public void draw(GL2 gl) {

        gl.glPushMatrix();
        gl.glTranslated(myX, 0, myZ);
        gl.glRotated(myAngle, 0, 1, 0);
        
        float[] difColor = {0.1f, 0.1f, 0.1f, 1}; 
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_DIFFUSE, difColor, 0);
        //gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, difColor, 0);
        //gl.glMaterialf(GL2.GL_FRONT, GL2.GL_SHININESS, 120);
        gl.glBegin(GL2.GL_TRIANGLE_FAN);
        {
            gl.glNormal3d(0, 0, -1);
            gl.glVertex3d(0, 0, 0);
        
            double angleStep = 2 * Math.PI / CIRCLE_STEPS; 
            for (int i = 0; i <= CIRCLE_STEPS; i++) {
                double x = Math.cos(i * angleStep);
                double y = Math.sin(i * angleStep);
                
                gl.glVertex3d(x, y, 0);
            }
            gl.glVertex3d(1, 0, 0);            
        }
        gl.glEnd();
        
        gl.glBegin(GL2.GL_TRIANGLE_FAN);
        {
            gl.glNormal3d(0, 0, 1);
            gl.glVertex3d(0, 0, 1);
            
            double angleStep = 2 * Math.PI / CIRCLE_STEPS; 
            for (int i = 0; i <= CIRCLE_STEPS; i++) {
                double x = Math.cos(i * angleStep);
                double y = Math.sin(i * angleStep);
                
                gl.glVertex3d(-x, y, 1);
            }
            gl.glVertex3d(1, 0, 0);            

        }
        gl.glEnd();
        
        gl.glBegin(GL2.GL_QUADS);
        {
            double angleStep = 2 * Math.PI / CIRCLE_STEPS; 
            for (int i = 0; i < CIRCLE_STEPS; i++) {
                double a0 = i * angleStep;
                double a1 = ((i+1) % CIRCLE_STEPS) * angleStep;
                
                double x0 = Math.cos(a0);
                double y0 = Math.sin(a0);

                double x1 = Math.cos(a1);
                double y1 = Math.sin(a1);

                gl.glNormal3d(x0, y0, 0);
                gl.glVertex3d(x0, y0, 0);
                gl.glVertex3d(x0, y0, 1);

                gl.glNormal3d(x1, y1, 0);
                gl.glVertex3d(x1, y1, 1);
                gl.glVertex3d(x1, y1, 0);
                
            }

        }
        gl.glEnd();
        
        
        
        
        gl.glPopMatrix();
        
    }
    
    
    

}
