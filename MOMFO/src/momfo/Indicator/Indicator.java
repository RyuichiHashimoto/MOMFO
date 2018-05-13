package momfo.Indicator;

import javax.naming.NameNotFoundException;

import Network.Buildable;
import lib.experiments.CommandSetting;
import lib.experiments.ParameterNames;
import lib.math.BuildInRandom;

public abstract class Indicator implements Buildable{

	protected BuildInRandom random;

	protected boolean isMAXproblem_;

	public void build(CommandSetting st) throws NameNotFoundException {
		random = st.get(ParameterNames.RANDOM_GENERATOR);
		isMAXproblem_ = st.get(ParameterNames.IS_MAX);
	}
	
	public void setRandom(BuildInRandom  random_){
		random = random_;
	}

	

	public void setMAXProblem(){
		isMAXproblem_ = true;
	}

	public void setMINProblem(){
		isMAXproblem_ = false;
	}


	public String getIndicatorName() {
		return this.getClass().getName(); 
	};
}
