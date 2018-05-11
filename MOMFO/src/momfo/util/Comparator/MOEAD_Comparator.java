package momfo.util.Comparator;

import java.util.HashMap;

import lib.math.BuildInRandom;
import momfo.util.JMException;

public class MOEAD_Comparator extends Comparator {

	double theta;

	public  MOEAD_Comparator(boolean ismax,BuildInRandom random){
		super(ismax,random);
	}

	@Override
	public int execute(Object one, Object two) {
		return 0;
	}

}
