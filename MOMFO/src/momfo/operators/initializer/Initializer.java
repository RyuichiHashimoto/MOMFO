package momfo.operators.initializer;

import java.io.IOException;

import javax.naming.NamingException;

import lib.experiments.CommandSetting;
import lib.experiments.NeedParameters;
import lib.experiments.ParameterNames;
import lib.lang.NeedOverriden;
import lib.math.MersenneTwisterFast;
import momfo.core.Operator;
import momfo.core.Solution;

abstract public class Initializer extends Operator {
	protected MersenneTwisterFast mt;

	public void setSeed(int seed) {
		mt.setSeed(seed);
	}

	@Override
	@NeedOverriden
	@NeedParameters(ParameterNames.RANDOM_GENERATOR)
	public void build(CommandSetting s) throws NamingException, ReflectiveOperationException, IOException {
		mt = (MersenneTwisterFast) s.get("randomGenerator");
	}

	abstract public void initialize(Solution i);

}
