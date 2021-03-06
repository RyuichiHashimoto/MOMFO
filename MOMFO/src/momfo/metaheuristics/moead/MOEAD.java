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

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import experiments.SettingWriter;
import lib.io.output.fileSubscription;
import lib.math.Calculator;
import lib.math.Permutation;
import momfo.Indicator.IGD;
import momfo.Indicator.IGDRef;
import momfo.core.Algorithm;
import momfo.core.Operator;
import momfo.core.Population;
import momfo.core.Problem;
import momfo.core.Solution;
import momfo.util.JMException;
import momfo.util.Neiborhood;
import momfo.util.WeightedVector;
import momfo.util.Comparator.MOEADComparator.MOEADComparator;
import momfo.util.Comparator.MOEADComparator.NomalMOEADComapator;
import momfo.util.ScalarzingFunction.ScalarzingFunction;
import momfo.util.ScalarzingFunction.ScalarzingFunctionFactory;
public class MOEAD extends Algorithm {


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
	boolean isNorm;
	Solution[] indArray_;
	String functionType_;
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

	/**
	 * Constructor
	 *
	 * @param problem
	 *            Problem to solve
	 */
	public MOEAD(Problem problem) {
		super(problem);
	} // DMOEA


	ScalarzingFunction ScalarzingFunction_;

	String directoryname;
	String ScalarzingFunctionName;
	int maxEvaluations;
	boolean isMax;
	int time;


	public Population execute() throws JMException, ClassNotFoundException {

		Setting();

		setNeighborhood();


		initPopulation();

		initReferencePoint();


		population_.printVariablesToFile(directoryname + "/InitialVAR/InitialVAR" + time + ".dat");
		population_.printObjectivesToFile(directoryname +  "/InitialFUN/InitialFUN" + time + ".dat");
		int counter = 0;
		List<double[]> igdHistory = new ArrayList<double[]>();
		double[] igd = new double[2];
		igd[0] = counter;
		igd[1] = (IGD.CalcNormalizeIGD_To_NonDominated(population_.getAllObjectives(), IGDRef.getNormalizeRefs(tasknumber),IGDRef.getMaxValue(tasknumber),IGDRef.getMinValue(tasknumber),random));
		igdHistory.add(igd.clone());
		int[] permutation = new int[populationSize_];
		Permutation.randomPermutation(permutation,populationSize_,random);
		Solution offSpring;
		boolean cont = true;

		int generation = 1;
		population_.printObjectivesToFile(directoryname +  "/Animation/FUN" + generation + ".dat");

		// STEP 2. Update
		do {
			generation++;
			for (int i = 0; i < populationSize_; i++) {
				int n = permutation[i]; // or int n = i;

				// STEP 2.1. Mating selection
				Vector<Integer> parentsNumber = new Vector<Integer>();

				matingSelection(parentsNumber, n, numberOfParents_);

				Solution[] parents = new Solution[numberOfParents_];

				for(int k=0;k<numberOfParents_;k++){
					parents[k] = population_.get(parentsNumber.get(k));
				}

				// Apply  crossover
				offSpring = (Solution) crossover_.execute(parents);



				mutation_.execute(offSpring);
				problem_.repair(offSpring,null);

				problem_.evaluate(offSpring);

				evaluations_++;

				updateReference(offSpring);


				// STEP 2.5. Update of solutions
				updateProblem(offSpring, n);

				if (evaluations_ == maxEvaluations){
					cont = false;
					break;
				}

			}
//			population_.printObjectivesToFile(directoryname +  "/Animation/FUN" + generation + ".dat");

			igd[0] = ++counter;
			igd[1] = (IGD.CalcNormalizeIGD_To_NonDominated(population_.getAllObjectives(), IGDRef.getNormalizeRefs(tasknumber),IGDRef.getMaxValue(tasknumber),IGDRef.getMinValue(tasknumber),random));
			igdHistory.add(igd.clone());
		} while (cont);
		System.out.print(evaluations_ +"	");
//		 NormalizationWithNadia();
		//if (isNorm)Normalization();
	//	if(outNormal_)population_.Normalization();
		fileSubscription. printToFile(directoryname + "/IGDHistory/"+"IGD"+time+".dat",igdHistory);
		setOutputParameter("IGD",igdHistory.get(igdHistory.size()-1)[1]);	
		population_.printVariablesToFile(directoryname + "/FinalVAR/FinalVAR" + time + ".dat");
		population_.printObjectivesToFile(directoryname +  "/FinalFUN/FinalFUN" + time + ".dat");
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

		for(int i=0;i<nadia.length;i++){
			nadia[i] = ( Math.abs(nadia[i]) >   1.0E-14) ? nadia[i]: 1.0E-14;
		}

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
		a.setNeiborhood(Math.max(sizeOfNeiborhoodRepleaced_,sizeOfMatingNeiborhood_),random);
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
			Solution newSolution = new Solution(problem_,random);
			problem_.repair(newSolution,null);
			problem_.evaluate(newSolution);
			evaluations_++;
			population_.add((newSolution));
		} // for
	} // initPopulation
 // initPopulation

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
			}
		}

	} // updateProblem

	private void Setting() throws JMException{
		isNorm = false;
		numberOfParents_= ((Integer) this.getInputParameter("numberOfParents")).intValue();
		directoryname = ((String) this.getInputParameter("DirectoryName"));
		ScalarzingFunctionName = (this.getInputParameter("ScalarzingFunctionName")).toString();
		maxEvaluations = ((Integer) this.getInputParameter("maxEvaluations")).intValue();
		numberOfDivision_    = ((Integer)this.getInputParameter("numberOfDivision"));
		numberofObjectives_    = ((Integer)this.getInputParameter("numberOfObjectives"));
		time = ((Integer) this.getInputParameter("times")).intValue();

		ScalarzingFunction_ = ScalarzingFunctionFactory.getScalarzingFunctionOperator(ScalarzingFunctionName,(Double)this.getInputParameter("PBITheta"));

		functionType_ = ScalarzingFunction_.getFunctionName();
		comparator = new NomalMOEADComapator(null,ScalarzingFunction_);
		evaluations_ = 0;
		crossover_ = operators_.get("crossover");
		mutation_ = operators_.get("mutation");
		alpha = ((double)this.getInputParameter("alphar"));
		sizeOfNeiborhoodRepleaced_ = ((Integer)this.getInputParameter("sizeOfNeiborhoodRepleaced_"));
		sizeOfMatingNeiborhood_    = ((Integer)this.getInputParameter("sizeOfMatingNeiborhood_"));
		InnerWeightVectorDivision_ = ((Integer)this.getInputParameter("InnerWeightDivision"));
		isInnerWeightVector_ = ((InnerWeightVectorDivision_ > 0));
		populationSize_ = Calculator.conbination(numberofObjectives_-1 + numberOfDivision_ ,numberofObjectives_-1);

	//	outNormal_ = ((boolean) this.getInputParameter("outputNormal"));

		isMax    = ((boolean)this.getInputParameter("IsMax"));
		isNorm = ((boolean)this.getInputParameter("IsNorm"));
		comparator.setIs(isMax);

		if(isInnerWeightVector_){
			populationSize_ += Calculator.conbination(numberofObjectives_-1 + InnerWeightVectorDivision_ ,numberofObjectives_-1);
		}

		population_ = new Population(populationSize_);
		indArray_ = new Solution[problem_.getNumberOfObjectives()];
		ReferencePoint_ = new double[problem_.getNumberOfObjectives()];

		SettingWriter.clear();
		SettingWriter.merge(inputParameters_);
		SettingWriter.add("alpha",alpha);
		SettingWriter.merge(mutation_.getMap());
		SettingWriter.add("Problemname",problem_.getName());
		SettingWriter.merge(crossover_.getMap());
		evaluations_ = 0;



	}





} // MOEAD

