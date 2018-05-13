package momfo.core;



import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.naming.NameNotFoundException;
import javax.naming.NamingException;

import lib.experiments.CommandSetting;
import lib.experiments.ParameterNames;
import lib.experiments.Exception.CommandSetting.notFoundException;
import lib.lang.Generics;
import lib.lang.NeedOverriden;
import lib.math.BuildInRandom;
import momfo.operators.crossover.Crossover;
import momfo.operators.evaluator.Evaluator;
import momfo.operators.initializer.Initializer;
import momfo.operators.mutation.Mutation;
import momfo.operators.selection.ParentsSelection.ParentsSelection;
import momfo.util.JMException;


public abstract  class GeneticAlgorithm implements Serializable {

	public static String name = "";

	protected Population population_;

	protected Population[] populationArray;

	protected boolean isMultitask;

	protected Problem problem_;

	protected ProblemSet problemSet_;

	protected BuildInRandom random;

	protected Initializer initialization;
	protected Crossover crossover;
	protected Mutation mutation;

	protected Evaluator[] evoEvaluator;
	protected Evaluator[] finEvaluator;

	protected ParentsSelection parentsSelection;
	protected CommandSetting setting;

	public GeneticAlgorithm() {

	}


	public ProblemSet getProblemSet(){
		return problemSet_;
	}

	public void setRandom(BuildInRandom d){
		random = d;
	}

	public BuildInRandom getRandom(){
		return random;
	}

	protected int tasknumber;

	public boolean isMultitask() {
		return isMultitask;
	}

	public void setTaskNumber(int d){
		tasknumber = d;
	}

	//名前を入力してそれに該当したoperator を返す
	protected Map<String, Operator> operators_ = null;

	protected Map<String, Object> inputParameters_ = null;

	private Map<String, Object> outPutParameters_ = null;

	protected boolean isMAX_;


	public Map<String,Object> getAllmap(){
		return inputParameters_;
	}

	public abstract Population execute() throws momfo.util.JMException, ClassNotFoundException;

	public void addOperator(String name, Operator operator) {
		if (operators_ == null) {
			operators_ = new HashMap<String, Operator>();
		}
		operators_.put(name, operator);
	}

	public Operator getOperator(String name) {
		Operator ret = operators_.get(name);
		return ret;
	}

	public void setInputParameter(String name, Object object) {
		if (inputParameters_ == null) {
			inputParameters_ = new HashMap<String, Object>();
		}
		inputParameters_.put(name, object);
	}

	public Object getInputParameter(String name) {
		return inputParameters_.get(name);
	}

	public void setOutputParameter(String name, Object object) {
		if (outPutParameters_ == null) {
			outPutParameters_ = new HashMap<String, Object>();
		}
		outPutParameters_.put(name, object);
	}

	public Object getOutputParameter(String name) {
		if (outPutParameters_ != null)
			return outPutParameters_.get(name);
		else
			return null;
	}

	public Problem getProblem() {
		return problem_;
	}

	final public void build(CommandSetting s) throws ReflectiveOperationException, NamingException, IOException, JMException, notFoundException {
		setting = s;
		random = (BuildInRandom)s.get(ParameterNames.RANDOM_GENERATOR);

		String genotypePack = "momfo.operators";

		initialization = Generics.cast(s.getAsInstanceByName(ParameterNames.INITIALIZATION, genotypePack));
		s.put(ParameterNames.INITIALIZATION, initialization);
		crossover = Generics.cast(s.getAsInstanceByName(ParameterNames.CROSSOVER, genotypePack));
		s.put(ParameterNames.CROSSOVER, crossover);
		mutation = Generics.cast(s.getAsInstanceByName(ParameterNames.MUTATION, genotypePack));
		s.put(ParameterNames.MUTATION, mutation);
		parentsSelection = Generics.cast(s.getAsInstanceByName(ParameterNames.ParentsSelection, genotypePack));;
		s.put(ParameterNames.ParentsSelection, parentsSelection);
		finEvaluator = Generics.cast(s.getAsInstanceByName(ParameterNames.FIN_EVALUATOR, genotypePack));;
		s.put(ParameterNames.FIN_EVALUATOR, finEvaluator);
		evoEvaluator = Generics.cast(s.getAsInstanceByName(ParameterNames.EVO_EVALUATOR, genotypePack));;
		s.put(ParameterNames.EVO_EVALUATOR, evoEvaluator );

		buildImpl(s);
		initialization.build(s);
		crossover.build(s);
		mutation.build(s);
		parentsSelection.build(s);


		for(int t = 0;t < finEvaluator.length;t++){
			s.putForce(ParameterNames.TEMP_TASK_NUMBER, t);
			finEvaluator[t].build(s);
		}

		for(int t = 0;t < evoEvaluator.length;t++) {
			s.putForce(ParameterNames.TEMP_TASK_NUMBER, t);
			evoEvaluator[t].build(s);
		}
		s.putForce(ParameterNames.TEMP_TASK_NUMBER, -10000);
	}

	@NeedOverriden
	public void initialize(int seed) throws ClassNotFoundException, JMException {
		random.setSeed(seed);
	}

	abstract public void recombination() throws JMException;

	abstract public void nextGeneration() throws JMException, NameNotFoundException;

	public void finEvaluation() {
		for(int t = 0;t < finEvaluator.length;t++) {
			finEvaluator[t].evaluate();;
		}
	};

	public void evoEvaluation(){
		for(int t = 0;t < evoEvaluator.length;t++) {
			evoEvaluator[t].evaluate();;
		}
	};

	abstract public boolean terminate();

	abstract public int getEvaluations();

	abstract public int getGeneration();


	abstract public Population getPopulation();


	abstract protected void buildImpl(CommandSetting s) throws ReflectiveOperationException, NamingException, IOException, notFoundException, JMException;


}
