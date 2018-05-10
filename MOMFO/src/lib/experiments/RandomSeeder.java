package lib.experiments;

import lib.math.MersenneTwisterFast;

public class RandomSeeder extends Seeder {
	private MersenneTwisterFast mt_;

	public RandomSeeder(int seed) {
		if (seed == 0)
			throw new IllegalArgumentException("Seed 0 is not allowed");
		mt_ = new MersenneTwisterFast(seed);
	}

	@Override
	public void setSeed(int seed) {
		mt_.setSeed(seed);
	}

	@Override
	public int nextSeed() {
		int retval;
		while ((retval = mt_.nextInt()) == 0);
		return retval;
	}
}
