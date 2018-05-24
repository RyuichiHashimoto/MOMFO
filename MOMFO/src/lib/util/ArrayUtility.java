package lib.util;

import java.util.List;

import lib.lang.Primitive;

public class ArrayUtility {

	public static final String DEFAULT_DELIMITER = ",";

	@SafeVarargs
	public static <T> T[] veco(T... v) {
		return v;
	}

	public static double[][] listToDArrays(List<Double[]> d) {
		double[][] ret = new double[d.size()][];

		for (int i = 0; i < ret.length; i++) {
			ret[i] = new double[(d.get(i).length)];
			for (int j = 0; j < ret[i].length; j++) {
				ret[i][j] = d.get(i)[j];
			}
		}
		return ret;
	}

	public static double[] listToDArray(List<Double> d) {
		double[] ret = new double[d.size()];

		for (int i = 0; i < ret.length; i++) {
				ret[i] = d.get(i);
		}
		return ret;
	}


	public static double[] fromCSVdouble(String s, String separator) {
		String[] fields = s.split(separator);
		double[] retval = new double[fields.length];
		for (int i = 0; i < retval.length; i++) {
			retval[i] = Double.parseDouble(fields[i]);
		}
		return retval;
	}

	public static int[] fromCSVint(String s, String separator) {
		String[] fields = s.split(separator);
		int[] retval = new int[fields.length];
		for (int i = 0; i < retval.length; i++) {
			retval[i] = Primitive.parseInt(fields[i]);
		}
		return retval;
	}

	public static boolean[] fromCSVboolean(String s, String separator) {
		String[] fields = s.split(separator);
		boolean[] retval = new boolean[fields.length];
		for (int i = 0; i < retval.length; i++) {
			retval[i] = Boolean.parseBoolean(fields[i]);
		}
		return retval;
	}

	public static double[] doubleValue(String[] nums) {
		return doubleValue(nums, nums.length);
	}
	public static int[] sequence(int len) {
		return sequence(0, len);
	}

	public static int[] sequence(int from, int len) {
		return sequence(from, len, 1);
	}

	public static int[] sequence(int from, int len, int step) {
		int[] retval = new int[len];
		retval[0] = from;
		for (int i = 0; i < retval.length - 1; i++) {
			retval[i + 1] = retval[i] + step;
		}
		return retval;
	}
	public static int[] toInt(double[] da) {
		int[] retval = new int[da.length];
		for (int i = 0; i < da.length; i++) {
			retval[i] = (int) da[i];
		}
		return retval;
	}
	public static double[] doubleValue(String[] nums, int length) {
		double[] retval = new double[length];
		for (int i = 0; i < length; i++) {
			retval[i] = Double.parseDouble(nums[i]);
		}
		return retval;
	}

}
