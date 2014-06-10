package effects;

import units.AbstractMonster;

public abstract class Effect {
	private int ticks;
	private float value;
	public Effect(int ticks, float value) {
		this.ticks = ticks;
		this.value = value;
	}
	
	public void setValue(float value) {
		this.value = value;
	}
	public void setTicks(int ticks) {
		this.ticks = ticks;
	}
	public float getValue() {
		return value;
	}
	public int getTicks() {
		return ticks;
	}
	
	public void applyAffect(AbstractMonster monster) {
		ticks--;
		affect(monster);
	}
	public abstract void affect(AbstractMonster monster);
	public abstract void dispose(AbstractMonster monster);
	public boolean equals(Effect object) {
		return true;
	}
	
	@Override
	public int hashCode() {
		int hash = ticks * (int)value * 147;
		hash *= 31;
		return hash;
	}
}
