//  MOEAD.java
//
//  Author:
//       Antonio J. Nebro <antonio@lcc.uma.es>
//       Juan J. Durillo <durillo@lcc.uma.es>
//
//  Copyright (c) 2011 Antonio J. Nebro, Juan J. Durillo
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

package momfo.metaheuristics.moead;

import java.io.IOException;
import java.util.Vector;

import javax.naming.NamingException;

import lib.experiments.CommandSetting;
import lib.experiments.ParameterNames;
import lib.experiments.Exception.CommandSetting.notFoundException;
import lib.lang.Generics;
import lib.lang.NotVerifiedYet;
import lib.math.Calculator;
import lib.math.Permutation;
import momfo.core.GeneticAlgorithm;
import momfo.core.Operator;
import momfo.core.Population;
import momfo.core.ProblemSet;
import momfo.core.Solution;
import momfo.util.JMException;
import momfo.util.Neiborhood;
import momfo.util.WeightedVector;
import momfo.util.Comparator.MOEADComparator.MOEADComparator;
import momfo.util.ScalarzingFunction.ScalarzingFunction;
public class MOEAD extends GeneticAlgorithm{
	public MOEAD() {
		super();
		isMultitask = false;
	}


	/**
	 * Stores the population size
	 */
	private MOEADComparator comparator;


	private double alpha = 1.0;

	/**
	 * Stores the population size
	 */
	private int populationSize_;
	/**
	 * Z vector (ideal point)
	 */
	double[] ReferencePoint_;
	/**
	 * Lambda vectors
	 */
	// Vector<Vector<Double>> lambda_ ;
	static double[][] WeightedVector_;

	private int numberofObjectives_;
	private int numberOfDivision_;


	static int[][] neighborhood_;

	int numberOfParents_;
	boolean isNorm;
	Solution[] indArray_;
	int evaluations_;

	int maxGeneration_;
	int InnerWeightVectorDivision_;
	boolean isInnerWeightVector_;
	/**
	 * Operators
	 */
	Operator crossover_;
	Operator mutation_;


	int sizeOfNeiborhoodRepleaced_;
	int sizeOfMatingNeiborhood_;

	ScalarzingFunction ScalarzingFunction_;

	String directoryname;
	String ScalarzingFunctionName;
	int maxEvaluations;
	boolean isMax;
	int time;


	public Population execute() throws JMException, ClassNotFoundException {
		return null;

	}



	public void setNeighborhood() throws JMException{

		Neiborhood a = new Neiborhood();
		double[][] weight,weight_one;

		weight_one = WeightedVector.getWeightedVector(numberofObjectives_,numberOfDivision_);

		if (isInnerWeightVector_){
			double[][] weightinner = WeightedVector.getWeightedVector(numberofObjectives_,InnerWeightVectorDivision_);
			WeightedVector.getinnerWeightVector(weightinner);
			weight = 	WeightedVector.conbine(weight_one, weightinner);
		} else {
			weight = weight_one;
		}

		a.setWeightedVector(weight);
		a.setNeiborhood(Math.max(sizeOfNeiborhoodRepleaced_,sizeOfMatingNeiborhood_),random);
		neighborhood_ = a.getNeiborhood();
		WeightedVector_       = a.getWeight();
	}
	public void initPopulation() throws JMException, ClassNotFoundException {
 		for (int i = 0; i < populationSize_; i++) {
			Solution newSolution = new Solution(problem_,random);
			try {
				newSolution = new Solution(problem_, random);
				initialization.initialize(newSolution);
			} catch(NotVerifiedYet e){				
				throw new JMException(e.getClass().getName() + "   "+e.getMessage());
			}
			
			problem_.repair(newSolution,null);
			problem_.evaluate(newSolution);
			
			
			solEvaluator[taskNumber].evaluate(newSolution);

			evaluations_++;
			population_.add((newSolution));
		}
	}

	public void initReferencePoint() throws ClassNotFoundException, JMException{
		Solution a = population_.get(0);

		for (int n = 0; n < problem_.getNumberOfObjectives(); n++) {
				ReferencePoint_[n] =  a.getObjective(n);
				indArray_[n] = a;
		}
		for (int i = 1; i < populationSize_; i++) {
			updateReference(population_.get(i));
		} // for
	}

	public void matingSelection(Vector<Integer> list, int cid, int size){
		int ss;
		int r;
		int p;
		boolean flag;
		ss = sizeOfMatingNeiborhood_;

//		System.out.println(sizeOfMatingNeiborhood_ + " 	");
		while (list.size() < size) {

				r = random.nextIntIE(ss);
				p = neighborhood_[cid][r];

				// p = population[cid].table[r];
			flag = true;
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i) == p) // p is in the list
				{
					flag = false;
					break;
				}
			}
			if (flag) {
				list.addElement(p);
			}
		}
	}

	public void matingSelection_without_replacement(Vector<Integer> list, int cid, int size) {
		int ss;
		int r;
		int p;
		ss = sizeOfMatingNeiborhood_;
		while (list.size() < size) {

				r = random.nextIntIE(ss);
				p = neighborhood_[cid][r];
				// p = population[cid].table[r];
				list.addElement(p);
		}
	}



	void updateReference(Solution individual) {
		for (int n = 0; n < problem_.getNumberOfObjectives(); n++) {
			if (comparator.compare(individual.getObjective(n) ,indArray_[n].getObjective(n))) {
				ReferencePoint_[n] =  alpha*individual.getObjective(n);
				indArray_[n] = individual;
			}
		}
	}

	void updateProblem(Solution indiv, int id) throws JMException {
		int size;

		size = sizeOfNeiborhoodRepleaced_;
		int[] perm = new int[size];

		// generate teh random permutation.
		Permutation.randomPermutation(perm, size,random);


		for (int i = 0; i < size; i++) {
			int k;
			k = neighborhood_[id][perm[i]];
			comparator.setWeightedVector(WeightedVector_[k]);
			comparator.setRefernecePoint(ReferencePoint_);
			if (comparator.execute(indiv, population_.get(k)) == 1) {
				population_.replace(k, (indiv));
			} else {
			}

		}

	} // updateProblem


	public void initialize(int seed) throws ClassNotFoundException, JMException {
		super.initialize(seed);

		setNeighborhood();


		initPopulation();

		initReferencePoint();

		population_.printVariablesToFile("initialVAR" + time + ".dat");
		population_.printObjectivesToFile("InitialFUN" + time + ".dat");

		permutation = new int[populationSize_];
		Permutation.randomPermutation(permutation,populationSize_,random);

	}


	@Override
	public void recombination() throws JMException {
		Solution offSpring[];

		for (int i = 0; i < populationSize_; i++) {
			int n = permutation[i]; // or int n = i;

			// STEP 2.1. Mating selection
			Vector<Integer> parentsNumber = new Vector<Integer>();

			matingSelection(parentsNumber, n, numberOfParents_);

			Solution[] parents = new Solution[numberOfParents_];

			for(int k=0;k<numberOfParents_;k++){
				 parents[k] = population_.get(parentsNumber.get(k));
			}

			offSpring =crossover.crossover(parents[0],parents[1]);



			offSpring[0] = mutation.mutation(offSpring[0]);


//			problem_.repair(offSpring[0],null);
//			problem_.evaluate(offSpring[0]);
			solEvaluator[taskNumber].evaluate(offSpring[0]);
			
			evaluations_++;

			updateReference(offSpring[0]);


			// STEP 2.5. Update of solutions
			updateProblem(offSpring[0], n);

			if(terminate()) {
				break;
			}
		}
	}

	@Override
	public void nextGeneration() throws JMException {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public boolean terminate() {
		return evaluations_ == maxEvaluations;
	}

	@Override
	public int getEvaluations() {
		return evaluations_;
	}

	@Override
	public int getGeneration() {
		return evaluations_/populationSize_;
	}

	@Override
	public Population getPopulation() {
		return population_;
	}

	protected int [] permutation;
	@Override
	protected void buildImpl(CommandSetting s) throws JMException, notFoundException, NamingException, ReflectiveOperationException, IOException {

		problem_ = ((ProblemSet) (setting.get(ParameterNames.PROBLEM_SET)))
				.get(setting.get(ParameterNames.TASK_NUMBER));

		ScalarzingFunctionName = s.get(ParameterNames.SCALAR_FUNCTION);
		maxEvaluations = s.get(ParameterNames.N_OF_EVALUATIONS);

		numberOfParents_ = s.get(ParameterNames.N_OF_PARENTS);

		numberOfDivision_    = s.get(ParameterNames.OUTER_DIVISION_SIZE);
		numberofObjectives_    = problem_.getNumberOfObjectives();

		isMax    = s.get(ParameterNames.IS_MAX);


		finEvaluator[taskNumber].build(setting);
		evoEvaluator[taskNumber].build(setting);
		ScalarzingFunction_ = Generics.cast(s.getAsInstanceByName(ParameterNames.SCALAR_FUNCTION, ""));;
		s.putForce(ParameterNames.SCALAR_FUNCTION, ScalarzingFunction_);
		Object[] temp = Generics.cast(s.getAsInstanceArray(ParameterNames.MOEAD_COMPARATOR, ""));
		comparator =  ( Generics.cast(temp[taskNumber]) );


		ScalarzingFunction_.build(s);
		comparator.build(s);


		evaluations_ = 0;
		alpha = s.get(ParameterNames.MOEAD_ALPHA);
		sizeOfNeiborhoodRepleaced_ = s.get(ParameterNames.SIZE_OF_NEIBORHOOD_At_UPDATE);
		sizeOfMatingNeiborhood_    = s.get(ParameterNames.SIZE_OF_NEIBORHOOD_At_MATING);
		InnerWeightVectorDivision_ = s.get(ParameterNames.INNER_DIVISION_SIZE);
		isInnerWeightVector_ = ((InnerWeightVectorDivision_ > 0));

		populationSize_ = Calculator.conbination(numberofObjectives_-1 + numberOfDivision_ ,numberofObjectives_-1);

		taskNumber =  s.get(ParameterNames.TASK_NUMBER);

		if(isInnerWeightVector_){
			populationSize_ += Calculator.conbination(numberofObjectives_-1 + InnerWeightVectorDivision_ ,numberofObjectives_-1);
		}

		isNorm = s.getAsBool(ParameterNames.IS_NORM);
		comparator.set(isMax);

		population_ = new Population(populationSize_);
		indArray_ = new Solution[problem_.getNumberOfObjectives()];
		ReferencePoint_ = new double[problem_.getNumberOfObjectives()];
		evaluations_ = 0;
	}





} // MOEAD

