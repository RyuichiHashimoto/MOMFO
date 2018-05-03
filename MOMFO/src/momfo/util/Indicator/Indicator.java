package momfo.util.Indicator;

import java.util.HashMap;

import momfo.core.Population;
import momfo.util.JMException;

public abstract class Indicator {


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
