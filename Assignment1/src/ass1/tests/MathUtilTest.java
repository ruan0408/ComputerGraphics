package ass1.tests;

import ass1.MathUtil;
import junit.framework.TestCase;

import org.junit.Test;

/**
 * COMMENT: Comment ass1.tests.MathUtilTest
 *
 * @author malcolmr
 */
public class MathUtilTest extends TestCase {

    // to account for rounding errors on doubles, we will
    // test to within epsilon of the correct answer:
    
    private static final double EPSILON = 0.001;

    @Test
    public void testTranslation0() {
        double[] v = {0,0};
        double[][] m = MathUtil.translationMatrix(v);
        
        // Should be:
        // [[1,0,0]
        //  [0,1,0]
        //  [0,0,1]]
    
        assertEquals(1.0, m[0][0], EPSILON);
        assertEquals(0.0, m[0][1], EPSILON);
        assertEquals(0.0, m[0][2], EPSILON);

        assertEquals(0.0, m[1][0], EPSILON);
        assertEquals(1.0, m[1][1], EPSILON);
        assertEquals(0.0, m[1][2], EPSILON);

        assertEquals(0.0, m[2][0], EPSILON);
        assertEquals(0.0, m[2][1], EPSILON);
        assertEquals(1.0, m[2][2], EPSILON);    
    }
    
    @Test
    public void testTranslation1() {
        double[] v = {2,3};
        double[][] m = MathUtil.translationMatrix(v);
        
        // Should be:
        // [[1,0,2]
        //  [0,1,3]
        //  [0,0,1]]
    
        assertEquals(1.0, m[0][0], EPSILON);
        assertEquals(0.0, m[0][1], EPSILON);
        assertEquals(2.0, m[0][2], EPSILON);

        assertEquals(0.0, m[1][0], EPSILON);
        assertEquals(1.0, m[1][1], EPSILON);
        assertEquals(3.0, m[1][2], EPSILON);

        assertEquals(0.0, m[2][0], EPSILON);
        assertEquals(0.0, m[2][1], EPSILON);
        assertEquals(1.0, m[2][2], EPSILON);
    
    }

    @Test
    public void testRotation0() {
        double theta = 0;
        double[][] m = MathUtil.rotationMatrix(theta);
        
        // Should be:
        // [[1,0,0]
        //  [0,1,0]
        //  [0,0,1]]
    
        assertEquals(1.0, m[0][0], EPSILON);
        assertEquals(0.0, m[0][1], EPSILON);
        assertEquals(0.0, m[0][2], EPSILON);

        assertEquals(0.0, m[1][0], EPSILON);
        assertEquals(1.0, m[1][1], EPSILON);
        assertEquals(0.0, m[1][2], EPSILON);

        assertEquals(0.0, m[2][0], EPSILON);
        assertEquals(0.0, m[2][1], EPSILON);
        assertEquals(1.0, m[2][2], EPSILON);    
    }

    @Test
    public void testRotation1() {
        double theta = 90;
        double[][] m = MathUtil.rotationMatrix(theta);
        
        // Should be:
        // [[0,-1,0]
        //  [1,0,0]
        //  [0,0,1]]
    
        assertEquals(0.0, m[0][0], EPSILON);
        assertEquals(-1.0, m[0][1], EPSILON);
        assertEquals(0.0, m[0][2], EPSILON);

        assertEquals(1.0, m[1][0], EPSILON);
        assertEquals(0.0, m[1][1], EPSILON);
        assertEquals(0.0, m[1][2], EPSILON);

        assertEquals(0.0, m[2][0], EPSILON);
        assertEquals(0.0, m[2][1], EPSILON);
        assertEquals(1.0, m[2][2], EPSILON);    
    }

    @Test
    public void testRotation2() {
        double theta = -90;
        double[][] m = MathUtil.rotationMatrix(theta);
        
        // Should be:
        // [[0,1,0]
        //  [-1,0,0]
        //  [0,0,1]]
    
        assertEquals(0.0, m[0][0], EPSILON);
        assertEquals(1.0, m[0][1], EPSILON);
        assertEquals(0.0, m[0][2], EPSILON);

        assertEquals(-1.0, m[1][0], EPSILON);
        assertEquals(0.0, m[1][1], EPSILON);
        assertEquals(0.0, m[1][2], EPSILON);

        assertEquals(0.0, m[2][0], EPSILON);
        assertEquals(0.0, m[2][1], EPSILON);
        assertEquals(1.0, m[2][2], EPSILON);    
    }
    
    @Test
    public void testScale0() {
        double scale = 1;
        double[][] m = MathUtil.scaleMatrix(scale);
        
        // Should be:
        // [[1,0,0]
        //  [0,1,0]
        //  [0,0,1]]
    
        assertEquals(1.0, m[0][0], EPSILON);
        assertEquals(0.0, m[0][1], EPSILON);
        assertEquals(0.0, m[0][2], EPSILON);

        assertEquals(0.0, m[1][0], EPSILON);
        assertEquals(1.0, m[1][1], EPSILON);
        assertEquals(0.0, m[1][2], EPSILON);

        assertEquals(0.0, m[2][0], EPSILON);
        assertEquals(0.0, m[2][1], EPSILON);
        assertEquals(1.0, m[2][2], EPSILON);    
    }

    @Test
    public void testScale1() {
        double scale = 2;
        double[][] m = MathUtil.scaleMatrix(scale);
        
        // Should be:
        // [[2,0,0]
        //  [0,2,0]
        //  [0,0,1]]
    
        assertEquals(2.0, m[0][0], EPSILON);
        assertEquals(0.0, m[0][1], EPSILON);
        assertEquals(0.0, m[0][2], EPSILON);

        assertEquals(0.0, m[1][0], EPSILON);
        assertEquals(2.0, m[1][1], EPSILON);
        assertEquals(0.0, m[1][2], EPSILON);

        assertEquals(0.0, m[2][0], EPSILON);
        assertEquals(0.0, m[2][1], EPSILON);
        assertEquals(1.0, m[2][2], EPSILON);    
    }

}
