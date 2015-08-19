import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;

/**
 * COMMENT: Comment Mouse 
 *
 * @author malcolmr
 */
public class MouseUtils {

    //Converts screen(viewport) co-ordinates x and y into world co-ordinates   
    public static double[] computeMousePosition(GL2 gl, int x, int y) {
        int viewport[] = new int[4];
        double mvmatrix[] = new double[16];
        double projmatrix[] = new double[16];

        double mousePoints[] = new double[3];
       
        gl.glGetIntegerv(GL2.GL_VIEWPORT, viewport, 0);
        gl.glGetDoublev(GL2.GL_MODELVIEW_MATRIX, mvmatrix, 0);
        gl.glGetDoublev(GL2.GL_PROJECTION_MATRIX, projmatrix, 0);

        GLU glu = new GLU();
        /* note viewport[3] is height of window in pixels */
        y = viewport[3] - y - 1;
        
        
        glu.gluUnProject((double) x, (double) y, 0.0, //
        		mvmatrix, 0, projmatrix, 0, viewport, 0, mousePoints, 0);

        return mousePoints;
    }

    
}
