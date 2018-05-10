package lib.experiments;

public abstract class Seeder {
	abstract public void setSeed(int seed);
	abstract public int nextSeed();

	public void skip(int n) {
		for (int i = 0; i < n; i++) nextSeed();
	}
}
