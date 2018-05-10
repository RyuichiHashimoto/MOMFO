package lib.experiments;

import java.util.Date;
import java.util.HashSet;

import lib.math.MersenneTwisterFast;

public class NoDuplicationRandomSeeder extends Seeder {
	private MersenneTwisterFast mt_ = new MersenneTwisterFast();
	private HashSet<Integer> outputs_ = new HashSet<Integer>();

	public NoDuplicationRandomSeeder() {
		this((int) (((new Date()).getTime() % Integer.MAX_VALUE) + 1));
	}

	public NoDuplicationRandomSeeder(int seed) {
		setSeed(seed);
	}

	public void clearOutputs() {
		outputs_.clear();
	}

	@Override
	public void setSeed(int seed) {
		if (seed == 0) throw new IllegalArgumentException("The value of seed cannot be 0.");
		mt_.setSeed(seed);
	}

	@Override
	public int nextSeed() {
		int retval;
		do {
			retval = mt_.nextInt();
		} while(!outputs_.add(retval));
		return retval;
	}
}
