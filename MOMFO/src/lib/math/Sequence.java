package lib.math;

import lib.experiments.Numeric;
import lib.util.ArrayUtility;

abstract public class Sequence implements Cloneable {

	/**
	 * Parse a string as a sequence of integer. <p>
	 * Specification Examples:
	 * <br>
	 * <dl>
	 * 	<dt>Arithmetic Sequences</dt>
	 * <dd><ul>
	 * <li>1, 2, 3, 4, 5</li>
	 * <li>1, 2, ..., 5</li>
	 * <li>1, 2, 3, ..., 5</li>
	 * <li>1, 2, 3, 4, ..., 5</li>
	 * <li>1, 2, 3, 4, 5, ..., 5</li>
	 * </dd></ul>
	 * </dl>
	 * <dl>
	 * 	<dt>Geometric Sequence</dt>
	 * <dd><ul>
	 * <li>1, 2, 4, 8, 16</li>
	 * <li>1, 2, 4 ..., 16</li>
	 * <li>1, 2, 4, 8, ..., 16</li>
	 * <li>1, 2, 4, 8, 16, ..., 16</li>
	 * </dd></ul>
	 * </dl>
	 * @param seq
	 * @return
	 */
	public static int[] parseIntSequence(String seq) {
		int[] retval;
		if (seq.contains("...")) {
			// arithmetic sequence or geometric sequence
			String[] headtail = seq.split(", ..., ");
			if (headtail.length != 2) throw new IllegalArgumentException();
			int end = Integer.parseInt(headtail[1]);
			int[] headings = ArrayUtility.fromCSVint(headtail[0], (", "));
			if (headings.length == 2) {
				retval = arithmeticSeq(headings, end);
			} else {
				if (headings[1] == headings[0]) throw new IllegalArgumentException("The sequence takes a constance value.");
				if (headings[2] - headings[1] == headings[1] - headings[0]) {
					retval = arithmeticSeq(headings, end);
				} else {
					retval = geometricSeq(headings, end);
				}
			}
		} else {
			// all elements are written in the argument
			retval = ArrayUtility.fromCSVint(seq, ", ");
		}
		return retval;
	}

	private static int[] arithmeticSeq(int[] heading, int last) {
		int diff = heading[1] - heading[0];
		// check whether given numbers are arithmetic
		for (int i = 2; i < heading.length; i++) {
			if (heading[i] - heading[i - 1] != diff) throw new IllegalArgumentException();
		}
		if ((last - heading[0]) % diff != 0) throw new IllegalArgumentException();

		// generate sequence
		int[] retval = new int[(last - heading[0]) / diff + 1];
		retval[0] = heading[0];
		for (int i = 1; i < retval.length; i++) {
			retval[i] = retval[i - 1] + diff;
		}
		return retval;
	}

	private static int[] geometricSeq(int[] heading, int last) {
		if (heading[0] == 0) throw new IllegalArgumentException("The initial term is 0.");
		double ratio = (double) heading[1] / heading[0];
		// check whether given numbers are geometric
		for (int i = 1; i < heading.length; i++) {
			if (!Numeric.isEquals(heading[i], heading[i - 1] * ratio)) throw new IllegalArgumentException();
		}
		double length = Math.log((double) last / heading[0]) / Math.log(ratio) + 1;
		if (!Numeric.isEquals(length, Math.rint(length))) throw new IllegalArgumentException("Initial: "+ heading[0] +"; ratio: "+ ratio +"; but the last term is "+ last);

		// generate sequence
		int[] retval = new int[(int) Math.rint(length)];
		retval[0] = heading[0];
		for (int i = 1; i < retval.length; i++) {
			retval[i] = (int) Math.rint(retval[i - 1] * ratio);
		}
		return retval;
	}

	/**
	 * Parse a string as a sequence. <p>
	 * Specification Examples:
	 * <br>
	 * <dl>
	 * 	<dt>Arithmetic Sequences</dt>
	 * <dd><ul>
	 * <li>1, 2, 3, 4, 5</li>
	 * <li>1, 2, ...</li>
	 * <li>1, 2, 3, ...</li>
	 * <li>1, 2, 3, 4, ...</li>
	 * <li>1, 2, 3, 4, 5, ...</li>
	 * </dd></ul>
	 * </dl>
	 * <dl>
	 * 	<dt>Geometric Sequence</dt>
	 * <dd><ul>
	 * <li>1, 2, 4, 8, 16</li>
	 * <li>1, 2, 4 ...</li>
	 * <li>1, 2, 4, 8, ...</li>
	 * <li>1, 2, 4, 8, 16, ...</li>
	 * </dd></ul>
	 * </dl>
	 * @param seq
	 * @return
	 */
	public static double[] getArray(String seq, int len) {
		return getSequence(seq).asArray(len);
	}

	private static final String DELIMITER = ", ?";

	/**
	 * Parse a string as a sequence. <p>
	 * Specification Examples:
	 * <br>
	 * <dl><dt>Iteration</dt>
	 * <dd><ul>
	 * <li>1</li>
	 * <li>1, 2</i>
	 * <dl>
	 * 	<dt>Arithmetic Sequences</dt>
	 * <dd><ul>
	 * <li>1, 2, ...</li>
	 * <li>1, 2, 3, ...</li>
	 * </dd></ul>
	 * </dl>
	 * <dl>
	 * 	<dt>Geometric Sequence</dt>
	 * <dd><ul>
	 * <li>1, 2, 4, ...</li>
	 * <li>1, 2, 4, 8, ...</li>
	 * </dd></ul>
	 * </dl>
	 * @param seq
	 * @return
	 */
	public static Sequence getSequence(String sequence) {
		if (!sequence.contains("...")) {
			return new RepeatSequence(ArrayUtility.doubleValue(sequence.split(DELIMITER)));
		}

		if (sequence.endsWith("...")) {
			String[] terms = sequence.split(DELIMITER);
			if (terms.length <= 2) throw new IllegalArgumentException("Second term must be given.");
			if (terms.length == 3) {
				double first = Double.parseDouble(terms[0]);
				return new ArithmeticSequence(first, Double.parseDouble(terms[1]) - first);
			}
			double[] nums = ArrayUtility.doubleValue(terms, terms.length - 1);
			if (ArithmeticSequence.isArithmetic(nums)) {
				return new ArithmeticSequence(nums[0], nums[1] - nums[0]);
			} else if (GeometricSequence.isGeometric(nums)) {
				return new GeometricSequence(nums[0], nums[1] / nums[0]);
			}
		}

		throw new IllegalArgumentException(sequence +" was not parsed as a sequence.");
	}


	protected int doubleHash(double x) {
		// taken from java.lang.Double#hashCode()
        long bits = Double.doubleToLongBits(x);
        return (int)(bits ^ (bits >>> 32));
	}

	abstract public double next();
	abstract public void reset();

	/**
	 * Return a array of sequence until a value exceeds
	 * threshold (if it's monotonic increasing.) If the
	 * sequence is monotonic decreasing, returns value
	 * until a value become smaller than threshold.
	 * For non-monotonic increasing and decreasing sequence
	 * {@link UnsupportedOperationException} is thron.
	 */
	public double[] until(double threshold) {
		throw new UnsupportedOperationException();
	}

	public double[] asArray(int length) {
		double[] retval = new double[length];
		for (int i = 0; i < length; i++) {
			retval[i] = next();
		}
		return retval;
	}

	@Override
	public Sequence clone() {
		try {
			return (Sequence) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new InternalError();
		}
	}
}
