package towers;

import java.util.List;

import effects.SlowEffect;

import units.AbstractMonster;
import utils.TowerType;

public class SlowTower extends AbstractTower {

	public SlowTower(float x, float y) {
		super(x, y, 0, 1);
		setDamageType(TowerType.SLOW);
		setAttackRate(250);
		setCost(100);
		setConstructionTime(5000);
	}

	@Override
	protected int getImageIndex() {
		return 2;
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
//				m.damage(getDamage());
				setTargetMonster(m);
				rotateToMonster();
				m.addEffect(new SlowEffect(500, 0.5f));
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
		SlowTower st = new SlowTower(getPosition().X(), getPosition().Y());
		return st;
	}

}
