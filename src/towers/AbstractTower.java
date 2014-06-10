package towers;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import specialEffects.SpecialEffect;

import units.AbstractMonster;
import units.GameObject;
import utils.Animator;
import utils.GMath;
import utils.SpriteSheet;
import utils.SpritesCollection;
import utils.TowerType;

public abstract class AbstractTower extends GameObject {
	private int attackRate;
	private int timeElapsed = 0;
	private int damage;
	private int radius;
	private int cost;
	private float rotateSpeed = 1;
	private float rotateTowardsTo;
	private AbstractMonster targetMonster;
	private float buildTime = 0.0f;
	
	private TowerType damageType = TowerType.ORDINARY;
	
	private static class Singletone {
		private static List<BufferedImage> images = new ArrayList<>();
		static {
			try {
				images.add(ImageIO.read(new File("images//towers//ordinary.png")));
				images.add(ImageIO.read(new File("images//towers//gun.png")));
				images.add(ImageIO.read(new File("images//towers//freeze.png")));
				images.add(ImageIO.read(new File("images//towers//poison.png")));
				images.add(ImageIO.read(new File("images//towers//laser.png")));
			} catch (IOException e) { }
		}
		public static BufferedImage getImage(int index) {
			return images.get(index);
		}
	}
		
	public AbstractTower(float x, float y, int damage, int radius) {
		super(x,y);
		this.damage = damage;
		this.radius = radius;
		setAnimator(new Animator(new SpriteSheet(Singletone.getImage(getImageIndex()), 80, 80)));
		setSize(80, 80);
	}
	
	public boolean constructed(float timeShift) {
		return (buildTime -= timeShift) <= 0;
	}
	
	public void setConstructionTime(float time) {
		buildTime = time;
	}
	@Override
	public void setAngle(float angle) {
		int diff = (int)Math.abs(angle - getAngle());
		if(angle < getAngle()) {
			if(diff > 180)
				rotateSpeed = 1;
			else
				rotateSpeed = -1;
		} else if(angle > getAngle()) {
			if(diff > 180)
				rotateSpeed = -1;
			else
				rotateSpeed  = 1;
		} else
			rotateSpeed = 0;
		
		rotateTowardsTo = angle;
	}
	
	public void setTargetMonster(AbstractMonster m) {
		targetMonster = m;
	}
	
	public AbstractMonster getTargetMonster() {
		return targetMonster;
	}
	
	public void setCost(int cost) {
		this.cost = cost;
	}
	
	public int getCost() {
		return cost;
	}
	
	public void setDamage(int damage) {
		this.damage = damage;
	}
	
	public void setRadius(int radius) {
		this.radius = radius;
	}
	
	public int getDamage() {
		return damage;
	}
	
	public int getRadius() {
		return radius;
	}
	
	public TowerType getDamageType() {
		return damageType;
	}
	
	public void setDamageType(TowerType type) {
		damageType = type;
	}
	
	public int getAttackRate() {
		return attackRate;
	}
	
	public void setAttackRate(int rate) {
		attackRate = rate;
		timeElapsed = attackRate;
	}
	
	public boolean isReadyToAttack(int time) {
		if(targetMonster != null) {
			setAngle((float)GMath.angle(this.getPosition(), targetMonster.getPosition()));
		}
		if(getAngle() != rotateTowardsTo) {
			super.setAngle((360 + getAngle() + rotateSpeed) % 360);
		}
		timeElapsed += time;
		if(targetMonster != null && Math.abs(rotateTowardsTo - getAngle()) > 0.5) {
			return false;
		}
		if(timeElapsed >= attackRate) {
			timeElapsed = 0;
			return true;
		}
		return false;
	}
	
	public void rotateToMonster() {
		setAngle((int)GMath.angle(this.getPosition(), targetMonster.getPosition()));
	}
	
	@Override
	public List<String> getDetails() {
		List<String> details = new ArrayList<>();
		details.add("Damage type: " + getDamageType());
		details.add("Damage: " + getDamage());
		details.add("Radius: " + getRadius());
		details.add("Attack speed: " + getAttackRate());
		return details;
	}
	
	public SpecialEffect createEffect(String name, int ticks, int timeDelay, float x, float y, float angle) {
		SpriteSheet sheet = SpritesCollection.getSpriteForName(name);
		if(sheet == null)
			return null;
		
		Animator a = new Animator(sheet);
		a.setTicks(ticks);
		a.setTimeShift(timeDelay);
		
		SpecialEffect effect = new SpecialEffect(x, y);
		effect.setAnimator(a);
		effect.setAngle(angle);
		return effect;
	}
	
	protected abstract int getImageIndex();
	
	public abstract void attack(List<AbstractMonster> monsters);
	
	public abstract AbstractTower createTower();
}
