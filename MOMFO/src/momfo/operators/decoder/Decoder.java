package momfo.operators.decoder;

import java.io.IOException;

import javax.naming.NameNotFoundException;
import javax.naming.NamingException;

import lib.experiments.CommandSetting;
import lib.experiments.ParameterNames;
import lib.experiments.Exception.CommandSetting.notFoundException;
import lib.io.FileConstants;
import momfo.core.Operator;
import momfo.core.Problem;
import momfo.core.ProblemSet;
import momfo.core.Solution;
import momfo.util.JMException;

/*
 * This class is mainly for using unified search space
 * 
 * 
 */

abstract public class Decoder extends Operator {

	ProblemSet problemSet;
	Problem problem;

	boolean isMultitask;

	int taskNumber;

	@Override
	public void build(CommandSetting s) throws NameNotFoundException, JMException, NamingException,
			ReflectiveOperationException, IOException, notFoundException {

		isMultitask = s.getAsBool(ParameterNames.IS_MULTITASK);

		problemSet = s.get(ParameterNames.PROBLEM_SET);

		if (isMultitask) {
			taskNumber = s.getAsInt(ParameterNames.TEMP_TASK_NUMBER);
			problem = problemSet.get(taskNumber);
		} else {
			if (s.containsKey(ParameterNames.PROBLEM_SET)) {
				taskNumber = s.getAsInt(ParameterNames.TEMP_TASK_NUMBER);
				problem =  problemSet.get(taskNumber);
			} else if (s.containsKey(ParameterNames.PROBLEM)) {
				problem = (s.get(ParameterNames.PROBLEM));
			} else {
				throw new notFoundException(this.getClass().getName() + ": sorry we cannot found "
						+ ParameterNames.PROBLEM + "/" + ParameterNames.PROBLEM_SET + " key. please config the setting"
						+ FileConstants.NEWLINE_DEMILITER + FileConstants.NEWLINE_DEMILITER + s.toString());
			}
		}
	}
	public abstract double[] decoder(double[] val);
		

	public double[] decoder(Solution sol) {
		return decoder(sol.getValue());
	}
		
	

}
