package momfo.operators.selection.environmentalselection;

import javax.naming.NameNotFoundException;

import lib.experiments.CommandSetting;
import lib.experiments.JMException;
import lib.experiments.ParameterNames;
import lib.lang.NeedOverriden;
import lib.math.BuildInRandom;
import momfo.core.Operator;
import momfo.core.Population;

public abstract class EnvironmentalSelection extends Operator {

	protected int populationSize;

	protected boolean isMax;

	protected BuildInRandom random;

	@NeedOverriden
	public void build(CommandSetting st) throws NameNotFoundException{
		random = st.get(ParameterNames.RANDOM_GENERATOR);
		isMax = st.getAsBool(ParameterNames.IS_MAX);
	}

	abstract public Population getNextPopulation(Population pop) throws JMException;

	public EnvironmentalSelection(){
		random = null;
	}

}
