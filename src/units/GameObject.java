package units;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import specialEffects.SpecialEffect;
import utils.Animator;
import utils.Position;


public class GameObject implements Details {
	private Animator animator;
	private Position position;
	private float angle;
	private boolean isSelected;
	private float health;
	private float armor;
	private Dimension size;
	
	protected List<SpecialEffect> effects = new ArrayList<SpecialEffect>();
	
	public GameObject() {
		
	}
	
	public GameObject(float x, float y) {
		position = new Position(x, y);
	}
	
	public Position getPosition() {
		return position;
	}
	
	public void setPosition(Position p) {
		position = p;
	}
	
	public boolean isSelected() {
		return isSelected;
	}
	
	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}
		
	public void setAnimator(Animator animator) {
		this.animator = animator;
	}
	
	public Animator getAnimator() {
		return animator;
	}
	
	public float getAngle() {
		return angle;
	}
	
	public void setAngle(float angle) {
		this.angle = angle;
	}
	
	public float getHealth() {
		return health;
	}
	
	public void setHealth(float health) {
		this.health  = health;
	}
	
	public float getArmor() {
		return armor;
	}
	
	public void setArmor(int armor) {
		this.armor = armor;
	}
	
	public void setSize(int width, int height) {
		size = new Dimension(width, height);
	}
	
	public Dimension getSize() {
		return size;
	}
	public void addEffect(SpecialEffect effect) {
		effects.add(effect);
	}
	
	public List<SpecialEffect> getEffects() {
		Iterator<SpecialEffect> iter = effects.iterator();
		
		while(iter.hasNext()) {
			SpecialEffect effect = iter.next();
			if(effect.getAnimator().getTicks() == 0)
				iter.remove();
		}
		
		return effects;
	}
	
	@Override
	public String toString() {
		return position.toString();
	}

	@Override
	public List<String> getDetails() {
		return new ArrayList<>();
	}
}
