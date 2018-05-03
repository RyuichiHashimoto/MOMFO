package momfo.util.Comparator.NSGAIIComparator;

import java.util.HashMap;

import momfo.core.Solution;
import momfo.util.JMException;

public class NSGAIIComparatorNextGen extends NSGAIIComparator{

	public NSGAIIComparatorNextGen(HashMap<String, Object> parameters) {
		super(parameters);
		// TODO 自動生成されたコンストラクター・スタブ
	}

	@Override
	public int execute(Object one, Object two) throws JMException {
		Solution a = (Solution)one;
		Solution b = (Solution)two;


		if(a.getRank() < b.getRank()){
			return 1 ;
		} else if (a.getRank() > b.getRank()){
			return -1;
		}

		if(a.getCrowdDistance_() > b.getCrowdDistance_()){
			return 1;
		}  else if(a.getCrowdDistance_() < b.getCrowdDistance_()) {
			return -1;
		}


		return 0;


	}
}
