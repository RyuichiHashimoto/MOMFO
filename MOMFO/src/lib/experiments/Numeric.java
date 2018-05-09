package lib.experiments;

import static java.lang.Double.*;
import static java.lang.Math.*;

public class Numeric {
	private Numeric() {}

	// Java8: use DoubleUnaryOperator
	public interface Function {
		double value(double x);
	}

	public static Function yShift(final Function f, final double shift) {
		return new Function() {
			@Override
			public double value(double x) {
				return f.value(x) + shift;
			}
		};
	}

	// Java8: use BinaryOperator
	public interface BinaryOperator {
		double value(double x, double y);
	}

	private static final int[] digitTable =
		{-9, -99, -999, -9999, -99999, -999999, -9999999,
		-99999999, -999999999, Integer.MIN_VALUE};

	public static int getDigit(int n) {
		if (n > 0) n *= -1;
		int retval = 0;
		while (n < digitTable[retval++]);
		return retval;
	}

	/** Returns the base 10 exponent of a double value. */
	public static int getExponent10(double x) {
		if (Double.isNaN(x) || Double.isInfinite(x))
			throw new ArithmeticException("Digit of "+ x +" is not defined.");
		return x == 0 ? 1 : (int) floor(nextUp(log10(abs(x))));
	}

	// TODO: improve precision (Use BigDecimal)
	public static double round(double x, int digit) {
		int exp = getExponent10(x) + 1 - digit;
		return Math.round(x / pow(10d, exp)) * pow(10d, exp);
	}

	public static int getSignificantDigit(double x, double y) {
		if (x == y) return getExponent10(x);
		for (int i = 0; ;i++) {
			if (round(x, i) != round(y, i)) return i - 1;
		}
	}

	public static boolean isInteger(double x) {
		return x == (int) x;
	}

	// 2047L << 52. All bits in exponent are 1.
	public static final long EXP_BIT_MASK_D = 0x7ff0000000000000L;
	public static final long SIGN_BIT_MASK_D = 0x8000000000000000L;

	public static boolean isReal(double x) {
		return (Double.doubleToRawLongBits(x) & EXP_BIT_MASK_D) != EXP_BIT_MASK_D;
	}

	public static boolean isZero(double num) {
		return isZero(num, 1e-13);
	}

	public static boolean isZero(double num, double threshold) {
		return abs(num) < threshold;
	}

	/**
	 * Returns whether distance between x and y are smaller than or equal to ulp ULP.
	 * <p>
	 * Special cases: if either x or y is NaN, false is returned.
	 */
	public static boolean isEqualInULP(double x, double y, long ulp) {
		if (isNaN(x) || isNaN(y)) return false;
		if (isInfinite(x) || isInfinite(y)) {
			return x == y;
		}

		// ensure x <= y holds
		if (x > y) {
			double swp = x;
			x = y;
			y = swp;
		}
		long lx = doubleToRawLongBits(x);
		long ly = doubleToRawLongBits(y);
		if (0 < x) {
			return ly - lx <= ulp;
		} else if (x == 0) {
			return ly <= ulp;
		} else if (x < 0 && 0 < y) {
			double diff = (lx & (~SIGN_BIT_MASK_D)) + ly;
			if (diff < 0) return false; // difference is more than Long.MAX_VALUE
			return diff <= ulp;
		} else if (y == 0) {
			return (lx & (~SIGN_BIT_MASK_D)) <= ulp;
		} else { // x <= y < 0
			return lx - ly <= ulp;
		}
	}

	public static boolean isEqualInDigit(double a, double b, int digit) {
		return round(a, digit) == round(b, digit);
	}

	public static boolean isBiggerInULP(double obtained, double threshold, double ulp) {
		return isBigger(obtained, threshold, Math.ulp(obtained) * ulp);
	}

	public static boolean isBigger(double obtained, double threshold, double err) {
		return threshold - obtained <= err;
	}

	public static boolean isSmallerInULP(double obtained, double threshold, double ulp) {
		return isSmaller(obtained, threshold, Math.ulp(obtained) * ulp);
	}

	public static boolean isSmaller(double obtained, double threshold, double err) {
		return obtained - threshold <= err;
	}

	public static boolean isEquals(double obtained, double ideal) {
		return isEquals(obtained, ideal, 10);
	}

	public static boolean isEquals(double obtained, double ideal, int ulp) {
		return isEquals(obtained, ideal, ulp, 1e-13);
	}

	/** Numerical-error concerned equals method. */
	public static boolean isEquals(double obtained, double ideal, int ulp, double threshold0) {
		if (isZero(ideal)) return isZero(obtained, threshold0);
		return isEqualInULP(obtained, ideal, ulp);
	}

	public static boolean isEquals(double[] v, double[] u) {
		return diffIndex(v, u) == -1;
	}

	public static int diffIndex(double[] v, double[] u) {
		if (v.length != u.length) return Math.max(v.length, u.length);
		for (int i = v.length - 1; 0 <= i; i--) {
			if (!isEquals(v[i], u[i])) return i;
		}
		return -1;
	}

	public static int iteration = 10000;

	public static double interPoint(Function f, double a, double b, double error) {
		double bottom = f.value(a);
		double top = f.value(b);
		if (abs(bottom) < error) {
			return a;
		} else if (abs(top) < error) {
			return b;
		}
		if (bottom * top > 0) throw new IllegalArgumentException(
				"f("+ a +") = "+ bottom +", f("+ b +") = "+ top);
		if (bottom > 0) {
			double swp = b;
			b = a;
			a = swp;
			swp = bottom;
			bottom = top;
			top = swp;
		}
		for (int i = 0; i < iteration; i++) {
			double inter = f.value((a + b) / 2);
			if (abs(inter) < error) return (a + b) / 2;
			if (inter < 0) {
				a = (a + b) / 2;
				bottom = inter;
			} else if (inter > 0) {
				b = (a + b) / 2;
				top = inter;
			}
		}
		throw new ArithmeticException("Failed to solve equation.");
	}

	public static double solveEquation(double x, Function f, Function df) {
		return solveEquation(x,  f, df, 10);
	}

	public static double solveEquation(double x, Function f, Function df, double ulp) {
		int lim = 0;
		double prev;
		double diff;
		do {
			prev = x;
			x -= f.value(x) / df.value(x);
			lim++;
			if (lim > iteration) throw new ArithmeticException("failed to find the answer");

			diff = x - prev;
		} while(ulp * Math.ulp(x) <= abs(diff));
		if (Double.isNaN(x)) throw new ArithmeticException("failed to find the answer");
		return x;
	}

	// Java8: Function -> DoubleUnaryOperator
	public static double simpsonIntegrate(Function f, double floor, double ceil) {
		return simpsonIntegrate(f, floor, ceil, 10000);
	}

	/**
	 *
	 * @param f
	 * @param floor
	 * @param ceil
	 * @param division This value must be even. If not, division + 1 is used except for Integer.MAX_VALUE, which is changed to Integer.MAX_VALUE - 1.
	 * @return integral of f over [floor, ceil]
	 */
	// Java8: Function -> DoubleUnaryOperator
	public static double simpsonIntegrate(Function f, double floor, double ceil, int division) {
		if (division <= 0) throw new IllegalArgumentException("Argument division must be positive but found "+ division +". ");
		if (division == Integer.MAX_VALUE) division--;
		if ((division & 1) == 1) division++;
		assert (division & 1) == 0;
		assert 0 < division;

		double span = (ceil - floor) / division;
		double retval = 0;

		for (int i = 2; i < division; i += 2) {
			retval += f.value(floor + span * i);
		}
		retval *= 2;
		double add = 0;
		for (int i = 1; i < division; i += 2) {
			add += f.value(floor + span * i);
		}
		retval += add * 4;
		retval += f.value(floor) + f.value(ceil);
		retval *= span;
		retval /= 3;

		return retval;
	}

	/** Gauss-Legendre Quadrature */
	public static double gaussQuadrature(Function f, double floor, double ceil) {
		double midPoint = (ceil + floor) / 2;
		double radius = (ceil - floor) / 2;
		double retval = 0;
		for (int i = 0; i < GL_abscissas64.length; i++) {
			retval += GL_weight64[i] * f.value(radius * GL_abscissas64[i] + midPoint);
		}
		return radius * retval;
	}

	public static double inverseIntegral(Function f, double floor, double value, double error) {
		return inverseIntegral(f, floor, value, value / f.value(floor), error);
	}


	/**
	 * Calculate x, where x satisfies \int_floor^x f(t)dt = value.
	 * @param error the maximum error between value and the integral.
	 * Note that this is not the error of the returned value and its theoretical value.
	 */
	public static double inverseIntegral(Function f, double floor, double value, double init, double error) {
		double x = init;
		double xprev = 0;
		double delta = gaussQuadrature(f, floor, x) - value;
		int itr = 0;
//		while (Math.abs(xprev - x) > error) {
		while (Math.abs(delta) > error) {
			xprev = x;
			x -= delta / f.value(x);
			delta += gaussQuadrature(f, xprev, x);
			if (itr++ > 10000) throw new ArithmeticException("Failed to solve.");
		}
		return x;
	}

	public static double[][] rungeKutta(BinaryOperator f, double start, double y0, double step, int nStep) {
		double[][] retval = new double[nStep + 1][2];
		double x = start;
		double y = y0;
		retval[0][0] = x;
		retval[0][1] = y0;
		for (int i = 1; i < retval.length; i++) {
			double k1 = step * f.value(x, y);
			double k2 = step * f.value(x + step / 2, y + k1 / 2);
			double k3 = step * f.value(x + step / 2, y + k2 / 2);
			double k4 = step * f.value(x + step, y + k3);
			x += step;
			y += (k1 + 2 * k2 + 2 * k3 + k4) / 6;
			retval[i][0] = x;
			retval[i][1] = y;
		}
		return retval;
	}

	/**
	 * RKF45 without adaptive step-size modification.
	 */
	public static double[][] RKF45(BinaryOperator f, double start, double y0, double step, int nStep) {
		double[][] retval = new double[nStep + 1][2];
		double x = start;
		double y = y0;
		retval[0][0] = x;
		retval[0][1] = y0;
		for (int i = 1; i < retval.length; i++) {
			double k1 = step * f.value(x, y);
			double k2 = step * f.value(x+step   / 4, y + k1     /   4);
			double k3 = step * f.value(x+step* 3/ 8, y + k1*   3/  32 + k2*   9/ 32);
			double k4 = step * f.value(x+step*12/13, y + k1*1932/2197 - k2*7200/2197 + k3*7296/2197);
			double k5 = step * f.value(x+step      , y + k1* 439/ 216 - k2*   8      + k3*3680/ 513 - k4* 845/4104);
			double k6 = step * f.value(x+step   / 2, y - k1*   8/  27 + k2*   2      - k3*3544/2565 + k4*1859/4104 - k5*11/40);
			x += step;
			y += k1*16/135 + k3*6656/12825 + k4*28561/56430 - k5*9/50 + k6*2/55;
			retval[i][0] = x;
			retval[i][1] = y;
		}
		return retval;
	}



	/**
	 * src:
	 * Numeric Integration,
	 * http://www.holoborodko.com/pavel/numerical-methods/numerical-integration/
	 * 2015/3/26.
	 */
	protected static final double[] GL_abscissas64 = {
		-0.9993050417357721394569056,
		-0.9963401167719552793469245,
		-0.9910133714767443207393824,
		-0.9833362538846259569312993,
		-0.9733268277899109637418535,
		-0.9610087996520537189186141,
		-0.9464113748584028160624815,
		-0.9295691721319395758214902,
		-0.9105221370785028057563807,
		-0.8893154459951141058534040,
		-0.8659993981540928197607834,
		-0.8406292962525803627516915,
		-0.8132653151227975597419233,
		-0.7839723589433414076102205,
		-0.7528199072605318966118638,
		-0.7198818501716108268489402,
		-0.6852363130542332425635584,
		-0.6489654712546573398577612,
		-0.6111553551723932502488530,
		-0.5718956462026340342838781,
		-0.5312794640198945456580139,
		-0.4894031457070529574785263,
		-0.4463660172534640879849477,
		-0.4022701579639916036957668,
		-0.3572201583376681159504426,
		-0.3113228719902109561575127,
		-0.2646871622087674163739642,
		-0.2174236437400070841496487,
		-0.1696444204239928180373136,
		-0.1214628192961205544703765,
		-0.0729931217877990394495429,
		-0.0243502926634244325089558,
		0.0243502926634244325089558,
		0.0729931217877990394495429,
		0.1214628192961205544703765,
		0.1696444204239928180373136,
		0.2174236437400070841496487,
		0.2646871622087674163739642,
		0.3113228719902109561575127,
		0.3572201583376681159504426,
		0.4022701579639916036957668,
		0.4463660172534640879849477,
		0.4894031457070529574785263,
		0.5312794640198945456580139,
		0.5718956462026340342838781,
		0.6111553551723932502488530,
		0.6489654712546573398577612,
		0.6852363130542332425635584,
		0.7198818501716108268489402,
		0.7528199072605318966118638,
		0.7839723589433414076102205,
		0.8132653151227975597419233,
		0.8406292962525803627516915,
		0.8659993981540928197607834,
		0.8893154459951141058534040,
		0.9105221370785028057563807,
		0.9295691721319395758214902,
		0.9464113748584028160624815,
		0.9610087996520537189186141,
		0.9733268277899109637418535,
		0.9833362538846259569312993,
		0.9910133714767443207393824,
		0.9963401167719552793469245,
		0.9993050417357721394569056,
	};
	protected static final double[] GL_weight64 = {
		0.0017832807216964329472961,
		0.0041470332605624676352875,
		0.0065044579689783628561174,
		0.0088467598263639477230309,
		0.0111681394601311288185905,
		0.0134630478967186425980608,
		0.0157260304760247193219660,
		0.0179517157756973430850453,
		0.0201348231535302093723403,
		0.0222701738083832541592983,
		0.0243527025687108733381776,
		0.0263774697150546586716918,
		0.0283396726142594832275113,
		0.0302346570724024788679741,
		0.0320579283548515535854675,
		0.0338051618371416093915655,
		0.0354722132568823838106931,
		0.0370551285402400460404151,
		0.0385501531786156291289625,
		0.0399537411327203413866569,
		0.0412625632426235286101563,
		0.0424735151236535890073398,
		0.0435837245293234533768279,
		0.0445905581637565630601347,
		0.0454916279274181444797710,
		0.0462847965813144172959532,
		0.0469681828162100173253263,
		0.0475401657148303086622822,
		0.0479993885964583077281262,
		0.0483447622348029571697695,
		0.0485754674415034269347991,
		0.0486909570091397203833654,
		0.0486909570091397203833654,
		0.0485754674415034269347991,
		0.0483447622348029571697695,
		0.0479993885964583077281262,
		0.0475401657148303086622822,
		0.0469681828162100173253263,
		0.0462847965813144172959532,
		0.0454916279274181444797710,
		0.0445905581637565630601347,
		0.0435837245293234533768279,
		0.0424735151236535890073398,
		0.0412625632426235286101563,
		0.0399537411327203413866569,
		0.0385501531786156291289625,
		0.0370551285402400460404151,
		0.0354722132568823838106931,
		0.0338051618371416093915655,
		0.0320579283548515535854675,
		0.0302346570724024788679741,
		0.0283396726142594832275113,
		0.0263774697150546586716918,
		0.0243527025687108733381776,
		0.0222701738083832541592983,
		0.0201348231535302093723403,
		0.0179517157756973430850453,
		0.0157260304760247193219660,
		0.0134630478967186425980608,
		0.0111681394601311288185905,
		0.0088467598263639477230309,
		0.0065044579689783628561174,
		0.0041470332605624676352875,
		0.0017832807216964329472961,
	};
}
