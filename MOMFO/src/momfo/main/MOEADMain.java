package momfo.main;

import java.io.IOException;
import java.util.HashMap;

import javax.naming.NameNotFoundException;

import experiments.Setting;
import momfo.core.AlgorithmMain;
import momfo.core.Operator;
import momfo.core.Problem;
import momfo.core.ProblemSet;
import momfo.metaheuristics.moead.MOEAD;
import momfo.operators.crossover.CrossoverFactory;
import momfo.operators.mutation.MutationFactory;
import momfo.problems.MOMFOP.NTU.ProblemSetFactory;
import momfo.util.JMException;

public class MOEADMain extends AlgorithmMain{

	public MOEADMain(Setting test) {
		super(test);
	}
	private double[] getRef(String name, int obj){
		double[] ret = new double[obj];
		String[] ps = name.split(",");
		if(ps.length == 1){
			for(int i=0;i<obj;i++){
				ret[i] = Double.parseDouble(ps[0]);
			}
		} else if (ps.length == 2){
			assert ps.length == obj : "obj";
			ret[0] = Double.parseDouble(ps[0]);
			ret[1] = Double.parseDouble(ps[1]);
		} else if ((ps.length == 3) && (obj != 3 )){

			double[] emp = new double[3];
			for(int i=0;i<obj;i++){
				emp[i] = Double.parseDouble(ps[i]);
			}
			if(  Math.abs(emp[2] - 2*emp[1] + emp[0]) < 1.0E-14){
				ret[0] = emp[0] ;
				for(int i=1;i <obj;i++){
						ret[i] =  emp[i-1] + emp[2] - emp[1];
				}
			} else if ( Math.abs(emp[1]*emp[1] - emp[0]*emp[2]) < 1.0E-14){
				ret[0] = emp[0] ;
				for(int i=1;i <obj;i++){
						ret[i] =  emp[i-1] + emp[2] - emp[1];
				}
			}
		} else if (ps.length == obj){
			for(int i=0;i<obj;i++){
				ret[i] = Double.parseDouble(ps[i]);
			}
		}
		return ret;
	}


	@Override
	public void setParameter() throws NameNotFoundException, ClassNotFoundException, JMException {


		String Problemname = setting_.getAsStr("Problem");
	//	int OBJ = setting_.getAsInt("Objectives");
		int NumberOfRun = setting_.getAsInt("NumberOfTrial");
	//	algorithm = new GA[2];
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

//		DirectoryName = new String[problemSet_.countProblem()];

		for (int t=0;t < problemSet_.countProblem();t++){

			if(t != tasknumber){
				continue;
			}
			int OBJ = problemSet_.get(t).getNumberOfObjectives();
			d.put("numberOfObjectives",OBJ);


			algorithm = new MOEAD(problemSet_.get(t));

			String dddname = problemSet_.get(t).getNumberOfObjectives()  + "OBJ" + setting_.getAsInt("Division") + "div";
			//上書きはしないため，本来持っている値は消えない．
			setting_.add("PBITheta",-1000000);
			algorithm.setInputParameter("PBITheta", setting_.getAsDouble("PBITheta"));

			if(setting_.getAsStr("ScalarFunction").startsWith("PBI")){
				DirectoryName = "result/MOEAD/" + Problemname+"/"+"Task"+String.valueOf(t+1)+"/"+dddname + "/" + setting_.getAsStr("ScalarFunction")+setting_.getAsStr("PBITheta").replace(".","_");
			}else {
				DirectoryName = "result/MOEAD/" + Problemname+"/"+"Task"+String.valueOf(t+1)+"/"+dddname + "/" + setting_.getAsStr("ScalarFunction");
			}

			algorithm.setInputParameter("DirectoryName",  DirectoryName);
			System.out.println();
			System.out.println(DirectoryName);

			algorithm.setInputParameter("Norm",setting_.getAsBool("IsNorm"));
			algorithm.setInputParameter("maxEvaluations", setting_.getAsInt("maxEvaluations"));
			algorithm.setInputParameter("numberOfParents", 2);
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
			System.out.println(NumberOfRun);
			algorithm.setInputParameter("ScalarzingFunctionName", setting_.getAsStr("ScalarFunction"));




			algorithm.setInputParameter("numberOfObjectives", OBJ);// Boolean.valueOf((String)(experiment_setting_.get("isNorm"))));



	 		algorithm.setInputParameter("alphar", setting_.getAsDouble("alpha"));
			algorithm.setInputParameter("numberOfDivision",setting_.getAsInt("Division"));

	 		int innnerWeightDivision =  setting_.getAsInt("InnerWeightDivision");
			innnerWeightDivision =   OBJ >= 6 ? innnerWeightDivision : 0;
			algorithm.setInputParameter("InnerWeightDivision",innnerWeightDivision);


			algorithm.setInputParameter("sizeOfNeiborhoodRepleaced_",setting_.getAsInt("matingNeighborhood"));
			algorithm.setInputParameter("sizeOfMatingNeiborhood_",setting_.getAsInt("ReplaceNeighborhood"));

			algorithm.setInputParameter("outputNormal",false);
			algorithm.addOperator("crossover", crossover);
			algorithm.addOperator("mutation", mutation);
			algorithm.setInputParameter("IsMax", setting_.getAsBool("IsMax"));
			algorithm.setInputParameter("IsNorm", setting_.getAsBool("IsNorm"));
		}
	}

}