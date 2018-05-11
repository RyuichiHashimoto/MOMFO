package momfo.operators.selection;

import javax.naming.NameNotFoundException;

import lib.experiments.CommandSetting;
import lib.experiments.ParameterNames;
import lib.experiments.Exception.CommandSetting.notFoundException;
import lib.math.BuildInRandom;
import	momfo.core.Operator;
import momfo.core.Population;
import momfo.util.JMException;


public abstract class Selection extends Operator{

	protected BuildInRandom random;
	
	public abstract int selection(Population pop) throws JMException;

	public void build(CommandSetting setting) throws NameNotFoundException, JMException, notFoundException {
		random = (BuildInRandom)setting.get(ParameterNames.RANDOM_GENERATOR);
	}
}
