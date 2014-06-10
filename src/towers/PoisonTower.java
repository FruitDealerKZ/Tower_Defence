package towers;

import java.util.List;

import effects.PoisonEffect;

import units.AbstractMonster;
import utils.TowerType;

public class PoisonTower extends AbstractTower {

	public PoisonTower(float x, float y) {
		super(x, y, 1, 1);
		setDamageType(TowerType.POISON);
		setAttackRate(250);
		setCost(150);
		setConstructionTime(5500);
	}

	@Override
	protected int getImageIndex() {
		return 3;
	}

	@Override
	public void attack(List<AbstractMonster> monsters) {
		int x1 = (int)getPosition().X() - getRadius();
		int x2 = (int)getPosition().X() + getRadius();
		int y1 = (int)getPosition().Y() - getRadius();
		int y2 = (int)getPosition().Y() + getRadius();
		if(monsters.size() > 0) {
			setTargetMonster(monsters.get(monsters.size()/2));
			rotateToMonster();
		}
		for(AbstractMonster m : monsters) {
			int x = (int)m.getPosition().X();
			int y = (int)m.getPosition().Y();
			if((x >= x1 && x <= x2) && (y >= y1 && y <= y2)) {
				m.damage(getDamage());
				setTargetMonster(m);
				rotateToMonster();
				m.addEffect(new PoisonEffect(500, 0.005f));
				m.getAnimator().setYIndex(1);
				m.getAnimator().setTicks(20);
				m.getAnimator().setTimeShift(10);
				getAnimator().setYIndex(0);
				getAnimator().setTicks(20);
				getAnimator().setTimeShift(10);
				break;
			}
		}
	}

	@Override
	public AbstractTower createTower() {
		return new PoisonTower(getPosition().X(), getPosition().Y());
	}

}
