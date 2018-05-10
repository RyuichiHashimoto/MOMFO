package momfo.Indicator;

import java.util.HashMap;

import lib.math.BuildInRandom;
import momfo.core.Population;
import momfo.util.JMException;

public abstract class Indicator {

	BuildInRandom random;

	public void setRandom(BuildInRandom  random_){
		random = random_;
	}

	protected static boolean isMAXproblem_;

	public void setMAXProblem(){
		isMAXproblem_ = true;
	}

	public void setMINProblem(){
		isMAXproblem_ = false;
	}

	abstract public Object execute(Population ind, HashMap<String,Object> d) throws JMException;

	abstract public String getIndicatorName();
}
