package momfo.operators.initializer;

import java.io.IOException;

import javax.naming.NamingException;

import lib.experiments.CommandSetting;
import lib.experiments.NeedParameters;
import lib.experiments.ParameterNames;
import lib.lang.NeedOverriden;
import lib.lang.NotVerifiedYet;
import lib.math.BuildInRandom;
import momfo.core.Operator;
import momfo.core.Problem;
import momfo.core.ProblemSet;
import momfo.core.Solution;

abstract public class Initializer extends Operator {
	protected BuildInRandom mt;

	ProblemSet problemSet;

	Problem problem;

	boolean isMultitask;

	int taskNumber =0;
	public void setSeed(int seed) {
		mt.setSeed(seed);
	}


	@Override
	@NeedOverriden
	@NeedParameters({ParameterNames.RANDOM_GENERATOR,ParameterNames.IS_MULTITASK})
	public void build(CommandSetting s) throws NamingException, ReflectiveOperationException, IOException{
		mt = (BuildInRandom) s.get(ParameterNames.RANDOM_GENERATOR);
		isMultitask = s.getAsBool(ParameterNames.IS_MULTITASK);
		if(isMultitask){
			problemSet = s.get(ParameterNames.PROBLEM_SET);
			taskNumber = s.getAsInt(ParameterNames.TASK_NUMBER);
			problem = problemSet.get(taskNumber);
		} else {
			if(s.containsKey(ParameterNames.PROBLEM_SET)) {
				taskNumber = s.getAsInt(ParameterNames.TASK_NUMBER);
				problem = ((ProblemSet) s.get(ParameterNames.PROBLEM_SET)).get(taskNumber);
			} else if (s.containsKey(ParameterNames.PROBLEM)) {
//				System.out.println("clear");
				problem = s.get(ParameterNames.PROBLEM);
			} else {
				throw new NamingException("in " + this.getName() +": we cannot found " + ParameterNames.PROBLEM_SET +"/"+ParameterNames.PROBLEM);
			}
		}

	}

	abstract public void initialize(Solution sol) throws NotVerifiedYet;

}
