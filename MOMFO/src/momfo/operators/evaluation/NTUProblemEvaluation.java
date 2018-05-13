package momfo.operators.evaluation;

import javax.naming.NamingException;

import lib.experiments.CommandSetting;
import lib.experiments.NeedParameters;
import lib.experiments.ParameterNames;
import momfo.core.Population;
import momfo.core.ProblemSet;
import momfo.core.Solution;
import momfo.util.JMException;

public class NTUProblemEvaluation extends Evaluation{
	
	ProblemSet problemset;
		
	@Override
	@NeedParameters({ParameterNames.PROBLEM_SET})
	public void build(CommandSetting s) throws NamingException, ReflectiveOperationException {
		problemset = s.get(ParameterNames.PROBLEM_SET);
	}

	@Override
	public void evaluate(Solution t) throws JMException {		
		problemset.get(t.getSkillFactor()).evaluate(t);
	}

	@Override
	public void evaluate() throws JMException {
		// TODO 自動生成されたメソッド・スタブ
		
	}

	@Override
	public void evaluate(Population t) throws JMException {
		// TODO 自動生成されたメソッド・スタブ
		
	}

}
