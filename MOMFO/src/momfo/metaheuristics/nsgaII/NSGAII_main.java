package momfo.metaheuristics.nsgaII;

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
import momfo.util.Configuration;
import momfo.util.JMException;
import momfo.util.Random;

public class NSGAII_main {
	public static Logger logger_; // Logger object
	public static FileHandler fileHandler_; // FileHandler object

	void subscriptLogToFile(){

	};

	static HashMap log = new HashMap<>();





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
		// this value has the name of weight vector data
		experiment_setting("setting/MOMFEA.st");
		String empty;
		String Problemname = args[0];
		String nobk = args[1];

		int OBJ = Integer.parseInt(nobk);
		int NumberOfRun = 101;

	    Problem problem = null; // The problem to solve
	    Algorithm algorithm; // The algorithm to use
		Operator crossover; // Crossover operator
		Operator mutation; // Mutation operator

		HashMap parameters; // Operator parameters

		HashMap d = new HashMap();
		d.put("numberOfObjectives",OBJ);
//	      Problem problem = null; // The problem to solve
//		problem = MOPFactory.getMOP(Problemname,d,"MOMFEA");

		algorithm = new NSGAII(problem);

		// Logger object and file to store log messages

		logger_ = Configuration.logger_;

		algorithm = new NSGAII(problem);
		// algorithm = new MOEAD_DRA(problem);

		String dddname = problem.getNumberOfObjectives()  + "OBJ";

		//int populationsize = 100;
/*
		switch(problem.getNumberOfObjectives()){
			case 2:populationsize = 100; break;
			case 3:populationsize = 105; break;
			case 4:populationsize = 120; break;
			case 5:populationsize = 126; break;
			case 6:populationsize = 126; break;
			case 8:populationsize = 120; break;
			case 10:populationsize = 220; break;
			default:break;
		}
*/
		empty = (String) experiment_setting_.get("populationSize");
		algorithm.setInputParameter("populationSize", Integer.parseInt(empty));

		File newDir = new File("result");
		newDir.mkdir();
		newDir = new File("result/MOMFEA");
		newDir.mkdir();
		newDir = new File("result/MOMFEA/" + Problemname);
		newDir.mkdir();
		newDir = new File("result/MOMFEA/" + Problemname +"/"+dddname );
		newDir.mkdir();
		newDir = new File("result/MOMFEA/" + Problemname+"/"+dddname+"/FinalFUN");
		newDir.mkdir();
		newDir = new File("result/MOMFEA/" + Problemname+"/"+dddname+"/InitialFUN");
		newDir.mkdir();
		newDir = new File("result/MOMFEA/" + Problemname+"/"+dddname+"/FinalVAR");
		newDir.mkdir();
		newDir = new File("result/MOMFEA/" + Problemname+"/"+dddname+"/InitialVAR");
		newDir.mkdir();
		algorithm.setInputParameter("DirectoryName",  "result/MOMFEA/" + Problemname+"/"+dddname);
		empty = (String) experiment_setting_.get("populationSize");
		String directory = "result/MOMFEA/" + Problemname+"/"+dddname + "/Setting";
		newDir = new File(directory);
		newDir.mkdir();


		//algorithm.setInputParameter("populationSize", Integer.parseInt(empty));

		empty = (String) experiment_setting_.get("maxEvaluations");
		algorithm.setInputParameter("maxEvaluations", Integer.parseInt(empty));


		algorithm.setInputParameter("numberOfParents", 2);



		parameters = new HashMap();


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
		algorithm.setInputParameter("ismax", Boolean.valueOf((String)(experiment_setting_.get("isMax"))));
		algorithm.setInputParameter("isNorm", Boolean.valueOf((String)(experiment_setting_.get("isNorm"))));


		// Execute the Algorithm

		Population max = new Population();
		Population population = new Population();

		SettingWriter.clear();
		SettingWriter.merge(algorithm.getAllmap());
		SettingWriter.merge(mutation.getMap());
		SettingWriter.add("numberOfvariable",problem.getNumberOfVariables());
		SettingWriter.add("numberOfObjective",problem.getNumberOfObjectives());
		SettingWriter.add("Problemname",Problemname);
		SettingWriter.add("mutation",mutation.getName());
		SettingWriter.add("numberOfObjective",problem.getNumberOfObjectives());
		SettingWriter.add("Problemname",Problemname);
		SettingWriter.merge(crossover.getMap());
		SettingWriter.write(directory);


		int counter=0;
		Random.set_seed(56);
		long initTime = System.currentTimeMillis();
		do {
			counter++;

			algorithm.setInputParameter("times", counter);
			System.out.println("MOMFEA" +":"+ counter + "回目開始");
			long innerinitTime = System.currentTimeMillis();
			algorithm.execute();
			long estimatedTime = System.currentTimeMillis() - innerinitTime;
			System.out.println("MOMFEA" +":"+ counter + "回目終了 実行時間"  + estimatedTime + "ms" );

		} while(counter<NumberOfRun);

		long estimatedTime = System.currentTimeMillis() - initTime;

		// Result messages
		logger_.info("Total execution time: " + estimatedTime /1000 + "ms");
		logger_.info("Objectives values have been writen to file FUN");
		//DominancedSolutionSet.printObjectivesToFile("DOMINANCEEDFUN");

		logger_.info("Variables values have been writen to file VAR");
		//DominancedSolutionSet.printVariablesToFile("DOMINANCEEDVAR");

	}
} // MOEAD_main
