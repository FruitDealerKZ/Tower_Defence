package units;

public class Pig extends AbstractMonster {
	public Pig(float x, float y) {
		super(x,y, 0.025f);
		setReward(20);
		setHealth(6);
	}

	public Pig() {
		super(-1.0f, -1.0f, 0.0f);
	}
	@Override
	protected int getImageIndex() {
		return 2;
	}

	@Override
	public Pig next(float x, float y) {
		Pig m = new Pig(x,y);
		return m;
	}

}
