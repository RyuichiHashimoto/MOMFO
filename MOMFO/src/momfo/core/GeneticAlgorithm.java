package momfo.core;



import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.naming.NamingException;

import lib.experiments.CommandSetting;
import lib.experiments.ParameterNames;
import lib.math.BuiltInRandom;
import momfo.operators.crossover.Crossover;
import momfo.operators.mutation.Mutation;
import momfo.operators.selection.ParentsSelection.ParentsSelection;
import momfo.util.JMException;


public abstract  class GeneticAlgorithm implements Serializable {
	protected Problem problem_;

	protected ProblemSet problemSet_;

	protected BuiltInRandom random;

	protected Crossover crossover;	
	protected Mutation mutation;
	protected ParentsSelection parentsSelection;
	protected CommandSetting setting;
	
	
	
	public ProblemSet getProblemSet(){
		return problemSet_;
	}

	public void setRandom(BuiltInRandom d){
		random = d;
	}

	public BuiltInRandom getRandom(){
		return random;
	}

	protected int tasknumber;
	public void setTaskNumber(int d){
		tasknumber = d;
	}

	//名前を入力してそれに該当したoperator を返す
	protected Map<String, Operator> operators_ = null;

	protected Map<String, Object> inputParameters_ = null;

	private Map<String, Object> outPutParameters_ = null;

	public GeneticAlgorithm(Problem problem) {
		problem_ = problem;
		problemSet_ = null;
	}

	public GeneticAlgorithm(ProblemSet d) {
		tasknumber = -1;
		problem_ = null;
		problemSet_ = d;
	}
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
		ret.setRandomGenerator(random);


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

	final public void build(CommandSetting s) throws ReflectiveOperationException, NamingException, IOException, JMException {
		setting = s;
		random = (BuiltInRandom)s.get(ParameterNames.RANDOM_GENERATOR);
		
//		initializer = Generics.cast(s.getAsInstanceByName(INITIALIZATION, genotypePack));
//		s.put(INITIALIZATION, initializer);
//		evaluation = Generics.cast(s.getAsInstanceByName(ParameterNames.RANDOM_GENERATOR, genotypePack));
//		s.put(EVALUATION, evaluation);
//		crossover = Generics.cast(s.getAsInstanceByName(ParameterNames.CROSSOVER, genotypePack));
		s.put(ParameterNames.CROSSOVER, crossover);
//		mutation = Generics.cast(s.getAsInstanceByName(ParameterNames.MUTATION, genotypePack));
		s.put(ParameterNames.MUTATION, mutation);

		buildImpl(s);

//		initializer.build(s);
		crossover.build(s);
		mutation.build(s);
//		evaluation.build(s);
		//s.remove(GENOTYPE_PACKAGE);
	}
	
	abstract protected void buildImpl(CommandSetting s) throws ReflectiveOperationException, NamingException, IOException;


}
