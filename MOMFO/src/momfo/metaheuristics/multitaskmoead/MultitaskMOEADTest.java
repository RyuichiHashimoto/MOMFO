package momfo.metaheuristics.multitaskmoead;


import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import javax.naming.NameNotFoundException;
import javax.naming.NamingException;

import org.junit.jupiter.api.Test;

import lib.experiments.CommandSetting;
import lib.experiments.ParameterNames;
import lib.experiments.Exception.CommandSetting.CannotConvertException;
import lib.experiments.Exception.CommandSetting.notFoundException;
import momfo.core.GAFramework;
import momfo.util.JMException;


class MultitaskMOEADTest {

	@Test
	public void test() throws JMException, notFoundException, IllegalArgumentException, CannotConvertException, NamingException, IOException, ReflectiveOperationException {
		for (int i = 0; i < 9; i++) {
			eachTest(i);
		}
	}

	private final String[] ProblemName = { "CIHS", "CIMS", "CILS", "PIHS", "PIMS", "PILS", "NIHS", "NIMS", "NILS" };

	public void eachTestTask(int problemNumber, int taskNumber)
			throws JMException, notFoundException, IllegalArgumentException, CannotConvertException, NamingException, IOException, ReflectiveOperationException {
		CommandSetting setting = new CommandSetting();
	//	System.out.println(IGDRef.CountTask());

		setting
		.put(ParameterNames.GA, "MultitaskMOEAD")
		.put(ParameterNames.CROSSOVER, "momfo.operators.crossover.SBXCrossover")
		.put(ParameterNames.CROSSOVERProbability, "0.9")
		.put(ParameterNames.SBXDisIndex, "20")
		.put(ParameterNames.MUTATION, "momfo.operators.mutation.PolynomialMutation")
		.put(ParameterNames.MUTATIONProbability, "-1")
		.put(ParameterNames.PMDisIndex, "20")
		.put(ParameterNames.INNER_DIVISION_SIZE, "0,0" )
		.put(ParameterNames.OUTER_DIVISION_SIZE, (problemNumber== 8 ||problemNumber== 7)  ? "13,99" :"99,99")
		.put(ParameterNames.MOEAD_ALPHA,"1.0,1.0")
		.put(ParameterNames.SCALAR_FUNCTION,"momfo.util.ScalarzingFunction.TchebycheffForMin,momfo.util.ScalarzingFunction.TchebycheffForMin")
		.put(ParameterNames.MOEAD_COMPARATOR,"momfo.util.Comparator.MOEADComparator.NomalMOEADComapator,momfo.util.Comparator.MOEADComparator.NomalMOEADComapator")
		.put(ParameterNames.SIZE_OF_NEIBORHOOD_At_UPDATE,"20,20")
		.put(ParameterNames.IS_NORM,"false,false")
		.put(ParameterNames.SIZE_OF_NEIBORHOOD_At_MATING,"20,20")
		.put(ParameterNames.SEEDER, "lib.experiments.SequenceSeeder")
		.put(ParameterNames.INITIALIZATION, "momfo.operators.initializer.testInitializer")
		.put(ParameterNames.PBI_PARAMETER, "5.0,5.0")
		.put(ParameterNames.ParentsSelection, "momfo.operators.selection.ParentsSelection.RandomSelectionWithoutReplacement")
		.put(ParameterNames.EVALUATION, "momfo.operators.evaluation.NTUProblemEvaluation")
		.put(ParameterNames.POPULATION_SIZE, "100,100")
		.put(ParameterNames.PROBLEM_SET, (ProblemName[problemNumber]))
		.put(ParameterNames.SEEDER_SEED, "1")
		.put(ParameterNames.NTRIALS, 1)
		.put(ParameterNames.N_OF_EVALUATIONS, "100000,100000")
		.put(ParameterNames.RMP, 0.1)
		.put(ParameterNames.N_OF_PARENTS, "2,2")
		.put(ParameterNames.IS_MAX, "false,false")
//		.put(ParameterNames.TASK_NUMBER, taskNumber)
		.put("times",0);
		System.out.println(ProblemName[problemNumber]+ "");
		GAFramework solver = new GAFramework();


		solver.build(setting);
		solver.runOnce();


		double[] IGD = (double[]) solver.getGA().getOutputParameter("IGDCalclator");
			if (!(IGD[0] == IGDValues_Task1[taskNumber]))
				fail("IGDCalclator Value of Task 1 is wrong " + "corrct anser is " + IGDValues_Task1[taskNumber] + " but my answer is" + IGD[0]);
			else if (!(IGD[1] == IGDValues_Task2[taskNumber]))
				fail("IGDCalclator Value of Task 2 is wrong " + "corrct anser is " + IGDValues_Task2[taskNumber] + " but my answer is" + IGD[1]);
			else
				System.out.println("SUCCCESS");

	}

	public void eachTest(int problemNumber) throws JMException, notFoundException, IllegalArgumentException, CannotConvertException, NamingException, IOException, ReflectiveOperationException {
		eachTestTask(problemNumber,0);
	//	eachTestTask(problemNumber,1);
	}

	public void Separate() throws JMException, ClassNotFoundException, NameNotFoundException {
	}

	private final double[] IGDValues_Task1 = {
			3.5357668013543563E-4,1.7981917043366627E-4	,2.070604162075519433e-04,
			2.837082007232086993e-04,4.024248299196193229e-03,1.754022360052057334e-04,
			1.522138933479712630e+00,8.122739679472584795e-01,7.453151598261105459e-04
	}; 
	
	private final double[] IGDValues_Task2 = {
			0.0024182954167999314,6.921732011169843E-4,1.643551083100139331e-04,
			4.987954895799217442e-02,1.634405017276245076e+01,3.687665361718667732e-03,
			2.394295859861442796e-04,3.151659714796690992e-02,2.618409750496212413e-02
	}; 

}
