package momfo.operators.crossover;

import javax.naming.NameNotFoundException;

import lib.experiments.CommandSetting;
import lib.experiments.ParameterNames;
import lib.math.BuildInRandom;
import momfo.core.Operator;
import momfo.core.Solution;
import momfo.util.JMException;

public abstract class Crossover extends Operator{

	BuildInRandom random;
	
	protected double crossoverProbability;

	public abstract void crossover(Solution offspring1, Solution offspring2 ,Solution ... parent) throws JMException;

	public void build(CommandSetting setting) throws NameNotFoundException {
		random = (BuildInRandom)setting.get(ParameterNames.RANDOM_GENERATOR);
	}
}