package week7.revision;

import javax.media.opengl.GL2;

/**
 * COMMENT: Comment Car 
 *
 * @author malcolmr
 */
public class Car {

    private static final double WIDTH = 1;
    private static final double LENGTH = 2;
    private static final double HEIGHT = 0.5;
    
    private Wheel[] myWheels;
    
    public Car() {
        myWheels = new Wheel[4];
        
        myWheels[0] = new Wheel(WIDTH, LENGTH - 0.5, 90);
        myWheels[1] = new Wheel(-WIDTH, LENGTH -0.5 , -90);
        myWheels[2] = new Wheel(WIDTH, -LENGTH +0.5, 90);
        myWheels[3] = new Wheel(-WIDTH, -LENGTH +0.5, -90);
        
    }
    
    public void draw(GL2 gl) {
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glPushMatrix();
                
        drawBody(gl);
        
        for (int i = 0; i < myWheels.length; i++) {
            myWheels[i].draw(gl);
        }
        
        
        gl.glPopMatrix();
    }

    private void drawBody(GL2 gl) {
        float[] difColor = {1.0f, 0f, 0f, 1}; 
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_DIFFUSE, difColor, 0);

        gl.glPushMatrix();
        
        gl.glScaled(WIDTH, HEIGHT, LENGTH);
        gl.glBegin(GL2.GL_QUADS);
        {
            // top y = 1
            gl.glNormal3d(0,1,0);
            gl.glVertex3d(-1, 1, -1);
            gl.glVertex3d(1, 1, -1);
            gl.glVertex3d(1, 1, 1);
            gl.glVertex3d(-1, 1, 1);

            // bottom y = -1
            gl.glNormal3d(0,-1,0);
            gl.glVertex3d(-1, -1, -1);
            gl.glVertex3d(-1, -1, 1);
            gl.glVertex3d(1, -1, 1);
            gl.glVertex3d(1, -1, -1);

            // left x = -1
            gl.glNormal3d(-1,0,0);
            gl.glVertex3d(-1, -1, -1);
            gl.glVertex3d(-1, 1, -1);
            gl.glVertex3d(-1, 1, 1);
            gl.glVertex3d(-1, -1, 1);
            
            // right x = 1
            gl.glNormal3d(1,0,0);
            gl.glVertex3d(1, -1, -1);
            gl.glVertex3d(1, -1, 1);
            gl.glVertex3d(1, 1, 1);
            gl.glVertex3d(1, 1, -1);

            // front z = 1
            gl.glNormal3d(0,0,1);
            gl.glVertex3d(-1, -1, 1);
            gl.glVertex3d(1, -1, 1);
            gl.glVertex3d(1, 1, 1);
            gl.glVertex3d(-1, 1, 1);

            // back z = -1
            gl.glNormal3d(0,0,-1);
            gl.glVertex3d(-1, -1, -1);
            gl.glVertex3d(-1, 1, -1);
            gl.glVertex3d(1, 1, -1);
            gl.glVertex3d(1, -1, -1);
        }        
        gl.glEnd();
        
        gl.glPopMatrix();

    }

}
