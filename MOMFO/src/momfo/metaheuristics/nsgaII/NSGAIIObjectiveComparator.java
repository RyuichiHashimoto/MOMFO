package momfo.metaheuristics.nsgaII;

import momfo.core.Solution;
import momfo.util.JMException;
import momfo.util.Comparator.Comparator;

public class NSGAIIObjectiveComparator extends Comparator{

	public NSGAIIObjectiveComparator(boolean max) {
		super(max);
	}
	private int objective ;


	public NSGAIIObjectiveComparator(boolean max,int objective_){
		super(max);
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
