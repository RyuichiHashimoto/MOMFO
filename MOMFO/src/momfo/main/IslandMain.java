package momfo.main;

import java.io.IOException;
import java.util.HashMap;

import javax.naming.NameNotFoundException;

import experiments.Setting;
import momfo.core.AlgorithmMain;
import momfo.core.Operator;
import momfo.core.Problem;
import momfo.core.ProblemSet;
import momfo.metaheuristics.Island.IslandModel;
import momfo.operators.crossover.CrossoverFactory;
import momfo.operators.mutation.MutationFactory;
import momfo.problems.MOMFOP.NTU.ProblemSetFactory;
import momfo.util.JMException;

public class IslandMain extends AlgorithmMain{

	public IslandMain(Setting test) {
		super(test);
	}



	@Override
	public void setParameter() throws NameNotFoundException, ClassNotFoundException, JMException {


		String Problemname = setting_.getAsStr("Problem");
//		int OBJ = setting_.getAsInt("Objectives");
		int NumberOfRun = setting_.getAsInt("NumberOfTrial");

        Problem problem; // The problem to solve
		Operator crossover = null; // Crossover operator
		Operator mutation = null; // Mutation operator
		ProblemSet problemSet_ = null;
		try {
			problemSet_ = ProblemSetFactory.getProblemSet(Problemname);
		} catch (IOException e1) {
			// TODO 自動生成された catch ブロック
			e1.printStackTrace();
		}
		HashMap parameters; // Operator parameters
		HashMap d = new HashMap();

		algorithm = new IslandModel(problemSet_);

		String dddname =  "rmp" +setting_.getAsStr("rmp") ;
		DirectoryName = "result/Island/"+dddname  +"/"+ Problemname+"/Task"+String.valueOf(1);
		algorithm.setInputParameter("DirectoryName", DirectoryName);
		algorithm.setInputParameter("rmp", setting_.getAsDouble("rmp"));

		for(int t = 0 ; t < problemSet_.countProblem();t++){

			algorithm.setInputParameter("populationSize" + String.valueOf(t+1), Integer.valueOf(setting_.getAsStr("populationSize").split(",")[t]));
			algorithm.setInputParameter("maxEvaluation" + String.valueOf(t+1), Integer.valueOf(setting_.getAsStr("maxEvaluations").split(",")[t]));
			algorithm.setInputParameter("IsMax"+String.valueOf(t+1), setting_.getAsBool("IsMax"+ String.valueOf(t+1)) );

		}



	//		algorithm.setInputParameter("SMSEMOA", 2);
//		algorithm.setInputParameter("outputNormal",setting_.getAsBool("outputNormal"));
		parameters = new HashMap();
		parameters.put("Mutationprobability",setting_.getAsDouble("MutationProbability"));
		parameters.put("MutationdistributionIndex",setting_.getAsDouble("MutationDistribution"));

		try {
			mutation = MutationFactory.getMutationOperator(setting_.getAsStr("MutationName"), parameters);
		} catch (JMException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		parameters.put("Crossoverprobability",setting_.getAsDouble("CrossoverProbability"));
		parameters.put("CrossoverdistributionIndex",setting_.getAsDouble("CrossoverDistribution"));
		try {
			crossover = CrossoverFactory.getCrossoverOperator(setting_.getAsStr("CrossoverName"), parameters);
		} catch (JMException e) {
				// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		
		algorithm.addOperator("crossover", crossover);
		algorithm.addOperator("mutation", mutation);
		algorithm.setInputParameter("ismax", setting_.getAsBool("isMax"));
		
	}







}
