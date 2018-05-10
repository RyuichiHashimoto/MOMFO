package momfo.util.Comparator;

import java.util.HashMap;

import lib.math.BuiltInRandom;
import momfo.core.Solution;
import momfo.util.JMException;

public class ScalarFitnessComparator extends Comparator{

	public ScalarFitnessComparator(HashMap parameters) throws JMException {
		super(parameters);
		// TODO 自動生成されたコンストラクター・スタブ
	}

	public ScalarFitnessComparator(boolean b,BuiltInRandom random) {
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
