package utils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

public class SpritesCollection {
	private static Map<String, SpriteSheet> sprites = new HashMap<String, SpriteSheet>();
	
	static {
		try {
			sprites.put("laserEffect", new SpriteSheet(ImageIO.read(new File("images//effects//laserEffect.png")), 200, 50));
			sprites.put("bulletEffect", new SpriteSheet(ImageIO.read(new File("images//effects//bulletEffect.png")), 10, 5));
		} catch(IOException e) {
			System.out.println("Couldn't load effects\n" + e.getMessage());
		}
	}
	
	public static SpriteSheet getSpriteForName(String name) {
		return sprites.get(name);
	}
}
