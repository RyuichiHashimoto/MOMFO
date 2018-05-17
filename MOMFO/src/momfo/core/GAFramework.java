package momfo.core;


import static lib.experiments.ParameterNames.*;
import static momfo.util.GAEvent.*;

import java.io.IOException;
import java.io.PrintWriter;

import javax.naming.NamingException;

import Network.Solver;
import Network.GridComputing.asg.cliche.Command;
import lib.experiments.CommandSetting;
import lib.experiments.FormatDate;
import lib.experiments.ParameterNames;
import lib.experiments.Seeder;
import lib.experiments.Exception.CommandSetting.notFoundException;
import lib.math.BuildInRandom;
import momfo.util.JMException;

public class GAFramework extends Solver{

	public int nTrials;
	protected long startTime_;
	protected int run_;
	protected PrintWriter info;
	protected Seeder seeder_;
	protected GeneticAlgorithm ga_;

	public GeneticAlgorithm getGA() {
		return ga_;
	}

	@Override
//	@NeedParameters({NTRIALS, RESULT, SEEDER, SEEDER_SEED, INITIALIZATION,
//		EVALUATION, RECOMBINATION, MUTATION})
	protected void buildImpl() throws NamingException, IOException, ReflectiveOperationException, JMException, notFoundException {
		if (!(setting.get(PROBLEM_SET) instanceof ProblemSet)) {
			setting.putForce(PROBLEM_SET, setting.getToClass(PROBLEM_SET, "momfo.problems.MOMFOP.NTU").getDeclaredConstructor(CommandSetting.class).newInstance(setting));
		}
		// configurations for GA
		nTrials = setting.getAsInt(NTRIALS);
		seeder_ = (Seeder) setting.getAsInstance(SEEDER);
		seeder_.setSeed(setting.getAsInt(SEEDER_SEED));
		
		try {
			seeder_.skip(setting.getAsInt(SEED_OFFSET));
		} catch (NamingException e) {

		}
		setting.put(RANDOM_GENERATOR, new BuildInRandom(ParameterNames.DEFAULT_SEED));
		String ga = setting.getAsStr(GA);
		
		if (!ga.contains(".")) setting.putForce(GA, DEF_GA_PACKAGE + ga.toLowerCase() +"."+ ga);
		setting.set(GA, ga_ = setting.getAsInstance(GA));
		
		ga_.build(setting);
		
	}


	@Override
	public void solve() throws IOException, ClassNotFoundException, JMException, notFoundException, NamingException {
		startTime_ = System.currentTimeMillis();
		System.gc();  // clear heap before running

		for (run_ = 0; run_ < nTrials; run_++) {
			System.out.println((run_ + 1) + " run has started");
			runOnce();			
		}
	}

	public void runOnce() throws IOException, ClassNotFoundException, JMException, notFoundException, NamingException {
		// TODO: record seed value
		int seed = seeder_.nextSeed();
		ga_.initialize(seed);
//		System.out.println(StringUtility.toString(ga_.getPopulation().getAllObjectives()));
		notifyEvent(AFTER_INTITIALIZATION);
		while (!ga_.terminate()) {
			ga_.recombination();
			ga_.nextGeneration();
			notifyEvent(AFTER_GENERATION);
		}
		ga_.finEvaluation();
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

	public Population getPopulation() {
		return ga_.getPopulation();
	}

	public Population[] getPopulationSet(){
		return ga_.getPopulationSet();
	}

	
	public String getGAName() {
		return ga_.getClass().getSimpleName();
	}
}
