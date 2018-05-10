package momfo.util.Comparator.NSGAIIComparator;

import java.util.HashMap;

import momfo.core.Solution;
import momfo.util.JMException;



public class NSGAIIComparatorDominance extends NSGAIIComparator {

	public NSGAIIComparatorDominance(HashMap<String, Object> parameters) throws JMException {
		super(parameters);
		// TODO 自動生成されたコンストラクター・スタブ
	}

	@Override
	public int execute(Object one, Object two) throws JMException {
		Solution me = (Solution)one;
		Solution you = (Solution)two;


		return isDominaning(me, you);

	}


}
