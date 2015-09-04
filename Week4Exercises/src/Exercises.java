import javax.media.opengl.*;
import javax.media.opengl.awt.GLJPanel;
import javax.media.opengl.glu.GLU;
import javax.swing.*;

/**
 * Created by ruan0408 on 22/08/15.
 */
public class Exercises implements GLEventListener {

    public static void main(String[] args) {
        GLProfile glProfile = GLProfile.getDefault();
        GLCapabilities glCapabilities = new GLCapabilities(glProfile);

        GLJPanel panel = new GLJPanel(glCapabilities);
        final JFrame frame = new JFrame("Exercises");
        frame.setSize(300,300);
        frame.add(panel);
        frame.setVisible(true);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        panel.addGLEventListener(new Exercises());
    }

    @Override
    public void init(GLAutoDrawable glAutoDrawable) {
        GL2 gl2 = glAutoDrawable.getGL().getGL2();
        gl2.glEnable(GL2.GL_DEPTH_TEST);
    }

    @Override
    public void display(GLAutoDrawable glAutoDrawable) {
        GL2 gl2 = glAutoDrawable.getGL().getGL2();
        gl2.glMatrixMode(GL2.GL_MODELVIEW);
        gl2.glLoadIdentity();

        gl2.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
        gl2.glColor4d(1,0,0,1);
        gl2.glBegin(GL2.GL_TRIANGLES);
        {
            gl2.glNormal3dv(getNormal(new double[]{2,1,-4}, new double[]{0,-1,-3}, new double[]{-2,1,-4}),0);
            gl2.glVertex3d(2, 1, -4);
            gl2.glVertex3d(0, -1, -3);
            gl2.glVertex3d(-2, 1, -4);
        }
        gl2.glEnd();
    }

    @Override
    public void reshape(GLAutoDrawable glAutoDrawable, int i, int i1, int i2, int i3) {
        GL2 gl = glAutoDrawable.getGL().getGL2();
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        GLU glu = new GLU();
        //gl.glOrtho(-3,3,-3,3,3,8);
        //gl.glFrustum(-3,3,-3,3,0.5,8);
        //gl.glFrustum(-3,3,-3,3,-2,8);
        glu.gluPerspective(60, 1, 2, 8);
        //glu.gluPerspective(60,1,0,8);
        //gl.glOrtho(-1.5,1.5,-1.5,1.5,1,8);
    }

    @Override
    public void dispose(GLAutoDrawable glAutoDrawable) {

    }

    private double [] cross(double u [], double v[]){
        double crossProduct[] = new double[3];
        crossProduct[0] = u[1]*v[2] - u[2]*v[1];
        crossProduct[1] = u[2]*v[0] - u[0]*v[2];
        crossProduct[2] = u[0]*v[1] - u[1]*v[0];
        //System.out.println("CP " + crossProduct[0] + " " +  crossProduct[1] + " " +  crossProduct[2]);
        return crossProduct;
    }

    private double [] getNormal(double[] p0, double[] p1, double[] p2){
        double u[] = {p1[0] - p0[0], p1[1] - p0[1], p1[2] - p0[2]};
        double v[] = {p2[0] - p0[0], p2[1] - p0[1], p2[2] - p0[2]};

        return cross(u,v);

    }
}