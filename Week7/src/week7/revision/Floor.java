package week7.revision;

import javax.media.opengl.GL2;

/**
 * COMMENT: Comment Floor 
 *
 * @author malcolmr
 */
public class Floor {
    
    private double myWidth;
    private double myDepth;

    public Floor(double width, double depth) {
        myWidth = width;
        myDepth = depth;
    }

    public void draw(GL2 gl) {
        
        float[] difColor = {0.0f, 0.5f, 0.0f, 1}; 
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_DIFFUSE, difColor, 0);
       
        gl.glPushMatrix();
        gl.glScaled(myWidth, 1, myDepth);
        
        gl.glBegin(GL2.GL_QUADS);
        {
            gl.glNormal3d(0, 1, 0);
            gl.glVertex3d(-1, -1, -1);
            gl.glVertex3d(1, -1, -1);
            gl.glVertex3d(1, -1, 1);
            gl.glVertex3d(-1, -1, 1);
        }
        gl.glEnd();        
        
        gl.glPopMatrix();
    }
    
}
