package momfo.operators.initializer;

import java.io.IOException;

import javax.naming.NamingException;

import lib.experiments.CommandSetting;
import lib.experiments.NeedParameters;
import lib.experiments.ParameterNames;
import lib.lang.NeedOverriden;
import lib.math.BuildInRandom;
import lib.math.MersenneTwisterFast;
import momfo.core.Operator;
import momfo.core.Solution;

abstract public class Initializer extends Operator {
	protected BuildInRandom mt;

	public void setSeed(int seed) {
		mt.setSeed(seed);
	}

	@Override
	@NeedOverriden
	@NeedParameters(ParameterNames.RANDOM_GENERATOR)
	public void build(CommandSetting s) throws NamingException, ReflectiveOperationException, IOException {
		mt = (BuildInRandom) s.get(ParameterNames.RANDOM_GENERATOR);
	}

	abstract public void initialize(Solution i);

}
