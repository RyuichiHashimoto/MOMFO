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

package momfo.metaheuristics.multitaskmoead;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.naming.NamingException;

import lib.experiments.CommandSetting;
import lib.experiments.ParameterNames;
import lib.experiments.Exception.CommandSetting.notFoundException;
import lib.lang.NotVerifiedYet;
import lib.math.Calculator;
import lib.math.Permutation;
import momfo.core.GeneticAlgorithm;
import momfo.core.Operator;
import momfo.core.Population;
import momfo.core.Solution;
import momfo.util.JMException;
import momfo.util.Neiborhood;
import momfo.util.WeightedVector;
import momfo.util.Comparator.MOEADComparator.MOEADComparator;
import momfo.util.ScalarzingFunction.ScalarzingFunction;

public class MultitaskMOEAD extends GeneticAlgorithm {

	private int[] permutation;

	private MOEADComparator[] comparator;

	private double[] alpha;

	private int[] populationSize_;

	double[][] ReferencePoint_;

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

	int[] updataTime;
	int[] updateOptunityTime;

	public boolean[] cont;

	ScalarzingFunction[] ScalarzingFunction_;

	String directoryname;
	String[] ScalarzingFunctionName;
	int[] maxEvaluations;
	boolean[] isMax;
	int time;

	private double rmp;
	double[] NofReplacing;

	// 1つ目の要素はタスク番号, 2つ目の要素は個体番号，
	private int[] getSolutionID(int n, int[] populationSize) {
		int[] ret = new int[2];

		int sum = populationSize[0];
		for (int t = 0; t < populationSize.length; t++) {
			if (n < sum) {
				ret[0] = t;
				ret[1] = n - sum + populationSize[t];
				break;
			} else {
				sum += populationSize[t + 1];
			}
		}

		return ret;
	}

	public void setNeighborhood() throws JMException {

		for (int t = 0; t < problemSet_.countProblem(); t++) {
			Neiborhood a = new Neiborhood();
			double[][] weight, weight_one;

			weight_one = WeightedVector.getWeightedVector(numberofObjectives_[t], numberOfDivision_[t]);

			if (isInnerWeightVector_[t]) {
				double[][] weightinner = WeightedVector.getWeightedVector(numberofObjectives_[t],
						InnerWeightVectorDivision_[t]);
				WeightedVector.getinnerWeightVector(weightinner);
				weight = WeightedVector.conbine(weight_one, weightinner);
			} else {
				weight = weight_one;
			}
			a.setWeightedVector(weight);
			a.setNeiborhood(Math.max(sizeOfNeiborhoodRepleaced_[t], sizeOfMatingNeiborhood_[t]), random);
			neighborhood_[t] = a.getNeiborhood().clone();
			WeightedVector_[t] = a.getWeight().clone();
		}
	}

	public void initPopulation() throws JMException, ClassNotFoundException {
		for (int t = 0; t < problemSet_.countProblem(); t++) {
			for (int i = 0; i < populationSize_[t]; i++) {
				Solution newSolution = null;// new Solution(problemSet_, i % problemSet_.countProblem(), random);
				// Solution newSolution = new Solution(problemSet_.get(t));
				try {
					newSolution = new Solution(problemSet_, t, random);
					initialization.initialize(newSolution);
				} catch (NotVerifiedYet e) {
					throw new JMException(e.getClass().getName() + "   " + e.getMessage());
				}

				problemSet_.get(t).repair(newSolution, null);
				// problemSet_.get(t).evaluate(newSolution);
				solEvaluator[t].evaluate(newSolution);
				evaluations_[t]++;
				populationArray[t].add((newSolution));
			} // for
		}
	} // initPopulation
		// initPopulation

	public void initReferencePoint() throws ClassNotFoundException, JMException {
		for (int t = 0; t < problemSet_.countProblem(); t++) {
			Solution a = populationArray[t].get(0);
			for (int n = 0; n < problemSet_.get(t).getNumberOfObjectives(); n++) {
				ReferencePoint_[t][n] = a.getObjective(n);
				indArray_[t][n] = a;
			}
			for (int i = 1; i < populationSize_[t]; i++) {
				updateReference(populationArray[t].get(i), t);
			} // for
		}
	}

	public boolean matingSelection(Vector<Integer> pop_list, Vector<Integer> task_list, int tasknumber, int cid,
			int size) {
		int ss;
		int r;
		int p;
		int taskID;
		boolean flag;
		ss = sizeOfMatingNeiborhood_[tasknumber];
		boolean returnflag = false;

		// 他タスクを解く個体との交叉
		if (random.nextDoubleIE() < rmp) {
			returnflag = true;
			do {
				taskID = random.nextIntIE(problemSet_.countProblem());
			} while (taskID == tasknumber);

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
					if (pop_list.get(i) == p && task_list.get(i) == taskID) {
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
		// System.out.println("test");

		return returnflag;
	}

	void updateReference(Solution individual, int task) {
		for (int n = 0; n < problemSet_.get(task).getNumberOfObjectives(); n++) {
			if (comparator[task].compare(individual.getObjective(n), indArray_[task][n].getObjective(n))) {
				ReferencePoint_[task][n] = alpha[task] * individual.getObjective(n);
				indArray_[task][n] = individual;
			}
		}
	}

	// Updateした回数を返す
	int updateProblem(Solution indiv, int Tasks_id, int id) throws JMException {
		int size;

		int Updatetimes = 0;
		size = sizeOfNeiborhoodRepleaced_[Tasks_id];
		int[] perm = new int[size];

		// generate teh random permutation.
		Permutation.randomPermutation(perm, size, random);
		for (int i = 0; i < size; i++) {
			int k;
			k = neighborhood_[Tasks_id][id][perm[i]];
			comparator[Tasks_id].setWeightedVector(WeightedVector_[Tasks_id][k]);
			comparator[Tasks_id].setRefernecePoint(ReferencePoint_[Tasks_id]);

			if (comparator[Tasks_id].execute(indiv, populationArray[Tasks_id].get(k)) == 1) {
				populationArray[Tasks_id].replace(k, (indiv));
				Updatetimes = Updatetimes + 1;
			}
		}
		return Updatetimes;
	} // updateProblem

	public void initialize(int seed) throws ClassNotFoundException, JMException {
		super.initialize(seed);

		populationArray = new Population[numberOfTasks];
		evaluations_ = new int[numberOfTasks];
		ReferencePoint_ = new double[numberOfTasks][];
		indArray_ = new Solution[numberOfTasks][];
		cont = new boolean[numberOfTasks];
		for (int t = 0; t < numberOfTasks; t++) {
			evaluations_[t] = 0;
			populationArray[t] = new Population(populationSize_[t]);
			indArray_[t] = new Solution[problemSet_.get(t).getNumberOfObjectives()];
			ReferencePoint_[t] = new double[problemSet_.get(t).getNumberOfObjectives()];
			cont[t] = false;
		}

		setNeighborhood();

		initPopulation();

		initReferencePoint();

		double[] igd = new double[2];
		double[] Replacing = new double[2];
		updataTime = new int[problemSet_.countProblem()];
		updateOptunityTime = new int[problemSet_.countProblem()];

		List<List<double[]>> igdHistory = new ArrayList<List<double[]>>();
		List<List<double[]>> replacingRate = new ArrayList<List<double[]>>();

		int counter = 0;
		boolean[] cont = new boolean[numberOfTasks];

		ParentsSelectioncounter = new int[numberOfTasks];
		for (int t = 0; t < ParentsSelectioncounter.length; t++) {
			ParentsSelectioncounter[t] = 10;
		}
		/*
		 * for (int t = 0; t < problemSet_.countProblem(); t++) { List<double[]> d = new
		 * ArrayList<double[]>(); boolean[] calc = new
		 * boolean[populationArray[t].size()]; //IGD計算をする個体の選出するための配列 //SkillFactorの照合
		 * for (int p = 0; p < populationArray[t].size(); p++) { calc[p] = true; }
		 * //IGD計算 igd[0] = counter; igd[1] =
		 * (IGDCalclator.CalcNormalizeIGD_To_NonDominated(populationArray[t].
		 * getAllObjectives(), calc, IGDRef.getNormalizeRefs(t), IGDRef.getMaxValue(t),
		 * IGDRef.getMinValue(t), random)); replacingRate.add(new
		 * ArrayList<double[]>(d));
		 * 
		 * d.add(igd.clone()); igdHistory.add(new ArrayList<double[]>(d)); cont[t] =
		 * false; populationArray[t].printObjectivesToFile("output_"+ (t+1)+".dat"); }
		 */
		int totalPopulationSize_ = 0;

		for (int t = 0; t < numberOfTasks; t++) {
			totalPopulationSize_ += populationSize_[t];
		}

		permutation = new int[totalPopulationSize_];

		Permutation.randomPermutation(permutation, totalPopulationSize_, random);
	}

	@Override
	public void recombination() throws JMException {
		Solution offspring;
		// STEP 2. Update

		int count = 0;
		for (int t = 0; t < problemSet_.countProblem(); t++) {
			updataTime[t] = 0;
			updateOptunityTime[t] = 0;
		}
		for (int t = 0; t < problemSet_.countProblem(); t++) {
			for (int i = 0; i < populationSize_[t]; i++) {
				int n = permutation[count++];
				// 1つ目の要素はタスク番号, 2つ目の要素は個体番号，
				int[] temp = getSolutionID(n, populationSize_);

				// or int n = i;
				int tasks = temp[0];
				n = temp[1];
				// STEP 2.1. Mating selection
				if (cont[tasks]) {
					continue;
				}

				Vector<Integer> parentsNumber = new Vector<Integer>();
				Vector<Integer> TaskNumber_List = new Vector<Integer>();

				// 他タスクを解く個体を選択したかどうか
				boolean selectOtherTasksFlag = matingSelection(parentsNumber, TaskNumber_List, tasks, n,
						numberOfParents_[tasks]);

				Solution[] parents = new Solution[numberOfParents_[tasks]];

				for (int k = 0; k < numberOfParents_[tasks]; k++) {
					parents[k] = populationArray[TaskNumber_List.get(k)].get(parentsNumber.get(k));
				}

				parentsNumber = null;
				TaskNumber_List = null;
				// Apply crossover
				offspring = crossover.crossover(parents)[0];

				offspring = mutation.mutation(offspring);

				problemSet_.get(tasks).repair(offspring, null);

				// problemSet_.get(tasks).evaluate();

				solEvaluator[tasks].evaluate(offspring);

				evaluations_[tasks]++;

				updateReference(offspring, tasks);

				int updatetime = updateProblem(offspring, tasks, n);

				if (selectOtherTasksFlag) {
					updataTime[tasks] = updatetime;
					updateOptunityTime[tasks] += sizeOfNeiborhoodRepleaced_[tasks];
				}

				if (evaluations_[tasks] == maxEvaluations[tasks]) {
					cont[tasks] = true;
				}
			}
		}

	}

	@Override
	public void nextGeneration() throws JMException {
		//
	}

	@Override
	public boolean terminate() {
		for (int i = 0; i < cont.length; i++) {
			if (!cont[i])
				return false;
		}

		return true;
	}

	@Override
	public int getEvaluations() {
		int evaluation = 0;
		for (int t = 0; t < populationArray.length; t++) {
			evaluation += evaluations_[t];
		}

		return evaluation;
	}

	/*
	 * this method does not return the exact number of generations in some case
	 * especially when the number of solutions assigned to each island is different.
	 */

	@Override
	public int getGeneration() {
		int size = 0;
		int evaluation = 0;
		for (int t = 0; t < populationArray.length; t++) {
			size += populationArray[t].size();
			evaluation += evaluations_[t];
		}

		return evaluation / size;
	}

	@Override
	public Population getPopulation() {
		int size = 0;

		for (int t = 0; t < populationArray.length; t++) {
			size += populationArray[t].size();
		}
		Population ret = new Population(size);
		for (int t = 0; t < populationArray.length; t++) {
			ret.merge(populationArray[t]);
		}
		return ret;
	}

	@Override
	protected void buildImpl(CommandSetting s)
			throws ReflectiveOperationException, NamingException, IOException, notFoundException, JMException {

		s.putForce(ParameterNames.IS_MULTITASK, isMultitask);
		problemSet_ = s.get(ParameterNames.PROBLEM_SET);
		numberOfTasks = problemSet_.countProblem();
		isMax = s.getAsBArray(ParameterNames.IS_MAX);
		isNorm = s.getAsBArray(ParameterNames.IS_NORM);

		InnerWeightVectorDivision_ = s.getAsNArray(ParameterNames.INNER_DIVISION_SIZE);
		numberOfParents_ = s.getAsNArray(ParameterNames.N_OF_PARENTS);
		numberOfDivision_ = s.getAsNArray(ParameterNames.OUTER_DIVISION_SIZE);
		numberofObjectives_ = problemSet_.getNumberOfObjectives();
		alpha = s.getAsDArray(ParameterNames.MOEAD_ALPHA);
		sizeOfNeiborhoodRepleaced_ = s.getAsNArray(ParameterNames.SIZE_OF_NEIBORHOOD_At_UPDATE);
		sizeOfMatingNeiborhood_ = s.getAsNArray(ParameterNames.SIZE_OF_NEIBORHOOD_At_MATING);
		maxEvaluations = s.getAsNArray(ParameterNames.N_OF_EVALUATIONS);
		;
		rmp = s.get(ParameterNames.RMP);

		Object[] sss = s.getAsInstanceArray(ParameterNames.SCALAR_FUNCTION);
		Object[] ccc = s.getAsInstanceArray(ParameterNames.MOEAD_COMPARATOR);

		ScalarzingFunction_ = new ScalarzingFunction[numberOfTasks];
		comparator = new MOEADComparator[numberOfTasks];

		Object task = null;
		if (s.containsKe(ParameterNames.TASK_NUMBER))
			task = s.get(ParameterNames.TASK_NUMBER);

		for (int i = 0; i < sss.length; i++) {
			ScalarzingFunction_[i] = (ScalarzingFunction) sss[i];
			comparator[i] = (MOEADComparator) ccc[i];
			s.putForce(ParameterNames.TASK_NUMBER, i);
			finEvaluator[i].build(s);
			evoEvaluator[i].build(s);
		}
		s.putForce(ParameterNames.TASK_NUMBER, task);

		// ScalarzingFunction_ = (ScalarzingFunction[])
		// s.getAsInstanceArray(ParameterNames.SCALAR_FUNCTION);
		s.putForce(ParameterNames.SCALAR_FUNCTION, ScalarzingFunction_);
		s.putForce(ParameterNames.MOEAD_COMPARATOR, comparator);
		s.getAsNArray(ParameterNames.N_OF_EVALUATIONS);

		neighborhood_ = new int[problemSet_.countProblem()][][];
		WeightedVector_ = new double[problemSet_.countProblem()][][];

		populationArray = new Population[numberOfTasks];
		evaluations_ = new int[numberOfTasks];
		ReferencePoint_ = new double[numberOfTasks][];
		indArray_ = new Solution[numberOfTasks][];
		populationSize_ = new int[numberOfTasks];
		isInnerWeightVector_ = new boolean[numberOfTasks];
		for (int t = 0; t < numberOfTasks; t++) {
			s.putForce(ParameterNames.SCALAR_FUNCTION, ScalarzingFunction_[t]);
			s.putForce(ParameterNames.MOEAD_COMPARATOR, comparator[t]);
			s.put(ParameterNames.TEMP_TASK_NUMBER, t);
			ScalarzingFunction_[t].build(s);
			;
			comparator[t].build(s);

			populationSize_[t] = Calculator.conbination(numberofObjectives_[t] - 1 + numberOfDivision_[t],
					numberofObjectives_[t] - 1);
			isInnerWeightVector_[t] = problemSet_.get(t).getNumberOfObjectives() >= 5;
			if (problemSet_.get(t).getNumberOfObjectives() >= 5) {
				populationSize_[t] += Calculator.conbination(numberofObjectives_[t] - 1 + InnerWeightVectorDivision_[t],
						numberofObjectives_[t] - 1);
			}

		}
		s.put(ParameterNames.TEMP_TASK_NUMBER, Integer.MIN_VALUE);
		s.putForce(ParameterNames.SCALAR_FUNCTION, ScalarzingFunction_);
		s.putForce(ParameterNames.MOEAD_COMPARATOR, comparator);
	}

	public MultitaskMOEAD() {
		super();
		isMultitask = true;
	}

	@Override
	public Population execute() throws JMException, ClassNotFoundException {
		return null;
	}

} // MOEAD
