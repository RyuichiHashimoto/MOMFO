package momfo.metaheuristics.MultitaskMOEAD;

import static org.junit.jupiter.api.Assertions.fail;

import javax.naming.NameNotFoundException;

import org.junit.jupiter.api.Test;

import experiments.Setting;
import momfo.core.AlgorithmMain;
import momfo.main.AlgorithmMainFactory;
import momfo.main.MultitaskMOEAD_Main;
import momfo.util.JMException;

class MultitaskMOEADTest {

	@Test
	void test() throws ClassNotFoundException, NameNotFoundException, JMException {		
		for(int i = 0; i < 9;i++) {
			eachTest(i);
		}
	}

	private  final String[] ProblemName = {"CIHS","CIMS","CILS","PIHS","PIMS","PILS","NIHS","NIMS","NILS"};
	
	public void eachTest(int problemNumber) throws ClassNotFoundException, NameNotFoundException, JMException {
		
		Setting hashmap  = new Setting();
        AlgorithmMain algorithm;
        
        String geneticAlgorithmName = "MultitaskMOEAD_DE";
        
        //コマンドラインによる実験設定の追加
        
        hashmap
        .add("CrossoverName", "SBXCrossover")
        .add("CrossoverProbability", "0.9")
        .add("CrossoverDistribution", "20")
        .add("MutationName", "PolynomialMutation")
        .add("MutationProbability", "-1")
        .add("MutationDistribution", "20")
        .add("populationSize", "100,100")
        .add("Problem", ProblemName[problemNumber])
        .add("Seed", "1")
        .add("NumberOfTrial", 1)	
        .add("maxEvaluations","100000,100000");
        
        
        hashmap
        .add("rmp", "0.1");
        
        hashmap
        .add("IsMax"+ String.valueOf(1), false)
        .add("IsMax"+ String.valueOf(2), false);
        
        hashmap
        .add("ScalarFunction","TchebycheffFormin,TchebycheffFormin")
        .add("matingNeighborhood", "20,20")
        .add("ReplaceNeighborhood", "20,20")
        .add("Division", (problemNumber == 7 ||problemNumber == 8 ) ? "13,99":"99,99")
        .add("InnerWeightDivision","0,0")
        .add("alpha","1.0")
        .add("PBITheta","5.0,5.0");

        hashmap.add("TaskNumber",0);
        algorithm = new MultitaskMOEAD_Main(hashmap);
        algorithm.runMultitask(0);
        
        double[]  obj_1= (double[])algorithm.getAlgorithm().getOutputParameter("FinalFUN1");
        double[]  val_1= (double[])algorithm.getAlgorithm().getOutputParameter("FinalVAR1");
        
        double[]  obj_2= (double[])algorithm.getAlgorithm().getOutputParameter("FinalFUN2");
        double[]  val_2= (double[])algorithm.getAlgorithm().getOutputParameter("FinalVAR2");
        
        double  IGD1= (double)algorithm.getAlgorithm().getOutputParameter("IGD1");
        double  IGD2= (double)algorithm.getAlgorithm().getOutputParameter("IGD2");
        
        if(! (IGD1 == IGDValues_Task1[problemNumber]))
        	fail("IGD Value of Task 1 is wrong");

        if(!(IGD2 == IGDValues_Task2[problemNumber]))
        	fail("IGD Value of Task 2 is wrong");        
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
	
		
	public void add(Setting hashmap) throws JMException{

       
     
	}
	
}
