package lib.experiments;

public class SequenceSeeder extends Seeder {
	private int seed_;

	@Override
	public void setSeed(int seed) {
		seed_ = seed;
	}

	@Override
	public int nextSeed() {
		if (seed_ == -1) {
			seed_ = 1;
		} else if (seed_ == Integer.MAX_VALUE) {
			seed_ = Integer.MIN_VALUE;
		} else {
			seed_++;
		}
		return seed_;
	}

}
