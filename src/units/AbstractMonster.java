package units;

import effects.Effect;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.imageio.ImageIO;

import components.Board;

import utils.Animator;
import utils.SpriteSheet;
import utils.Generator;


public abstract class AbstractMonster extends MotionObject implements Generator<AbstractMonster> {
	private static int counter = 0;
	private final int id = counter++;
	private int reward;
	private Set<Effect> effects = new HashSet<>();
	
	public static class Singleton {
		private static List<BufferedImage> images = new ArrayList<>();
		static {
			try {
				images.add(ImageIO.read(new File("images//monsters//monsterSprite.png")));
				images.add(ImageIO.read(new File("images//monsters//fastmonsterSprite.png")));
				images.add(ImageIO.read(new File("images//monsters//pigSprite.png")));
			} catch (IOException e) {}

		}
		public static BufferedImage getImage(int index) {
			return images.get(index);
		}
	}
	
	public AbstractMonster(float x, float y, float speed) {
		super(x,y,speed);
		SpriteSheet sprite = new SpriteSheet(Singleton.getImage(getImageIndex()), (int) (50 * Board.CURRENT_SCALE_FACTOR), (int)(50 * Board.CURRENT_SCALE_FACTOR), Board.CURRENT_SCALE_FACTOR);
		setAnimator(new Animator(sprite));
		getAnimator().setTicks(1000);
		getAnimator().setTimeShift(150);
		setSize((int)(50 * Board.CURRENT_SCALE_FACTOR), (int)(50 * Board.CURRENT_SCALE_FACTOR));
	}
	
	protected abstract int getImageIndex();
	
	public void damage(float damage) {
		float dif = getArmor() - damage;
		if(dif  < 0)
			setHealth(getHealth() + dif);
	}
	
	public int getReward() {
		return reward;
	}
	
	protected void setReward(int reward) {
		this.reward = reward;
	}
	
	public void killMonster() {
		Animator anim = getAnimator();
		anim.setTicks(6);
		anim.setTimeShift(50);
		anim.setYIndex(3);
	}
	
	public void addEffect(Effect effect) {
		effects.add(effect);
	}
	@Override
	public abstract AbstractMonster next(float x, float y);
	
	@Override
	public String toString() {
		return "Monster #" + id + "\n" + super.toString() + String.format("\nHealth = %d, Armor = %d", getHealth(), getArmor());
	}
	@Override
	public List<String> getDetails() {
		List<String> details = new ArrayList<>();
		details.add("Health: " + getHealth());
		details.add("Armor: " + getArmor());
		details.add("Speed: " + (getSpeed() - getAcceleration()));
		return details;
	}
	
	public void applyEffects() {
		Iterator<Effect> effects = this.effects.iterator();
		Effect ef = null;

		while(effects.hasNext()) {
			ef = effects.next();
			if(ef.getTicks() == 0) {
				ef.dispose(this);
				effects.remove();
			} else {
				ef.applyAffect(this);
			}
		}
	}
}
