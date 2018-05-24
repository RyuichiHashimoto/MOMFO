package momfo.util.ScalarzingFunction;

import java.io.Serializable;

import javax.naming.NameNotFoundException;

import Network.Buildable;
import lib.experiments.CommandSetting;
import lib.experiments.JMException;
import lib.lang.NeedOverriden;
import momfo.core.Solution;

public abstract class ScalarzingFunction implements Serializable,Buildable{

	
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
