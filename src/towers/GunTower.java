package towers;

import java.util.List;

import units.AbstractMonster;
import utils.TowerType;

public class GunTower extends AbstractTower {
	public GunTower(float x, float y) {
		super(x, y, 1, 1);
		setDamageType(TowerType.GUN);
		setAttackRate(300);
		setCost(120);
		setConstructionTime(5000);
	}
	@Override
	protected int getImageIndex() {
		return 1;
	}

	@Override
	public void attack(List<AbstractMonster> monsters) {
		int x = 0;
		int y = 0;
		int x1 = (int)getPosition().X() - getRadius();
		int x2 = (int)getPosition().X() + getRadius();
		int y1 = (int)getPosition().Y() - getRadius();
		int y2 = (int)getPosition().Y() + getRadius();
		
		boolean isTargetFound = false;
		
		for(AbstractMonster m : monsters) {
			x = (int)m.getPosition().X();
			y = (int)m.getPosition().Y();
			if((x >= x1 && x <= x2) && (y >= y1 && y <= y2)) {
				m.damage(getDamage());
				setTargetMonster(m);
				rotateToMonster();
				isTargetFound = true;
				m.getAnimator().setYIndex(2);
				m.getAnimator().setTicks(10);
				m.getAnimator().setTimeShift(180);
				getAnimator().setYIndex(0);
				getAnimator().setTicks(20);
				getAnimator().setTimeShift(10);
				break;
			}
		}
		
		if(monsters.size() > 0 && !isTargetFound) {
			setTargetMonster(monsters.get(0));
			rotateToMonster();
		}
	}

	@Override
	public GunTower createTower() {
		GunTower g = new GunTower(getPosition().X(), getPosition().Y());
		return g;
	}

}
