package towers;

import java.util.List;

import units.AbstractMonster;
import utils.GMath;
import utils.Position;
import utils.TowerType;

public class LaserTower extends AbstractTower {	
	public LaserTower(float x, float y) {
		super(x, y, 2, 4);
		setDamageType(TowerType.SPLASH);
		setAttackRate(800);
		setCost(50);
		setConstructionTime(6000);
	}

	@Override
	protected int getImageIndex() {
		return 4;
	}

	@Override
	public void attack(List<AbstractMonster> monsters) {
		Position target = getTargetPosition(monsters);
		if(target == null)
			return;
		else if(monsters.size() > 0)
			rotateToMonster();
		
		int angle = (int)GMath.angle(getPosition(), target);
		
		for(AbstractMonster m : monsters) {
			int mAngle = (int)GMath.angle(getPosition(), m.getPosition());
			double distance = GMath.distance(getPosition(), m.getPosition());
			if(Math.abs(mAngle - angle) < 4 && distance < getRadius()) {
				rotateToMonster();
				m.damage(getDamage());
				setTargetMonster(m);
				m.getAnimator().setYIndex(2);
				m.getAnimator().setTicks(10);
				m.getAnimator().setTimeShift(180);
				getAnimator().setYIndex(0);
				getAnimator().setTicks(20);
				getAnimator().setTimeShift(10);
			}
		}
	}
	
	private Position getTargetPosition(List<AbstractMonster> monsters) {
		int x1 = (int)getPosition().X() - getRadius();
		int x2 = (int)getPosition().X() + getRadius();
		int y1 = (int)getPosition().Y() - getRadius();
		int y2 = (int)getPosition().Y() + getRadius();
		if(monsters.size() > 0) {
			setTargetMonster(monsters.get(monsters.size()/2));
			rotateToMonster();
		}
		
		Position target = null;
		
		for(AbstractMonster m : monsters) {
			int x = (int)m.getPosition().X();
			int y = (int)m.getPosition().Y();
			if((x >= x1 && x <= x2) && (y >= y1 && y <= y2)) {
				addEffect(createEffect("laserEffect", 6, 20, 0, 0, getAngle()));
				setTargetMonster(m);
				rotateToMonster();
				target = m.getPosition();
				break;
			}
		}
		
		return target;
	}
	@Override
	public AbstractTower createTower() {
		LaserTower lt = new LaserTower(getPosition().X(), getPosition().Y());
		return lt;
	}

}
