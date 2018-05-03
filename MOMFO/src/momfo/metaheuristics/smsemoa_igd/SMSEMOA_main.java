 //  MOEAD_main.java
//
//  Author:
//       Antonio J. Nebro <antonio@lcc.uma.es>
//       Juan J. Durillo <durillo@lcc.uma.es>
//
//  Copyright (c) 2011 Antonio J. Neb2o, Juan J. Durillo
//
//  This program is free software: you can redistribute it and/or modify
//  it under the terms of the GNU Lesser General Public License as published by
//  the Free Software Foundation, either version 3 of the License, or
//  (at your option) any later version.
//
//  This program is distributed in the hope that it will be useful,
//  but WITHOUT ANY WARRANTY; without even the implied warranty of
//  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//  GNU Lesser General Public License for more details.
//
//  You should have received a copy of the GNU Lesser General Public License
//  along with this program.  If not, see <http://www.gnu.org/licenses/>.
package momfo.metaheuristics.smsemoa_igd;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

import experiments.SettingWriter;
import momfo.core.Algorithm;
import momfo.core.Operator;
import momfo.core.Population;
import momfo.core.Problem;
import momfo.operators.crossover.CrossoverFactory;
import momfo.operators.mutation.MutationFactory;
import momfo.util.JMException;
import momfo.util.Random;

/**
 * This class executes the algorithm described in: H. Li and Q. Zhang,
 * "Multiobjective Optimization Problems with Complicated Pareto Sets, MOEA/D
 * and NSGA-II". IEEE Trans on Evolutionary Computation, vol. 12, no 2, pp
 * 284-302, April/2009.
 */
public class SMSEMOA_main {
	public static Logger logger_; // Logger object
	public static FileHandler fileHandler_; // FileHandler object


	void subscriptLogToFile(){

	};

	static HashMap log = new HashMap<>();

	static HashMap experiment_setting_ = new HashMap<>();

	static void experiment_setting(String name){
			try(BufferedReader br = new BufferedReader(new FileReader(name))){
			String[] S;
			String line;
			int counter=0;
			while(( line = br.readLine())!= null){
				S  = line.split(":");
				System.out.print(S[0] + " " + S[1] + "\n");
				experiment_setting_.put(S[0],S[1]);
     			}
			} catch (IOException e){
				e.printStackTrace();
			}
	}

	public static void main(String[] args) throws JMException, SecurityException, IOException, ClassNotFoundException {
		// this value has the name of weight vector data
		experiment_setting("setting/SMSEMOA.st");
		String knapsackfileName;
		String empty;

		String Problemname = args[0];
		String nobk = args[1];

		int NumberOfRun = 10;
		int numberOfObj = Integer.parseInt(nobk);

     //   Problem problem; // The problem to solve
		Algorithm algorithm; // The algorithm to use
		Operator crossover; // Crossover operator
		Operator mutation; // Mutation operator
		HashMap parameters; // Operator parameters


		HashMap d = new HashMap();
		d.put("numberOfObjectives",numberOfObj);
        Problem problem = null; // The problem to solve

		algorithm = new SMSEMOAIGD(problem);
		String problemname = problem.getName();
		// algorithm = new MOEAD_DRA(problem);





		String dddname = problem.getNumberOfObjectives()  + "OBJ" + "ref" + args[2];



		File newDir = new File("result");
		newDir.mkdir();
		newDir = new File("result/SMSEMOA");
		newDir.mkdir();
		newDir = new File("result/SMSEMOA/" + Problemname);
		newDir.mkdir();
		newDir = new File("result/SMSEMOA/" + Problemname +"/"+dddname );
		newDir.mkdir();

		newDir = new File("result/SMSEMOA/" + Problemname+"/"+dddname+"/FinalFUN");
		newDir.mkdir();
		newDir = new File("result/SMSEMOA/" + Problemname+"/"+dddname+"/InitialFUN");
		newDir.mkdir();
		newDir = new File("result/SMSEMOA/" + Problemname+"/"+dddname+"/FinalVAR");
		newDir.mkdir();
		newDir = new File("result/SMSEMOA/" + Problemname+"/"+dddname+"/InitialVAR");
		newDir.mkdir();
		algorithm.setInputParameter("DirectoryName",  "result/SMSEMOA/" + Problemname+"/"+dddname);
		newDir = new File("result/SMSEMOA/" + Problemname+"/"+dddname + "/Setting");
		newDir.mkdir();
		String directory = "result/SMSEMOA/" + Problemname+"/"+dddname + "/Setting";


		empty = (String) experiment_setting_.get("populationSize");
		algorithm.setInputParameter("populationSize",Integer.parseInt(empty));
		empty = (String) experiment_setting_.get("maxEvaluations");
		algorithm.setInputParameter("maxEvaluations", Integer.parseInt(empty));
		algorithm.setInputParameter("numberOfParents", 2);

		empty = (String) experiment_setting_.get("maxEvaluations");
		algorithm.setInputParameter("maxEvaluations", Integer.parseInt(empty));
		empty = (String) experiment_setting_.get("MaxGeneration");
		algorithm.setInputParameter("MaxGeneration", Integer.parseInt(empty));
		algorithm.setInputParameter("numberOfParents", 2);

		//	algorithm.setInputParameter("ScalazingFunction","WeightedSum");
		// Crossover operator
		parameters = new HashMap();


		algorithm.setInputParameter("ismax", Boolean.valueOf((String)(experiment_setting_.get("isMax"))));
		algorithm.setInputParameter("Norm", Boolean.valueOf((String)(experiment_setting_.get("Norm"))));

		algorithm.setInputParameter("maxEvaluation", Integer.valueOf((String)(experiment_setting_.get("maxEvaluations"))));
		double[] ref = new double[problem.getNumberOfObjectives()];

		ref = ReferencePointDistributor.getReferencePoint(Integer.parseInt(args[2]), problem.getNumberOfObjectives());
		algorithm.setInputParameter("ReferencePoint", ref);
			// Mutation operator
		empty = (String) experiment_setting_.get("MutationProbability");
		parameters.put("Mutationprobability",Double.parseDouble(empty));
		empty = (String) experiment_setting_.get("MutationDistribution");
		parameters.put("MutationdistributionIndex",Double.parseDouble(empty));
		mutation = MutationFactory.getMutationOperator((String)experiment_setting_.get("MutationName"), parameters);

		// Crossover operator
		empty = (String) experiment_setting_.get("CrossoverProbability");
		parameters.put("Crossoverprobability",Double.parseDouble(empty));
		empty = (String) experiment_setting_.get("CrossoverDistribution");
		parameters.put("CrossoverdistributionIndex",Double.parseDouble(empty));
		crossover = CrossoverFactory.getCrossoverOperator((String)experiment_setting_.get("CrossoverName"), parameters);


        algorithm.addOperator("crossover", crossover);
		algorithm.addOperator("mutation", mutation);

		// Execute the Algorithm
		long initTime = System.currentTimeMillis();

		Population max = new Population();
		Population population = new Population();


		SettingWriter.clear();
		SettingWriter.merge(algorithm.getAllmap());
		SettingWriter.merge(mutation.getMap());
		SettingWriter.add("numberOfvariable",problem.getNumberOfVariables());
		SettingWriter.add("numberOfObjective",problem.getNumberOfObjectives());
		SettingWriter.add("Problemname",Problemname);
		SettingWriter.add("mutation",mutation.getName());
		SettingWriter.add("crossover",crossover.getName());
		SettingWriter.add("numberOfObjective",problem.getNumberOfObjectives());
		SettingWriter.add("Problemname",Problemname);
		SettingWriter.merge(crossover.getMap());
		SettingWriter.write(directory);


		System.out.print( "mutation	"+ mutation.getName());
		int counter=0;
		Random.set_seed(56);
		do {
			counter++;
			algorithm.setInputParameter("times", counter);
			System.out.println("SMSEMOA" +":"+ counter + "回目開始");
			population = algorithm.execute();
			System.out.println("SMSEMOA" +":"+ counter + "回目終了");

		} while(counter<NumberOfRun);

		long estimatedTime = System.currentTimeMillis() - initTime;
		System.out.print(estimatedTime  + "ms");
		// Result messages
		//logger_.info("Total execution time: " + estimatedTime + "ms");
		//logger_.info("Objectives values have been writen to file FUN");
		//DominancedSolutionSet.printObjectivesToFile("DOMINANCEEDFUN");

		//logger_.info("Variables values have been writen to file VAR");
		//DominancedSolutionSet.printVariablesToFile("DOMINANCEEDVAR");

	}
} // MOEAD_main
