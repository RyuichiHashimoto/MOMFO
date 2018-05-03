package momfo.metaheuristics.Island.EnvironmentalSelection;

import java.util.HashMap;

import momfo.core.Population;
import momfo.util.JMException;

public abstract class EnvironmentalSelection {

	HashMap<String, Object> parameter;

	protected int populationSize;

	protected boolean isMax_;

	abstract public Population getNextPopulation(Population pop) throws JMException;

	public EnvironmentalSelection(int popSize,boolean isMax,HashMap<String, Object> parameter_){
		populationSize = popSize;
		parameter = parameter_;
		isMax_ = isMax;
	}

	public String getName (){
		return name;
	}

	private String name;

}
