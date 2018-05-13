package momfo.metaheuristics.moead;


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


class MOEADTest {

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


		setting
		.put(ParameterNames.GA, "MOEAD")
		.put(ParameterNames.CROSSOVER, "momfo.operators.crossover.SBXCrossover")
		.put(ParameterNames.CROSSOVERProbability, "0.9")
		.put(ParameterNames.SBXDisIndex, "20")
		.put(ParameterNames.MUTATION, "momfo.operators.mutation.PolynomialMutation")
		.put(ParameterNames.MUTATIONProbability, "-1")
		.put(ParameterNames.PMDisIndex, "20")
		.put(ParameterNames.SCALAR_FUNCTION,"momfo.util.ScalarzingFunction.TchebycheffForMin")
		.put(ParameterNames.MOEAD_COMPARATOR,"momfo.util.Comparator.MOEADComparator.NomalMOEADComapator")
		.put(ParameterNames.INNER_DIVISION_SIZE, 0 )
		.put(ParameterNames.OUTER_DIVISION_SIZE,( ( problemNumber== 8 ||problemNumber== 7) && (taskNumber == 0) )  ? 13:99)
		.put(ParameterNames.MOEAD_ALPHA,1.0)
		.put(ParameterNames.SIZE_OF_NEIBORHOOD_At_UPDATE,20)
		.put(ParameterNames.IS_NORM,false)
		.put(ParameterNames.SIZE_OF_NEIBORHOOD_At_MATING,20)
		.put(ParameterNames.SEEDER, "lib.experiments.SequenceSeeder")
		.put(ParameterNames.INITIALIZATION, "momfo.operators.initializer.testInitializer")
		.put(ParameterNames.ParentsSelection, "momfo.operators.selection.ParentsSelection.BinaryTournament")
		.put(ParameterNames.EVALUATION, "momfo.operators.evaluation.NTUProblemEvaluation")
		.put(ParameterNames.POPULATION_SIZE, "100")
		.put(ParameterNames.PROBLEM_SET, (ProblemName[problemNumber]))
		.put(ParameterNames.SEEDER_SEED, "1")
		.put(ParameterNames.NTRIALS, 1)
		.put(ParameterNames.N_OF_EVALUATIONS, 100000)
		.put(ParameterNames.N_OF_PARENTS, 2)
		.put(ParameterNames.IS_MAX, false)
		.put(ParameterNames.TASK_NUMBER, taskNumber)
		.put("times",0);
		System.out.println(ProblemName[problemNumber]+ ": Task" + (taskNumber+1));
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

	private final double[] IGDValues_Task1 = {6.555719926443199065e-04, 8.455804890991372447e-02,5.040729666030300260e-01,
			9.643073518346039538e-04,2.057718859321112578e-03, 1.748346790366789229e-04,
			5.429671353946223356e+00, 6.721122910707991727e-01, 8.429169374070397732e-04 };

	private final double[] IGDValues_Task2 = { 8.177472011362102097e-03, 1.531828337174828691e-01, 1.725847814598050281e-04,
			4.417153043393089867e-02, 8.499183875356997930e+01, 6.336990228038563711e-01,
			1.397922567852673964e-03,5.914914231196163191e-03,7.476777884588600709e-02};

}
