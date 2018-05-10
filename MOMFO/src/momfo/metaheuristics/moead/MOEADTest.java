package momfo.metaheuristics.moead;

import static org.junit.jupiter.api.Assertions.fail;

import javax.naming.NameNotFoundException;

import org.junit.Test;

import experiments.Setting;
import momfo.core.AlgorithmMain;
import momfo.main.MOEADMain;
import momfo.util.JMException;

class MOEADTest {

	@Test
	void test() throws ClassNotFoundException, NameNotFoundException, JMException {
		for (int i = 7; i < 9; i++) {
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
		.add("Seed", "1")
		.add("NumberOfTrial", 1)
		.add("maxEvaluations", "100000")
		.add("IsMax", false)
		.add("TaskNumber", taskNumber);

		
		hashmap
	    .add("ScalarFunction","TchebycheffFormin")
	    .add("matingNeighborhood", "20")
	    .add("ReplaceNeighborhood", "20")
	    .add("Division", ((problemNumber == 7 && taskNumber == 0)||(problemNumber == 8 && taskNumber == 0))? "13":"99")
	    .add("InnerWeightDivision","0")
	    .add("alpha","1.0")
	    .add("PBITheta","5.0")
		.add("IsNorm","false");

				
		algorithm = new MOEADMain(hashmap);
		algorithm.run(0);

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

	private final double[] IGDValues_Task1 = {6.555719926443199065e-04, 8.455804890991372447e-02,5.040729666030300260e-01,
			9.643073518346039538e-04,2.057718859321112578e-03, 1.748346790366789229e-04, 
			5.429671353946223356e+00, 6.721122910707991727e-01, 8.429169374070397732e-04 };

	private final double[] IGDValues_Task2 = { 8.177472011362102097e-03, 1.531828337174828691e-01, 1.725847814598050281e-04,
			4.417153043393089867e-02, 8.499183875356997930e+01, 6.336990228038563711e-01,
			1.397922567852673964e-03,5.914914231196163191e-03,7.476777884588600709e-02};

}
