package momfo.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lib.experiments.JMException;

public class ProblemSet  implements Serializable{
	protected final List<Problem> problemsList_;

	protected List<Integer> accObjsList_;

	private int capacity_ = Integer.MAX_VALUE;

	private int maxDim_;

	private double unifiedLowerLimit_;

	private double unifiedUpperLimit_;

	String ProblemName;

	public int[] getNumberOfObjectives() {
		int[] ret = new int[problemsList_.size()];
		for(int i = 0; i < ret.length;i++) ret[i] = problemsList_.get(i).getNumberOfObjectives();
		return ret;
	}
	
	
	public void setProblemSetName(String d){
		ProblemName = d;
	}

	public ProblemSet() {
		problemsList_ = new ArrayList<Problem>();
		accObjsList_ = new ArrayList<Integer>();
		maxDim_ = 0;

		unifiedLowerLimit_ = 0;
		unifiedUpperLimit_ = 1;
	} // SolutionSet

	public ProblemSet(int maximumSize) {
		problemsList_ = new ArrayList<Problem>();
		accObjsList_ = new ArrayList<Integer>();

		capacity_ = maximumSize;
		maxDim_ = 0;

		unifiedLowerLimit_ = 0;
		unifiedUpperLimit_ = 1;
	} // SolutionSet


	public ProblemSet add(Problem problem) {
		problemsList_.add(problem);
		accObjsList_.add(problem.getNumberOfObjectives());
		if(problem.getNumberOfVariables() > maxDim_){
			maxDim_ = problem.getNumberOfVariables();
		}
		
		return this;
	} // add

	
	public Problem get(int i) {
		return problemsList_.get(i);
	} // get

	public int countProblem() {
		return problemsList_.size();
	} // size

	public int getMaxDimension() {
		return maxDim_;
	}


	public double getUnifiedLowerLimit() {
		return unifiedLowerLimit_;
	}

	public double getUnifiedUpperLimit() {
		return unifiedUpperLimit_;
	}
	


	public int getNumberOfObjs(int id) {
		if (id == 0)
			return accObjsList_.get(id);
		else
			return accObjsList_.get(id) - accObjsList_.get(id - 1);
	}

	public String getProblemName(){
		return ProblemName;
	}

	public void evaluate(Solution newSolution) throws JMException {
		int f = newSolution.getSkillFactor();

		for(int t = 0; t < countProblem();t++){
			if(t == f){
			 	problemsList_.get(t).evaluate(newSolution);
			}
		}

	}
	private int  maxDimOfObjective;

	public void setMaxDimensionOfObjective(int i) {
		maxDimOfObjective = i;
	}
	public int getMaxDimensionOfObjective() {
		return maxDimOfObjective;
	}
	int numberOfMAXCcnstrain = 0;;


	public void setMaxNumberOfConstrain(int d) {
		numberOfMAXCcnstrain = d;
	}

	public int getMaxNumberOfConstrain() {
		return numberOfMAXCcnstrain;
	}


}
