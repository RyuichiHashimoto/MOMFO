package momfo.core;

import java.util.Map;

import momfo.util.JMException;

public abstract class Problem {


	public abstract void evaluate(Solution a) throws JMException;


	public abstract void repair(Solution d,Map<String, Object> a) throws JMException;

	public abstract double[] decode(Solution d) ;

	protected String IGDRef;

	public String getIGDRefFile() {
		return IGDRef;
	}

	public void setIGDRefFile(String name){
		IGDRef = name;
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

	public double getUpperlimit_(int key){
		return upperLimit_[key];
	}
	public double getLowerlimit_(int key){
		return lowerLimit_[key];
	}

	public	int getSolutionType_(){
		return variableType_;
	};





}