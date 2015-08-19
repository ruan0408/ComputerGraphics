import javax.media.opengl.GL2;

public class CircularGameObject extends GameObject {
	
	private double 	 radius;
	private double[] myPoints;
	private double[] myFillColour;
    private double[] myLineColour;
    
	//Create a CircularGameObject with centre 0,0 and a given radius
	public CircularGameObject(GameObject parent, double radius,double[] fillColour, double[] lineColour) {
        super(parent);
        
        this.radius = radius;
        buildCircle();
        myFillColour = fillColour;
        myLineColour = lineColour;
	}
	
	//Create a CircularGameObject with centre 0,0 and radius 1
	public CircularGameObject(GameObject parent, double[] fillColour, double[] lineColour) {
		this(parent, 1.0, fillColour, lineColour);
	}
	
    public double[] getPoints() {
        return myPoints;
    }
    
    public double getRadius() {
    	return radius;
    }

    public void setRadius(double r) {
        radius = r;
    }

    public double[] getFillColour() {
        return myFillColour;
    }

    public void setFillColour(double[] fillColour) {
        myFillColour = fillColour;
    }

    public double[] getLineColour() {
        return myLineColour;
    }

    public void setLineColour(double[] lineColour) {
        myLineColour = lineColour;
    }
    
    @Override
    public void drawSelf(GL2 gl) {

    	if(myFillColour != null) {
    		gl.glColor4d(myFillColour[0], myFillColour[1], myFillColour[2], myFillColour[3]);
        	gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);
        	drawPolygon(gl);
    	}
    	
    	if(myLineColour != null) {
    		gl.glColor4d(myLineColour[0], myLineColour[1], myLineColour[2], myLineColour[3]);
        	gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_LINE);
        	drawPolygon(gl);
    	}
    	gl.glColor4d(1, 1, 1, 1);
    	gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);
    }
    
    private void drawPolygon(GL2 gl) {
    	gl.glBegin(GL2.GL_POLYGON);
    		for(int i = 0; i < myPoints.length; i+=2)
    			gl.glVertex2d(myPoints[i], myPoints[i+1]);
    	gl.glEnd();
    }
    
    private void buildCircle() {
    	myPoints = new double[64];
    	double angle = 0;
        double angleIncrement = 2*Math.PI/32;
        for(int i=0, j = 0; i < 32; i++, j++){
        	angle = i* angleIncrement;
        	double x = radius * Math.cos(angle);
        	double y = radius * Math.sin(angle);
        	myPoints[j] = x;
        	myPoints[++j] = y;
        }
    }
}
