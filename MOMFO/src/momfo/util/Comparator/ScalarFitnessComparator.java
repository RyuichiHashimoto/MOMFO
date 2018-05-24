package momfo.util.Comparator;

import java.util.HashMap;

import lib.experiments.JMException;
import lib.math.BuildInRandom;
import momfo.core.Solution;

public class ScalarFitnessComparator extends Comparator{


	public ScalarFitnessComparator(boolean b,BuildInRandom random) {
		super(b,random);
// 	TODO 自動生成されたコンストラクター・スタブ
	}

	@Override
	public int execute(Object one, Object two) throws JMException {
		assert (one instanceof Solution): "Object one is not Solution";
		assert (one instanceof Solution): "Object two is not Solution";

		Solution sol_one = (Solution)one;
		Solution sol_two = (Solution)two;

		if( sol_one.getScalarFitness()> sol_two.getScalarFitness() ){
			return 1;
		} else if (sol_one.getScalarFitness() < sol_two.getScalarFitness()){
			return -1;
		}

		
		
		return 0;
	}

}
