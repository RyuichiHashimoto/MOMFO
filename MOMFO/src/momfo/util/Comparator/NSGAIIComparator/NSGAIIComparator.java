package momfo.util.Comparator.NSGAIIComparator;

import java.util.HashMap;

import momfo.core.Solution;
import momfo.util.JMException;
import momfo.util.Comparator.DominationComparator;

public abstract class NSGAIIComparator extends DominationComparator {

	public NSGAIIComparator(HashMap<String, Object> parameters) throws JMException {
		super(parameters);
		// TODO 自動生成されたコンストラクター・スタブ
	}

	public int isDominaning(Solution me, Solution you){
		if (isMAX_){
			return isDominanceForMAX(me,you);
		} else {
			return isDominanceForMIN(me,you);
		}

	}

	// 大きいほうがいい
	public  int isDominanceForMAX(Solution me , Solution you){
			int size = me.getNumberOfObjectives();
			for(int i = 0;i< size;i++){
				if(me.getObjective(i) < you.getObjective(i) ){
					return -1;
				}
			}
			for(int i = 0;i< size;i++){
				if(me.getObjective(i) > you.getObjective(i) ){
					return 1;
				}
			}
			return 0;
	}



	//lower Objective value  is better than high Objective Value;
	public int isDominanceForMIN(Solution me , Solution you){
		int size = me.getNumberOfObjectives();
		for(int i = 0;i< size;i++){
			if(me.getObjective(i) > you.getObjective(i) ){
				return -1;
			}
		}

		//
		for(int i = 0;i< size;i++){
			if(me.getObjective(i) < you.getObjective(i) ){
				return 1;
			}
		}
		return 0;
	}


}
