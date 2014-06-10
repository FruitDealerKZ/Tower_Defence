package utils;

import java.awt.image.BufferedImage;

public class SpriteSheet {
	private BufferedImage spriteSheet;
	private final int xTILESIZE;
	private final int yTILESIZE;
	private int x = 0;
	private int y = 0;
	
	public SpriteSheet(BufferedImage image, int tileSizeX, int tileSizeY) {
		spriteSheet = image;
		this.xTILESIZE = tileSizeX;
		this.yTILESIZE = tileSizeY;
	}
	
	public BufferedImage getSprite() {
		BufferedImage sprite = spriteSheet.getSubimage(x * xTILESIZE, y * yTILESIZE, xTILESIZE, yTILESIZE);
		return sprite;
	}
	
	public BufferedImage nextSprite() {
		x = (x + 1) % (spriteSheet.getWidth() / xTILESIZE);
		return getSprite();
	}
	
	public void setIndexX(int x) { 
		this.x = x;
	}
	
	public void setIndexY(int y) {
		this.y = y;
	}
	
	@Override
	public int hashCode() {
		return spriteSheet.hashCode();
	}
	
	@Override
	public boolean equals(Object o) {
		return spriteSheet.equals(o);
	}
}