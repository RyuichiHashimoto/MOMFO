package momfo.operators.solutionevaluator;

import javax.naming.NameNotFoundException;

import lib.experiments.CommandSetting;
import lib.experiments.ParameterNames;
import lib.experiments.Exception.CommandSetting.notFoundException;
import lib.io.FileConstants;
import momfo.core.Operator;
import momfo.core.Problem;
import momfo.core.ProblemSet;
import momfo.core.Solution;
import momfo.util.JMException;

@SuppressWarnings("serial")
abstract public class SolutionEvaluator extends Operator {

	Problem problem;
	
	boolean isMultitask;
	
	int taskNumber;
	@Override	
	public void build(CommandSetting s) throws NameNotFoundException, notFoundException {
		isMultitask = s.getAsBool(ParameterNames.IS_MULTITASK);
		
		if(isMultitask) {
			taskNumber = s.getAsInt(ParameterNames.TEMP_TASK_NUMBER); 
			problem = ((ProblemSet)s.get(ParameterNames.PROBLEM_SET))  .get(taskNumber);			
		} else {
			if(s.containsKey(ParameterNames.PROBLEM_SET)) {
				taskNumber = s.getAsInt(ParameterNames.TEMP_TASK_NUMBER); 
				problem = ((ProblemSet)s.get(ParameterNames.PROBLEM_SET))  .get(taskNumber);
			} else if (s.containsKey(ParameterNames.PROBLEM)) { 
				problem = (s.get(ParameterNames.PROBLEM));
			} else {
				throw new notFoundException (this.getClass().getName()+": sorry we cannot found "+ ParameterNames.PROBLEM+ "/"+ ParameterNames.PROBLEM_SET+" key. please config the setting"+FileConstants.NEWLINE_DEMILITER+FileConstants.NEWLINE_DEMILITER +s.toString());
			}
		}
		
		
	}
	
	abstract public void evaluate(Solution t) throws JMException;
	
		
}
