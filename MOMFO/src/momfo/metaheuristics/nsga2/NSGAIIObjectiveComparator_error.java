package momfo.metaheuristics.nsga2;

import lib.experiments.JMException;
import momfo.core.Solution;
import momfo.util.Comparator.Comparator;

public class NSGAIIObjectiveComparator_error extends Comparator{

	public NSGAIIObjectiveComparator_error(boolean max) {
		super(max,null);
	}
	private int objective ;


	public NSGAIIObjectiveComparator_error(boolean max,int objective_){
		super(max,null);
		objective = objective_;
	}


	@Override

	public int execute(Object one, Object two) throws JMException {
		assert one instanceof Solution : "Object one is not Solution";
		assert two instanceof Solution : "Object two is not Solution";
		Solution me = (Solution)one;
		Solution you = (Solution)two;

		if((isMAX_) ==  me.getObjective(objective) > you.getObjective(objective)){
			return 1;
		}else if((isMAX_) == me.getObjective(objective) < you.getObjective(objective))	 {
			return -1;
		}else if(me.getID() > you.getID()){
			return 1;
		}else if(me.getID() < you.getID()){
			return -1;
		}
		assert false: "plasoo";
		return 0;
	}

}
