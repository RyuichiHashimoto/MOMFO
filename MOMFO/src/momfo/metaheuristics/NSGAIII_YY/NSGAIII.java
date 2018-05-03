package momfo.metaheuristics.NSGAIII_YY;

import momfo.core.Algorithm;
import momfo.core.Operator;
import momfo.core.Population;
import momfo.core.Problem;
import momfo.core.Solution;
import momfo.util.JMException;
import momfo.util.Niching;
import momfo.util.Ranking.NDSRanking;
import momfo.util.vector.TwoLevelWeightVectorGenerator;
import momfo.util.vector.VectorGenerator;

public class NSGAIII extends Algorithm {

	private int populationSize_;

	private int div1_;
	private int div2_;

	private Population population_;
	private Population offspringPopulation_;
	private Population union_;

	int generations_;

	Operator crossover_;
	Operator mutation_;
	Operator selection_;

	double[][] lambda_; // reference points

	boolean normalize_; // do normalization or not


	public NSGAIII(Problem problem) {
		super(problem);
	} // MOMFEA

	public Population execute() throws JMException, ClassNotFoundException {
		int maxGenerations_;

		generations_ = 0;

		maxGenerations_ = ((Integer) this.getInputParameter("maxGenerations"))
				.intValue();

		div1_ = ((Integer) this.getInputParameter("div1")).intValue();

		div2_ = ((Integer) this.getInputParameter("div2")).intValue();


		normalize_ = ((Boolean) this.getInputParameter("normalize")).booleanValue();

		VectorGenerator vg = new TwoLevelWeightVectorGenerator(div1_, div2_,
				problem_.getNumberOfObjectives());
		lambda_ = vg.getVectors();

		populationSize_ = vg.getVectors().length;
		if (populationSize_ % 2 != 0)
			populationSize_ += 1;


		mutation_ = operators_.get("mutation");
		crossover_ = operators_.get("crossover");
		selection_ = operators_.get("selection");

		initPopulation();

		while (generations_ < maxGenerations_) {
			offspringPopulation_ = new Population(populationSize_);
			Solution[] parents = new Solution[2];
			for (int i = 0; i < (populationSize_ / 2); i++) {
				if (generations_ < maxGenerations_) {
					// obtain parents

					parents = (Solution[]) selection_.execute(population_);

					Solution[] offSpring = (Solution[]) crossover_
							.execute(parents);

					mutation_.execute(offSpring[0]);
					mutation_.execute(offSpring[1]);

					problem_.evaluate(offSpring[0]);
//					problem_.evaluateConstraints(offSpring[0]);
					problem_.evaluate(offSpring[1]);
//					problem_.evaluateConstraints(offSpring[1]);

					offspringPopulation_.add(offSpring[0]);
					offspringPopulation_.add(offSpring[1]);

				} // if
			} // for

			union_ = new Population(populationSize_*2);
			union_.merge(population_);
			union_.merge(offspringPopulation_);

			// Ranking the union
	//		Ranking ranking = new NondominatedRanking(union_);
			NDSRanking ranking = new NDSRanking(false);
			ranking.setPop(union_);
			int remain = populationSize_;
			int index = 0;
			population_.clear();

			// Obtain the next front
			Population front = ranking.get(index);

			while ((remain > 0) && (remain >= front.size())) {

				for (int k = 0; k < front.size(); k++) {
					population_.add(front.get(k));
				} // for

				// Decrement remain
				remain = remain - front.size();

				// Obtain the next front
				index++;
				if (remain > 0) {
					front = ranking.get(index);
				} // if
			}

			if (remain > 0) { // front contains individuals to insert

				new Niching(population_, front, lambda_, remain, normalize_)
						.execute();
				remain = 0;
			}

			generations_++;

		}

		return population_;

	}

	public void initPopulation() throws JMException, ClassNotFoundException {

		population_ = new Population(populationSize_);

		for (int i = 0; i < populationSize_; i++) {
			Solution newSolution = new Solution(problem_);

			problem_.evaluate(newSolution);
//			problem_.evaluateConstraints(newSolution);

			population_.add(newSolution);
		} // for
	} // initPopulation


} // NSGA-III

