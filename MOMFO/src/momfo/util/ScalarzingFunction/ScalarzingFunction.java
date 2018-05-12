package momfo.util.ScalarzingFunction;

import javax.naming.NameNotFoundException;

import Network.Buildable;
import lib.experiments.CommandSetting;
import lib.lang.NeedOverriden;
import momfo.core.Solution;
import momfo.util.JMException;

public abstract class ScalarzingFunction implements Buildable{

	
	@NeedOverriden
	public void build(CommandSetting st) throws NameNotFoundException{		
	}
	
	public double execute(Solution ind,double[] weight,double[] referencePoint) throws JMException{
		return execute(ind.getObjectives(),weight,referencePoint);
	}

	abstract public double execute(double[] d, double[] s, double[] p) throws JMException;

	
	public String getFunctionName() {
		return this.getClass().getName();
	};
}
