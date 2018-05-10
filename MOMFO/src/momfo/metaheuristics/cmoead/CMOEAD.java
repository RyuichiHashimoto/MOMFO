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

package momfo.metaheuristics.cmoead;

import java.util.HashMap;
import java.util.Vector;

import experiments.SettingWriter;
import lib.math.Calculator;
import lib.math.Permutation;
import momfo.core.Algorithm;
import momfo.core.Operator;
import momfo.core.Population;
import momfo.core.Problem;
import momfo.core.Solution;
import momfo.util.JMException;
import momfo.util.Neiborhood;
import momfo.util.Random;
import momfo.util.WeightedVector;
import momfo.util.Comparator.MOEADComparator.ConstrainSumMOEADComparator;
import momfo.util.Comparator.MOEADComparator.MOEADComparator;
import momfo.util.ScalarzingFunction.ScalarzingFunction;
import momfo.util.ScalarzingFunction.ScalarzingFunctionFactory;
public class CMOEAD extends Algorithm {


	/**
	 * Stores the population size
	 */
	private MOEADComparator comparator;


	private double alpha = 1.1;

	/**
	 * Stores the population size
	 */
	private int populationSize_;
	/**
	 * Stores the population
	 */
	private Population population_;
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

	Solution[] indArray_;
	String functionType_;
	int evaluations_;


	int InnerWeightVectorDivision_;
	boolean isInnerWeightVector_;
	/**
	 * Operators
	 */
	Operator crossover_;
	Operator mutation_;


	int sizeOfNeiborhoodRepleaced_;
	int sizeOfMatingNeiborhood_;

	/**
	 * Constructor
	 *
	 * @param problem
	 *            Problem to solve
	 */
	public CMOEAD(Problem problem) {
		super(problem);
	} // DMOEA


	ScalarzingFunction ScalarzingFunction_;

	public Population execute() throws JMException, ClassNotFoundException {


		numberOfParents_= ((Integer) this.getInputParameter("numberOfParents")).intValue();
		String directoryname = ((String) this.getInputParameter("directoryname"));
		String ScalarzingFunctionName = (this.getInputParameter("ScalarzingFunctionName")).toString();
		int maxEvaluations = ((Integer) this.getInputParameter("maxEvaluations")).intValue();
		numberOfDivision_    = ((Integer)this.getInputParameter("numberOfDivision"));
		numberofObjectives_    = ((Integer)this.getInputParameter("numberOfObjectives"));
		boolean ismax    = ((boolean)this.getInputParameter("ismax"));
		int time = ((Integer) this.getInputParameter("times")).intValue();
		ScalarzingFunction_ = ScalarzingFunctionFactory.getScalarzingFunctionOperator(ScalarzingFunctionName,(double)this.getInputParameter("PBITheta"));
		functionType_ = ScalarzingFunction_.getFunctionName();
		comparator = new ConstrainSumMOEADComparator(null,ScalarzingFunction_);
		comparator.setIs(ismax);
		evaluations_ = 0;
		crossover_ = operators_.get("crossover");
		mutation_ = operators_.get("mutation");
		alpha = ((double)this.getInputParameter("alphar"));


		sizeOfNeiborhoodRepleaced_ = ((Integer)this.getInputParameter("sizeOfNeiborhoodRepleaced_"));
		sizeOfMatingNeiborhood_    = ((Integer)this.getInputParameter("sizeOfMatingNeiborhood_"));

		int generation=1;

		InnerWeightVectorDivision_ = ((Integer)this.getInputParameter("InnerWeightDivision"));


		 isInnerWeightVector_ = ((InnerWeightVectorDivision_ > 0));



		HashMap parameters = new HashMap(); // Operator parameters
		populationSize_ = Calculator.conbination(numberofObjectives_-1 + numberOfDivision_ ,numberofObjectives_-1);

		if(isInnerWeightVector_){
			populationSize_ += Calculator.conbination(numberofObjectives_-1 + numberOfDivision_ ,numberofObjectives_-1);
		}


		population_ = new Population();
		indArray_ = new Solution[problem_.getNumberOfObjectives()];
		ReferencePoint_ = new double[problem_.getNumberOfObjectives()];

		SettingWriter.clear();
		SettingWriter.merge(inputParameters_);
		SettingWriter.add("alpha",alpha);
		SettingWriter.merge(mutation_.getMap());
		SettingWriter.add("Problemname",problem_.getName());
		SettingWriter.merge(crossover_.getMap());



		// Create the initial solutionSet

		// STEP 1. Initialization

		boolean cont = true;

		setNeighborhood();

		evaluations_ = 0;


		// STEP 1.3. Initialize population
		initPopulation();

		// STEP 1.4. Initialize z_
		initReferencePoint();


		population_.printVariablesToFile(directoryname + "/InitialVAR/InitialVAR" + time + ".dat");
		population_.printObjectivesAndTotalConstrainToFile(directoryname + "/InitialFUN/ALL/InitialFUN" + time + ".dat");
		population_.printFeasibleObjectivesToFile(directoryname + "/InitialFUN/Feasible/InitialFUN" + time + ".dat");
		population_.printInfeasibleObjectivesToFile(directoryname + "/InitialFUN/Infeasible/InitialFUN" + time + ".dat");


		int[] permutation = new int[populationSize_];
		Permutation.randomPermutation(permutation,populationSize_);



		// STEP 2. Update
		do {
			generation++;
			for (int i = 0; i < populationSize_; i++) {
				int n = permutation[i]; // or int n = i;

				// STEP 2.1. Mating selection

				Vector<Integer> parentsNumber = new Vector<Integer>();

				matingSelection(parentsNumber, n, numberOfParents_);

				Solution child;
				Solution[] parents = new Solution[numberOfParents_];

				for(int k=0;k<numberOfParents_;k++){
					parents[k] = population_.get(parentsNumber.get(k));
				}

				// Apply  crossover
				Solution[] offSpring;
				offSpring = (Solution[]) crossover_.execute(parents);

				if (Random.nextDoubleIE() < 0.5){
					child = new Solution(offSpring[0]);
				} else {
					child = new Solution(offSpring[1]);
				}
//				child = (Random.nextDoubleIE() < 0.5) ? new Solution(offSpring[0]) :  new Solution(offSpring[1]);

				offSpring = null;

				mutation_.execute(child);
				problem_.repair(child,parameters);

				problem_.evaluate(child);

				evaluations_++;

				updateReference(child);


				// STEP 2.5. Update of solutions
				updateProblem(child, n);

				if (evaluations_ == maxEvaluations){
					cont = false;
					break;
				}

			}
		} while (cont);
		System.out.print(evaluations_ +"	");
		// NormalizationWithNadia();
		//Normalization();
		population_.printVariablesToFile(directoryname + "/FinalVAR/FinalVAR" + time + ".dat");
		population_.printObjectivesAndTotalConstrainToFile(directoryname +  "/FinalFUN/ALL/FinalFUN" + time + ".dat");
		population_.printFeasibleObjectivesToFile(directoryname +  "/FinalFUN/Feasible/FinalFUN" + time + ".dat");
		population_.printInfeasibleObjectivesToFile(directoryname +  "/FinalFUN/Infeasible/FinalFUN" + time + ".dat");
		return population_;
	}

	public void subscriptZ(){
		for(int i=0;i<problem_.getNumberOfObjectives();i++){
			System.out.print(ReferencePoint_[i] +" ");
		}
		System.out.println(" ");

	}

	public double[]  getNadia(Population pop){
		int size = pop.get(0).getNumberOfObjectives();
		double[]  nadirPoint = new double[size];
		Solution empty = pop.get(0);
		for(int k=0;k<size;k++){
			nadirPoint[k] = empty.getObjective(k);
		}
		for(int  i =1;i< pop.size();i++){
			empty = pop.get(i);
			for(int key =0 ; key < size;key++){
				if(comparator.compare(nadirPoint[key],empty.getObjective(key) ) ){
						nadirPoint[key] = empty.getObjective(key);
					}
			}
		}
		return nadirPoint;
	};

	public double[]  getIdeal(Population pop){
		int size = pop.get(0).getNumberOfObjectives();
		double[]  nadirPoint = new double[size];
		Solution empty = pop.get(0);
		for(int k=0;k<size;k++){
			nadirPoint[k] = empty.getObjective(k);
		}
		for(int  i =1;i< pop.size();i++){
			empty = pop.get(i);
			for(int key =0 ; key < size;key++){
				if(comparator.compare(empty.getObjective(key),nadirPoint[key])){
						nadirPoint[key] = empty.getObjective(key);
					}
			}
		}
		return nadirPoint;
	};

	public void Normalization(){
		double [] ideal = getIdeal(population_);

		for(int i=0;i<population_.size();i++){
			Solution sol = population_.get(i);
			for(int k =0;k<sol.getNumberOfObjectives();k++){
				double a = sol.getObjective(k);
				a  = a - ideal[k];
				sol.setObjective(k, a);
			}
		}
		NormalizationWithNadia();

	}

	public void NormalizationWithNadia(){
		double[] nadia = getNadia(population_);
		for(int i = 0;i < population_.size();i++){
			Solution sol = population_.get(i);
			for(int key = 0;key< sol.getNumberOfObjectives(); key++){
				double a = sol.getObjective(key);
				a = a /( nadia[key] );
				sol.setObjective(key, a);
			}

		}

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
		a.setNeiborhood(sizeOfNeiborhoodRepleaced_);
		neighborhood_ = a.getNeiborhood();
		WeightedVector_       = a.getWeight();
	}

	public void subscript() {
		for (int i = 0; i < populationSize_; i++) {
			for (int j = 0; j < WeightedVector_[i].length; j++) {
					System.out.print(WeightedVector_[i][j] + "	");
			}
			System.out.println("	");
		}
	}



	public void initPopulation() throws JMException, ClassNotFoundException {
 		for (int i = 0; i < populationSize_; i++) {
			Solution newSolution = new Solution(problem_);
			problem_.repair(newSolution,null);
			problem_.evaluate(newSolution);
			evaluations_++;
			population_.add(new Solution(newSolution));
		} // for
	} // initPopulation
 // initPopulation


	public void initReferencePoint() throws ClassNotFoundException, JMException{
		Solution a = new Solution(problem_);

		for (int n = 0; n < problem_.getNumberOfObjectives(); n++) {
				ReferencePoint_[n] =  a.getObjective(n);
				indArray_[n] = a;
		}
		for (int i = 0; i < populationSize_; i++) {
			updateReference(population_.get(i));
		} // for
	}

	public void matingSelection(Vector<Integer> list, int cid, int size){
		int ss;
		int r;
		int p;
		boolean flag;
		ss = sizeOfMatingNeiborhood_;
		while (list.size() < size) {

				r = Random.nextIntIE(ss);
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
		boolean flag;
		ss = sizeOfMatingNeiborhood_;
		while (list.size() < size) {

				r = Random.nextIntIE(ss);
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
		Permutation.randomPermutation(perm, size);


		for (int i = 0; i < size; i++) {
			int k;
			k = neighborhood_[id][perm[i]];
			comparator.setWeightedVector(WeightedVector_[k]);
			comparator.setRefernecePoint(ReferencePoint_);

			if (comparator.execute(indiv, population_.get(k)) == 1) {
				population_.replace(k, new Solution(indiv));
			}
		}

	} // updateProblem
} // MOEAD

