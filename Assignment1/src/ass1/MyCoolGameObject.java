package ass1;

import javax.media.opengl.GL2;

public class MyCoolGameObject extends GameObject{

	private double myPoints[];
	
	public MyCoolGameObject() {
		super(ROOT);
		myPoints = new double[] {
				0.1, 0, 0.1, 0.2, 0.4, 0.2, 0.1, 0.45,
				0.3, 0.45, 0.1, 0.6, 0.2, 0.6, 0, 0.75,
				-0.2, 0.60, -0.1, 0.60, -0.3, 0.45,
				-0.1, 0.45, -0.4, 0.2, -0.1, 0.2, -0.1, 0 
		};
		CircularGameObject c1 = new CircularGameObject(this, 0.03, new double[]{1,0,0,1}, null);
		CircularGameObject c2 = new CircularGameObject(this, 0.03, new double[]{1,0,0,1}, null);
		c1.translate(0.3, 0.42);
		c2.translate(-0.3, 0.42);
	}
	
	@Override
	public void drawSelf(GL2 gl) {
		gl.glEnable(GL2.GL_LINE_SMOOTH);
		gl.glBegin(GL2.GL_LINE_STRIP);
		{
			for(int i = 0; i < myPoints.length; i+=2)
    			gl.glVertex2d(myPoints[i], myPoints[i+1]);
		}
		gl.glEnd();
		gl.glDisable(GL2.GL_LINE_SMOOTH);
	}

}
