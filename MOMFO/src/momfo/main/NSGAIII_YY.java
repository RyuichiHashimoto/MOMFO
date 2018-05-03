package momfo.main;

import java.io.IOException;
import java.util.HashMap;

import javax.naming.NameNotFoundException;

import experiments.Setting;
import momfo.core.AlgorithmMain;
import momfo.core.Operator;
import momfo.core.Problem;
import momfo.core.ProblemSet;
import momfo.metaheuristics.NSGAIII_YY.NSGAIII;
import momfo.operators.crossover.CrossoverFactory;
import momfo.operators.mutation.MutationFactory;
import momfo.problems.MOMFOP.NTU.ProblemSetFactory;
import momfo.util.JMException;

public class NSGAIII_YY extends AlgorithmMain{

	public NSGAIII_YY(Setting test) {
		super(test);
	}



	@Override
	public void setParameter() throws NameNotFoundException, ClassNotFoundException, JMException {

		String Problemname = setting_.getAsStr("Problem");
		int NumberOfRun = setting_.getAsInt("NumberOfTrial");
		Problem problem; // The problem to solve
		Operator crossover = null; // Crossover operator
		Operator mutation = null; // Mutation operator

		HashMap parameters; // Operator parameters
		HashMap d = new HashMap();

		ProblemSet problemSet_ = null;
		try {
			problemSet_ = ProblemSetFactory.getProblemSet(Problemname);
		} catch (IOException e1) {
			// TODO 自動生成された catch ブロック
			e1.printStackTrace();
		}

//		algorithm = new Algorithm[2];
	//	DirectoryName = new String[2];

		for (int t=0;t < problemSet_.countProblem();t++){


			problem = problemSet_.get(t);
			if(t != tasknumber){
				continue;
			}
			int OBJ = problemSet_.get(t).getNumberOfObjectives();
			d.put("numberOfObjectives",OBJ);


			String dddname = "Task" + String.valueOf(tasknumber)+"/"+OBJ  + "OBJ" + setting_.getAsInt("Division") +"div";
			algorithm = new NSGAIII(problem);

			algorithm.setInputParameter("populationSize", setting_.getAsInt("populationSize"));
			String name = "";

			if (setting_.containsKey("DirectoryName") && setting_.getAsStr("DirectoryName") != null){
				name += setting_.getAsStr("DirectoryName");
			}
			DirectoryName = "result/NSGAIII/" + Problemname+"/"+dddname;

			algorithm.setInputParameter("DirectoryName",  DirectoryName);
			algorithm.setInputParameter("maxEvaluations", setting_.getAsInt("maxEvaluations"));
			algorithm.setInputParameter("numberOfParents", 2);
	//		algorithm.setInputParameter("SMSEMOA", 2);
			parameters = new HashMap();
			parameters.put("Mutationprobability",setting_.getAsDouble("MutationProbability"));
			parameters.put("MutationdistributionIndex",setting_.getAsDouble("MutationDistribution"));

			try {
				mutation = MutationFactory.getMutationOperator(setting_.getAsStr("MutationName"), parameters);
			} catch (JMException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
			algorithm.setInputParameter("numberOfDivision",setting_.getAsInt("Division"));

	 		int innnerWeightDivision =  setting_.getAsInt("InnerWeightDivision");
			innnerWeightDivision =  OBJ>= 6 ? innnerWeightDivision : 0;
			algorithm.setInputParameter("InnerWeightDivision",innnerWeightDivision);
			algorithm.setInputParameter("numberOfObjectives",OBJ);// Boolean.valueOf((String)(experiment_setting_.get("isNorm"))));

			parameters.put("Crossoverprobability",setting_.getAsDouble("CrossoverProbability"));
			parameters.put("CrossoverdistributionIndex",setting_.getAsDouble("CrossoverDistribution"));
			try {
				crossover = CrossoverFactory.getCrossoverOperator(setting_.getAsStr("CrossoverName"), parameters);
			} catch (JMException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}

			algorithm.setInputParameter("ismax", setting_.getAsBool("IsMax"));
			algorithm.setInputParameter("IsNorm", setting_.getAsBool("IsNorm"));
			//algoritt].setInputParameter("outputNormal",setting_.getAsBool("outputNormal"));
			algorithm.addOperator("crossover", crossover);
			algorithm.addOperator("mutation", mutation);
			}
	}








}
