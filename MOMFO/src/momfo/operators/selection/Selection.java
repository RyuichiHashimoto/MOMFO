package momfo.operators.selection;

import javax.naming.NameNotFoundException;

import lib.experiments.CommandSetting;
import lib.experiments.JMException;
import lib.experiments.ParameterNames;
import lib.math.BuildInRandom;
import	momfo.core.Operator;
import momfo.core.Population;


public abstract class Selection extends Operator{

	protected BuildInRandom random;
	
	public abstract int selection(Population pop) throws JMException;

	public void build(CommandSetting setting) throws NameNotFoundException, JMException{
		random = (BuildInRandom)setting.get(ParameterNames.RANDOM_GENERATOR);
	}
}
