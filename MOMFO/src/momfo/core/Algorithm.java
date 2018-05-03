package momfo.core;


import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import momfo.util.Random;


public abstract  class Algorithm implements Serializable {
	protected Problem problem_;

	protected ProblemSet problemSet_;

	Random random;

	public ProblemSet getProblemSet(){
		return problemSet_;
	}

	public void setRandom(Random d){
		random = d;
	}

	protected int tasknumber;
	public void setTaskNumber(int d){
		tasknumber = d;
	}

	//名前を入力してそれに該当したoperator を返す
	protected Map<String, Operator> operators_ = null;

	protected Map<String, Object> inputParameters_ = null;

	private Map<String, Object> outPutParameters_ = null;

	public Algorithm(Problem problem) {
		problem_ = problem;
		problemSet_ = null;
	}

	public Algorithm(ProblemSet d) {
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
		return operators_.get(name);
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


}
