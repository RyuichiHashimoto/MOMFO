package lib.lang;


public class Primitive {
	private Primitive() {}

	public static Integer[] toWrapper(int[] a) {
		Integer[] retval = new Integer[a.length];

		for (int i = 0; i < a.length; i++) {
			retval[i] = a[i];
		}
		return retval;
	}

	public static Double[] toWrapper(double[] a) {
		Double[] retval = new Double[a.length];

		for (int i = 0; i < a.length; i++) {
			retval[i] = a[i];
		}
		return retval;
	}

	public static int[] toPrimitive(Integer[] a) {
		int[] retval = new int[a.length];

		for (int i = 0; i < a.length; i++) {
			retval[i] = a[i];
		}
		return retval;
	}

	public static double[] toPrimitive(Double[] a) {
		double[] retval = new double[a.length];

		for (int i = 0; i < a.length; i++) {
			retval[i] = a[i];
		}
		return retval;
	}

	
	
	
	// taken from Java7.
	// Java7: (parseInt) useless in Java7 or higher versions
    /**
     * Parses the string argument as a signed integer in the radix
     * specified by the second argument. The characters in the string
     * must all be digits of the specified radix (as determined by
     * whether {@link java.lang.Character#digit(char, int)} returns a
     * nonnegative value), except that the first character may be an
     * ASCII minus sign {@code '-'} (<code>'&#92;u002D'</code>) to
     * indicate a negative value or an ASCII plus sign {@code '+'}
     * (<code>'&#92;u002B'</code>) to indicate a positive value. The
     * resulting integer value is returned.
     *
     * <p>An exception of type {@code NumberFormatException} is
     * thrown if any of the following situations occurs:
     * <ul>
     * <li>The first argument is {@code null} or is a string of
     * length zero.
     *
     * <li>The radix is either smaller than
     * {@link java.lang.Character#MIN_RADIX} or
     * larger than {@link java.lang.Character#MAX_RADIX}.
     *
     * <li>Any character of the string is not a digit of the specified
     * radix, except that the first character may be a minus sign
     * {@code '-'} (<code>'&#92;u002D'</code>) or plus sign
     * {@code '+'} (<code>'&#92;u002B'</code>) provided that the
     * string is longer than length 1.
     *
     * <li>The value represented by the string is not a value of type
     * {@code int}.
     * </ul>
     *
     * <p>Examples:
     * <blockquote><pre>
     * parseInt("0", 10) returns 0
     * parseInt("473", 10) returns 473
     * parseInt("+42", 10) returns 42
     * parseInt("-0", 10) returns 0
     * parseInt("-FF", 16) returns -255
     * parseInt("1100110", 2) returns 102
     * parseInt("2147483647", 10) returns 2147483647
     * parseInt("-2147483648", 10) returns -2147483648
     * parseInt("2147483648", 10) throws a NumberFormatException
     * parseInt("99", 8) throws a NumberFormatException
     * parseInt("Kona", 10) throws a NumberFormatException
     * parseInt("Kona", 27) returns 411787
     * </pre></blockquote>
     *
     * @param      s   the {@code String} containing the integer
     *                  representation to be parsed
     * @param      radix   the radix to be used while parsing {@code s}.
     * @return     the integer represented by the string argument in the
     *             specified radix.
     * @exception  NumberFormatException if the {@code String}
     *             does not contain a parsable {@code int}.
     */
	
    public static int parseInt(String s, int radix) throws NumberFormatException {
        if (s == null) {
            throw new NumberFormatException("null");
        }

        if (radix < Character.MIN_RADIX) {
            throw new NumberFormatException("radix " + radix +
                                            " less than Character.MIN_RADIX");
        }

        if (radix > Character.MAX_RADIX) {
            throw new NumberFormatException("radix " + radix +
                                            " greater than Character.MAX_RADIX");
        }

        int result = 0;
        boolean negative = false;
        int i = 0, len = s.length();
        int limit = -Integer.MAX_VALUE;
        int multmin;
        int digit;

        if (len > 0) {
            char firstChar = s.charAt(0);
            if (firstChar < '0') { // Possible leading "+" or "-"
                if (firstChar == '-') {
                    negative = true;
                    limit = Integer.MIN_VALUE;
                } else if (firstChar != '+')
                    throw new NumberFormatException("For input string: \"" + s + "\"");

                if (len == 1) // Cannot have lone "+" or "-"
                    throw new NumberFormatException("For input string: \"" + s + "\"");
                i++;
            }
            multmin = limit / radix;
            while (i < len) {
                // Accumulating negatively avoids surprises near MAX_VALUE
                digit = Character.digit(s.charAt(i++),radix);
                if (digit < 0) {
                    throw new NumberFormatException("For input string: \"" + s + "\"");
                }
                if (result < multmin) {
                    throw new NumberFormatException("For input string: \"" + s + "\"");
                }
                result *= radix;
                if (result < limit + digit) {
                    throw new NumberFormatException("For input string: \"" + s + "\"");
                }
                result -= digit;
            }
        } else {
            throw new NumberFormatException("For input string: \"" + s + "\"");
        }
        return negative ? result : -result;
    }

	// taken from Java7.
	// Java7: (parseInt) useless in Java7 or higher versions
    /**
     * Parses the string argument as a signed decimal integer. The
     * characters in the string must all be decimal digits, except
     * that the first character may be an ASCII minus sign {@code '-'}
     * (<code>'&#92;u002D'</code>) to indicate a negative value or an
     * ASCII plus sign {@code '+'} (<code>'&#92;u002B'</code>) to
     * indicate a positive value. The resulting integer value is
     * returned, exactly as if the argument and the radix 10 were
     * given as arguments to the {@link #parseInt(java.lang.String,
     * int)} method.
     *
     * @param s    a {@code String} containing the {@code int}
     *             representation to be parsed
     * @return     the integer value represented by the argument in decimal.
     * @exception  NumberFormatException  if the string does not contain a
     *               parsable integer.
     * @deprecated Use Integer#parseInt in Java 7.
     */
    @Deprecated
    public static int parseInt(String s) throws NumberFormatException {
        return parseInt(s,10);
    }
}
