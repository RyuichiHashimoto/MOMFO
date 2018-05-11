package momfo.util.Comparator.NSGAIIComparator;

import java.util.HashMap;

import lib.math.BuildInRandom;
import momfo.core.Solution;
import momfo.util.JMException;



public class NSGAIIComparatorDominance extends NSGAIIComparator {


	public NSGAIIComparatorDominance(boolean d, BuildInRandom random_) {
		super(d, random_);
		// TODO Auto-generated constructor stub
	}

	@Override
	public int execute(Object one, Object two) throws JMException {
		Solution me = (Solution)one;
		Solution you = (Solution)two;


		return isDominaning(me, you);

	}


}
