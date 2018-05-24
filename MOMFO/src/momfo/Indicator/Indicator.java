package momfo.Indicator;

import java.io.Serializable;

import javax.naming.NameNotFoundException;

import Network.Buildable;
import lib.experiments.CommandSetting;
import lib.experiments.ParameterNames;
import lib.math.BuildInRandom;

public abstract class Indicator implements Buildable,Serializable{

	protected BuildInRandom random;

	protected boolean isMAXproblem_;

	public void build(CommandSetting st) throws NameNotFoundException, ReflectiveOperationException {
		random = st.get(ParameterNames.RANDOM_GENERATOR);

		if(st.containsKey(ParameterNames.IS_MULTITASK)) {

			if(st.getAsBool(ParameterNames.IS_MULTITASK)) {
				isMAXproblem_ = (boolean) st.getAsBArray(ParameterNames.IS_MAX)[st.getAsInt(ParameterNames.TASK_NUMBER)];
//				System.out.println(isMAXproblem_);
			} else {
				isMAXproblem_ = (boolean) st.getAsBool(ParameterNames.IS_MAX);
			}
			
		} else {
			isMAXproblem_ = (boolean) st.getAsBool(ParameterNames.IS_MAX);
		}
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
