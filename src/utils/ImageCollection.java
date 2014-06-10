package utils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

public class ImageCollection {
	private static Map<String, BufferedImage> images = new HashMap<String, BufferedImage>();
	static {
		try {
			images.put("heart", ImageIO.read(new File("images//other//heart.png")));
			images.put("armor", ImageIO.read(new File("images//other//armor.png")));
		} catch(IOException e) {
			System.out.println("Couldn't load effects\n" + e.getMessage());
		}
	}
	
	public static BufferedImage getSpriteForName(String name) {
		return images.get(name);
	}
}
