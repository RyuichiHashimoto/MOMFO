package lib.util;

import lib.lang.Primitive;

public class ArrayUtility {

	public static final String DEFAULT_DELIMITER = ",";

	@SafeVarargs
	public static <T> T[] veco(T ... v) {
		return v;
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

	
	

	
}
