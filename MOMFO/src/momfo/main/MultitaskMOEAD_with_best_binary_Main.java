package momfo.main;

import java.io.IOException;
import java.util.HashMap;

import javax.naming.NameNotFoundException;

import experiments.Setting;
import momfo.core.AlgorithmMain;
import momfo.core.Operator;
import momfo.core.Problem;
import momfo.core.ProblemSet;
import momfo.metaheuristics.multitaskMOEAD_with_Best_binary.MultitaskMOEAD_with_Best_Binary;
import momfo.operators.crossover.CrossoverFactory;
import momfo.operators.mutation.MutationFactory;
import momfo.problems.MOMFOP.NTU.ProblemSetFactory;
import momfo.util.JMException;

public class MultitaskMOEAD_with_best_binary_Main extends AlgorithmMain{

	public MultitaskMOEAD_with_best_binary_Main(Setting test) {
		super(test);
	}

	@Override
	public void setParameter() throws NameNotFoundException, ClassNotFoundException, JMException {


		String Problemname = setting_.getAsStr("Problem");
	//	int OBJ = setting_.getAsInt("Objectives");
		int NumberOfRun = setting_.getAsInt("NumberOfTrial");
	//	algorithm = new Algorithm[2];
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

		algorithm = new MultitaskMOEAD_with_Best_Binary(problemSet_);
		for (int t=0;t < problemSet_.countProblem();t++){

			int OBJ = problemSet_.get(t).getNumberOfObjectives();
			d.put("numberOfObjectives",OBJ);

			String dddname = setting_.getAsStr("rmp")  + "rmp" + setting_.getAsStr("Division").split(",")[t] + "div";
			setting_.add("PBITheta",-1000000);
			algorithm.setInputParameter("PBITheta"+String.valueOf(t+1),  Double.valueOf(setting_.getAsStr("PBITheta").split(",")[t]));

			if(setting_.getAsStr("ScalarFunction").split(",")[t].startsWith("PBI")){
				DirectoryName = "result/MultitaskMOEAD_with_best_binary/" + Problemname+"/"+"Task"+String.valueOf(1)+"/"+dddname + "/" + setting_.getAsStr("ScalarFunction").split(",")[t]+setting_.getAsStr("PBITheta").split(",")[t].replace(".","_");
			}else {
				DirectoryName = "result/MultitaskMOEAD_with_best_binary/" + Problemname+"/"+"Task"+String.valueOf(1)+"/"+dddname + "/" + setting_.getAsStr("ScalarFunction").split(",")[t];
			}

			algorithm.setInputParameter("DirectoryName",  DirectoryName);
			algorithm.setInputParameter("maxEvaluations" + String.valueOf(t+1), Integer.valueOf(setting_.getAsStr("maxEvaluations").split(",")[t]) );
			algorithm.setInputParameter("numberOfParents"+ String.valueOf(t+1), 2);

			parameters = new HashMap();
			parameters.put("Mutationprobability",setting_.getAsDouble("MutationProbability"));
			parameters.put("MutationdistributionIndex",setting_.getAsDouble("MutationDistribution"));
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
			algorithm.setInputParameter("BinarySize", setting_.getAsInt("BinarySize"));

			algorithm.setInputParameter("ScalarzingFunctionName" + String.valueOf(t+1), setting_.getAsStr("ScalarFunction").split(",")[t]);

			algorithm.setInputParameter("numberOfObjectives"+String.valueOf(t+1), problemSet_.get(t).getNumberOfObjectives());// Boolean.valueOf((String)(experiment_setting_.get("isNorm"))));
	 		algorithm.setInputParameter("alphar", setting_.getAsDouble("alpha"));
	 		algorithm.setInputParameter("numberOfDivision"+String.valueOf(t+1), Integer.valueOf(setting_.getAsStr("Division").split(",")[t]));

	 		int innnerWeightDivision = 0;;
	 		if( OBJ > 6){
	 			innnerWeightDivision = Integer.valueOf( setting_.getAsStr("InnerWeightDivision").split(",")[t]);
	 		}
			algorithm.setInputParameter("InnerWeightDivision"+String.valueOf(t+1),innnerWeightDivision);

			algorithm.setInputParameter("sizeOfNeiborhoodRepleaced_"+String.valueOf(t+1), Integer.valueOf(setting_.getAsStr("matingNeighborhood").split(",")[t]));
			algorithm.setInputParameter("sizeOfMatingNeiborhood_"+String.valueOf(t+1), Integer.valueOf(setting_.getAsStr("ReplaceNeighborhood").split(",")[t]));

			algorithm.setInputParameter("rmp", setting_.getAsDouble("rmp"));
			algorithm.addOperator("crossover", crossover);
			algorithm.addOperator("mutation", mutation);
			algorithm.setInputParameter("IsMax"+String.valueOf(t+1), setting_.getAsBool("IsMax"+ String.valueOf(t+1)) );
			//algorithm.setInputParameter("IsNorm", setting_.getAsBool("IsNorm"));
		}
	}


}