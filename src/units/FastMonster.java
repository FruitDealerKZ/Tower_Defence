package units;

public class FastMonster extends AbstractMonster {
	public FastMonster(float x, float y) {
		super(x,y, 0.05f);
		setReward(15);
	}

	public FastMonster() {
		super(-1.0f, -1.0f, 0.0f);
	}
	@Override
	public FastMonster next(float x, float y) {
		FastMonster m = new FastMonster(x,y);
		return m;
	}

	@Override
	protected int getImageIndex() {
		return 1;
	}
}
