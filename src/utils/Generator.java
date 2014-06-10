package utils;

import units.AbstractMonster;

public interface Generator<T extends AbstractMonster> {
	public T next(float x, float y);
}
