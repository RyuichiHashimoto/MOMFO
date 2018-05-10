package momfo.util.Comparator.SMSEMOAComparator;

import java.util.HashMap;

import momfo.core.Solution;
import momfo.util.JMException;
import momfo.util.Comparator.Comparator;

public abstract class SMSEMOAComparator extends Comparator {

	public SMSEMOAComparator(HashMap<String, Object> parameters) throws JMException {
		super(parameters);
		// TODO 自動生成されたコンストラクター・スタブ
	}

	public boolean isDominaning(Solution me, Solution you){
		if (isMAX_){
			return isDominanceForMAX(me,you);
		} else {
			return isDominanceForMIN(me,you);
		}

	}

	// 大きいほうがいい
	public static boolean isDominanceForMAX(Solution me , Solution you){
			int size = me.getNumberOfObjectives();
			for(int i = 0;i< size;i++){
				if(me.getObjective(i) < you.getObjective(i) ){
					return false;
				}
			}
			for(int i = 0;i< size;i++){
				if(me.getObjective(i) > you.getObjective(i) ){
					return true;
				}
			}
			return false;
	}



	//lower Objective value  is better than high Objective Value;
	public static boolean isDominanceForMIN(Solution me , Solution you){
		int size = me.getNumberOfObjectives();
		for(int i = 0;i< size;i++){
			if(me.getObjective(i) > you.getObjective(i) ){
				return false;
			}
		}

		//
		for(int i = 0;i< size;i++){
			if(me.getObjective(i) < you.getObjective(i) ){
				return true;
			}
		}
		return false;
	}


}
