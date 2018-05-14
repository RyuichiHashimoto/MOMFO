package momfo.core;

import java.util.Map;

import momfo.util.JMException;

public abstract class Problem {


//	public abstract void evaluate(Solution a) throws JMException;

	public abstract double[] evaluate(double[] val) throws JMException;

	public double[] evaluate(Solution sol) throws JMException {
		return evaluate(sol.getValue());
	};

	public double[] decode(Solution sol){
		return decode(sol.getValue());
	};
	
	public abstract double[] decode(double[] val);

	
	public abstract void repair(Solution d,Map<String, Object> a) throws JMException;


	protected String IGDRef;

	public String getIGDRefFile() {
		return IGDRef;
	}

	public void setIGDRefFile(String name){
		IGDRef = name;
	}
	
	public double[] getUpperLimit() {
		return upperLimit_;
	}
	
	public double[] getLowerLimit() {
		return upperLimit_;
	}
	
	protected int numberOfConstraint_;
	protected int numberOfVariables_;
	protected int numberOfObjectives_;
	protected String problemName_;

	protected int	variableType_;

	protected double[] upperLimit_;
	protected double[] lowerLimit_;

	public String getName(){
		return this.getClass().getName();
	}

	public int getNumberOfConstrain(){
		return numberOfConstraint_;
	}

	public int getNumberOfObjectives(){
		return numberOfObjectives_;
	}

	public int getNumberOfVariables(){
		return numberOfVariables_;
	}

	public double getUpperLimit(int key){
		return upperLimit_[key];
	}
	public double getLowerLimit(int key){
		return lowerLimit_[key];
	}

	public	int getSolutionType_(){
		return variableType_;
	};

}