//  NSGAIII_main.java
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
package momfo.metaheuristics.NSGAIII;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

import org.kohsuke.args4j.Option;

import experiments.SettingWriter;
import momfo.core.Algorithm;
import momfo.core.Operator;
import momfo.core.Problem;
import momfo.operators.crossover.CrossoverFactory;
import momfo.operators.mutation.MutationFactory;
import momfo.util.Configuration;
import momfo.util.JMException;
import momfo.util.Random;
import momfo.util.ScalarzingFunction.ScalarzingFunction;
/**
 * This class executes the algorithm described in: H. Li and Q. Zhang,
 * "Multiobjective Optimization Problems with Complicated Pareto Sets, MOEA/D
 * and NSGA-II". IEEE Trans on Evolutionary Computation, vol. 12, no 2, pp
 * 284-302, April/2009.
 */
public class NSGAIII_main {
	public static Logger logger_; // Logger object
	public static FileHandler fileHandler_; // FileHandler object
	@Option(name="-pop", aliases= "--populationSize", required=false, metaVar="<Division>", usage="specify the populationSize")
		private int  populationSize ;
	@Option(name="-problem", aliases= "--problem", required=true, metaVar="<problem>", usage="specify the problem name")
		private String problem ;
	@Option(name="-obj", aliases= "--Objectives", required=true, metaVar="<Objectives>", usage="specify the objectives")
	private int numberOfObj;
	@Option(name="-div", aliases= "--Division", required=true, metaVar="<Division>", usage="specify the objectives")
	private int div;




	void subscriptLogToFile(){

	};

	static HashMap log = new HashMap<>();

	static String functionname(String name){
		String Functionname = "boid";
		try(BufferedReader br = new BufferedReader(new FileReader(name))){
		String line;

		while(( line = br.readLine())!= null){
			Functionname  = line;
		}	} catch (IOException e){
			e.printStackTrace();
	}

		return Functionname;

	}
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

	public void domain(String[] args){

	}

	//　arg1 = Scalarfunctionname, arg2 = Problem Name, arg3 =
	public static void main(String[] args) throws JMException, SecurityException, IOException, ClassNotFoundException {
		experiment_setting("setting/NSGAIII.st");
		String empty;

		int NumberOfRun;

		String Problemname = args[0];
		String nobk = args[1];
		String nod = args[2];
		String nod_ = args[3];

		int numberOfObj = Integer.parseInt(nobk);
		int  numberOfdivision = Integer.parseInt(nod);
		int  InnnernumberOfdivision = Integer.parseInt(nod);

	    Problem problem = null; // The problem to solve
		Algorithm algorithm; // The algorithm to use
		Operator crossover; // Crossover operator
		Operator mutation; // Mutation operator
		ScalarzingFunction Function;

		HashMap parameters; // Operator parameters


		// Logger object and file to store log messages
		logger_ = Configuration.logger_;

		empty = (String) experiment_setting_.get("NumberOfTrial");

		NumberOfRun = Integer.parseInt(empty);

		HashMap d = new HashMap();

		d.put("numberOfObjectives",numberOfObj);
	//	problem = MOPFactory.getMOP(Problemname,d,"NSGAIII");

		algorithm = new NSGAIII(problem);

		algorithm.setInputParameter("numberOfObjectives",numberOfObj);
		String problemname = problem.getName();
		// algorithm = new NSGAIII_DRA(problem);

		algorithm.setInputParameter("numberOfDivision", numberOfdivision);

		String expro =   nobk+"OBJ" + nod  + "div"  ;
		File newDir = new File("result");
		newDir.mkdir();
		newDir = new File("result/NSGAIII");
		newDir.mkdir();
		newDir = new File("result/NSGAIII/" + problemname);
		newDir.mkdir();
		newDir = new File("result/NSGAIII/" + problemname + "/" +expro);
		newDir.mkdir();
		newDir = new File("result/NSGAIII/" + problemname + "/" +expro +"/");
		newDir.mkdir();
		algorithm.setInputParameter("directoryname",  "result/NSGAIII/" + problemname + "/" +expro +"/");
		newDir = new File("result/NSGAIII/" + problemname + "/" +expro +"/" + "/FinalFUN");
		newDir.mkdir();
		newDir = new File("result/NSGAIII/" + problemname + "/" +expro  +"/" + "/InitialFUN");
		newDir.mkdir();
		newDir = new File("result/NSGAIII/" + problemname + "/" +expro +"/"  + "/InitialVAR");
		newDir.mkdir();
		newDir = new File("result/NSGAIII/" + problemname + "/" +expro+"/"  + "/FinalVAR");
		newDir.mkdir();

		algorithm.setInputParameter("DirectoryName", "result/NSGAIII/" + problemname + "/" +expro);
		String directory = "result/NSGAIII/" + problemname + "/" +expro  + "/" + "Setting";

		newDir = new File(directory);
		newDir.mkdir();

		/*
		if (numberOfObj == 3){
			algorithm.setInputParameter("MaxGeneration",400);
		} else if (numberOfObj == 5){
			algorithm.setInputParameter("MaxGeneration",600);
		}		 else if (numberOfObj == 8){
				algorithm.setInputParameter("MaxGeneration",750);
		} else if (numberOfObj == 10){
				algorithm.setInputParameter("MaxGeneration",1000);
		}
		*/

		algorithm.setInputParameter("numberOfParents",2);

		empty = (String) experiment_setting_.get("InnerWeightDivision");
		int innnerWeightDivision = Integer.parseInt(empty);
		innnerWeightDivision =  numberOfObj > 6 ? innnerWeightDivision : 0;
		algorithm.setInputParameter("InnerWeightDivision",innnerWeightDivision);

		empty = (String) experiment_setting_.get("populationSize");
		System.out.println(empty+"");

		algorithm.setInputParameter("populationSize", Integer.parseInt(empty));
/*
		int evaluation_ = -1;
		int populationSize  = -1;
		int div 			  = -1;
		int innerdiv = -1;
		int gen = 0;
		switch (numberOfObj){
			case 3: populationSize = 92; div = 12; innerdiv = 0;break;
			case 5: populationSize = 212;div = 6;innerdiv = 0; break;
			case 8: populationSize = 156;div = 3;innerdiv = 2; break;
			case 10: populationSize = 276;div = 3;innerdiv = 2; break;
			case 15: populationSize = 136;div = 12; innerdiv = 1;break;
			default:
				System.err.println("error");
				System.exit(-1);
		}
		switch (Problemname){
			case "DTLZ1":
				switch (numberOfObj){
				case
				}



		}
		*/




		empty = (String) experiment_setting_.get("maxEvaluations");
		algorithm.setInputParameter("maxEvaluations", Integer.parseInt(empty));


/*
		if (numberOfObj ==3 ){
			algorithm.setInputParameter("sizeOfNeiborhoodRepleaced_",9 );
			algorithm.setInputParameter("sizeOfMatingNeiborhood_",9);
		} else if (numberOfObj ==5 ){
			algorithm.setInputParameter("sizeOfNeiborhoodRepleaced_",21);
			algorithm.setInputParameter("sizeOfMatingNeiborhood_",21);		} else if (numberOfObj ==8 ){
			algorithm.setInputParameter("sizeOfNeiborhoodRepleaced_",15);
			algorithm.setInputParameter("sizeOfMatingNeiborhood_",15);
		} else if (numberOfObj ==10 ){
			algorithm.setInputParameter("sizeOfNeiborhoodRepleaced_",27);
			algorithm.setInputParameter("sizeOfMatingNeiborhood_",27);
		}
*/		// Crossover operator
		parameters = new HashMap();


		// Mutation operator
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
		empty = (String) experiment_setting_.get("CrossoverName");
		crossover = CrossoverFactory.getCrossoverOperator(empty, parameters);



		algorithm.setInputParameter("isNorm", Boolean.valueOf((String)(experiment_setting_.get("isNorm"))));

		algorithm.setInputParameter("ismax", Boolean.valueOf((String)(experiment_setting_.get("isMax"))));
        algorithm.addOperator("crossover", crossover);
        algorithm.addOperator("mutation", mutation);



		SettingWriter.clear();
		SettingWriter.merge(algorithm.getAllmap());
		SettingWriter.merge(mutation.getMap());
		SettingWriter.add("Problemname",Problemname);
		SettingWriter.merge(crossover.getMap());
		SettingWriter.write(directory);

		long initTime = System.currentTimeMillis();

		int counter=0;
		Random.set_seed(10);
		do {
			counter++;

			algorithm.setInputParameter("times", counter);
			System.out.println("NSGAIII:"+ counter + "回目開始");
			long initTime_ = System.currentTimeMillis();
			algorithm.execute();
			long estimatedTime_ = System.currentTimeMillis() - initTime_;
			System.out.println("NSGAIII:"+ counter + "回目終了	" +estimatedTime_ + "ms" );

		} while(counter<NumberOfRun);

		long estimatedTime = System.currentTimeMillis() - initTime;

		logger_.info("Total execution time: " + estimatedTime + "ms");
		logger_.info("Objectives values have been writen to file FUN");
		logger_.info("Variables values have been writen to file VAR");

	}
} // NSGAIII_main
