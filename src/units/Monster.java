package units;

public class Monster extends AbstractMonster {
	public Monster(float x, float y) {
		super(x,y, 0.025f);
		setReward(10);
		setHealth(1);
	}
	
	public Monster() {
		super(-1.0f, -1.0f, 0.0f);
	}

	@Override
	public Monster next(float x, float y) {
		Monster m = new Monster(x,y);
		return m;
	}

	@Override
	protected int getImageIndex() {
		return 0;
	}
}