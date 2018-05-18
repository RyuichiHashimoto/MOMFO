package momfo.operators.evaluator;

import java.io.IOException;
import java.io.Writer;
import java.util.regex.Pattern;

import javax.naming.NameNotFoundException;
import javax.naming.NamingException;

import Network.GridComputing.StreamProvider;
import lib.experiments.CommandSetting;
import lib.experiments.ParameterNames;
import lib.experiments.Exception.CommandSetting.notFoundException;
import momfo.core.Operator;
import momfo.util.JMException;

public abstract class Evaluator extends Operator{

	protected Object[] evaluation;

	protected Object evaluatee[];

	protected boolean flag;
	
	public boolean getFlag() {
		return flag;
	}
	
	public Evaluator() {
		
	}

	
	String outputFilePath = "mumin.dat";
	boolean isMultitask;
	
	@Override
	public void build(CommandSetting s) throws NameNotFoundException, JMException, NamingException,
			ReflectiveOperationException, IOException, notFoundException {
		isMultitask = s.getAsBool(ParameterNames.IS_MULTITASK);
		flag = false;
	}
	
	
	abstract public void initialize();
		
	

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

	abstract public void save(StreamProvider streamProvider) throws IOException; 

	abstract public void save(Writer streamProvider) throws IOException; 

	
	public void setOutputFileName(String d) {
		outputFilePath = d;
	}
	
	public String getClassName(){
		String name = this.getClass().getName();
		String[] d = name.split(Pattern.quote("."));
		return d[d.length-1];
	}
	
	public abstract void evaluate(Object d);
}
