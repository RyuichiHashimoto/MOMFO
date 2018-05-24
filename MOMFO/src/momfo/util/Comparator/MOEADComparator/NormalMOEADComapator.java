package momfo.util.Comparator.MOEADComparator;

import lib.experiments.JMException;
import lib.math.BuildInRandom;
import momfo.core.Solution;
import momfo.util.ScalarzingFunction.ScalarzingFunction;

public class NormalMOEADComapator extends MOEADComparator {

	public NormalMOEADComapator() {
		super();
	}

	public NormalMOEADComapator(boolean ismax, BuildInRandom random, ScalarzingFunction d) throws JMException {
		super(ismax, random, d);
	}
	
	

	@Override
	public int execute(Object one, Object two) throws JMException {
		Solution solone = (Solution) one;
		Solution soltwo = (Solution) two;

		double scalar_one = ScalaringFunction_.execute(solone, weightedVector.get(), referencePoint.get());
		double scalar_two = ScalaringFunction_.execute(soltwo, weightedVector.get(), referencePoint.get());

		if (isMAX_ == (scalar_one > scalar_two)) {
			return 1;
		} else if (isMAX_ == (scalar_one < scalar_two)) {
			return -1;
		}
		return 0;
	}

}
