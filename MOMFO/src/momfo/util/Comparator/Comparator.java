package momfo.util.Comparator;

import java.io.Serializable;

import javax.naming.NameNotFoundException;

import Network.Buildable;
import lib.experiments.CommandSetting;
import lib.experiments.ParameterNames;
import lib.experiments.Exception.CommandSetting.notFoundException;
import lib.lang.NeedOverriden;
import lib.math.BuildInRandom;
import momfo.util.JMException;

//isMAX is true if problem is max problem　
public abstract class Comparator implements Serializable, Buildable {

	protected boolean isMAX_;

	protected BuildInRandom random;


	public Comparator(){

	}

	@NeedOverriden
	public void build(CommandSetting st) throws NameNotFoundException, notFoundException {
		random = st.get(ParameterNames.RANDOM_GENERATOR);
		isMAX_ = st.getAsBool(ParameterNames.IS_MAX);


	}

	public double better(double a, double b) {
		if (isMAX_) {
			if (a > b) {
				return a;
			} else {
				return b;
			}
		} else {
			if (a > b) {
				return b;
			} else {
				return a;
			}
		}
	}

	public double worse(double a, double b) {
		if (isMAX_) {
			if (a > b) {
				return b;
			} else {
				return a;
			}
		} else {
			if (a > b) {
				return a;
			} else {
				return b;
			}
		}
	}

	public void setMax(){
		isMAX_ = true;
	}

	public void setMin(){
		isMAX_ = false;
	}

	public void set(boolean d){
		isMAX_ = d;
	}
	public boolean isMax(){
		return isMAX_;
	}


	public boolean compare(double a, double b){
		return (isMAX_ == (a > b));
	}

	abstract public int execute(Object one, Object two) throws JMException;

	public Comparator(boolean is, BuildInRandom random_) {
		isMAX_ = is;
		random = random_;
	}

	public String getName() {
		return this.getClass().getName();
	};
}
