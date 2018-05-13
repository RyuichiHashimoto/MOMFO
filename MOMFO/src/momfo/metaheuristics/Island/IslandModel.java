package momfo.metaheuristics.Island;

import java.util.ArrayList;
import java.util.List;

import lib.io.output.fileSubscription;
import momfo.Indicator.IGD.IGDCalclator;
import momfo.Indicator.IGD.IGDRef;
import momfo.core.GeneticAlgorithm;
import momfo.core.Operator;
import momfo.core.Population;
import momfo.core.Solution;
import momfo.operators.selection.ParentsSelection.BinaryTournament;
import momfo.operators.selection.ParentsSelection.ParentsSelection;
import momfo.operators.selection.environmentalselection.EnvironmentalSelection;
import momfo.operators.selection.environmentalselection.LabSpecifiedNSGAIISelection;
import momfo.util.JMException;
import momfo.util.Comparator.NSGAIIComparator.NSGAIIComparatorWithRandom;

public class IslandModel extends GeneticAlgorithm {

	public IslandModel() {
		super();
		isMultitask = true;		
	}
	
	private int[] populationSize;

	private Population[] population;

	private Population[] currentArchive;

	private Population[] offSpring;

	int numberOfParents;

	int generation;

	int[] evaluations;

	int[] maxEvaluations;

	private boolean []isMax;

	Operator crossover;

	Operator mutation;

	EnvironmentalSelection[] environmentalSelection;

	ParentsSelection[] parantsSelection;

	private double rmp; // this is the only Charasteristic parameter of this algorithm

	int time;


	String directoryName;

	double[] igd = new double[2];

	List<List<double[]>> igdHistory = new ArrayList<List<double[]>>();

	@Override
	public Population execute() throws JMException, ClassNotFoundException {

		population = new Population[problemSet_.countProblem()];
		offSpring = new Population[problemSet_.countProblem()];
		String name = problemSet_.getProblemName();

		System.out.print(name + " ");


		setting();

		initPopulation();

		CalclateIGD(generation++);


		do {
			UpdateArchive();
			for(int t= 0; t < problemSet_.countProblem();t++){
				makeOffspring(t);
			}
			for(int t= 0; t < problemSet_.countProblem();t++){
				enviromnentalSelection(t);
			}
			CalclateIGD(generation++);
		} while(check());

		for(int t=0;t < problemSet_.countProblem();t++){
			fileSubscription. printToFile(directoryName.replace("Task1", "Task"+String.valueOf(t+1)) + "/IGDHistory/"+"IGDCalclator"+time+".dat",igdHistory.get(t));
			population[t].printVariablesToFile(directoryName.replace("Task1", "Task"+String.valueOf(t+1))  + "/FinalVAR/FinalVAR" + time + ".dat");
			population[t].printObjectivesToFile(directoryName.replace("Task1", "Task"+String.valueOf(t+1))  +  "/FinalFUN/FinalFUN" + time + ".dat");
		}

		return null;
	}


	private void CalclateIGD(int generation){
		for(int t = 0; t < problemSet_.countProblem();t++){
			CalclateIGD(t,generation);
		}
	}

	private void CalclateIGD(int taskNumber,int generation){
		igd[0] = generation;
		igd[1] = (IGDCalclator.CalcNormalizeIGD(population[taskNumber].getAllObjectives(), IGDRef.getNormalizeRefs(taskNumber),IGDRef.getMaxValue(taskNumber),IGDRef.getMinValue(taskNumber)));
		igdHistory.get(taskNumber).add(igd.clone());
	}

	private void UpdateArchive(){
		for(int t = 0; t< problemSet_.countProblem();t++){
			currentArchive[t] = population[t];
		}
	}

	private boolean check(){
		for(int t = 0; t < problemSet_.countProblem();t++){
			if (maxEvaluations[t] != evaluations[t]){
				return true;
			}
		}
		return false;
	}

	public void initPopulation() throws JMException, ClassNotFoundException {
		population = new Population[problemSet_.countProblem()];
		offSpring = new Population[problemSet_.countProblem()];
		currentArchive = new Population[problemSet_.countProblem()];
		for(int  t = 0; t < problemSet_.countProblem(); t ++){
			population[t] = new Population(populationSize[t]);
			offSpring[t] = new Population(populationSize[t]);
			for(int p = 0 ; p < populationSize[t];p++){
				Solution newSolution = new Solution(problemSet_, t,random);
				problemSet_.get(t).evaluate(newSolution);
				evaluations[t]++;
				population[t].add(new Solution(newSolution));
			}
		}
	}

	public void makeOffspring(int taskNumber) throws JMException, ClassNotFoundException {

		offSpring[taskNumber].clear();
		Solution[] parents = new Solution[2];
		for(int p = 0; p  < populationSize[taskNumber];p++){
			if (evaluations[taskNumber] == maxEvaluations[taskNumber]) break;
			if(random.nextDoubleIE() < rmp){
				int parentTaskNumber;
				do {
					parentTaskNumber= random.nextIntIE(problemSet_.countProblem());
				} while(parentTaskNumber == taskNumber);
				int popIndex = random.nextIntIE(currentArchive[parentTaskNumber].size());
				parents[0] = currentArchive[parentTaskNumber].get(popIndex);
				parents[1] = population[taskNumber].get((Integer)parantsSelection[taskNumber].execute(population[taskNumber]));


				/*int popIndex = Random.nextIntIE(currentArchive[parentTaskNumber].size());
				parents[0] = currentArchive[parentTaskNumber].get(popIndex);
				int popIndex_2 = Random.nextIntIE(population[taskNumber].size());
				parents[1] = population[taskNumber].get(popIndex_2);*/
			} else{
/*				int popIndex = Random.nextIntIE(population[taskNumber].size());
				parents[0] = population[taskNumber].get(popIndex);
				int popIndex_2 = Random.nextIntIE(population[taskNumber].size());
				parents[1] = population[taskNumber].get(popIndex_2);
*/
				parents[0] = population[taskNumber].get((Integer)parantsSelection[taskNumber].execute(population[taskNumber]));
				parents[1] = population[taskNumber].get((Integer)parantsSelection[taskNumber].execute(population[taskNumber]));
			}

			Solution sol = (Solution) crossover.execute(parents);
			mutation.execute(sol);
			problemSet_.get(taskNumber).evaluate(sol);
			problemSet_.get(taskNumber).evaluate(sol);
			offSpring[taskNumber].add(sol);
			evaluations[taskNumber]++;
		}
	}

	public void enviromnentalSelection(int taskNumber) throws JMException {
		Population empty = new Population(populationSize[taskNumber] * 2);
		empty.merge(population[taskNumber]);
		empty.merge(offSpring[taskNumber]);
		population[taskNumber] =  environmentalSelection[taskNumber].getNextPopulation(empty);
	}

	public void setting() throws JMException{
		crossover = operators_.get("crossover"); // default: DE crossover
		mutation = operators_.get("mutation"); // default: polynomial mutation
		populationSize = new int[problemSet_.countProblem()];
		maxEvaluations = new int[problemSet_.countProblem()];
		evaluations = new int[problemSet_.countProblem()];
		parantsSelection = new ParentsSelection[problemSet_.countProblem()];
		time = ((Integer) this.getInputParameter("times")).intValue();
		isMax = new boolean[problemSet_.countProblem()];
		environmentalSelection = new EnvironmentalSelection[problemSet_.countProblem()];
		directoryName = ((String) this.getInputParameter("DirectoryName"));

		igdHistory = new ArrayList<List<double[]>>();

		for(int t = 0; t< problemSet_.countProblem();t++){
			List<double[]> d = new ArrayList<double[]>();
			igdHistory.add(new ArrayList<double []>(d));


			populationSize[t] = ((Integer) this.getInputParameter("populationSize" + String.valueOf(t+1))).intValue();
			maxEvaluations[t] = ((Integer) this.getInputParameter("maxEvaluation" + String.valueOf(t+1))).intValue();
			evaluations[t] = 0;
			parantsSelection[t] = new BinaryTournament(null,new NSGAIIComparatorWithRandom(null));


			isMax[t] = ((boolean)this.getInputParameter("IsMax"+String.valueOf(t+1)));
			environmentalSelection[t] = new LabSpecifiedNSGAIISelection(populationSize[t],isMax[t],null,random);

		}
		generation = 1;
		rmp = ((double)this.getInputParameter("rmp"));
	}
}
