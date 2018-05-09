package lib.lang;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;


public class Generics {
	@SuppressWarnings("unchecked")
	final public static <E>E cast(Object org) {
		return (E) org;
	}

	/**
	 *
	 * @param length
	 * @param dummy
	 * @exception NullPointerException null is assigned to type
	 * @return
	 */
	@SafeVarargs
	public static <T> T[] newArray(int length, T ... type) {
		return cast(Array.newInstance(type.getClass().getComponentType(), length));
	}

	/**
	 * Returns String representation of the argument. If the argument is instance of
	 * an array, this method is equal to {@link Arrays#deepToString(Object[])}. Otherwise,
	 * this method just call the toString of given object.
	 * @param o
	 * @return
	 */
	public static String toText(Object o) {
		if (o == null) return "null";

		if (o instanceof Object[]) {
			return Arrays.deepToString((Object[]) o);
		} else if (o instanceof int[]) {
			return Arrays.toString((int[]) o);
		} else if (o instanceof double[]) {
			return Arrays.toString((double[]) o);
		} else if (o instanceof boolean[]) {
			return Arrays.toString((boolean[]) o);
		} else if (o instanceof long[]) {
			return Arrays.toString((long[]) o);
		} else if (o instanceof byte[]) {
			return Arrays.toString((byte[]) o);
		} else if (o instanceof char[]) {
			return Arrays.toString((char[]) o);
		} else if (o instanceof short[]) {
			return Arrays.toString((short[]) o);
		} else if (o instanceof float[]) {
			return Arrays.toString((float[]) o);
		} else {
			return o.toString();
		}
	}

	public static boolean isEqual(Object a, Object b) {
		if (a == b) return true;
		if (a == null || b == null) return false;

		if (a instanceof Object[] && b instanceof Object[]) {
			return Arrays.deepEquals((Object[]) a, (Object[]) b);
		} else if (a instanceof int[] && b instanceof int[]) {
			return Arrays.equals((int[]) a, (int[]) b);
		} else if (a instanceof double[] && b instanceof double[]) {
			return Arrays.equals((double[]) a, (double[]) b);
		} else if (a instanceof boolean[] && b instanceof boolean[]) {
			return Arrays.equals((boolean[]) a, (boolean[]) b);
		} else if (a instanceof long[] && b instanceof long[]) {
			return Arrays.equals((long[]) a, (long[]) b);
		} else if (a instanceof byte[] && b instanceof byte[]) {
			return Arrays.equals((byte[]) a, (byte[]) b);
		} else if (a instanceof char[] && b instanceof char[]) {
			return Arrays.equals((char[]) a, (char[]) b);
		} else if (a instanceof short[] && b instanceof short[]) {
			return Arrays.equals((short[]) a, (short[]) b);
		} else if (a instanceof float[] && b instanceof float[]) {
			return Arrays.equals((float[]) a, (float[]) b);
		} else {
			return a.equals(b);
		}
	}

	public static <T> T clone(Cloneable c) {
		if (c == null) return null;
		try {
			Class<? extends Cloneable> cls = c.getClass();
			// clone method of array cannot be obtained by reflection (Java's bug?)
			if (!cls.isArray()) {
				return Generics.cast(cls.getMethod("clone").invoke(c));
			}
			if (c instanceof char[]) {
				return Generics.cast(((char[]) c).clone());
			} else if (c instanceof byte[]) {
				return Generics.cast(((byte[]) c).clone());
			} else if (c instanceof short[]) {
				return Generics.cast(((short[]) c).clone());
			} else if (c instanceof int[]) {
				return Generics.cast(((int[]) c).clone());
			} else if (c instanceof long[]) {
				return Generics.cast(((long[]) c).clone());
			} else if (c instanceof float[]) {
				return Generics.cast(((float[]) c).clone());
			} else if (c instanceof double[]) {
				return Generics.cast(((double[]) c).clone());
			} else if (c instanceof boolean[]) {
				return Generics.cast(((boolean[]) c).clone());
			} else if (c instanceof Cloneable[]) {
				Cloneable[] retval = Generics.newArray(((Cloneable[]) c).length, c);
				for (int i = 0; i < retval.length; i++) {
					retval[i] = clone(((Cloneable[]) c)[i]);
				}
				return Generics.cast(retval);
			} else {
				throw new IllegalArgumentException(cls +" is not Cloneable");
			}
		// Java7: multi catch
		} catch (NoSuchMethodException e) {
			throw new IllegalArgumentException(e);
		} catch (InvocationTargetException e) {
			throw new AssertionError(e);
		} catch (IllegalAccessException e) {
			throw new AssertionError(e);
		}
	}
}
