package lib.util;

import java.util.ArrayList;
import java.util.List;

import lib.io.FileConstants;

public class StringUtility {
	private StringUtility() {
	}

	public static final String StringDelimieter = "\t";

	public static String capitalize(String word) {
		char[] chars = word.toCharArray();
		Character.toUpperCase(chars[0]);
		return String.valueOf(chars);
	}

	public static String ordinal(int n) {
		switch (n) {
		case 1:
			return "1st";
		case 2:
			return "2nd";
		case 3:
			return "3rd";
		default:
			return n + "th";
		}
	}

	public static String join(Object... obj) {
		return join(", ", obj);
	}

	public static String join(String delimiter, Object... obj) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < obj.length - 1; i++) {
			sb.append(obj[i]);
			sb.append(delimiter);
		}
		sb.append(obj[obj.length - 1]);
		return sb.toString();
	}

	public static String toCSV(double[][] matrix, String separator) {
		String ret = "";

		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[i].length - 1; j++) {
				ret = ret + matrix[i][j] + separator;
			}
			ret = ret + matrix[i][matrix[i].length - 1] + FileConstants.NEWLINE_DEMILITER;
		}
		return ret;
	}

	public static String toCSV(double[][] matrix) {
		return toCSV(matrix, FileConstants.FILE_DEMILITER);
	}

	public static String toCSV(int[][] matrix) {
		return toCSV(matrix, FileConstants.FILE_DEMILITER);
	}

	public static String toCSV(int[][] matrix, String separator) {
		String ret = "";

		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[i].length - 1; j++) {
				ret = ret + matrix[i][j] + separator;
			}
			ret = ret + matrix[i][matrix[i].length - 1] + FileConstants.NEWLINE_DEMILITER;
		}
		return ret;
	}

	public static String toCSV(List<ArrayList<Double>> matrix) {
		return toCSV(matrix, FileConstants.FILE_DEMILITER);
	}

	public static String toCSV(List<ArrayList<Double>> matrix, String separator) {
		String ret = "";

		for (int i = 0; i < matrix.size(); i++) {
			for (int j = 0; j < matrix.get(i).size() - 1; j++) {
				ret = ret + matrix.get(i).get(j) + separator;
			}
			ret = ret + matrix.get(i).get(matrix.get(i).size() - 1) + FileConstants.NEWLINE_DEMILITER;
		}
		return ret;

	}

	public static String toString(double[] val){
		return toString(val,StringDelimieter);
	}

	private static String toString(double[] object, String delimiter) {
		String ret = "";
		int size = object.length - 1;
		for(int i = 0;i < size;i++) {
			ret += object[i] + delimiter;
		}
		ret += object[size];
		return ret;
	}

	/*
	 * public static void assertEqualString(String str, Path path) throws
	 * IOException { List<String> file = Files.readAllLines(path,
	 * Charset.defaultCharset()); List<String> given =
	 * Arrays.asList(str.split("\r\n?|\n"));
	 * 
	 * int len = Math.min(file.size(), given.size()); for (int i = 0; i < len; i++)
	 * { if (!file.get(i).equals(given.get(i))) { throw new AssertionError(i
	 * +"th line: \n<br>"+ file.get(i) + "\n<br>"+ given.get(i)); } } int i = len;
	 * while (i < file.size()) assertTrue(file.get(i).isEmpty(), i
	 * +"th line in file."); i = len; while (i < given.size())
	 * assertTrue(file.get(i).isEmpty(), i +"th line in String."); }
	 */
	/**
	 * Replaces \r or \r\n to \n. Note that \r\n is always replaced to \n, not to
	 * \n\n.
	 * 
	 * @param obj
	 * @return Replaced string
	 */
	public static String toLF(String str) {
		StringBuilder sb = new StringBuilder(str.length());
		for (int i = 0; i < str.length(); i++) {
			if (str.charAt(i) == '\r') {
				sb.append('\n');
				if (i + 1 < str.length() && str.charAt(i + 1) == '\n')
					i++;
			} else {
				sb.append(str.charAt(i));
			}
		}
		return sb.toString();
	}
}
