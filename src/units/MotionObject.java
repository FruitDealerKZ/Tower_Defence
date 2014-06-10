package units;

import utils.Position;

public class MotionObject extends GameObject {
	private float acceleration;
	private float speed;
	private int dirX;
	private int dirY;
	private boolean isRotating;
	private boolean rotateX;
	private boolean rotateY;
	private float padding = 0.45f;
	private float currentPadding = 0.0f;
	private float currentAngle;
	public int rotateDirection = 1;
	
	public MotionObject() {
		super();
	}
	
	public MotionObject(float x, float y, float speed) {
		super(x,y);
		this.speed = speed;
	}
	
	public void setDirX(int dir) {
		dirX = dir;
		if(dir > 0) {
			isRotating = true;
			rotateDirection = currentAngle == 90 ? -1 : 1;
			currentAngle = 0;
		}
		if(dir < 0) {
			isRotating = true;
			rotateDirection = currentAngle == 90 ? 1 : -1;
			currentAngle = 180;
		}
		if(dir != 0 && dirY != 0) {
			rotateY = true;
			currentPadding = padding;
			dirY = 0;
		}
		System.out.println("DirX");
	}
	
	public void setDirY(int dir) {
		dirY = dir;
		if(dir > 0) {
			isRotating = true;
			rotateDirection = currentAngle == 0 ? 1 : -1;
			currentAngle = 90;
		}
		if(dir < 0) {
			isRotating = true;
			if(getAngle() == 0)
				setAngle(360);
			rotateDirection = currentAngle == 0 ? -1 : 1;
			currentAngle = 270;
		}
		if(dir != 0 && dirX != 0) {
			rotateX = true;
			currentPadding = padding;
			dirX = 0;
		}
		System.out.println("DirY");
	}
	
	public int getDirX() {
		return dirX;
	}
	
	public int getDirY() {
		return dirY;
	}
	
	public float getSpeed() {
		return speed;
	}
	
	public void setSpeed(float speed) {
		this.speed = speed;
	}
	
	public void setAcceleration(float a) {
		acceleration = a;
	}
	
	public float getAcceleration() {
		return acceleration;
	}
	
	public void rotate() {
		isRotating = true;
		System.out.println("Rotate");
	}
	
	public void move() {
		Position pos = getPosition();
		
		if(dirX != 0) {
			pos.setY(pos.Y());
		}
		if(dirY != 0) {
			pos.setX(pos.X());
		}
		if(isRotating) {
			setAngle((getAngle() + rotateDirection * 2.2f) % 360);
			if(Math.abs(currentAngle - getAngle()) <= 2.2f) {
				setAngle(currentAngle);
				isRotating = false;
			}
		}
		
		if(rotateX) {
			currentPadding -= acceleration + speed;
			if(currentPadding < (acceleration + speed)) {
				rotateX = false;
				dirX = 0;
				currentPadding = padding;
			}
		}
		if(rotateY) {
			currentPadding -= acceleration + speed;
			if(currentPadding < (acceleration + speed)) {
				rotateY = false;
				dirY = 0;
				currentPadding = padding;
			}
		}
		
		pos.setX(pos.X() + acceleration * dirX + speed * dirX);
		pos.setY(pos.Y() + acceleration * dirY + speed * dirY);
		setPosition(pos);
		System.out.println("Dirx = " + dirX + " DirY = " + dirY);
		//System.out.println(pos);
	}
	
	@Override
	public String toString() {
		return super.toString() + String.format(" (dirX:dirY) = {%d:%d}. Speed = %f", dirX,dirY,speed); 
	}
}
