package lib.math;

import lib.experiments.Numeric;

public class ArithmeticSequence extends Sequence {
	public static boolean isArithmetic(double[] seq) {
		if (seq.length <= 2) return true;
		double diff = seq[1] - seq[0];
		for (int i = 2; i < seq.length; i++) {
			if (!Numeric.isEquals(diff, seq[i] - seq[i - 1])) return false;
		}
		return true;
	}

	protected double first;
	protected double diff;

	protected double term;

	public ArithmeticSequence(double firstTerm, double difference) {
		first = firstTerm;
		diff = difference;
	}

	public double getFirst() {
		return first;
	}

	public double getDiff() {
		return diff;
	}

	@Override
	public double next() {
		return diff * term++ + first;
	}

	@Override
	public void reset() {
		term = 0;
	}

	@Override
	public double[] until(double threshold) {
		double current = first + diff * term;

		if (diff == 0) throw new UnsupportedOperationException();
		if (diff > 0) {
			if (threshold < current) return new double[0];
		} else {
			if (threshold > current) return new double[0];
		}

		int nTerms = (int) Math.floor(Math.nextUp((threshold - first) / diff))
					- (int) term + 1;
		double[] retval = new double[nTerms];
		for (int i = 0; i < retval.length; i++) {
			retval[i] = next();
		}
		return retval;
	}

	@Override
	public int hashCode() {
		return doubleHash(first) ^ doubleHash(diff);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (ArithmeticSequence.class.equals(obj.getClass())) {
			ArithmeticSequence as = (ArithmeticSequence) obj;
			return first == as.first && diff == as.diff;
		}
		return false;
	}

	@Override
	public String toString() {
		return "[first term: "+ first +", common difference: "+ diff +"]";
	}
}
