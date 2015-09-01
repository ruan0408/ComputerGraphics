package ass1;

import javax.media.opengl.GL2;

public class LineGameObject extends GameObject {

	// In the form [x1, y1, x2, y2]
	double[] myPoints;
	double[] myLineColour;
	
	//Create a ass1.LineGameObject from (x1,y1) to (x2,y2)
	public LineGameObject(GameObject parent,  double x1, double y1, 
												double x2, double y2,
		                                        double[] lineColour) {
		super(parent);
		myPoints = new double[4];
		myPoints[0] = x1;
		myPoints[1] = y1;
		myPoints[2] = x2;
		myPoints[3] = y2;
		myLineColour = lineColour;
	}
		
	//Create a ass1.LineGameObject from (0,0) to (1,0)
	public LineGameObject(GameObject parent, double[] lineColour) {
		this(parent, 0, 0, 1, 0, lineColour);
	}

	public double[] getPoints() {
        return myPoints;
    }
    
    public double[] getLineColour() {
        return myLineColour;
    }

    public void setLineColour(double[] lineColour) {
        myLineColour = lineColour;
    }
    
    @Override
    public void drawSelf(GL2 gl) {

    	if(myLineColour != null) {
    		gl.glColor4d(myLineColour[0], myLineColour[1], myLineColour[2], myLineColour[3]);
    		gl.glBegin(GL2.GL_LINES);
    			gl.glVertex2d(myPoints[0], myPoints[1]);
    			gl.glVertex2d(myPoints[2], myPoints[3]);
    		gl.glEnd();
    	}
    }
}
