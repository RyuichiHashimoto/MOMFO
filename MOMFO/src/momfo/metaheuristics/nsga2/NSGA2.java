package momfo.metaheuristics.nsga2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.NameNotFoundException;
import javax.naming.NamingException;

import lib.experiments.CommandSetting;
import lib.experiments.ParameterNames;
import lib.experiments.Exception.CommandSetting.notFoundException;
import lib.lang.Generics;
import momfo.Indicator.IGD.IGDCalclator;
import momfo.Indicator.IGD.IGDRef;
import momfo.core.GeneticAlgorithm;
import momfo.core.Operator;
import momfo.core.Population;
import momfo.core.ProblemSet;
import momfo.core.Solution;
import momfo.util.JMException;
import momfo.util.Comparator.NSGAIIComparator.NSGAIIComparator;
import momfo.util.Ranking.NDSRanking;

public class NSGA2 extends GeneticAlgorithm {



	private int populationSize_;

	private Population offSpring_;

	private Population merge_;

	int evaluations_;

	Operator crossover_;
	Operator mutation_;

	int maxEvaluations_;

	int sizeOfNeiborhoodRepleaced_;
	int sizeOfMatingNeiborhood_;

	List<double[]> igdHistory;
	private NSGAIIComparator NSGAIIComparator_;

	public void initPopulation() throws JMException, ClassNotFoundException {
		population_ = new Population(populationSize_);
		for (int i = 0; i < populationSize_; i++) {
			Solution newSolution = new Solution(problem_, random);
			problem_.repair(newSolution, null);
			evaluations_++;
			problem_.evaluate(newSolution);
			population_.add(newSolution);
		}
	}

	@Override
	protected void buildImpl(CommandSetting setting)
			throws ReflectiveOperationException, NamingException, IOException, notFoundException {
		evaluations_ = 0;
		maxEvaluations_ = setting.get(ParameterNames.N_OF_EVALUATIONS);
		isMAX_ = setting.getAsBool(ParameterNames.IS_MAX);
		//		comparator_binary = new NSGAIIComparatorWithRandom(parameters);
		tasknumber = setting.get(ParameterNames.TASK_NUMBER);

		problem_ = ((ProblemSet) (setting.get(ParameterNames.PROBLEM_SET)))
				.get(setting.get(ParameterNames.TASK_NUMBER));
		populationSize_ = setting.getAsInt(ParameterNames.POPULATION_SIZE);

		NSGAIIComparator_ = Generics.cast(setting.getAsInstanceByName(ParameterNames.NSGAIIComparator, ""));;
		setting.putForce(ParameterNames.NSGAIIComparator, NSGAIIComparator_);
		NSGAIIComparator_.build(setting);
	}

	public void initialize(int seed) throws ClassNotFoundException, JMException {
		super.initialize(seed);

		initPopulation();

		merge_ = new Population(populationSize_ * 2);
		igdHistory = new ArrayList<double[]>();
		double[] igd = new double[2];
		int counter = 0;
		NDSRanking ranking = new NDSRanking(isMAX_, random);
		ranking.setPop(population_);
		ranking.Ranking();
		for (int i = 0; i < ranking.getworstrank(); i++) {
			CrowdingDistance.calc(ranking.get(i));
		}
		population_.printObjectivesToFile("output_InitialFUN.dat");
		ranking = null;
		igd[0] = counter;
		igd[1] = (IGDCalclator.CalcNormalizeIGD_To_NonDominated(population_.getAllObjectives(),
				IGDRef.getNormalizeRefs(tasknumber), IGDRef.getMaxValue(tasknumber), IGDRef.getMinValue(tasknumber),
				random));
		//		igd[1] = (IGDCalclator.CalcIGD(population_.getAllObjectives(), IGDRef.getRefs(tasknumber)));

		igdHistory.add(igd.clone());

	}

	@Override
	public void recombination() throws JMException {
		offSpring_ = new Population(populationSize_);
		Solution[] parents = new Solution[2];
		Solution[] offspring;

		for (int i = 0; i < populationSize_; i++) {

			int one = parentsSelection.selection(population_);
			int two = parentsSelection.selection(population_);

			parents[0] = population_.get(one);
			parents[1] = population_.get(two);

			offspring = crossover.crossover(parents[0], parents[1]);

			offspring[0] = mutation.mutation(offspring[0]);
			problem_.repair(offspring[0], null);
			problem_.evaluate(offspring[0]);

			evaluations_ = evaluations_ + 1;

			offSpring_.add(offspring[0]);

			if (evaluations_ == maxEvaluations_) {
				return;
			}
		}
		return;
	}

	@Override
	public void nextGeneration() throws JMException, NameNotFoundException {

		merge_.clear();
		merge_.merge(population_);
		merge_.merge(offSpring_);



		NDSRanking ranki_ = new NDSRanking(false, random);
		ranki_.setPop(population_);
		ranki_.Ranking();
		double igd = (IGDCalclator.CalcNormalizeIGD_To_NonDominated(population_.getAllObjectives(),
				IGDRef.getNormalizeRefs(tasknumber), IGDRef.getMaxValue(tasknumber), IGDRef.getMinValue(tasknumber),
				random));

		setOutputParameter("IGDCalclator", igd);
	}

	@Override
	public boolean terminate() {
		return evaluations_ == maxEvaluations_;
	}

	@Override
	public int getEvaluations() {
		return evaluations_;
	}

	@Override
	public int getGeneration() {
		return evaluations_ / populationSize_;
	}

	@Override
	public Population getPopulation() {
		return population_;
	}

	@Override
	public Population execute() throws JMException, ClassNotFoundException {
		return null;
	}

	public NSGA2() {
		super();
		isMultitask = false;
	}


}
