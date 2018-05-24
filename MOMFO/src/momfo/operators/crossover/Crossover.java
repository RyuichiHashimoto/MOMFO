package momfo.operators.crossover;

import javax.naming.NameNotFoundException;

import lib.experiments.CommandSetting;
import lib.experiments.JMException;
import lib.experiments.ParameterNames;
import lib.math.BuildInRandom;
import momfo.core.Operator;
import momfo.core.Solution;

public abstract class Crossover extends Operator{

	BuildInRandom random;

	protected double crossoverProbability;


	public abstract Solution[] crossover(Solution ... parent) throws JMException;

	public void build(CommandSetting setting) throws NameNotFoundException {

		random = (BuildInRandom)setting.get(ParameterNames.RANDOM_GENERATOR);

	  random.setSeed(0);

	}

}