package momfo.util.Comparator.NSGAIIComparator;

import javax.naming.NameNotFoundException;

import lib.experiments.CommandSetting;
import lib.math.BuildInRandom;
import momfo.core.Solution;
import momfo.util.Comparator.DominationComparator;

public abstract class NSGAIIComparator extends DominationComparator {

	public NSGAIIComparator(){
		super();
	}
	public void build(CommandSetting st) throws NameNotFoundException,  ReflectiveOperationException {
		super.build(st);
	}
	
	public NSGAIIComparator(boolean d, BuildInRandom random_) {
		super(d, random_);
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
