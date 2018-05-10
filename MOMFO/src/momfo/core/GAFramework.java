package momfo.core;


import java.io.IOException;
import java.io.PrintWriter;

import javax.naming.NamingException;

import Network.Solver;
import Network.GridComputing.asg.cliche.Command;
import lib.experiments.FormatDate;
import lib.experiments.NeedParameters;
import lib.experiments.Seeder;

public class GAFramework extends Solver{

	public int nTrials;
	protected long startTime_;
	protected int run_;
	protected PrintWriter info;
	protected Seeder seeder_;
	protected GeneticAlgorithm ga_;

	@Override
	@NeedParameters({NTRIALS, RESULT, SEEDER, SEEDER_SEED, INITIALIZATION,
		EVALUATION, RECOMBINATION, MUTATION})
	protected void buildImpl() throws NamingException, IOException, ReflectiveOperationException {
/*		
		if (!(setting.get(PROBLEM) instanceof MOProblem)) {
			setting.put(PROBLEM, setting.getToClass(PROBLEM, "mo.problem").getDeclaredConstructor(Setting.class).newInstance(setting));
		}
		// configurations for GA
		nTrials = setting.getAsInt(NTRIALS);
		seeder_ = (Seeder) setting.getAsInstance(SEEDER);
		seeder_.setSeed(setting.getAsInt(SEEDER_SEED));
		if (setting.containsKey(SEED_OFFSET)) seeder_.skip(setting.getAsInt(SEED_OFFSET));
		setting.put(RANDOM_GENERATOR, new MersenneTwisterFast());
		String ga = setting.getAsStr(GA);
		if (!ga.contains(".")) setting.set(GA, DEF_GA_PACKAGE + ga.toLowerCase() +"."+ ga);
		setting.set(GA, ga_ = setting.getAsInstance(GA));

		// build
		/* NeedChecked: Do not use rand here because the seed of rand is set
		 * in the initialize() method;
		 */
		ga_.build(setting);
	
	}


	@Override
	public void solve() throws IOException {
		startTime_ = System.currentTimeMillis();
		System.gc();  // clear heap before running

		for (run_ = 0; run_ < nTrials; run_++) {
			runOnce();
		}
	}

	public void runOnce() throws IOException {
		// TODO: record seed value
		int seed = seeder_.nextSeed();
		ga_.initialize(seed);
		notifyEvent(AFTER_INTITIALIZATION);
		while (!ga_.terminate()) {
			ga_.recombination();
			ga_.nextGeneration();
			notifyEvent(AFTER_GENERATION);
		}
		notifyEvent(AFTER_TRIAL);
	}

	@Command
	public boolean isTerminated() {
		return ga_.terminate();
	}

	@Command
	public int getEvaluation() {
		return ga_.getEvaluations();
	}

	@Command
	public int getGeneration() {
		return ga_.getGeneration();
	}

	@Command
	public int getRun() {
		return run_;
	}

	// TODO: Stop time
	public long getEpocTime() {
		return System.currentTimeMillis() - startTime_;
	}

	@Command
	public String getTime() {
		return FormatDate.readbleTime(getEpocTime());
	}

	public Solutionset getPopulation() {
		return ga_.getPopulation();
	}

	public String getGAName() {
		return ga_.getClass().getSimpleName();
	}
}
