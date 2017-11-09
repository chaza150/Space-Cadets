
public class SobelCoordinate {

	float theta;
	int x;
	int y;
	
	public SobelCoordinate(int x, int y, float theta) {
		this.x = x;
		this.y = y;
		this.theta = theta;
	}

	public float getTheta() {
		return theta;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
}
