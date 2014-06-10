package scenes;

public interface Scene {
	public void update(int timeShift);
	
	public void redraw(float interpolation);
}
