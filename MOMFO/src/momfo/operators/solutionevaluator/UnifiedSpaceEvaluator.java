package momfo.operators.solutionevaluator;

import javax.naming.NameNotFoundException;

import lib.experiments.CommandSetting;
import lib.experiments.NeedParameters;
import lib.experiments.ParameterNames;
import momfo.core.ProblemSet;
import momfo.core.Solution;
import momfo.util.JMException;

public class UnifiedSpaceEvaluator extends SolutionEvaluator{

	private static final long serialVersionUID = 1L;
	
	ProblemSet problemset;
		
	
	
	@Override
	@NeedParameters({ParameterNames.PROBLEM_SET})
	public void build(CommandSetting s) throws NameNotFoundException {
		this.build(s);
		problemset = s.get(ParameterNames.PROBLEM_SET);
	}

	@Override
	public void evaluate(Solution t) throws JMException {
		
		
		
		problemset.get(t.getSkillFactor()).evaluate(t);
	}
}
