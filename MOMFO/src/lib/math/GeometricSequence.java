package lib.math;

import static java.lang.Math.*;

import lib.experiments.Numeric;

public class GeometricSequence extends Sequence {
	public static boolean isGeometric(double[] seq) {
		// check zero
		int idx = 0;
		for (; idx < seq.length; idx++) {
			if (Numeric.isZero(seq[idx])) break;
		}
		for (; idx < seq.length; idx++) {
			if (!Numeric.isZero(seq[idx])) return false;
		}
		if (Numeric.isZero(seq[0])) return true;
		if (seq.length <= 2) return true;

		double ratio = seq[1] / seq[0];
		for (int i = 2; i < seq.length; i++) {
			// TODO: consider precision
			if (!Numeric.isEquals(ratio, seq[i] / seq[i - 1])) return false;
		}
		return true;
	}


	protected double first;
	protected double ratio;

	protected double term;

	public GeometricSequence(double firstTerm, double ratio) {
		first = firstTerm;
		this.ratio = ratio;
	}

	public double getFirst() {
		return first;
	}

	public double getRatio() {
		return ratio;
	}

	@Override
	public double next() {
		return first * Math.pow(ratio, term++);
	}

	@Override
	public void reset() {
		term = 0;
	}

	@Override
	public double[] until(double threshold) {
		double current = first * Math.pow(ratio, term);

		if (ratio == 1 || ratio <= 0) throw new UnsupportedOperationException();
		if (ratio > 1) {
			if (threshold < current) return new double[0];
		} else {
			if (threshold > current) return new double[0];
		}

		int nTerms = (int) floor(nextUp(log(threshold / first) / log(ratio)))
					- (int) term + 1;
		double[] retval = new double[nTerms];
		for (int i = 0; i < retval.length; i++) {
			retval[i] = next();
		}
		return retval;
	}

	@Override
	public int hashCode() {
		return doubleHash(first) ^ doubleHash(ratio);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (GeometricSequence.class.equals(obj.getClass())) {
			GeometricSequence gs = (GeometricSequence) obj;
			return first == gs.first && ratio == gs.ratio;
		}
		return false;
	}

	@Override
	public String toString() {
		return "[first term: "+ first +", common ratio: "+ ratio +"]";
	}
}
