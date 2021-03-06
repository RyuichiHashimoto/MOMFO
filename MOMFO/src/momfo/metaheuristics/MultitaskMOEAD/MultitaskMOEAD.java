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

package momfo.metaheuristics.MultitaskMOEAD;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import experiments.SettingWriter;
import lib.directory.DirectoryMaker;
import lib.io.output.fileSubscription;
import lib.math.Calculator;
import lib.math.Permutation;
import momfo.Indicator.IGD;
import momfo.Indicator.IGDRef;
import momfo.core.Algorithm;
import momfo.core.Operator;
import momfo.core.Population;
import momfo.core.Problem;
import momfo.core.ProblemSet;
import momfo.core.Solution;
import momfo.util.JMException;
import momfo.util.Neiborhood;
import momfo.util.WeightedVector;
import momfo.util.Comparator.MOEADComparator.MOEADComparator;
import momfo.util.Comparator.MOEADComparator.NomalMOEADComapator;
import momfo.util.ScalarzingFunction.ScalarzingFunction;
import momfo.util.ScalarzingFunction.ScalarzingFunctionFactory;
public class MultitaskMOEAD extends Algorithm {


	public MultitaskMOEAD(ProblemSet d) {
		super(d);
		// TODO 自動生成されたコンストラクター・スタブ
	}


	/**
	 * Stores the population size
	 */
	private MOEADComparator[] comparator;


	private double alpha = 1.0;

	/**
	 * Stores the population size
	 */
	private int[] populationSize_;
	/**
	 * Stores the population
	 */
	private Population[] population_;
	/**
	 * Z vector (ideal point)
	 */
	double[][] ReferencePoint_;
	/**
	 * Lambda vectors
	 */
	// Vector<Vector<Double>> lambda_ ;
	static double[][][] WeightedVector_;

	private int[] numberofObjectives_;
	private int[] numberOfDivision_;

	static int[][][] neighborhood_;

	int[] numberOfParents_;
	boolean[] isNorm;
	Solution[][] indArray_;
	String[] functionType_;

	Solution[][] nonDominatedSolution;

	int[] evaluations_;

	int[] maxGeneration_;
	int[] InnerWeightVectorDivision_;
	boolean[] isInnerWeightVector_;
	/**
	 * Operators
	 */
	Operator crossover_;
	Operator mutation_;

	int numberOfTasks;

	int[] sizeOfNeiborhoodRepleaced_;
	int[] sizeOfMatingNeiborhood_;
	int[] ParentsSelectioncounter;

	/**
	 * Constructor
	 *
	 * @param problem
	 *            Problem to solve
	 */
	public MultitaskMOEAD(Problem problem) {
		super(problem);
	} // DMOEA


	ScalarzingFunction[] ScalarzingFunction_;

	String directoryname;
	String[] ScalarzingFunctionName;
	int[] maxEvaluations;
	boolean[] isMax;
	int time;

	private double rmp;
	double[] NofReplacing;

	public Population execute() throws JMException, ClassNotFoundException {

		Setting();

		setNeighborhood();

		initPopulation();

		initReferencePoint();
		double[] igd = new double[2];
		double[] Replacing = new double[2];
		int[] updataTime = new int[problemSet_.countProblem()];
		int[] updateOptunityTime = new int[problemSet_.countProblem()];


		List<List<double[]>> igdHistory = new ArrayList<List<double[]>>();
		List<List<double[]>> replacingRate = new ArrayList<List<double[]>>();

		int counter = 0;
		boolean[] cont = new boolean[numberOfTasks];


		ParentsSelectioncounter = new int[numberOfTasks];
		for(int t = 0; t< ParentsSelectioncounter.length;t++){
			ParentsSelectioncounter[t] = 10;
		}

		for(int t = 0; t< problemSet_.countProblem();t++){
			population_[t].printVariablesToFile(directoryname.replace("Task1", "Task" + String.valueOf(t+1) ) + "/InitialVAR/InitialVAR" + time + ".dat");
			population_[t].printObjectivesToFile(directoryname.replace("Task1", "Task" + String.valueOf(t+1) )+  "/InitialFUN/InitialFUN" + time + ".dat");
			List<double[]> d = new ArrayList<double[]>();
			boolean[] calc = new boolean[population_[t].size()]; //IGD計算をする個体の選出するための配列
			//SkillFactorの照合
			for(int p = 0 ; p < population_[t].size();p++){
				calc[p] = true;
			}
			//IGD計算
			igd[0] = counter;
			igd[1] = (IGD.CalcNormalizeIGD_To_NonDominated(population_[t].getAllObjectives(),calc, IGDRef.getNormalizeRefs(t),IGDRef.getMaxValue(t),IGDRef.getMinValue(t),random));
			replacingRate.add(new ArrayList<double []>(d));

			setOutputParameter("InitialFUN"+t,population_[t].get(0).getObjectives());
			setOutputParameter("InitialVAR"+t,population_[t].get(1).getValue());

			d.add(igd.clone());
			igdHistory.add(new ArrayList<double []>(d));
			cont[t] = false;
		}
		int totalPopulationSize_ = 0;;
		for(int t = 0; t < numberOfTasks;t++){
			totalPopulationSize_ += populationSize_[t];
		}

		int[] permutation = new int[totalPopulationSize_];
		Permutation.randomPermutation(permutation,totalPopulationSize_,random);

		Solution offSpring;

		// STEP 2. Update
		do {
			int count = 0;
			for(int t= 0;t < problemSet_.countProblem();t++){
				updataTime[t] = 0;
				updateOptunityTime[t] = 0;
			}

			for(int t= 0;t < problemSet_.countProblem();t++){

				for (int i = 0; i < populationSize_[t]; i++) {
					int n = permutation[count++];

					//1つ目の要素はタスク番号, 2つ目の要素は個体番号，
					int[] temp = getSolutionID(n,populationSize_);

					// or int n = i;
					int tasks = temp[0];
					n  = temp[1];
					// STEP 2.1. Mating selection
					if(cont[tasks]){
						continue;
					}

					Vector<Integer> parentsNumber = new Vector<Integer>();
					Vector<Integer> TaskNumber_List  = new Vector<Integer>();

					//他タスクを解く個体を選択したかどうか
					boolean selectOtherTasksFlag  = matingSelection(parentsNumber,TaskNumber_List,tasks, n, numberOfParents_[tasks]);

					Solution[] parents = new Solution[numberOfParents_[tasks]];

					for(int k=0;k<numberOfParents_[tasks];k++){
						parents[k] = population_[TaskNumber_List.get(k)].get(parentsNumber.get(k));
					}

					parentsNumber = null;
					TaskNumber_List  = null;
					// Apply  crossover
					offSpring = (Solution) crossover_.execute(parents);

					mutation_.execute(offSpring);

					problemSet_.get(tasks).repair(offSpring,null);

					problemSet_.get(tasks).evaluate(offSpring);

					evaluations_[tasks]++;

					updateReference(offSpring,tasks);

					int updatetime = updateProblem(offSpring, tasks,n);
					if(selectOtherTasksFlag){
						updataTime[tasks] = updatetime;
						updateOptunityTime[tasks] += sizeOfNeiborhoodRepleaced_[tasks];
					}

					// STEP 2.5. Update of solutions

					if (evaluations_[tasks] == maxEvaluations[tasks]){
						cont[tasks] = true;
						break;
					}
					if(tasks == 0){
	//					System.out.println("	" + ParentsSelectioncounter[tasks]);
					} else if (tasks == 1){
	//					System.out.println(ParentsSelectioncounter[tasks] + "	");
					}
			}
			}
			for(int t = 0 ;t < numberOfTasks;t++){
				igd[0] = counter;
				igd[1] = (IGD.CalcNormalizeIGD_To_NonDominated(population_[t].getAllObjectives(), IGDRef.getNormalizeRefs(t),IGDRef.getMaxValue(t),IGDRef.getMinValue(t),random));
				igdHistory.get(t).add(igd.clone());

				Replacing[0] = counter;
				if (updateOptunityTime[t] == 0){
					Replacing[1] = -1 ;
				} else {
					Replacing[1] =  (double)updataTime[t]/updateOptunityTime[t];
				}
				replacingRate.get(t).add(Replacing.clone());
			}
			counter++;

		} while (check(cont));
		System.out.print(evaluations_ +"	");
//		 NormalizationWithNadia();
		//if (isNorm)Normalization();
	//	if(outNormal_)population_.Normalization();
		for(int t=0;t < problemSet_.countProblem();t++){
			fileSubscription. printToFile(directoryname.replace("Task1", "Task"+String.valueOf(t+1)) + "/IGDHistory/"+"IGD"+time+".dat",igdHistory.get(t));

			fileSubscription. printToFile(directoryname.replace("Task1", "Task"+String.valueOf(t+1)) + "/ReplacingRateHistory/"+"ReplacingRate"+time+".dat",replacingRate.get(t));
//			fileSubscription. printToFile(directoryname.replace("Task1", "Task"+String.valueOf(t+1)) + "/Animation/"+"Dat"+time+".dat",igdHistory.get(t));
			population_[t].printVariablesToFile(directoryname.replace("Task1", "Task"+String.valueOf(t+1))  + "/FinalVAR/FinalVAR" + time + ".dat");
			population_[t].printObjectivesToFile(directoryname.replace("Task1", "Task"+String.valueOf(t+1))  +  "/FinalFUN/FinalFUN" + time + ".dat");

			setOutputParameter("IGD"+(t+1),igdHistory.get(t).get(igdHistory.get(t).size()-1)[1]);
			setOutputParameter("FinalFUN"+(t+1),population_[t].get(0).getObjectives());
			setOutputParameter("FinalVAR"+(t+1),population_[t].get(1).getValue());
		}
		return null;
	}

	private boolean check(boolean[] cont) {
		for(int i = 0;i < cont.length;i++){
			if (!cont[i]){
				return true;
			}
		}
		return false;
	}

	//1つ目の要素はタスク番号, 2つ目の要素は個体番号，
	private int[] getSolutionID(int n, int[] populationSize ){
		int[] ret = new int[2];

		int sum = populationSize[0];
		for(int t = 0; t < populationSize.length;t++){
			if(n <sum ){
				ret[0] = t;
				ret[1] = n  - sum + populationSize[t];
				break;
			} else {
				sum += populationSize[t+1];
			}
		}

		return ret;
	}

	private boolean  check_pop(Population population_2) {
		int d = population_2.get(0).getNumberOfObjectives();
		for(int i = 0; i < population_2.size();i++){
			assert d == population_2.get(i).getNumberOfObjectives() : i;
		}
		return true;
	}

	public void subscriptZ(){
		for(int i=0;i<problem_.getNumberOfObjectives();i++){
			System.out.print(ReferencePoint_[i] +" ");
		}
		System.out.println(" ");
	}

	public void setNeighborhood() throws JMException{

		for(int t = 0;t < problemSet_.countProblem();t++){
			Neiborhood a = new Neiborhood();
			double[][] weight,weight_one;

			weight_one = WeightedVector.getWeightedVector(numberofObjectives_[t],numberOfDivision_[t]);

			if (isInnerWeightVector_[t]){
				double[][] weightinner = WeightedVector.getWeightedVector(numberofObjectives_[t],InnerWeightVectorDivision_[t]);
				WeightedVector.getinnerWeightVector(weightinner);
				weight = 	WeightedVector.conbine(weight_one, weightinner);
			} else {
				weight = weight_one;
			}
			a.setWeightedVector(weight);
			a.setNeiborhood(Math.max(sizeOfNeiborhoodRepleaced_[t],sizeOfMatingNeiborhood_[t]),random);
			neighborhood_[t] = a.getNeiborhood().clone();
			WeightedVector_[t]       = a.getWeight().clone();
		}
	}

	public void initPopulation() throws JMException, ClassNotFoundException {
		for(int t = 0 ; t < problemSet_.countProblem();t++){
	 		for (int i = 0; i < populationSize_[t]; i++) {
				Solution newSolution = new Solution(problemSet_, i%problemSet_.countProblem(),random);
//				Solution newSolution = new Solution(problemSet_.get(t));

				problemSet_.get(t).repair(newSolution,null);
				problemSet_.get(t).evaluate(newSolution);
				evaluations_[t]++;
				population_[t].add((newSolution));
			} // for
	 	}
	} // initPopulation
 // initPopulation

	public void initReferencePoint() throws ClassNotFoundException, JMException{
		for(int t=0;t<problemSet_.countProblem();t++){
			Solution a = population_[t].get(0);
			for (int n = 0; n < problemSet_.get(t).getNumberOfObjectives(); n++) {
					ReferencePoint_[t][n] =  a.getObjective(n);
					indArray_[t][n] = a;
			}
			for (int i = 1; i < populationSize_[t]; i++) {
				updateReference(population_[t].get(i),t);
			} // for
		}
	}

	public boolean matingSelection(Vector<Integer> pop_list,Vector<Integer> task_list, int tasknumber,int cid, int size){
		int ss;
		int r;
		int p;
		int taskID;
		boolean flag;
		ss = sizeOfMatingNeiborhood_[tasknumber];
		boolean returnflag = false;


		//他タスクを解く個体との交叉
		if (random.nextDoubleIE() < rmp){
			returnflag = true;
			do {
				taskID = random.nextIntIE(problemSet_.countProblem());
			} while(taskID == tasknumber);

			p = random.nextIntIE(populationSize_[taskID]);
			flag = true;
			pop_list.addElement(p);
			task_list.addElement(taskID);
			taskID = tasknumber;
			r = random.nextIntIE(ss);
			p = neighborhood_[taskID][cid][r];
			pop_list.addElement(p);
			task_list.addElement(taskID);

		} else {
			while (pop_list.size() < size) {

				taskID = tasknumber;
				r = random.nextIntIE(ss);
				p = neighborhood_[taskID][cid][r];

				flag = true;
				for (int i = 0; i < pop_list.size(); i++) {
					if (pop_list.get(i) == p && task_list.get(i) == taskID){
						flag = false;
						break;
					}
				}
				if (flag) {
					pop_list.addElement(p);
					task_list.addElement(taskID);
				}
			}
		}
		//System.out.println("test");

		return returnflag;
	}

	void updateReference(Solution individual,int task) {
		for (int n = 0; n < problemSet_.get(task).getNumberOfObjectives(); n++) {
			if (comparator[task].compare(individual.getObjective(n) ,indArray_[task][n].getObjective(n))) {
				ReferencePoint_[task][n] =  alpha*individual.getObjective(n);
				indArray_[task][n] = individual;
			}
		}
	}

	//Updateした回数を返す
	int updateProblem(Solution indiv, int Tasks_id,int id) throws JMException {
		int size;

		int Updatetimes = 0;
		size = sizeOfNeiborhoodRepleaced_[Tasks_id];
		int[] perm = new int[size];

		// generate teh random permutation.
		Permutation.randomPermutation(perm, size,random);


		for (int i = 0; i < size; i++) {
			int k;
			k = neighborhood_[Tasks_id][id][perm[i]];
			comparator[Tasks_id].setWeightedVector(WeightedVector_[Tasks_id][k]);
			comparator[Tasks_id].setRefernecePoint(ReferencePoint_[Tasks_id]);

			if (comparator[Tasks_id].execute(indiv, population_[Tasks_id].get(k)) == 1) {
				population_[Tasks_id].replace(k, (indiv));
				Updatetimes = Updatetimes + 1;
			}
		}
		return Updatetimes;
	} // updateProblem

	private void Setting() throws JMException{

//		isNorm = false;
		numberOfTasks = problemSet_.countProblem();
		numberOfParents_ = new int[numberOfTasks];
		directoryname = ((String) this.getInputParameter("DirectoryName"));
		ScalarzingFunctionName = new String[problemSet_.countProblem()];
//		maxEvaluations = ((Integer) this.getInputParameter("maxEvaluations")).intValue();
		time = ((Integer) this.getInputParameter("times")).intValue();

		numberOfDivision_    =new int[numberOfTasks];// ((Integer)this.getInputParameter("numberOfDivision"));
		numberofObjectives_    = new int[numberOfTasks];
		ScalarzingFunction_ = new ScalarzingFunction[numberOfTasks];
		functionType_ =  new String[numberOfTasks];

		comparator = new MOEADComparator[numberOfTasks];
		evaluations_ = new int[numberOfTasks];
		crossover_ = getOperator("crossover");
		mutation_ = getOperator("mutation");
		alpha = ((double)this.getInputParameter("alphar"));

		sizeOfNeiborhoodRepleaced_ = new int[numberOfTasks];// ((Integer)this.getInputParameter("sizeOfNeiborhoodRepleaced_"));
		sizeOfMatingNeiborhood_    = new int[numberOfTasks];//((Integer)this.getInputParameter("sizeOfMatingNeiborhood_"));
		InnerWeightVectorDivision_ = new int[numberOfTasks];//((Integer)this.getInputParameter("InnerWeightDivision"));
		isInnerWeightVector_ = new boolean[numberOfTasks]; //((InnerWeightVectorDivision_ > 0));
		populationSize_ = new int[numberOfTasks];
		isMax    = new boolean[numberOfTasks];//((boolean)this.getInputParameter("IsMax"));
		isNorm = new boolean[numberOfTasks];//((boolean)this.getInputParameter("IsNorm"));

		neighborhood_ = new int[problemSet_.countProblem()][][];
		WeightedVector_ = new double[problemSet_.countProblem()][][];
		population_ = new Population[numberOfTasks];
		ReferencePoint_ = new double[numberOfTasks][];
		indArray_ = new Solution[numberOfTasks][];
		maxEvaluations = new int[numberOfTasks];

		HashMap<String,Object> parameter = new HashMap<String,Object>();
		parameter.put("RandomGenerator",random);

		for(int t =0;t < numberOfTasks;t++){
			DirectoryMaker.Make(directoryname.replace("Task1", "Task"+String.valueOf(t+1)) + "/ReplacingRateHistory");

			ScalarzingFunctionName[t] = ((String) this.getInputParameter("ScalarzingFunctionName"+String.valueOf(1+t)));
			numberOfParents_[t]= 2;
			numberOfDivision_[t] = ((Integer)this.getInputParameter("numberOfDivision"+ String.valueOf(1+t))).intValue();
			numberofObjectives_[t] =problemSet_.get(t).getNumberOfObjectives();
			ScalarzingFunction_[t] = ScalarzingFunctionFactory.getScalarzingFunctionOperator(ScalarzingFunctionName[t],(Double)this.getInputParameter("PBITheta"+String.valueOf(t+1)));
			functionType_[t] = ScalarzingFunction_[t].getFunctionName();
			comparator[t] =  new NomalMOEADComapator(parameter,ScalarzingFunction_[t]);
			evaluations_[t] = 0;
			rmp = ((Double)this.getInputParameter("rmp"));
			sizeOfNeiborhoodRepleaced_[t] = ((Integer)this.getInputParameter("sizeOfNeiborhoodRepleaced_"+String.valueOf(t+1)));
			sizeOfMatingNeiborhood_[t]    = ((Integer)this.getInputParameter("sizeOfMatingNeiborhood_"+String.valueOf(t+1)));
			InnerWeightVectorDivision_[t] = ((Integer)this.getInputParameter("InnerWeightDivision" + String.valueOf(t+1)));
			isInnerWeightVector_[t] = ((InnerWeightVectorDivision_[t] > 0));
			populationSize_[t] = Calculator.conbination(numberofObjectives_[t] -1 + numberOfDivision_[t] ,numberofObjectives_[t]-1);
			isMax[t] = ((boolean)this.getInputParameter("IsMax"+String.valueOf(t+1)));
			//isNorm[t] = ((boolean)this.getInputParameter("IsNorm"));
			comparator[t].setIs(isMax[t]);

			if(isInnerWeightVector_[t]){
				populationSize_[t] += Calculator.conbination(numberofObjectives_[t]-1 + InnerWeightVectorDivision_[t] ,numberofObjectives_[t]-1);
			}
			population_[t]  = new Population(populationSize_[t]);
			indArray_[t] = new Solution[problemSet_.get(t).getNumberOfObjectives()];
			ReferencePoint_[t] = new double[problemSet_.get(t).getNumberOfObjectives()];
			maxEvaluations[t] = ((Integer) this.getInputParameter("maxEvaluations"+String.valueOf(t+1))).intValue();

		}



	//	outNormal_ = ((boolean) this.getInputParameter("outputNormal"));




		SettingWriter.clear();
		SettingWriter.merge(inputParameters_);
		SettingWriter.add("alpha",alpha);
		SettingWriter.merge(mutation_.getMap());
		SettingWriter.add("Problemname",problemSet_.getProblemName());
		SettingWriter.merge(crossover_.getMap());





	}





} // MOEAD

