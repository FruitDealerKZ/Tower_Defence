package utils;

public class Position {
	private float x;
	private float y;
	
	public Position(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public void setX(float x) {
		this.x = x;
	}
	
	public void setY(float y) {
		this.y = y;
	}
	
	public float X() {
		return x;
	}
	
	public float Y() {
		return y;
	}
	
	@Override
	public String toString() {
		return String.format("{%f;%f}",x,y);
	}
}
