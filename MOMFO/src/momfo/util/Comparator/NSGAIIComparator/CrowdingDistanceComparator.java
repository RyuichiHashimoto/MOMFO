package momfo.util.Comparator.NSGAIIComparator;

import java.util.HashMap;

import lib.math.BuildInRandom;
import momfo.core.Solution;
import momfo.util.JMException;

public class CrowdingDistanceComparator extends  NSGAIIComparator{


	public CrowdingDistanceComparator(boolean d, BuildInRandom random_) {
		super(d, random_);
		// TODO Auto-generated constructor stub
	}

	@Override
	public int execute(Object one, Object two) throws JMException {
		Solution a = (Solution)one;
		Solution b = (Solution)two;

		if(a.getCrowdDistance_() > b.getCrowdDistance_()){
			return 1;
		}  else if(a.getCrowdDistance_() < b.getCrowdDistance_()) {
			return -1;
		}

		if(random.nextDoubleIE() > 0.5){
			return 1;
		}  else  {
			return -1;
		}

	}

}
