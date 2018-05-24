package lib.math;

import java.util.Arrays;

public class RepeatSequence extends Sequence {
	protected int index;
	protected double[] generator;

	public RepeatSequence(double ... pattern) {
		generator = pattern.clone();
	}

	public int getUnitLength() {
		return generator.length;
	}

	@Override
	public double next() {
		return generator[index++ % generator.length];
	}

	@Override
	public void reset() {
		index = 0;
	}

	@Override
	public int hashCode() {
		return Arrays.hashCode(generator);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (RepeatSequence.class.equals(obj.getClass())) {
			RepeatSequence rs = (RepeatSequence) obj;
			return Arrays.equals(generator, rs.generator);
		}
		return false;
	}

	@Override
	public String toString() {
		return Arrays.toString(generator);
	}
}
