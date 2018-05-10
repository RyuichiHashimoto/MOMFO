package momfo.operators.selection;

import javax.naming.NameNotFoundException;

import lib.experiments.CommandSetting;
import lib.experiments.ParameterNames;
import lib.math.BuiltInRandom;
import	momfo.core.Operator;
import momfo.core.Solution;
import momfo.util.JMException;


public abstract class Selection extends Operator{

	protected BuiltInRandom random;

	protected double mutationProbability;

	public abstract void mutation(Solution offspring,Solution parent) throws JMException;

	public void build(CommandSetting setting) throws NameNotFoundException, JMException {
		random = (BuiltInRandom)setting.get(ParameterNames.RANDOM_GENERATOR);
	}
}
