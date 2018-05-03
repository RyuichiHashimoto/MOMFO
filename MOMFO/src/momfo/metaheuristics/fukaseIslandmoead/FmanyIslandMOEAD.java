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

package momfo.metaheuristics.fukaseIslandmoead;

import java.util.HashMap;
import java.util.Vector;

import experiments.SettingWriter;
import momfo.core.Algorithm;
import momfo.core.Operator;
import momfo.core.Population;
import momfo.core.Problem;
import momfo.core.Solution;
import momfo.util.JMException;
import momfo.util.Neiborhood;
import momfo.util.Permutation;
import momfo.util.Random;
import momfo.util.WeightedVector;
import momfo.util.Comparator.MOEADComparator.ConstrainMOEADComparator;
import momfo.util.Comparator.MOEADComparator.MOEADComparator;
import momfo.util.Comparator.MultiIslandMOEADComparator.ConstrainMultiIslandMOEADComparator;
import momfo.util.Comparator.MultiIslandMOEADComparator.MultiIslandMOEADComparator;
import momfo.util.ScalarzingFunction.ScalarzingFunction;
import momfo.util.ScalarzingFunction.ScalarzingFunctionFactory;
public class FmanyIslandMOEAD extends Algorithm {


	/**
	 * Stores the population size
	 */
	private MultiIslandMOEADComparator comparator;


	private double alpha = 1.1;

	private Population[] population_;

	/**
	 * Stores the population size
	 */
	private int populationSize_;

	private int totalpopulationSize_;

	/**
	 * Stores the population
	 */
	/**
	 * Z vector (ideal point)
	 */
	double[][] ReferencePoint_;
	/**
	 * Lambda vectors
	 */
	// Vector<Vector<Double>> lambda_ ;
	static double[][] WeightedVector_;

	private int numberofObjectives_;
	private int numberOfDivision_;


	static int[][] neighborhood_;

	int numberOftotalParents_;
	int numberOfeachParents_;

	Solution[][] indArray_;
	String functionTypes_;
	int evaluations_;

	ScalarFunctionSelection[] FunctionSelection_;

	MOEADComparator[] comparator_;

	/**
	 * Operators
	 */
	String[] outputDirectory_;

	Operator crossover_;
	Operator mutation_;

	int numberOfIsland_;
	int sizeOfNeiborhoodRepleaced_;
	int sizeOfMatingNeiborhood_;
	int numberOfMakeChildlen_;
	/**
	 * Constructor
	 *
	 * @param problem
	 *            Problem to solve
	 */
	public FmanyIslandMOEAD(Problem problem) {
		super(problem);
	} // DMOEA


	ScalarzingFunction[] ScalarzingFunction_;

	public Population execute() throws JMException, ClassNotFoundException {


		numberOfIsland_ = ((Integer) this.getInputParameter("numberOfIsland")).intValue();

		numberOftotalParents_= 12;
		numberOfeachParents_ = 2;
		String   directorynamebase = ((String) this.getInputParameter("directoryname"));
		String[] ScalarzingFunctionName = (String [])(this.getInputParameter("ScalarzingFunctionName"));


		String[] directorynames = new String[numberOfIsland_];
		for(int i = 0 ;i <numberOfIsland_;i++){
			directorynames[i] = directorynamebase + "/" + ScalarzingFunctionName[i];
		}


		int maxEvaluations = ((Integer) this.getInputParameter("maxEvaluations")).intValue();
		numberOfDivision_    = ((Integer)this.getInputParameter("numberOfDivision"));
		numberofObjectives_    = ((Integer)this.getInputParameter("numberOfObjectives"));
		sizeOfNeiborhoodRepleaced_ = ((Integer)this.getInputParameter("sizeOfNeiborhoodRepleaced_"));
		sizeOfMatingNeiborhood_    = ((Integer)this.getInputParameter("sizeOfMatingNeiborhood_"));
		boolean ismax    = ((boolean)this.getInputParameter("ismax"));



		int time = ((Integer) this.getInputParameter("times")).intValue();

		evaluations_ = 0;

		crossover_ = operators_.get("crossover");
		mutation_ = operators_.get("mutation");
		alpha = ((double)this.getInputParameter("alphar"));
		int ma = Math.max(sizeOfMatingNeiborhood_, sizeOfMatingNeiborhood_);
		int generation;
		numberOfMakeChildlen_ = 6;


		ScalarzingFunction_ = new  ScalarzingFunction[numberOfIsland_];
		population_ = new Population[numberOfIsland_];
		comparator_ = new ConstrainMOEADComparator[numberOfIsland_];
		FunctionSelection_ = new ScalarFunctionSelection[numberOfIsland_];



		for(int i=0;i<numberOfIsland_;i++){
			ScalarzingFunction_[i] = ScalarzingFunctionFactory.getScalarzingFunctionOperator(ScalarzingFunctionName[i],(double)this.getInputParameter("PBITheta"));
			comparator_[i] = new ConstrainMOEADComparator(null,ScalarzingFunction_[i]);
			FunctionSelection_[i] = new ScalarFunctionSelection(null,comparator_[i]);
		}

		comparator = new ConstrainMultiIslandMOEADComparator(null,ScalarzingFunction_);
		comparator.setIs(ismax);

		HashMap parameters = new HashMap(); // Operator parameters
		indArray_ = new Solution[numberOfIsland_][problem_.getNumberOfObjectives()];
		neighborhood_ = new int[populationSize_][];
		ReferencePoint_ = new double[numberOfIsland_][problem_.getNumberOfObjectives()];
		WeightedVector_ = new double[populationSize_][problem_.getNumberOfObjectives()];

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

		populationSize_ = neighborhood_.length;
		evaluations_ = 0;

		// STEP 1.3. Initialize population
		initilaize();
		for(int i = 0;i<numberOfIsland_;i++){
			population_[i].printVariablesToFile(directorynames[i] + "/InitialVAR/InitialVAR" + time + ".dat");
		}

		int[] permutation = new int[populationSize_];
		Permutation.randomPermutation(permutation,populationSize_);
		int generation_ = 1;


		// STEP 2. Update
		do {
			generation_++;

			for(int k = 0; k < 6;k++){
				for (int i = 0; i < populationSize_; i++) {
					int n = permutation[i]; // or int n = i;
	         		Solution[] child  = makeChildlen(n);

	         		boolean notChange = true;

	         		for(int a=0;a<numberOfMakeChildlen_;a++){
	       				updateProblem(child[a], n);
	       				updateReference(child[a]);

	         		}

					if (evaluations_ == maxEvaluations){
						cont = false;
						break;
					}
				}
			}
		}while (cont);

		System.out.print(evaluations_ +"	");
		// NormalizationWithNadia();
		//Normalization();
//		population_.printVariablesToFile(directoryname + "/FinalVAR/FinalVAR" + time + ".dat");
//		population_.printObjectivesToFile(directoryname +  "/FinalFUN/FinalFUN" + time + ".dat");


		return null;
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
				if( nadirPoint[key] < empty.getObjective(key)){
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
				if(nadirPoint[key] > empty.getObjective(key)){
						nadirPoint[key] = empty.getObjective(key);
					}
			}
		}
		return nadirPoint;
	};

	public int[] patternselect(int key){
		int[] ret = new int[2];
		switch(key){
			case 0: ret[0] = 0; ret[1] = 0;	 break;
			case 1: ret[0] = 0; ret[1] = 1;	 break;
			case 2: ret[0] = 0; ret[1] = 2;	 break;
			case 3: ret[0] = 1; ret[1] = 1;	 break;
			case 4: ret[0] = 1; ret[1] = 2;	 break;
			case 5: ret[0] = 0; ret[1] = 2;	 break;
			default:
				assert true : "patternselect is wrong, the key is " + key;
		}

		return ret;
	}

	public Solution[] makeChildlen(int key) throws JMException{
		Solution[] child = new Solution[numberOfMakeChildlen_];
		Vector<Integer> parentsNumber = new Vector<Integer>();
		matingSelection( parentsNumber, key, numberOftotalParents_ );
		Solution[] parents = new Solution[numberOfeachParents_ ];
		Solution[] OffSpring;
		int[] angel;

		for(int i=0;i<numberOfMakeChildlen_/2;i++){
			angel = patternselect(i);
			for(int k=0;k<2;k++){
				parents[k] = population_[angel[k]].get(parentsNumber.get( k + numberOfeachParents_*i) );
			}
			OffSpring = (Solution[]) crossover_.execute(parents);
			child[i] = Random.nextDoubleIE() > 0.5 ? OffSpring[0] : OffSpring[1];
			child[i] = (Solution) mutation_.execute(child[i]);
			problem_.repair( child[i] , null);
			problem_.evaluate(child[i]);
			evaluations_++;

		}
		return child;
	}
/*
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
*/
	public void	 setNeighborhood() throws JMException{
		Neiborhood a = new Neiborhood();
		double[][] weight;
		weight = WeightedVector.getWeightedVector(numberofObjectives_,numberOfDivision_);
	/*	if (numberofObjectives_ <=10){
		}else {
			double[][] weightouter = WeightedVector.getWeightedVector(numberofObjectives_,3);
			double[] [] weightinner = WeightedVector.getWeightedVector(numberofObjectives_,2);
			weightinner = WeightedVector.	getinnerWeightVector(weightinner);
			weight = 	WeightedVector.conbine( weightouter, weightinner);
		}*/
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



	//今回，参照点の初期化の間にあれを挟む必要が
	public void initilaize() throws ClassNotFoundException, JMException{
		Solution[] newSolution = new Solution[numberOfMakeChildlen_];
		population_ = new Population[numberOfIsland_];

		//スカラー関数によっては初期個体の割り当てには参照点が必要なので参照点のとりあえずの初期化
		for(int k=0;k<numberOfIsland_;k++){
			population_[k] = new Population();
			Solution a = new Solution(problem_);
			for (int n = 0; n < problem_.getNumberOfObjectives(); n++) {
				ReferencePoint_[k][n] = alpha * a.getObjective(n) ;
				indArray_[k][n] = new Solution(a);
			}
		}

		//初期個体群の生成
        for(int i=0;i<populationSize_;i++){
			for(int c=0;c<numberOfMakeChildlen_;c++){
				 newSolution[c] = new Solution(problem_);
	      		 problem_.repair(newSolution[c],null);
	      		 problem_.evaluate(newSolution[c]);
				 evaluations_++;
			}
			int[] number = new int[numberOfIsland_];
			for(int k=0;k<numberOfIsland_;k++){
			    FunctionSelection_[k].setRefernecePoint(ReferencePoint_[k]);
				FunctionSelection_[k].setWeightedVector(WeightedVector_[i]);
				number[k] = (int)FunctionSelection_[k].execute(newSolution);
				population_[k].add(newSolution[number[k]]);
				updateReference(newSolution[number[k]]);
			}
         }



	}


	public void matingSelection(Vector<Integer> list,int cid, int size){
		int ss;
		int r;
		int p;
		boolean flag;
		ss = sizeOfMatingNeiborhood_;
		int store  = -1;
		int counter_ = 0;
		while (list.size() < size) {

		 if(store ==  - 1){
    		r = Random.nextIntIE(ss);
	    	p = neighborhood_[cid][r];
		 } else {
			 do {
	    		r = Random.nextIntIE(ss);
		    	p = neighborhood_[cid][r];
			} while(store == p);
		 }
		 list.addElement(p);
		 if(list.size() % 2 == 0){
				store = - 1;
		 } else {
			store = p;
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



	boolean updateReference(Solution individual) {
		boolean ret = false;
		for(int k = 0;k < numberOfIsland_;k++){
			for (int n = 0; n < problem_.getNumberOfObjectives(); n++) {
				System.out.println(individual.getObjective(n));
				if (comparator_[k].compare(individual.getObjective(n) ,indArray_[k][n].getObjective(n))) {
					ReferencePoint_[k][n] =  alpha*individual.getObjective(n);
					indArray_[k][n] = individual;
					ret = true;
				}
			}
		}
		return ret;
	}

	void updateProblem(Solution indiv, int id) throws JMException {
		int size;

		size = sizeOfNeiborhoodRepleaced_;
		int[] perm = new int[size];

		// generate teh random permutation.
		Permutation.randomPermutation(perm, size);

		for(int islandNumber = 0; islandNumber < numberOfIsland_;islandNumber++){
			for (int i = 0; i < size; i++) {
				int k = neighborhood_[id][i];

				comparator_[islandNumber].setWeightedVector(WeightedVector_[k]);
				comparator_[islandNumber].setRefernecePoint(ReferencePoint_[islandNumber]);

				if (comparator_[islandNumber].execute(indiv, population_[islandNumber].get(k)) == 1) {
					population_[islandNumber].replace(k, new Solution(indiv));
				}
			}
		}

	} // updateProblem
} // MOEAD

