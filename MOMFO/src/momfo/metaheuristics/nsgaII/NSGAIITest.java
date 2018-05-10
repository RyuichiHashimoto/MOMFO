package momfo.metaheuristics.nsgaII;

import static org.junit.jupiter.api.Assertions.fail;

import javax.naming.NameNotFoundException;

import org.junit.jupiter.api.Test;

import experiments.Setting;
import momfo.core.AlgorithmMain;
import momfo.main.NSGAIIMain;
import momfo.util.JMException;

class NSGAIITest {

	@Test
	void test() throws ClassNotFoundException, NameNotFoundException, JMException {
		for (int i = 3; i < 9; i++) {
			eachTest(i);
		}
	}

	private final String[] ProblemName = { "CIHS", "CIMS", "CILS", "PIHS", "PIMS", "PILS", "NIHS", "NIMS", "NILS" };

	public void eachTestTask(int problemNumber, int taskNumber)
			throws ClassNotFoundException, NameNotFoundException, JMException {
		Setting hashmap = new Setting();
		AlgorithmMain algorithm;

		String geneticAlgorithmName = "NSGAII";
		hashmap
		.add("CrossoverName", "SBXCrossover")
		.add("CrossoverProbability", "0.9")
		.add("CrossoverDistribution", "20")
		.add("MutationName", "PolynomialMutation")
		.add("MutationProbability", "-1")
		.add("MutationDistribution", "20")
		.add("populationSize", "100")
		.add("Problem", ProblemName[problemNumber])
		.add("Seed", "14")
		.add("NumberOfTrial", 1)
		.add("maxEvaluations", "100000")
		.add("isMax", false)
		.add("TaskNumber", taskNumber);

		algorithm = new NSGAIIMain(hashmap);
		algorithm.run(0);

//		double[] obj = (double[]) algorithm.getAlgorithm().getOutputParameter("FinalFUN");
//		double[] val = (double[]) algorithm.getAlgorithm().getOutputParameter("FinalVAR");
		double IGD = (double) algorithm.getAlgorithm().getOutputParameter("IGD");

		if (taskNumber == 0) {
			if (!(IGD == IGDValues_Task1[problemNumber]))
				fail("IGD Value of Task 1 is wrong");
		} else if(taskNumber == 1){
			if (!(IGD == IGDValues_Task2[problemNumber]))
				fail("IGD Value of Task 2 is wrong");			
		}
	}

	public void eachTest(int problemNumber) throws ClassNotFoundException, NameNotFoundException, JMException {
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
