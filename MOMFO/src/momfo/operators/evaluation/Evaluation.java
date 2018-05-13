package momfo.operators.evaluation;

import javax.naming.NamingException;

import lib.experiments.CommandSetting;
import momfo.core.Operator;
import momfo.core.Population;
import momfo.core.Solution;
import momfo.util.JMException;

abstract public class Evaluation extends Operator {

	Object evaluatee;
	

	public final String getLastName() {
		String [] temp = this.getClass().getName().split(".");
		return temp[temp.length-1];
	}
	
	@Override
	abstract public void build(CommandSetting s) throws NamingException, ReflectiveOperationException;

	abstract public void evaluate(Solution t) throws JMException;

	abstract public void evaluate() throws JMException;
	
	abstract public void evaluate(Population t) throws JMException;
}
