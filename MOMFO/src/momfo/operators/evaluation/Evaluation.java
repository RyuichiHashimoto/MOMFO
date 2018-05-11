package momfo.operators.evaluation;

import javax.naming.NamingException;

import lib.experiments.CommandSetting;
import momfo.core.Operator;
import momfo.core.Solution;
import momfo.util.JMException;

abstract public class Evaluation extends Operator {
	
	
	
	
	@Override
	abstract public void build(CommandSetting s) throws NamingException, ReflectiveOperationException;


	abstract public void evaluate(Solution t) throws JMException;
}
