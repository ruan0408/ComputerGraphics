package week7.modeling.extrusion;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import javax.media.opengl.GL2;

/**
 * COMMENT: Comment Polygon 
 *
 * @author malcolmr
 */
public class Polygon {
    private List<Point> myPoints;
    private double[] myNormal = null;
    private Color myColor;

    public Polygon() {
        myPoints = new ArrayList<Point>();
        myColor = Color.BLACK;
    }

    public List<Point> getPoints() {
        return myPoints;
    }

    public void addPoint(double x, double y, double z) {
        myPoints.add(new Point(x, y, z));
        myNormal = null; // to be recomputed
    }

    public void addPoint(Point p) {
        myPoints.add(p);
        myNormal = null; // to be recomputed
    }

    public Color getColor() {
        return myColor;
    }

    public void setColor(Color color) {
        myColor = color;
    }

    public void draw(GL2 gl) {

        double red = myColor.getRed() / 255.0;
        double green = myColor.getGreen() / 255.0;
        double blue = myColor.getBlue() / 255.0;

        gl.glColor3d(red, green, blue);

        gl.glBegin(GL2.GL_POLYGON);

        if (myNormal == null) {
            computeNormal();
        }

        // all vertices have the same normal
        gl.glNormal3d(myNormal[0], myNormal[1], myNormal[2]);
        for (Point p : myPoints) {
            p.draw(gl);
        }
        gl.glEnd();
    }

    //This normal has not been normalised.
    //Implementation of Newell's method for calculating face normals
    private void computeNormal() {
        double[] n = new double[3];

        int size = myPoints.size();
        for (int i = 0; i < size; i++) {
            Point p0 = myPoints.get(i);
            Point p1 = myPoints.get((i + 1) % size);

            n[0] += (p0.y - p1.y) * (p0.z + p1.z);
            n[1] += (p0.z - p1.z) * (p0.x + p1.x);
            n[2] += (p0.x - p1.x) * (p0.y + p1.y);
        }

        myNormal = n;
    }

}
