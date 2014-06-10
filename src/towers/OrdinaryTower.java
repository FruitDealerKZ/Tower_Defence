package towers;


import java.util.Iterator;
import java.util.List;

import specialEffects.SpecialEffect;
import units.AbstractMonster;
import utils.GMath;
import utils.TowerType;

public class OrdinaryTower extends AbstractTower {
	public OrdinaryTower(float x, float y) {
		super(x, y, 1, 1);
		setDamageType(TowerType.ORDINARY);
		setAttackRate(500);
		setCost(60);
		setConstructionTime(4000);
	}

	@Override
	public void attack(List<AbstractMonster> monsters) {
		int x = 0;
		int y = 0;
		int x1 = (int)getPosition().X() - getRadius();
		int x2 = (int)getPosition().X() + getRadius();
		int y1 = (int)getPosition().Y() - getRadius();
		int y2 = (int)getPosition().Y() + getRadius();
		if(monsters.size() > 0) {
			setTargetMonster(monsters.get(monsters.size()/2));
			rotateToMonster();
		}
		for(AbstractMonster m : monsters) {
			x = (int)m.getPosition().X();
			y = (int)m.getPosition().Y();
			if((x >= x1 && x <= x2) && (y >= y1 && y <= y2)) {
				m.damage(getDamage());
				setTargetMonster(m);
				//addEffect(createEffect("bulletEffect", 0, Board.TIMESHIFT, getPosition().X(), getPosition().Y(), 0));
				rotateToMonster();
				m.getAnimator().setYIndex(1);
				m.getAnimator().setTicks(10);
				m.getAnimator().setTimeShift(180);
				getAnimator().setYIndex(0);
				getAnimator().setTicks(20);
				getAnimator().setTimeShift(10);
				break;
			}
		}
	}

	@Override
	public OrdinaryTower createTower() {
		OrdinaryTower o = new OrdinaryTower(getPosition().X(), getPosition().Y());
		return o;
	}

	@Override
	protected int getImageIndex() {
		return 0;
	}
	
	@Override
	public List<SpecialEffect> getEffects() {
		Iterator<SpecialEffect> iter = effects.iterator();

		while(iter.hasNext()) {
			SpecialEffect effect = iter.next();
			if(GMath.distance(effect.getPosition(), getTargetMonster().getPosition()) < 0.2)
				iter.remove();
			else {
				effect.getPosition().setX(effect.getPosition().X() + 0.1f);
			}
		}
		
		return effects;
	}
}
