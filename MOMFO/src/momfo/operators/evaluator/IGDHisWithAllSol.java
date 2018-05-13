package momfo.operators.evaluator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.NameNotFoundException;
import javax.naming.NamingException;

import lib.experiments.CommandSetting;
import lib.experiments.ParameterNames;
import lib.experiments.Exception.CommandSetting.notFoundException;
import momfo.Indicator.IGDCalclator;
import momfo.Indicator.IGDRef;
import momfo.Indicator.IGDWithAllSol;
import momfo.core.Population;
import momfo.core.ProblemSet;
import momfo.core.Solution;
import momfo.problems.MOMFOP.NTU.CIHS;
import momfo.util.JMException;

/*
 * This class assumes that the class of evaluation in base class object is List<Double>
 *
 */

public class IGDHisWithAllSol extends Evaluator {

	IGDRef IGDReference;

	IGDCalclator IGDCalclator;

	public IGDHisWithAllSol() {

	}

	public IGDRef getIGDRef(){
		return IGDReference;
	}

	@Override
	public void build(CommandSetting s) throws NameNotFoundException, JMException, NamingException,
			ReflectiveOperationException, IOException, notFoundException {
		evaluation = new ArrayList<Double>();
		
		int taskNumber = s.get(ParameterNames.TASK_NUMBER);
		String filename = ((ProblemSet)s.get(ParameterNames.PROBLEM_SET)).get(taskNumber).getIGDRefFile();
		IGDReference = new IGDRef(filename);
		IGDCalclator = new IGDWithAllSol();
		IGDCalclator.build(s);
	}

	@Override
	public void evaluate(Object d) {
		flag = true;
		Population pop = (Population)d;
		double[][] obj = pop.getAllObjectives();
//		System.out.println(IGDCalclator.calcIGD(obj,IGDReference));
		((List<Double>)evaluation).add(IGDCalclator.calcIGD(obj,IGDReference));
	}

	public static void main(String[] args) throws IOException, NameNotFoundException, notFoundException, JMException, NamingException, ReflectiveOperationException {
		IGDHisWithAllSol igd = new IGDHisWithAllSol();
		CommandSetting setting = new CommandSetting();
		ProblemSet p = new CIHS(setting);


		setting
		.put(ParameterNames.IGD_REF_FILES,"Data/PF/circle.pf")
		.put(ParameterNames.IGD_CALCLATOR,"momfo.Indicator.IGD.IGDWithAllSol");

		igd.build(setting);

		Population d = new Population(10);
		Solution sol = new Solution(2);

		sol.setObjective(0, 0.0);
		sol.setObjective(1, 1.0);
		d.add(sol.clone());
		sol.setObjective(0, 1.0);
		sol.setObjective(1, 0.0);
		d.add(sol.clone());

		igd.evaluate(d);
		double dsafj= igd.getValue();
//		System.out.println(igd.getValue());
//		System.out.println(StringUtility.toCSV(igd.getIGDRef().getNormalizeRefs()));
	}
}
