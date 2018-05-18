package momfo.operators.solutionevaluator;

import java.io.IOException;

import javax.naming.NamingException;

import lib.experiments.CommandSetting;
import lib.experiments.NeedParameters;
import lib.experiments.ParameterNames;
import lib.experiments.Exception.CommandSetting.notFoundException;
import momfo.core.ProblemSet;
import momfo.core.Solution;
import momfo.operators.decoder.Decoder;
import momfo.util.JMException;

public class UnifiedSpaceEvaluator extends SolutionEvaluator{

	private static final long serialVersionUID = 1L;

	ProblemSet problemset;

	Decoder decoder;
	private static final String Def_PACK = "momfo.operators.decoder.";
	
	
	@Override
	@NeedParameters({ParameterNames.PROBLEM_SET})
	public void build(CommandSetting s) throws notFoundException, ReflectiveOperationException, JMException, NamingException, IOException {
		super.build(s);

		if(isMultitask) {
			problemset = s.get(ParameterNames.PROBLEM_SET);
			decoder = (Decoder) s.getAsInstanceArrayByName(ParameterNames.SOL_DECODER, Def_PACK,ParameterNames.SETTING_FILE_DEMILITER)[taskNumber];
			decoder.build(s);
		} else {
			if(s.containsKey(ParameterNames.PROBLEM_SET)) {
				problemset = s.get(ParameterNames.PROBLEM_SET);
				decoder = (Decoder) s.getAsInstanceByName(ParameterNames.SOL_DECODER,Def_PACK);
				decoder.build(s);
			}
		}
	}

	@Override
	public void evaluate(Solution sol) throws JMException {
		double[] value = decoder.decoder(sol);
		double[] obj = problem.evaluate(value);
//		System.out.println(taskNumber);
		
		if(obj.length != sol.getNumberOfObjectives()) {
//			System.out.println(StringUtility.toString(obj));
			sol.rescaleObjectives(obj.length);
		}
		for(int i = 0;i <obj.length;i++) {
			sol.setObjective(i, obj[i]);
		}
	}
}
