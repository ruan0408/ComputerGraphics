package week7.modeling.extrusion;

/**
 * COMMENT: Comment MathUtil 
 *
 * @author malcolmr
 */
public class MatrixMath {

    /**
     * COMMENT: matrixMultiply
     * 
     * @param t
     * @param r
     * @return
     */
    public static double[][] multiply(double[][] p, double[][] q) {

        double[][] m = new double[4][4];

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                m[i][j] = 0;
                for (int k = 0; k < 4; k++) {
                   m[i][j] += p[i][k] * q[k][j]; 
                }
            }
        }

        return m;
    }

    public static double[] multiply(double[][] m, double[] v) {

        double[] u = new double[4];

        for (int i = 0; i < 4; i++) {
            u[i] = 0;
            for (int j = 0; j < 4; j++) {
                u[i] += m[i][j] * v[j];
            }
        }

        return u;
    }

    public static void print(double[][] m) {

       

        for (int i = 0; i < 4; i++) {
            
            for (int j = 0; j < 4; j++) {
                System.out.print(m[i][j] + " " );
            }
            System.out.println();
        }
    }

}
