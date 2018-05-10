package momfo.util.Comparator.NSGAIIComparator;

import java.util.HashMap;

import momfo.core.Solution;
import momfo.util.JMException;

public class CrowdingDistanceComparator extends  NSGAIIComparator{

	public CrowdingDistanceComparator(HashMap<String, Object> parameters) throws JMException {
		super(parameters);
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
