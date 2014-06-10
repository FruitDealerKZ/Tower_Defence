package effects;

import units.AbstractMonster;

public class SlowEffect extends Effect {
	public SlowEffect(int ticks, float value) {
		super(ticks, value);
	}
	@Override
	public void affect(AbstractMonster monster) {
		monster.setAcceleration(-monster.getSpeed() * getValue());
	}
	@Override
	public boolean equals(Object effect) {
		if(effect instanceof SlowEffect)
			return true;
		return false;
	}
	@Override
	public int hashCode() {
		return 0;
	}
	@Override
	public void dispose(AbstractMonster monster) {
		monster.setAcceleration(0);
	}
}
