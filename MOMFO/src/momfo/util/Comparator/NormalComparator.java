package momfo.util.Comparator;

import lib.experiments.JMException;
import lib.math.BuildInRandom;

public class NormalComparator extends Comparator{

	public NormalComparator(boolean is,BuildInRandom random_) {
		super(is,random_);
	}

	@Override
	public int execute(Object one, Object two) throws JMException {
		assert (one instanceof Double): "Object one is not Double";
		assert (one instanceof Double): "Object two is not Double";

		Double int_one = (Double)one;
		Double int_two = (Double)two;

		if( int_one < int_two ){
			return 1;
		} else if (int_one > int_two){
			return -1;
		}

		return 0;
	}

}
