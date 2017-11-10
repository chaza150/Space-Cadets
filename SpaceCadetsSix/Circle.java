
public class Circle {

	private int x;
	private int y;
	private int radius;
	private int votes;
	
	public Circle(int x, int y, int radius, int votes) {
		this.x = x;
		this.y = y;
		this.radius = radius;
		this.votes = votes;
	}
	
	public int getVotes() {
		return votes;
	}

	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}
	public int getRadius() {
		return radius;
	}
}
