package momfo.util.Comparator.MultiIslandMOEADComparator;


import java.util.HashMap;

import momfo.core.Solution;
import momfo.util.JMException;
import momfo.util.ScalarzingFunction.ScalarzingFunction;
public class NomalMultiIslandMOEAD extends MultiIslandMOEADComparator {

	public NomalMultiIslandMOEAD(HashMap<String, Object> parameters) {
		super(parameters);
		// TODO 自動生成されたコンストラクター・スタブ
	}
	public NomalMultiIslandMOEAD(HashMap<String, Object> parameters,ScalarzingFunction[] set) {
		super(parameters);
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
