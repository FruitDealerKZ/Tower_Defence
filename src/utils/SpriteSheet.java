package utils;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

public class SpriteSheet {
	private BufferedImage spriteSheet;
	private final int xTILESIZE;
	private final int yTILESIZE;
	private int x = 0;
	private int y = 0;
	
	public SpriteSheet(BufferedImage image, int tileSizeX, int tileSizeY, double scale) {
		if(scale != 1)
			spriteSheet = scaleImage(image, scale);
		else
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
	
	private BufferedImage scaleImage(BufferedImage before, double scaleKoeff) {
		int w = before.getWidth();
		int h = before.getHeight();
		BufferedImage after = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		AffineTransform at = new AffineTransform();
		at.scale(scaleKoeff, scaleKoeff);
		AffineTransformOp scaleOp = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
		after = scaleOp.filter(before, after);
		return after;
	}
}