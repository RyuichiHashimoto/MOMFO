package momfo.main;

import java.io.IOException;
import java.util.HashMap;

import javax.naming.NameNotFoundException;

import experiments.Setting;
import momfo.core.AlgorithmMain;
import momfo.core.Operator;
import momfo.core.Problem;
import momfo.core.ProblemSet;
import momfo.metaheuristics.nsgaII.NSGAII;
import momfo.operators.crossover.CrossoverFactory;
import momfo.operators.mutation.MutationFactory;
import momfo.problems.MOMFOP.NTU.ProblemSetFactory;
import momfo.util.JMException;

public class NSGAIIMain extends AlgorithmMain{

	public NSGAIIMain(Setting test) {
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

		for (int t=0;t < problemSet_.countProblem();t++){



			if(t != tasknumber){
				continue;
			}
			algorithm = new NSGAII(problemSet_.get(t));
			int OBJ = problemSet_.get(t).getNumberOfObjectives();

			String dddname = problemSet_.get(t).getNumberOfObjectives()  + "OBJ";
			algorithm.setInputParameter("populationSize", setting_.getAsInt("populationSize"));

			DirectoryName = "result/NSGAII/" + Problemname+"/Task"+String.valueOf(t+1)+"/"+dddname;

			algorithm.setInputParameter("DirectoryName", DirectoryName);

			algorithm.setInputParameter("maxEvaluations", setting_.getAsInt("maxEvaluations"));
			algorithm.setInputParameter("numberOfParents", 2);
	//		algorithm.setInputParameter("SMSEMOA", 2);
			parameters = new HashMap();
			parameters.put("Mutationprobability",setting_.getAsDouble("MutationProbability"));
			parameters.put("MutationdistributionIndex",setting_.getAsDouble("MutationDistribution"));
	//		algorithm.setInputParameter("outputNormal",setting_.getAsBool("outputNormal"));

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
	//		algorithm.setInputParameter("isNorm", setting_.getAsBool("IsNorm"));

		}
	}








}
