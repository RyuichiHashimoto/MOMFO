package momfo.util.Comparator.MultiIslandMOEADComparator;


import java.util.HashMap;

import lib.math.BuildInRandom;
import momfo.core.Solution;
import momfo.util.JMException;
import momfo.util.ScalarzingFunction.ScalarzingFunction;
public class NomalMultiIslandMOEAD extends MultiIslandMOEADComparator {


	public NomalMultiIslandMOEAD(boolean ismax, BuildInRandom random, ScalarzingFunction[] d) throws JMException {
		super(ismax, random, d);
		// TODO Auto-generated constructor stub
	}

	@Override
	public int execute(Object one, Object two) throws JMException {
		Solution solone = (Solution)one;
		Solution soltwo = (Solution)two;

		double scalar_one = NowScalaringFunction_.execute(solone, weightedVector.get(), referencePoint.get());
		double scalar_two = NowScalaringFunction_.execute(soltwo, weightedVector.get(), referencePoint.get());

		if (isMAX_== (scalar_one > scalar_two)){
			return 1;
		} else if (isMAX_== (scalar_one < scalar_two)){
			return -1;

		}
			return 0;
	}


}
