package momfo.main;

import java.io.IOException;
import java.util.HashMap;

import javax.naming.NameNotFoundException;

import experiments.Setting;
import momfo.core.AlgorithmMain;
import momfo.core.Operator;
import momfo.core.Problem;
import momfo.core.ProblemSet;
import momfo.metaheuristics.smsemoa.SMSEMOA;
import momfo.operators.crossover.CrossoverFactory;
import momfo.operators.mutation.MutationFactory;
import momfo.problems.MOMFOP.NTU.ProblemSetFactory;
import momfo.util.JMException;

public class SMSEMOAMain extends AlgorithmMain{

	public SMSEMOAMain(Setting test) {
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
			for(int i=0;i<3;i++){
				emp[i] = Double.parseDouble(ps[i]);
			}
			if( Math.abs(emp[2] + emp[0]- 2*emp[1] ) < 1.0E-14){
				ret[0] = emp[0] ;
				for(int i=1;i <obj;i++){
						ret[i] =  ret[i-1] + emp[2] - emp[1];
				}

			} else if ( Math.abs(emp[1]*emp[1] - emp[0]*emp[2]) < 1.0E-14){
				ret[0] = emp[0] ;
				for(int i=1;i <obj;i++){
						ret[i] =  ret[i-1] * emp[1]/emp[0];
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

        Problem problem; // The problem to solve
		Operator crossover = null; // Crossover operator
		Operator mutation = null; // Mutation operator

		HashMap parameters; // Operator parameters
		 ProblemSet problemSet_ = null; // The problem to solve

		try {
			problemSet_ = ProblemSetFactory.getProblemSet(Problemname);
		} catch (IOException e1) {
			// TODO 自動生成された catch ブロック
			e1.printStackTrace();
		}

		for (int t=0;t < problemSet_.countProblem();t++){

			if(t != tasknumber){
				continue;
			}
			int OBJ = problemSet_.get(t).getNumberOfObjectives();
			problem = problemSet_.get(t);
			algorithm = new SMSEMOA(problem);
			String dddname = "Task" + String.valueOf(tasknumber+1) + "/"+problem.getNumberOfObjectives()  + "OBJ";
			algorithm.setInputParameter("populationSize", setting_.getAsInt("populationSize"));
			double[] ref = getRef(setting_.getAsStr("ref"),OBJ);

			DirectoryName = "result/SMSEMOA/" + Problemname+"/"+dddname + "/ref" + setting_.getAsStr("ref");
			algorithm.setInputParameter("DirectoryName",  DirectoryName);
			System.out.println();
			System.out.println(DirectoryName);

			algorithm.setInputParameter("Norm",setting_.getAsBool("Norm"));
			algorithm.setInputParameter("maxEvaluations", setting_.getAsInt("maxEvaluations"));
			algorithm.setInputParameter("numberOfParents", 2);

			parameters = new HashMap();
			parameters.put("Mutationprobability",setting_.getAsDouble("MutationProbability"));
			parameters.put("MutationdistributionIndex",setting_.getAsDouble("MutationDistribution"));

			try {
				mutation = MutationFactory.getMutationOperator(setting_.getAsStr("MutationName"), parameters);
			} catch (JMException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
			algorithm.setInputParameter("ReferencePoint", ref);
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
			algorithm.setInputParameter("isNorm", setting_.getAsBool("IsNorm"));
			}

	}



}
