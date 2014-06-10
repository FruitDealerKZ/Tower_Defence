package utils;

public class GMath {
	/**
	 * 
	 * @param p1 - tower position
	 * @param p2 - monster position
	 * @return angle between p1 and p2
	 */
	public static double angle(Position p1, Position p2) {
		double deltaX = p1.X() - p2.X();
		double deltaY = p1.Y() - p2.Y();
		double angle = Math.atan2(deltaY, deltaX) * 180 / Math.PI + 180;
		return angle;
	}
	
	public static double distance(Position p1, Position p2) {
		return Math.pow(Math.pow(p1.X() - p2.X(), 2) + Math.pow(p1.Y() - p2.Y(), 2), 0.5d);
	}
}
