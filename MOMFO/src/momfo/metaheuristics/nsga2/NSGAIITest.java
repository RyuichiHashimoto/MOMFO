package momfo.metaheuristics.nsga2;

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

class NSGAIITest {

	@Test
	void test() throws JMException, notFoundException, IllegalArgumentException, CannotConvertException, NamingException, IOException, ReflectiveOperationException {
		for (int i = 0; i < 9; i++) {
			eachTest(i);
		}
	}

	private final String[] ProblemName = { "CIHS", "CIMS", "CILS", "PIHS", "PIMS", "PILS", "NIHS", "NIMS", "NILS" };

	public void eachTestTask(int problemNumber, int taskNumber)
			throws JMException, notFoundException, IllegalArgumentException, CannotConvertException, NamingException, IOException, ReflectiveOperationException {
		CommandSetting setting = new CommandSetting();
//		System.out.println(IGDRef.CountTask());

		setting
		.put(ParameterNames.GA, "NSGA2")
		.put(ParameterNames.CROSSOVER, "momfo.operators.crossover.SBXCrossover")
		.put(ParameterNames.CROSSOVERProbability, "0.9")
		.put(ParameterNames.SBXDisIndex, "20")
		.put(ParameterNames.MUTATION, "momfo.operators.mutation.PolynomialMutation")
		.put(ParameterNames.MUTATIONProbability, "-1")
		.put(ParameterNames.PMDisIndex, "20")
		.put(ParameterNames.SEEDER, "lib.experiments.SequenceSeeder")
		.put(ParameterNames.INITIALIZATION, "momfo.operators.initializer.testInitializer")
		.put(ParameterNames.ParentsSelection, "momfo.operators.selection.ParentsSelection.BinaryTournament")
		.put(ParameterNames.EVALUATION, "momfo.operators.evaluation.NTUProblemEvaluation")
		.put(ParameterNames.POPULATION_SIZE, 100)
		.put(ParameterNames.PROBLEM_SET, (ProblemName[problemNumber]))
		.put(ParameterNames.SEEDER_SEED, "14")
		.put(ParameterNames.NTRIALS, 1)
		.put(ParameterNames.NSGAIIComparator, "momfo.util.Comparator.NSGAIIComparator.NSGAIIComparatorWithRandom")
		.put(ParameterNames.N_OF_EVALUATIONS, 100000)
		.put(ParameterNames.IS_MAX, false)
		.put(ParameterNames.EVO_EVALUATOR, "momfo.operators.evaluator.IGDHisWithAllSol,momfo.operators.evaluator.IGDHisWithAllSol")
		.put(ParameterNames.IGD_CALCLATOR, "momfo.Indicator.IGDHisWithAllSol")
		.put(ParameterNames.FIN_EVALUATOR, "momfo.operators.evaluator.NullEvaluator,momfo.operators.evaluator.NullEvaluator")
		.put(ParameterNames.TASK_NUMBER, taskNumber)
		.put("times",0);



		System.out.println("");
		GAFramework solver = new GAFramework();
		solver.build(setting);
		solver.runOnce();

//		double[] obj = (double[]) algorithm.getAlgorithm().getOutputParameter("FinalFUN");
//		double[] val = (double[]) algorithm.getAlgorithm().getOutputParameter("FinalVAR");
		double IGD = (double) solver.getGA().getOutputParameter("IGDCalclator");
		if (taskNumber == 0) {
			if (!(IGD == IGDValues_Task1[problemNumber]))
				fail("IGDCalclator Value of Task 1 is wrong " + "corrct anser is " + IGDValues_Task1[problemNumber] + " but my answer is" + IGD);
			else
				System.out.println("success");
		} else if(taskNumber == 1){
			if (!(IGD == IGDValues_Task2[problemNumber]))
				fail("IGDCalclator Value of Task 2 is wrong " + "corrct anser is " + IGDValues_Task2[problemNumber] + " but my answer is" + IGD);
		}
	}

	public void eachTest(int problemNumber) throws JMException, notFoundException, IllegalArgumentException, CannotConvertException, NamingException, IOException, ReflectiveOperationException {
		eachTestTask(problemNumber,0);
		eachTestTask(problemNumber,1);
	}

	public void Separate() throws JMException, ClassNotFoundException, NameNotFoundException {
	}

	private final double[] IGDValues_Task1 = {2.558676929108101832e-03, 1.864513136000726656e-01,4.724535546102606176e-01,
			1.263428153275060646e-03, 2.169475164826612130e-03, 1.959635566694015543e-04,
			7.005252081975403833e+00, 8.467908776847546948e-01, 7.985469798767856359e-04 };

	private final double[] IGDValues_Task2 = { 4.307229136133869027e-03, 5.635314319566877943e-03, 2.027964291998041028e-04,
			3.556296138079578784e-02, 1.488398454388887338e+01, 6.339765440340111580e-01,
			7.068662203164953039e-04,7.640117324764930662e-02, 6.419555778045834549e-01};

}
