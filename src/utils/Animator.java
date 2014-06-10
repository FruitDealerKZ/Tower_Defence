package utils;


import java.awt.image.BufferedImage;

import components.Board;

public class Animator {
	private SpriteSheet sheet;
	private int timeElapsed;
	private int timeShift;
	private int ticks;
	private int defSpriteIndex = 0;
	
	public Animator(SpriteSheet sheet) {
		this.sheet = sheet;
	}
	
	public BufferedImage getImage() {
		timeElapsed += Board.TIMESHIFT;
		if(timeElapsed >= timeShift) {
			timeElapsed = 0;
			if(ticks-- > 0) {
				return sheet.nextSprite();
			}
			else {
				timeShift = 0;
				timeShift = Integer.MAX_VALUE;
				setYIndex(defSpriteIndex);
			}
		}
		return sheet.getSprite();
	}
	
	public void setYIndex(int y) {
		sheet.setIndexY(y);
		sheet.setIndexX(0);
	}
	
	public void setTicks(int ticks) {
		this.ticks = ticks;
	}
	
	public void setTimeShift(int time) {
		timeShift = time;
	}
	
	public int getTicks() {
		return ticks;
	}
}