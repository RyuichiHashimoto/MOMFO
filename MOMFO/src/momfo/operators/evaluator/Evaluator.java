package momfo.operators.evaluator;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.naming.NameNotFoundException;
import javax.naming.NamingException;

import Network.GridComputing.StreamProvider;
import lib.experiments.CommandSetting;
import lib.experiments.JMException;
import lib.experiments.ParameterNames;
import lib.io.FileConstants;
import lib.lang.Generics;
import momfo.core.Operator;

public abstract class Evaluator extends Operator{

	protected Object[] evaluation;

	protected List<Object[]> Archive;

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
			ReflectiveOperationException, IOException{
		isMultitask = s.getAsBool(ParameterNames.IS_MULTITASK);
		flag = false;
		Archive = new ArrayList<Object[]>();
	}


	abstract public void initialize();

	public void addArchive(){
		if(Archive == null) {
			Archive = new ArrayList<Object[]>();
		}
		Archive.add(Generics.cast(evaluation));
	}

	protected void setOutputFilePath(int t){
		outputFilePath = this.getClassName()+FileConstants.FILEPATH_DEMILITER +this.getClassName()+t+".dat";
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

	abstract public void save(StreamProvider streamProvider,String namesapce,int offset) throws IOException;

	public void save(StreamProvider streamProvider,String namesapce) throws IOException {
		save(streamProvider,namesapce,1);
	}
	public void save(StreamProvider streamProvider) throws IOException {
			save(streamProvider,"");
	}
	abstract public void save(Writer writer) throws IOException;

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
