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
package momfo.metaheuristics.manyIslandmoead;

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
import momfo.core.Problem;
import momfo.operators.crossover.CrossoverFactory;
import momfo.operators.mutation.MutationFactory;
import momfo.util.Configuration;
import momfo.util.JMException;
import momfo.util.Random;
/**
 * This class executes the algorithm described in: H. Li and Q. Zhang,
 * "Multiobjective Optimization Problems with Complicated Pareto Sets, MOEA/D
 * and NSGA-II". IEEE Trans on Evolutionary Computation, vol. 12, no 2, pp
 * 284-302, April/2009.
 */
public class manyIslandMOEAD_main {
	public static Logger logger_; // Logger object
	public static FileHandler fileHandler_; // FileHandler object

	void subscriptLogToFile(){

	};

	static HashMap log = new HashMap<>();

	/**
	 * @param args
	 *            Command line arguments. The first (optional) argument
	 *            specifies the problem to solve.
	 * @throws JMException
	 * @throws IOException
	 * @throws SecurityException
	 *             Usage: three options - jmetal.metaheuristics.moead.MOEAD_main
	 *             - jmetal.metaheuristics.moead.MOEAD_main problemName -
	 *             jmetal.metaheuristics.moead.MOEAD_main problemName
	 *             ParetoFrontFile
	 * @throws ClassNotFoundException
	 *
	 */



	static String functionname(String name){
		String Functionname = "boid";
		try(BufferedReader br = new BufferedReader(new FileReader(name))){
		String line;
		int counter=0;

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


	//　arg1 = Scalarfunctionname, arg2 = Problem Name, arg3 =
	public static void main(String[] args) throws JMException, SecurityException, IOException, ClassNotFoundException {
		experiment_setting("setting/MultiIslandMOEAD.st");
		String empty;

		int NumberOfRun = 100;


		String algorithmName = "N+N";
		String Problemname = args[1];
		String nobk = args[2];
		String nod = args[3];

		int numberOfObj = Integer.parseInt(nobk);
		int  numberOfdivision = Integer.parseInt(nod);
		int numberOfIsland = Integer.parseInt( (String) experiment_setting_.get("NumberOfIsland") );
		String[] FunctionNameSet = new String[numberOfIsland];
		String[] ComparatorNameSet = new String[numberOfIsland];
		double[] param = new double[numberOfIsland];

		for(int i=0;i<numberOfIsland;i++){
			String name = "Function" + String.valueOf(i+1);
			FunctionNameSet[i] = (String) experiment_setting_.get(name)  ;
			name = "Comparator" + String.valueOf(i+1);
			ComparatorNameSet[i] = (String) experiment_setting_.get(name) ;
			name = "Parameter" + String.valueOf(i+1);
			param[i] = Double.parseDouble((String) experiment_setting_.get(name));
		}


        Problem problem = null; // The problem to solve
		Algorithm algorithm; // The algorithm to use
		Operator crossover; // Crossover operator
		Operator mutation; // Mutation operator

		HashMap parameters; // Operator parameters



		// Logger object and file to store log messages



		if(experiment_setting_.containsKey("NumberOfrun")){
			NumberOfRun = Integer.parseInt((String)experiment_setting_.get("NumberOfrun"));
		}

		HashMap d = new HashMap();
		d.put("numberOfObjectives",numberOfObj);
//		problem = MOPFactory.getMOP(Problemname,d,"MOEAD");
		algorithm = AlgorithmFactory.getAlgorithm(algorithmName, problem);

		algorithm.setInputParameter("param",param);
		algorithm.setInputParameter("numberOfObjectives",numberOfObj);
		String problemname = problem.getName();
		String FuntionName = "func";
		algorithm.setInputParameter("numberOfDivision", numberOfdivision);


		String expro =   nobk+"OBJ" + nod  + "div"  ;
		File newDir = new File("result");
		newDir.mkdir();
		newDir = new File("result/MultiIslandMOEAD");
		newDir.mkdir();
		newDir = new File("result/MultiIslandMOEAD/" + problemname);
		newDir.mkdir();
		newDir = new File("result/MultiIslandMOEAD/" + problemname + "/" +expro);
		newDir.mkdir();
		newDir = new File("result/MultiIslandMOEAD/" + problemname + "/" +expro +"/"+ FuntionName);
		newDir.mkdir();
		algorithm.setInputParameter("directoryname",  "result/MultiIslandMOEAD/" + problemname + "/" +expro +"/"+ FuntionName);

		String name_di = "result/MultiIslandMOEAD/" + problemname + "/" +expro+"/"+ FuntionName  + "/" + "Setting" ;
		newDir = new File(name_di);
		newDir.mkdir();
		for(int i=0;i<numberOfIsland;i++){
			newDir = new File("result/MultiIslandMOEAD/" + problemname + "/" +expro +"/"+ FuntionName+ "/Func" + String.valueOf(i+1));
			newDir.mkdir();
			newDir = new File("result/MultiIslandMOEAD/" + problemname + "/" +expro +"/"+ FuntionName + "/Func" + String.valueOf(i+1) + "/FinalFUN");
			newDir.mkdir();
			newDir = new File("result/MultiIslandMOEAD/" + problemname + "/" +expro +"/"+ FuntionName + "/Func" + String.valueOf(i+1)+ "/FinalVAR");
			newDir.mkdir();
			newDir = new File("result/MultiIslandMOEAD/" + problemname + "/" +expro  +"/"+ FuntionName +"/Func" + String.valueOf(i+1)+ "/InitialFUN");
			newDir.mkdir();
			newDir = new File("result/MultiIslandMOEAD/" + problemname + "/" +expro +"/"+ FuntionName  + "/Func" +String.valueOf(i+1) +"/InitialVAR");
			newDir.mkdir();

			newDir = new File("result/MultiIslandMOEAD/" + problemname + "/" +expro +"/"+ FuntionName+ "/Func" + String.valueOf(i+1)+ "/FinalFUN/All");
			newDir.mkdir();
			newDir = new File("result/MultiIslandMOEAD/" + problemname + "/" +expro +"/"+ FuntionName + "/Func" + String.valueOf(i+1)+ "/FinalFUN/Feasible");
			newDir.mkdir();
			newDir = new File("result/MultiIslandMOEAD/" + problemname + "/" +expro +"/"+ FuntionName + "/Func" + String.valueOf(i+1)+ "/FinalFUN/Infeasible");
			newDir.mkdir();
			newDir = new File("result/MultiIslandMOEAD/" + problemname + "/" +expro  +"/"+ FuntionName+ "/Func" + String.valueOf(i+1)+ "/InitialFUN/All");
			newDir.mkdir();
			newDir = new File("result/MultiIslandMOEAD/" + problemname + "/" +expro  +"/"+ FuntionName+ "/Func" + String.valueOf(i+1) + "/InitialFUN/Feasible");
			newDir.mkdir();
			newDir = new File("result/MultiIslandMOEAD/" + problemname + "/" +expro  +"/"+ FuntionName+ "/Func" + String.valueOf(i+1) + "/InitialFUN/Infeasible");
			newDir.mkdir();
		}

		logger_ = Configuration.logger_;

		newDir.mkdir();

		empty = (String) experiment_setting_.get("maxEvaluations");
		algorithm.setInputParameter("maxEvaluations", Integer.parseInt(empty));
		empty = (String) experiment_setting_.get("alphar");
		algorithm.setInputParameter("alphar", Double.parseDouble(empty));
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
		empty = (String) experiment_setting_.get("Neighborhood");
		algorithm.setInputParameter("sizeOfNeiborhoodRepleaced_",Integer.parseInt(empty));
		algorithm.setInputParameter("sizeOfMatingNeiborhood_", Integer.parseInt(empty));
		algorithm.setInputParameter("numberOfIsland", Integer.parseInt((String)experiment_setting_.get("NumberOfIsland")));
/*
		if (numberOfObj ==3 ){
			algorithm.setInputParameter("sizeOfNeiborhoodRepleaced_",9 );
			algorithm.setInputParameter("sizeOfMatingNeiborhood_",9);
		} else if (numberOfObj ==5 ){
			algorithm.setInputParameter("sizeOfNeiborhoodRepleaced_",21);
			algorithm.setInputParameter("sizeOfMatingNeiborhood_",21);
		} else if (numberOfObj ==8 ){
			algorithm.setInputParameter("sizeOfNeiborhoodRepleaced_",15);
			algorithm.setInputParameter("sizeOfMatingNeiborhood_",15);
		} else if (numberOfObj ==10 ){
			algorithm.setInputParameter("sizeOfNeiborhoodRepleaced_",27);
			algorithm.setInputParameter("sizeOfMatingNeiborhood_",27);
		}
*/
		// Crossover operator
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




		algorithm.setInputParameter("ismax", Boolean.valueOf((String)(experiment_setting_.get("isMax"))));
        algorithm.setInputParameter("ScalarzingFunctionName", FunctionNameSet);

        algorithm.addOperator("crossover", crossover);
        algorithm.addOperator("mutation", mutation);



		SettingWriter.clear();
		SettingWriter.merge(algorithm.getAllmap());
		SettingWriter.merge(experiment_setting_);
		SettingWriter.merge(mutation.getMap());
		SettingWriter.add("Problemname",Problemname);
		SettingWriter.merge(crossover.getMap());
		SettingWriter.write(name_di);

		long initTime = System.currentTimeMillis();

		int counter=0;
		Random.set_seed(56);
		do {
			counter++;

			algorithm.setInputParameter("times", counter);
			System.out.println(FuntionName +":"+ counter + "回目開始");
			algorithm.execute();
			System.out.println(FuntionName +":"+ counter + "回目終了");

		} while(counter<NumberOfRun);

		long estimatedTime = System.currentTimeMillis() - initTime;
		logger_.info("Total execution time: " + estimatedTime + "ms");
		logger_.info("Objectives values have been writen to file FUN");
		logger_.info("Variables values have been writen to file VAR");

	}
} // MOEAD_main
