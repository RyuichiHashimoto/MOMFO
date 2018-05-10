package lib.math;


/**
 * The Legacy implementation of Mersenne Twister. Do not use this class.
 *
 * @author Hiroyuki Masuda
 *
 */
@Deprecated
public class MersenneTwisterLegacy extends MersenneTwisterFast {
	private static final long serialVersionUID = 1L;

	public MersenneTwisterLegacy(){
		super();
		setSeedOldStyle(4357);
	}

	public MersenneTwisterLegacy(long seed) {
		super(seed);
		setSeedOldStyle(seed);
	}


	@Override
	public void setSeed(long seed) {
		setSeedOldStyle(seed);
	}

	@Override
	public void setSeed(int seed) {
		setSeedOldStyle(seed);
	}

	@Override
	public double nextDouble() {
		return super.nextDoubleOld();
	}
}
