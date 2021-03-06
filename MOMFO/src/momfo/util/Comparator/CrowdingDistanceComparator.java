package momfo.util.Comparator;

import lib.math.BuiltInRandom;
import momfo.core.Solution;
import momfo.util.JMException;

public class CrowdingDistanceComparator extends Comparator{

	public CrowdingDistanceComparator(boolean is,BuiltInRandom random) {
		super(is,random);
		// TODO 自動生成されたコンストラクター・スタブ
	}

	@Override
	public int execute(Object one, Object two) throws JMException {
		assert (one instanceof Solution): "Object one is not Solution";
		assert (one instanceof Solution): "Object two is not Solution";

		Solution sol_one = (Solution)one;
		Solution sol_two = (Solution)two;

		if( sol_one.getCrowdDistance_()< sol_two.getCrowdDistance_() ){
			return 1;
		} else if (sol_one.getCrowdDistance_() > sol_two.getCrowdDistance_()){
			return -1;
		}

		return 0;
	}

}
