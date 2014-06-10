package effects;

import units.AbstractMonster;

public class PoisonEffect extends Effect {

	public PoisonEffect(int ticks, float value) {
		super(ticks, value);
	}

	@Override
	public void affect(AbstractMonster monster) {
		monster.damage(getValue());
	}
	
	@Override
	public boolean equals(Object effect) {
		if(effect instanceof PoisonEffect)
			return true;
		return false;
	}
	@Override
	public int hashCode() {
		return 1;
	}

	@Override
	public void dispose(AbstractMonster monster) {

	}

}
