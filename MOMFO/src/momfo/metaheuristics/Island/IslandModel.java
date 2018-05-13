package momfo.metaheuristics.Island;

import java.io.IOException;

import javax.naming.NameNotFoundException;
import javax.naming.NamingException;

import lib.experiments.CommandSetting;
import lib.experiments.ParameterNames;
import lib.experiments.Exception.CommandSetting.notFoundException;
import lib.lang.Generics;
import momfo.core.GeneticAlgorithm;
import momfo.core.Population;
import momfo.core.Solution;
import momfo.operators.selection.environmentalselection.EnvironmentalSelection;
import momfo.util.JMException;

public class IslandModel extends GeneticAlgorithm {

	public IslandModel() {
		super();
		isMultitask = true;
	}

	private int[] populationSize;

	private Population[] currentArchive;

	private Population[] offSpring;

	int numberOfParents;

	int[] evaluations;

	int[] maxEvaluations;

	int totalPopulatiolnSize;

	private boolean[] isMax;

	EnvironmentalSelection[] environmentalSelection;

	private double rmp; // this is the only Charasteristic parameter of this algorithm


	@Override
	public Population execute() throws JMException, ClassNotFoundException {
		return null;
	}


	private void UpdateArchive(){
		for (int t = 0; t < problemSet_.countProblem(); t++) {
			currentArchive[t] = populationArray[t];
		}
	}


	public void initPopulation() throws JMException, ClassNotFoundException {
		populationArray = new Population[problemSet_.countProblem()];
		offSpring = new Population[problemSet_.countProblem()];
		currentArchive = new Population[problemSet_.countProblem()];
		for (int t = 0; t < problemSet_.countProblem(); t++) {
			populationArray[t] = new Population(populationSize[t]);
			offSpring[t] = new Population(populationSize[t]);
			for (int p = 0; p < populationSize[t]; p++) {
				Solution newSolution = new Solution(problemSet_, t, random);
				problemSet_.get(t).evaluate(newSolution);
				evaluations[t]++;
				populationArray[t].add(new Solution(newSolution));
			}
		}
	}



	@Override
	public void recombination() throws JMException {
		for (int t = 0; t < problemSet_.countProblem(); t++) {
			offSpring[t].clear();
			Solution[] parents = new Solution[2];
			for (int p = 0; p < populationSize[t]; p++) {

				if (evaluations[t] == maxEvaluations[t])
					break;

				if (random.nextDoubleIE() < rmp) {
					int parentTaskNumber;
					do {
						parentTaskNumber = random.nextIntIE(problemSet_.countProblem());
					} while (parentTaskNumber == t);
					int popIndex = random.nextIntIE(currentArchive[parentTaskNumber].size());
					parents[0] = currentArchive[parentTaskNumber].get(popIndex);

					int two = parentsSelection.selection(populationArray[t]);
					parents[1] = populationArray[t].get(two);

					/*int popIndex = Random.nextIntIE(currentArchive[parentTaskNumber].size());
					parents[0] = currentArchive[parentTaskNumber].get(popIndex);
					int popIndex_2 = Random.nextIntIE(population[taskNumber].size());
					parents[1] = population[taskNumber].get(popIndex_2);*/
				} else {
					/*				int popIndex = Random.nextIntIE(population[taskNumber].size());
									parents[0] = population[taskNumber].get(popIndex);
									int popIndex_2 = Random.nextIntIE(population[taskNumber].size());
									parents[1] = population[taskNumber].get(popIndex_2);
					*/
					int one = parentsSelection.selection(populationArray[t]);
					int two = parentsSelection.selection(populationArray[t]);
					parents[0] = populationArray[t].get(one);
					parents[0] = populationArray[t].get(two);
				}

				Solution sol = crossover.crossover(parents)[0];
				sol = mutation.mutation(sol);
				problemSet_.get(t).evaluate(sol);
				offSpring[t].add(sol);
				evaluations[t]++;
			}
		}
	}

	public void enviromnentalSelection(int taskNumber) throws JMException {
		Population empty = new Population(populationSize[taskNumber] * 2);
		empty.merge(populationArray[taskNumber]);
		empty.merge(offSpring[taskNumber]);
		populationArray[taskNumber] = environmentalSelection[taskNumber].getNextPopulation(empty);
	}

	@Override
	public void nextGeneration() throws JMException, NameNotFoundException {
		for (int i = 0; i < problemSet_.countProblem(); i++) {
			enviromnentalSelection(i);
			;
		}
		UpdateArchive();
	}

	@Override
	public boolean terminate() {
		for (int t = 0; t < problemSet_.countProblem(); t++) {
			if (evaluations[t] == maxEvaluations[t])
				return false;
		}

		return true;
	}

	@Override
	public int getEvaluations() {
		int ret = 0;
		for (int i = 0; i < problemSet_.countProblem(); i++)
			ret += evaluations[i];

		return ret;
	}

	@Override
	public int getGeneration() {
		return getEvaluations() / totalPopulatiolnSize;
	}

	@Override
	public Population getPopulation() {
		Population ret =new Population(totalPopulatiolnSize);
		for(int i =0;i<problemSet_.countProblem();i++) {
			ret.merge(populationArray[i]);
		}
		return ret;
	}

	@Override
	protected void buildImpl(CommandSetting s)
			throws ReflectiveOperationException, NamingException, IOException, notFoundException, JMException {

		totalPopulatiolnSize = 200;

		populationSize = s.getAsNArray(ParameterNames.POPULATION_SIZE);;
		maxEvaluations = s.getAsNArray(ParameterNames.N_OF_EVALUATIONS);;
		evaluations = new int[problemSet_.countProblem()];
//		time = ((Integer) this.getInputParameter("times")).intValue();
		isMax = s.getAsBArray(ParameterNames.IS_MAX);;
		rmp =  s.getAsDouble(ParameterNames.RMP);

		 environmentalSelection = Generics.cast(s.getAsInstanceArrayByName(ParameterNames.ENVIROMN_SELECT));


//		directoryName = ((String) this.getInputParameter("DirectoryName"));

//		s.put(ParameterNames.TASK_NUMBER, );
		for (int t = 0; t < problemSet_.countProblem(); t++) {
			environmentalSelection[t].build(s);
			evaluations[t] = 0;
		}

	}
}
