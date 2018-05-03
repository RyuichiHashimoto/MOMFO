package momfo.util.MersenneTwister;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Arrays;

/**
 * <h3>MersenneTwister and MersenneTwisterFast</h3>
 * A Java implementation of Mersenne Twister optimized for the use of genetic algirithms.
 *
 * <p><b>MersenneTwister</b> is a drop-in subclass replacement
 * for java.util.Random.  It is properly synchronized and
 * can be used in a multithreaded environment.  On modern VMs such
 * as HotSpot, it is approximately 1/3 slower than java.util.Random.
 * <br>
 * Instances of MersenneTwister are thread safe. However, the concurrent use of
 * the same instance across threads may encounter contention and consequent poor performance.
 * Consider instead using different instances in different threads like ThreadLocalRandom.
 *
 * <p><b>MersenneTwisterFast</b> is not a subclass of java.util.Random.  It has
 * the same public methods as Random does, however, and it is
 * algorithmically identical to MersenneTwister.
 * To speed up the random generation, MersenneTwisterFast is <b>NOT THREAD SAFE</b>.
 * Hence MersenneTwisterFast is not a sub-class of java.lib.Random, which is a thread
 * safe class.
 * The original implementation of MersenneTwisterFast applies some extra optimization
 * such as method hard-coding. Considering the Java's optimization mechanizm and
 * other factors, those meaningless optimizations are removed and readability is highly
 * improved. Yet the speed down can be ignored.
 *
 * <p>
 * The algorithm of Mersenne Twister is identical to the standard MT19937 C/C++
 * code by Takuji Nishimura, with the revised initial seed generation algirthm
 * (which was modified in 2002).
 *
 * <h3>Java notes</h3>
 *
 * <p>This implementation implements the bug fixes made
 * in Java 1.2's version of Random, which means it can be used with
 * earlier versions of Java.  See
 * <a href="http://www.javasoft.com/products/jdk/1.2/docs/api/java/util/Random.html">
 * the JDK 1.2 java.util.Random documentation</a> for further documentation
 * on the random-number generation contracts made.  Additionally, there's
 * an undocumented bug in the JDK java.util.Random.nextBytes() method,
 * which this code fixes.
 *
 * <p> Just like java.util.Random, this
 * generator accepts a long seed but doesn't use all of it.  java.util.Random
 * uses 48 bits.  The Mersenne Twister instead uses 32 bits (int size).
 * So it's best if your seed does not exceed the int range.
 *
 * <p>MersenneTwister can be used reliably
 * on JDK version 1.1.5 or above.  Earlier Java versions have serious bugs in
 * java.util.Random; only MersenneTwisterFast (and not MersenneTwister nor
 * java.util.Random) should be used with them.

 * <p>This is a Java version of the C-program for MT19937: Integer version.
 * The MT19937 algorithm was created by Makoto Matsumoto and Takuji Nishimura,
 * who ask: "When you use this, send an email to: matumoto@math.keio.ac.jp
 * with an appropriate reference to your work".  Indicate that this
 * is a translation of their algorithm into Java.
 *
 * <p><b>Reference. </b>
 * Makato Matsumoto and Takuji Nishimura,
 * "Mersenne Twister: A 623-Dimensionally Equidistributed Uniform
 * Pseudo-Random Number Generator",
 * <i>ACM Transactions on Modeling and. Computer Simulation,</i>
 * Vol. 8, No. 1, January 1998, pp 3--30.
 *
 * <h3>Licenses</h3>
 *
 * <h2>About the license of this implementation</h2>
 * This code is available under the BSD license.
 *
 * Copyright (c) 2015, Osaka Prefecture Computational Intelligence Laboratory.
 * All rights reserved.
 *
 *
 * <p>Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * <ul>
 * <li> Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * <li> Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * <li> Neither the name of the copyright owners, their employers, nor the
 * names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
 * </ul>
 * <p>THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 *
 * <h2>The original implementation for Java</h2>
 *
 * This code is based on the version 22 of MT implementation
 * by Sean Luke (http://cs.gmu.edu/~sean/research/), which is released under
 * the license of the BSD 3-Clause License.
 * <p>
 * Copyright (c) 2003 by Sean Luke. <br>
 * Portions copyright (c) 1993 by Michael Lecuyer. <br>
 * All rights reserved. <br>
 *
 * <p>Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * <ul>
 * <li> Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * <li> Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * <li> Neither the name of the copyright owners, their employers, nor the
 * names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
 * </ul>
 * <p>THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 *
 * <h2>The original implementation of Mersenne Twister for C</h2>
 * The original implementation of MT in C is by Makoto Matsumoto and Takuji Nishimura
 * (Mersenne Twister Home Page: http://www.math.sci.hiroshima-u.ac.jp/~m-mat/MT/mt.html),
 * which is released under the license of the BSD 3-Clause License.
 *
 * @version 1
*/

public class MersenneTwister extends java.util.Random implements Serializable, Cloneable
    {
    /** Serialization; version 1 */
    private static final long serialVersionUID = 3825832775130174188L;

	private static final long SHIFT53 = 1L << 53;

    // Period parameters
    private static final int N = 624;
    private static final int M = 397;
    private static final int MATRIX_A = 0x9908b0df;   //    private static final * constant vector a
    private static final int UPPER_MASK = 0x80000000; // most significant w-r bits
    private static final int LOWER_MASK = 0x7fffffff; // least significant r bits

    // Tempering parameters
    private static final int TEMPERING_MASK_B = 0x9d2c5680;
    private static final int TEMPERING_MASK_C = 0xefc60000;

    private int mt[]; // the array for the state vector
    private int mti; // mti==N+1 means mt[N] is not initialized
    private int mag01[]; // should be final (and static). But not modified here

    // a good initial seed (of int size, though stored in a long)
    //private static final long GOOD_SEED = 4357;

    /* implemented here because there's a bug in Random's implementation
       of the Gaussian code (divide by zero, and log(0), ugh!), yet its
       gaussian variables are private so we can't access them here.  :-( */

    private double __nextNextGaussian;

    /**
     * Constructor using the default seed.
     */
    public MersenneTwister()
        {
        this(System.currentTimeMillis());
        }

    /**
     * Constructor using a given seed.
     */
    public MersenneTwister(int seed)
        {
        super(seed);    /* just in case */
        setSeed(seed);
        }

    /**
     * Constructor using a given seed.
     * @deprecated Use an integer value for the seed.
     * @see MersenneTwister#setSeed(long)
     */
    @Deprecated
    public MersenneTwister(long seed)
        {
        super(seed);    /* just in case */
        setSeed(seed);
        }


    /**
     * Constructor using an array of integers as seed.
     * Your array must have a non-zero length.  Only the first 624 integers
     * in the array are used; if the array is shorter than this then
     * integers are repeatedly used in a wrap-around fashion.
     */
    public MersenneTwister(int[] array)
        {
        super(System.currentTimeMillis());    /* pick something at random just in case */
        setSeed(array);
        }

	/**
	 * Initialize the pseudo random number generator. Don't
     * pass in a long that's bigger than an int (Mersenne Twister
     * only uses the 32 bits for its seed).
	 *
	 * @deprecated Only 32 bits are used to construct the internal
	 * state vector of Mersenne Twister. Use {@link #setSeed(int)}.
	 */
	@Deprecated
	@Override
	public void setSeed(long seed) {
		// Same as Long#hashCode()
		setSeed((int) ((seed >>> 32) ^ seed));
	}

	/** Initialize the pseudo random number generator. */
	public synchronized void setSeed(int seed) {
        // it's always good style to call super
        super.setSeed(seed);

        // Due to a bug in java.util.Random clear up to 1.2, we're
        // doing our own Gaussian variable.
        __nextNextGaussian = MersenneTwisterFast.NOT_PREPARED;

        mt = new int[N];

        mag01 = new int[2];
        mag01[0] = 0x0;
        mag01[1] = MATRIX_A;

        mt[0]= seed & 0xffffffff;
        mt[0] = seed;
        for (mti=1; mti<N; mti++)
            {
            mt[mti] =
                (1812433253 * (mt[mti-1] ^ (mt[mti-1] >>> 30)) + mti);
            /* See Knuth TAOCP Vol2. 3rd Ed. P.106 for multiplier. */
            /* In the previous versions, MSBs of the seed affect   */
            /* only MSBs of the array mt[].                        */
            /* 2002/01/09 modified by Makoto Matsumoto             */
            // mt[mti] &= 0xffffffff;
            /* for >32 bit machines */
            }
        }


    /**
     * Sets the seed of the MersenneTwister using an array of integers.
     * Your array must have a non-zero length.  Only the first 624 integers
     * in the array are used; if the array is shorter than this then
     * integers are repeatedly used in a wrap-around fashion.
     */

    synchronized public void setSeed(int[] array)
        {
        if (array.length == 0)
            throw new IllegalArgumentException("Array length must be greater than zero");
        int i, j, k;
        setSeed(19650218);
        i=1; j=0;
        k = (N>array.length ? N : array.length);
        for (; k!=0; k--)
            {
            mt[i] = (mt[i] ^ ((mt[i-1] ^ (mt[i-1] >>> 30)) * 1664525)) + array[j] + j; /* non linear */
            // mt[i] &= 0xffffffff; /* for WORDSIZE > 32 machines */
            i++;
            j++;
            if (i>=N) { mt[0] = mt[N-1]; i=1; }
            if (j>=array.length) j=0;
            }
        for (k=N-1; k!=0; k--)
            {
            mt[i] = (mt[i] ^ ((mt[i-1] ^ (mt[i-1] >>> 30)) * 1566083941)) - i; /* non linear */
            // mt[i] &= 0xffffffff; /* for WORDSIZE > 32 machines */
            i++;
            if (i>=N)
                {
                mt[0] = mt[N-1]; i=1;
                }
            }
        mt[0] = 0x80000000; /* MSB is 1; assuring non-zero initial array */
        }

    /**
     * Initialize the pseudo random number generator using the old algorithm.
     *
     * The Mersenne Twister only uses an integer for its seed;
     * It's best that you don't pass in a long that's bigger
     * than an int.
     * <p>
     * This method uses the initialization algorithm used in the C-code of
     * Mersenne Twister in 1998.
     * Because this old initialization algorithm has a tiny problem
     * (see <a href="http://www.math.sci.hiroshima-u.ac.jp/~m-mat/MT/MT2002/mt19937ar.html">
     * http://www.math.sci.hiroshima-u.ac.jp/~m-mat/MT/MT2002/mt19937ar.html</a>),
     * the initialization algorithm was improved in 2002.
     * This method exists only for the backward compatibility.
     *
     * @param seed The seed value for the initialization.
     * @deprecated This method is only for the backward compatibility.
     * Use {@link #setSeed(int)} or {@link #setSeed(int[])}.
     */
	@Deprecated
	public synchronized void setSeedOldStyle(long seed) {
		if (seed == 0) {
			throw new IllegalArgumentException(
				"Seed value must be non-zero in setSeedOldStyle(). "
				+ "Use different seed or the new initialization method, setSeed()");
		}

		setSeed((int) seed);

		// setting initial seeds to mt[N] using
		// the generator Line 25 of Table 1 in
		// [KNUTH 1981, The Art of Computer Programming
		//    Vol. 2 (2nd Ed.), pp102]

		// the 0xffffffff is commented out because in Java
		// ints are always 32 bits; hence i & 0xffffffff == i

		mt[0] = ((int) seed); // & 0xffffffff;
		for (mti = 1; mti < N; mti++) mt[mti] = (69069 * mt[mti - 1]); //& 0xffffffff;
	}


	/** Discards the next <code>n</code> random numbers. This method is equivalent to
	 * the thread safe version of
	 * the following code (mtRand is an instance of this class):<p>
	 * <code>
	 * for (int i = 0; i < n; i++) mtRand.nextInt();
	 * </code>
	 * </p>
	 * Because some next methods uses multiple random numbers, <code>n</code> does not
	 * always equal to the number of the next methods invocation. For example,
	 * <code>mtRand.nextLong()</code> is equivalent to discard(2),
	 * because nextLong() uses two random numbers. <br>
	 * The internal Gaussian variable is NOT cleared by this method. To clear it, invoke {@link #clearGaussian()}.
	 *
	 * @param n The number of random variables to be discarded. If <code>n</code> is
	 * non-positive, this method do nothing.
	 * @see #clearGaussian()
	 */
	public synchronized void discard(int n) {
		while (0 < n) {
			if (N - mti < n) {
				// generate the N next random numbers
				n -= N - mti + 1;
				mti = N;
				next(32);
			} else {
				mti += n;
				break;
			}
		}
	}

    /**
     * Returns an integer with <i>bits</i> bits filled with a random number.
     */
    @Override
	synchronized protected int next(int bits)
        {
        int y;

        if (mti >= N)   // generate N words at one time
            {
            int kk;
            final int[] mt = this.mt; // locals are slightly faster
            final int[] mag01 = this.mag01; // locals are slightly faster

            for (kk = 0; kk < N - M; kk++)
                {
                y = (mt[kk] & UPPER_MASK) | (mt[kk+1] & LOWER_MASK);
                mt[kk] = mt[kk+M] ^ (y >>> 1) ^ mag01[y & 0x1];
                }
            for (; kk < N-1; kk++)
                {
                y = (mt[kk] & UPPER_MASK) | (mt[kk+1] & LOWER_MASK);
                mt[kk] = mt[kk+(M-N)] ^ (y >>> 1) ^ mag01[y & 0x1];
                }
            y = (mt[N-1] & UPPER_MASK) | (mt[0] & LOWER_MASK);
            mt[N-1] = mt[M-1] ^ (y >>> 1) ^ mag01[y & 0x1];

            mti = 0;
            }

        y = mt[mti++];
        y ^= y >>> 11;                          // TEMPERING_SHIFT_U(y)
        y ^= (y << 7) & TEMPERING_MASK_B;       // TEMPERING_SHIFT_S(y)
        y ^= (y << 15) & TEMPERING_MASK_C;      // TEMPERING_SHIFT_T(y)
        y ^= (y >>> 18);                        // TEMPERING_SHIFT_L(y)

        return y >>> (32 - bits);    // hope that's right!
        }

	@Override
	public synchronized MersenneTwister clone()
	    {
	    try
	        {
	        MersenneTwister f = (MersenneTwister)(super.clone());
	        f.mt = (mt.clone());
	        f.mag01 = (mag01.clone());
	        return f;
	        }
	    catch (CloneNotSupportedException e) { throw new InternalError(); } // should never happen
	    }

	/**
	 * Returns true if the MersenneTwister's current internal state is equal to another MersenneTwister.
	 */
	@Override
	public boolean equals(Object o) {
		if (o == this) return true;
		if (o == null) return false;
		if (!this.getClass().equals(o.getClass())) return false;

		MTMemento other = ((MersenneTwister) o).getState_();
		return getState_().equals(other);
	}

	@Override
	public synchronized int hashCode() {
		return Arrays.hashCode(mt) + mti;
	}


	/**
	 * Sets the current state of the Mersenne Twister as the given memento object.
	 * The argument must be one from {@link #getState_()} method.
	 * This method is similar to the Serialization mechanism but the state (memento
	 * object) can be shared with the different versions of MersenneTwister or
	 * MersnneTwisterFast.
	 * The state (Memento object) can be shared with MersenneTwister.
	 * {@link #getState()}
	 */
	public synchronized void setState(Object state) {
		if (!(state instanceof MTMemento)) throw new IllegalArgumentException("MT#readState: argument must be one from getState(). ");
		MTMemento memento = (MTMemento) state;
		mt = memento.getVector();
		mti = memento.mti_;
		__nextNextGaussian = memento.gauss_;
	}


	/**
	 * Returns the current state of Mersenne Twister.
	 * The returned object can be read via {@link #setState(Object)}.
	 * This method is similar to the Serialization mechanism but the state (memento
	 * object) can be shared with the different versions of MersenneTwister or
	 * MersnneTwisterFast.
	 * @return Memento object for the current state.
	 * {@link #setState(Object)}.
	 */
	public Serializable getState() {
		return getState_();
	}

	private synchronized MTMemento getState_() {
		return new MTMemento(mt, mti, __nextNextGaussian);
	}

	/* If you've got a truly old version of Java, you can omit these
       two next methods. */

    private synchronized void writeObject(ObjectOutputStream out)
        throws IOException
        {
        // just so we're synchronized.
        out.defaultWriteObject();
        }

    private void readObject (ObjectInputStream in)   // readObject never needs to be Synchronized
        throws IOException, ClassNotFoundException
        {
        in.defaultReadObject();
        }

    /** This method is missing from jdk 1.0.x and below.  JDK 1.1
        includes this for us, but what the heck.*/
    @Override
	public boolean nextBoolean() {return next(1) != 0;}

    /** This generates a coin flip with a probability <tt>probability</tt>
        of returning true, else returning false. <tt>probability</tt> must
        be between 0.0 and 1.0, inclusive.  Not as precise a random real
        event as nextBoolean(double), but twice as fast. To explicitly
        use this, remember you may need to cast to float first. */

    public boolean nextBoolean (float probability)
        {
        if (probability < 0.0f || probability > 1.0f)
            throw new IllegalArgumentException ("probability must be between 0.0 and 1.0 inclusive.");
        if (probability==0.0f) return false;            // fix half-open issues
        else if (probability==1.0f) return true;        // fix half-open issues
        return nextFloat() < probability;
        }

    /** This generates a coin flip with a probability <tt>probability</tt>
        of returning true, else returning false. <tt>probability</tt> must
        be between 0.0 and 1.0, inclusive. */

    public boolean nextBoolean (double probability)
        {
        if (probability < 0.0 || probability > 1.0)
            throw new IllegalArgumentException ("probability must be between 0.0 and 1.0 inclusive.");
        if (probability==0.0) return false;             // fix half-open issues
        else if (probability==1.0) return true; // fix half-open issues
        return nextDouble() < probability;
        }

    /** This method is missing from JDK 1.1 and below.  JDK 1.2
        includes this for us, but what the heck. */

    @Override
	public int nextInt(int n)
        {
        if (n<=0)
            throw new IllegalArgumentException("n must be positive, got: " + n);

        if ((n & -n) == n)
            return (int)((n * (long)next(31)) >> 31);

        int bits, val;
        do
            {
            bits = next(31);
            val = bits % n;
            }
        while(bits - val + (n-1) < 0);
        return val;
        }

    /** This method is for completness' sake.
        Returns a long drawn uniformly from 0 to n-1.  Suffice it to say,
        n must be greater than 0, or an IllegalArgumentException is raised. */

    public long nextLong(long n)
        {
        if (n<=0)
            throw new IllegalArgumentException("n must be positive, got: " + n);

        long bits, val;
        do
            {
            bits = (nextLong() >>> 1);
            val = bits % n;
            }
        while(bits - val + (n-1) < 0);
        return val;
        }


    /**
     * Returns the next pseudorandom, uniformly distributed on [0, 1);
     * ({@code double} value between {@code 0.0} (inclusive) and
     * {@code 1.0} (exclusive)) from this random number generator's sequence.
     * <p>
     * Although this method have no flaws in the randomness of the generated number,
     * the nextDobule algorithm was changed in order to generate the same random numbers
     * with C implementation of MT.
     *
     * @return the next pseudorandom, uniformly distributed {@code double}
     *         value between {@code 0.0} and {@code 1.0} from this
     *         random number generator's sequence
     * @deprecated This method generates a different sequence from the C-code of MT.
     * Use {@link #nextDouble()}.
     */
	@Deprecated
	public double nextDoubleOld() {
		return (((long) next(26) << 27) + next(27)) / (double) SHIFT53;
	}

	@Override
	public double nextDouble()
	{
		return (((long)next(27) << 26) + next(26))
				/ (double)(1L << 53);
	}

	public double nextDoubleIE() {
		return nextDouble();
	}

	public double nextDoubleEI() {
		return (((long)next(27) << 26) + next(26) + 1) / (double) (1L << 53);
	}

	public double nextDoubleEE() {
		double retval;
		do {
			retval = nextDoubleIE();
		} while (retval == 0);
		return retval;
	}


	/*
	 * Explanation on the algorithm of nextDoubleII
	 * =============================================
	 * A naive implementation
	 *  return (((long) next(27) << 26) + next(26)) / (double) (SHIFT53 - 1);
	 * is avoided here due to the numerical error.
	 * Since a long number divided by (SHIFT53 - 1) cannot be expressed by double
	 * without a rounding, the above calculation may cause some numerical problems.
	 *
	 * The pseudo code for the algorithm is as follows:
	 *   do {
	 *     n <- random 64 bit unsigned integer
	 *   } while (SHIFT53P1 * 2047 <= n)
	 *   return (n % (SHIFT53P1)) / (double) (1 << 53)
	 *
	 * The do-while loop is the rejection sampling to randomly pick up
	 * a number between 0 and (SHIFT53P1 * 2047), 0 is included and the latter is not.
	 * The upper bound (SHIFT53P1 * 2047) is derived from the following equation.
	 * 2^64 = SHIFT53P1 * 2047 + R
	 * R is a remaining of the division (R is a positive number smaller than SHIFT53P1).
	 * The probability a sampled point is rejected is R / 2^64, which is about 1 / 2048.
	 * (Note, R = 2^53 - 2^11 + 1.)
	 *
	 * Since (n % SHIFT53P1) is a uniform integer between 0 and (1 << 53),
	 * (n % SHIFT53P1) / (double) (1 << 53) is the uniform real number on [0, 1].
	 *
	 *
	 * Since Java does not have unsigned numbers, the following code takes into
	 * account the overflow and makes the implementation ugly.
	 */
	/** The lower 32 bits are 1. */
	private static final long L_MASK_BOTTOM_32BIT = 0x00000000FFFFFFFFL;
	/** (1L << 53) + 1 */
	private static final long SHIFT53P1 = SHIFT53 + 1L;
	/**
	 * The upper bound for the rejection sampling.
	 * Due to the overflow, UNSIGNED_QUOTIENT_MAX is negative.  */
	private static final long UNSIGNED_QUOTIENT_MAX = (SHIFT53P1 * ((1L << 11) - 1));
	private static final long OVERFLOW_OFFSET = (Long.MAX_VALUE % SHIFT53P1) + 1;

	/**
     * Returns the next pseudorandom, uniformly distributed on [0, 1];
     * ({@code double} value between {@code 0.0} (Inclusive) and
     * {@code 1.0} (Inclusive)) from this random number generator's sequence.
     *
     * @return the next pseudorandom, uniformly distributed {@code double}
     *         value between {@code 0.0} and {@code 1.0} from this
     *         random number generator's sequence
     * @see Math#random
     */
	public double nextDoubleII() {
		long num;
		do {
			/*
			 * Generates 64 bit integer.
			 * To obtain the same result with C/C++ implementation
			 * (which supports unsigned 64 bit numbers), a different mapping with
			 * nextLong() is used here. (Both are correct, though)
			 */
			num = (((long) next(32)) << 32) + (next(32) & L_MASK_BOTTOM_32BIT);
		} while (UNSIGNED_QUOTIENT_MAX <= num && num < 0);
		// calculates the modulo
		if (0 <= num) {
			// no overflow
			num %= SHIFT53P1;
		} else {
			// overflow occurred: num is negative
			// The first modulo operator is to avoid the overflow. Hence we need
			// modulo twice.
			num = (((num - Long.MIN_VALUE) % SHIFT53P1) + OVERFLOW_OFFSET) % SHIFT53P1;
		}
		return num / (double) SHIFT53;
	}


	@Override
	public float nextFloat()
	{
		return next(24) / ((float)(1 << 24));
	}

	public float nextFloatIE() {
		return nextFloat();
	}

	public float nextFloatEI() {
		return (next(24) + 1) / ((float) (1 << 24));
	}

	public float nextFloatEE() {
		float retval;
		do {
			retval = nextFloatIE();
		} while (retval == 0);
		return retval;
	}

	// 2^63 = (2^24 + 1)(2^39 - 2^15) + 2^15
	private static final Long FLOATII_MAX_LONG =
		((1L << 24) + 1) * ((1L << 39) - (1L << 15));
	public float nextFloatII() {
		long num = 0;
		do {
			// generate 63 bit integer
			num = (((long) next(31)) << 32) + (next(32) & L_MASK_BOTTOM_32BIT);
		} while (FLOATII_MAX_LONG <= num);
		return ((num % ((1L << 24) + 1)) / (float) (1 << 24));
	}



    /** A bug fix for all versions of the JDK.  The JDK appears to
        use all four bytes in an integer as independent byte values!
        Totally wrong. I've submitted a bug report. */

    @Override
	public void nextBytes(byte[] bytes)
        {
        for (int x=0;x<bytes.length;x++) bytes[x] = (byte)next(8);
        }

    /** For completeness' sake, though it's not in java.util.Random.  */

    public char nextChar()
        {
        // chars are 16-bit UniCode values
        return (char)(next(16));
        }

    /** For completeness' sake, though it's not in java.util.Random. */

    public short nextShort()
        {
        return (short)(next(16));
        }

    /** For completeness' sake, though it's not in java.util.Random.  */

    public byte nextByte()
        {
        return (byte)(next(8));
        }

    /**
        Clears the internal gaussian variable from the RNG.  You only need to do this
        in the rare case that you need to guarantee that two RNGs have identical internal
        state.  Otherwise, disregard this method. See stateEquals(other).
    */
	public synchronized void clearGaussian() {
		__nextNextGaussian = MersenneTwisterFast.NOT_PREPARED;
	}

    /** A bug fix for all JDK code including 1.2.  nextGaussian can theoretically
        ask for the log of 0 and divide it by 0! See Java bug
        <a href="http://developer.java.sun.com/developer/bugParade/bugs/4254501.html">
        http://developer.java.sun.com/developer/bugParade/bugs/4254501.html</a>
    */
	@Override
	synchronized public double nextGaussian() {
		if (!Double.isNaN(__nextNextGaussian)) {
			double retval = __nextNextGaussian;
			__nextNextGaussian = MersenneTwisterFast.NOT_PREPARED;
			return retval;
		} else {
			double v1, v2, s;
			do {
				v1 = 2 * nextDouble() - 1; // between -1.0 and 1.0
				v2 = 2 * nextDouble() - 1; // between -1.0 and 1.0
				s = v1 * v1 + v2 * v2;
			} while (s >= 1 || s == 0);
			double multiplier = StrictMath.sqrt(-2 * StrictMath.log(s) / s);
			__nextNextGaussian = v2 * multiplier;
			return v1 * multiplier;
		}
	}

	synchronized public double nextGaussianOld() {
		if (!Double.isNaN(__nextNextGaussian)) {
			double retval = __nextNextGaussian;
			__nextNextGaussian = MersenneTwisterFast.NOT_PREPARED;
			return retval;
		} else {
			double v1, v2, s;
			do {
				v1 = 2 * nextDoubleOld() - 1; // between -1.0 and 1.0
				v2 = 2 * nextDoubleOld() - 1; // between -1.0 and 1.0
				s = v1 * v1 + v2 * v2;
			} while (s >= 1 || s == 0);
			double multiplier = StrictMath.sqrt(-2 * StrictMath.log(s) / s);
			__nextNextGaussian = v2 * multiplier;
			return v1 * multiplier;
		}
	}


    /**
     * Tests the code.
     */
    public static void main(String args[])
        {
        int j;

        MersenneTwister r;

        // CORRECTNESS TEST
        // COMPARE WITH http://www.math.keio.ac.jp/matumoto/CODES/MT2002/mt19937ar.out
        r = new MersenneTwister(new int[]{0x123, 0x234, 0x345, 0x456});
        System.out.println("Output of MersenneTwister with new (2002/1/26) seeding mechanism");
        for (j=0;j<1000;j++)
            {
            // first, convert the int from signed to "unsigned"
            long l = r.nextInt();
            if (l < 0 ) l += 4294967296L;  // max int value
            String s = String.valueOf(l);
            while(s.length() < 10) s = " " + s;  // buffer
            System.out.print(s + " ");
            if (j%5==4) System.out.println();
            }


        // SPEED TEST
        final long SEED = 4357L;

        int xx; long ms;
        System.out.println("\nTime to test grabbing 100000000 ints");

        r = new MersenneTwister(SEED);
        ms = System.currentTimeMillis();
        xx=0;
        for (j = 0; j < 100000000; j++)
            xx += r.nextInt();
        System.out.println("Mersenne Twister: " + (System.currentTimeMillis()-ms) + "          Ignore this: " + xx);

        System.out.println("To compare this with java.util.Random, run this same test on MersenneTwisterFast.");
        System.out.println("The comparison with Random is removed from MersenneTwister because it is a proper");
        System.out.println("subclass of Random and this unfairly makes some of Random's methods un-inlinable,");
        System.out.println("so it would make Random look worse than it is.");



        // TEST TO COMPARE TYPE CONVERSION BETWEEN
        // MersenneTwisterFast.java AND MersenneTwister.java

        System.out.println("\nGrab the first 1000 booleans");
        r = new MersenneTwister(SEED);
        for (j = 0; j < 1000; j++)
            {
            System.out.print(r.nextBoolean() + " ");
            if (j%8==7) System.out.println();
            }
        if (!(j%8==7)) System.out.println();

        System.out.println("\nGrab 1000 booleans of increasing probability using nextBoolean(double)");
        r = new MersenneTwister(SEED);
        for (j = 0; j < 1000; j++)
            {
            System.out.print(r.nextBoolean(j/999.0) + " ");
            if (j%8==7) System.out.println();
            }
        if (!(j%8==7)) System.out.println();

        System.out.println("\nGrab 1000 booleans of increasing probability using nextBoolean(float)");
        r = new MersenneTwister(SEED);
        for (j = 0; j < 1000; j++)
            {
            System.out.print(r.nextBoolean(j/999.0f) + " ");
            if (j%8==7) System.out.println();
            }
        if (!(j%8==7)) System.out.println();

        byte[] bytes = new byte[1000];
        System.out.println("\nGrab the first 1000 bytes using nextBytes");
        r = new MersenneTwister(SEED);
        r.nextBytes(bytes);
        for (j = 0; j < 1000; j++)
            {
            System.out.print(bytes[j] + " ");
            if (j%16==15) System.out.println();
            }
        if (!(j%16==15)) System.out.println();

        byte b;
        System.out.println("\nGrab the first 1000 bytes -- must be same as nextBytes");
        r = new MersenneTwister(SEED);
        for (j = 0; j < 1000; j++)
            {
            System.out.print((b = r.nextByte()) + " ");
            if (b!=bytes[j]) System.out.print("BAD ");
            if (j%16==15) System.out.println();
            }
        if (!(j%16==15)) System.out.println();

        System.out.println("\nGrab the first 1000 shorts");
        r = new MersenneTwister(SEED);
        for (j = 0; j < 1000; j++)
            {
            System.out.print(r.nextShort() + " ");
            if (j%8==7) System.out.println();
            }
        if (!(j%8==7)) System.out.println();

        System.out.println("\nGrab the first 1000 ints");
        r = new MersenneTwister(SEED);
        for (j = 0; j < 1000; j++)
            {
            System.out.print(r.nextInt() + " ");
            if (j%4==3) System.out.println();
            }
        if (!(j%4==3)) System.out.println();

        System.out.println("\nGrab the first 1000 ints of different sizes");
        r = new MersenneTwister(SEED);
        int max = 1;
        for (j = 0; j < 1000; j++)
            {
            System.out.print(r.nextInt(max) + " ");
            max *= 2;
            if (max <= 0) max = 1;
            if (j%4==3) System.out.println();
            }
        if (!(j%4==3)) System.out.println();

        System.out.println("\nGrab the first 1000 longs");
        r = new MersenneTwister(SEED);
        for (j = 0; j < 1000; j++)
            {
            System.out.print(r.nextLong() + " ");
            if (j%3==2) System.out.println();
            }
        if (!(j%3==2)) System.out.println();

        System.out.println("\nGrab the first 1000 longs of different sizes");
        r = new MersenneTwister(SEED);
        long max2 = 1;
        for (j = 0; j < 1000; j++)
            {
            System.out.print(r.nextLong(max2) + " ");
            max2 *= 2;
            if (max2 <= 0) max2 = 1;
            if (j%4==3) System.out.println();
            }
        if (!(j%4==3)) System.out.println();

        System.out.println("\nGrab the first 1000 floats");
        r = new MersenneTwister(SEED);
        for (j = 0; j < 1000; j++)
            {
            System.out.print(r.nextFloat() + " ");
            if (j%4==3) System.out.println();
            }
        if (!(j%4==3)) System.out.println();

        System.out.println("\nGrab the first 1000 doubles");
        r = new MersenneTwister(SEED);
        for (j = 0; j < 1000; j++)
            {
            System.out.print(r.nextDouble() + " ");
            if (j%3==2) System.out.println();
            }
        if (!(j%3==2)) System.out.println();

        System.out.println("\nGrab the first 1000 gaussian doubles");
        r = new MersenneTwister(SEED);
        for (j = 0; j < 1000; j++)
            {
            System.out.print(r.nextGaussian() + " ");
            if (j%3==2) System.out.println();
            }
        if (!(j%3==2)) System.out.println();

        }

    }

/**
 * Immutable Memento object for the internal state of Mersenne Twister.
 */
class MTMemento implements Serializable {
	private static final long serialVersionUID = 4393541727157976564L;

	private final int[] mt_;
	protected final int mti_;
	protected final double gauss_;

	protected MTMemento(int[] mt, int mti, double nextGauss) {
		mt_ = mt.clone();
		mti_ = mti;
		gauss_ = nextGauss;
	}

	protected int[] getVector() {
		return mt_.clone();
	}

	protected boolean stateEquals(int[] mt, int mti, double gauss) {
		return mti_ == mti && Arrays.equals(mt_, mt) &&
			(Double.isNaN(gauss_) ? Double.isNaN(gauss_) : gauss_ == gauss);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (!this.getClass().equals(obj.getClass())) return false;
		MTMemento mtm = (MTMemento) obj;
		return stateEquals(mtm.mt_, mtm.mti_, mtm.gauss_);
	}

	@Override
	public int hashCode() {
		return Arrays.hashCode(mt_) + mti_;
	}
}