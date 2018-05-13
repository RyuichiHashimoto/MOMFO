package momfo.operators.evaluator;

import java.io.IOException;

import javax.naming.NameNotFoundException;
import javax.naming.NamingException;

import lib.experiments.CommandSetting;
import lib.experiments.Exception.CommandSetting.notFoundException;
import momfo.core.Operator;
import momfo.util.JMException;

public abstract class Evaluator extends Operator{

	protected Object evaluation;

	protected Object evaluatee;

	protected boolean flag;
	
	public boolean getFlag() {
		return flag;
	}
	
	public Evaluator() {
		
	}

	String filePath;

	@Override
	public void build(CommandSetting s) throws NameNotFoundException, JMException, NamingException,
			ReflectiveOperationException, IOException, notFoundException {
//		evaluatee = s.get(ParameterNames.EVALUATEE);
	}

	public <T>T getValue(){
		return (T) evaluation;
	}

	public <T>T getEvaluatee(){
		return (T) evaluatee;
	}

	public void evaluate(){
		flag = true;
		evaluate(evaluatee);
	}

	public abstract void evaluate(Object d);
}
