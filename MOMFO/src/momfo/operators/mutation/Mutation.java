package momfo.operators.mutation;
import javax.naming.NameNotFoundException;

import lib.experiments.CommandSetting;
import lib.experiments.ParameterNames;
import lib.math.BuildInRandom;
import momfo.core.Operator;
import momfo.core.Solution;
import momfo.util.JMException;

public abstract class Mutation extends Operator {

	protected BuildInRandom random;

	protected double mutationProbability;

	public abstract Solution mutation(Solution parent) throws JMException;

	public void build(CommandSetting setting) throws NameNotFoundException, JMException {
		random = (BuildInRandom)setting.get(ParameterNames.RANDOM_GENERATOR);
	}

}

