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

package momfo.metaheuristics.nsga2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.naming.NamingException;

import lib.experiments.CommandSetting;
import lib.experiments.ParameterNames;
import lib.experiments.Exception.CommandSetting.notFoundException;
import lib.math.Permutation;
import momfo.Indicator.IGD;
import momfo.Indicator.IGDRef;
import momfo.core.GeneticAlgorithm;
import momfo.core.Operator;
import momfo.core.Population;
import momfo.core.ProblemSet;
import momfo.core.Solution;
import momfo.operators.selection.ParentsSelection.ParentsSelection;
import momfo.util.JMException;
import momfo.util.Comparator.NSGAIIComparator.NSGAIIComparator;
import momfo.util.Ranking.NDSRanking;


public class NSGA2 extends GeneticAlgorithm {

	/**
	 * Stores the population size
	 */
	private int populationSize_;
	/**
	 * Stores the population
	 */
	private Population population_;

	private Population  offSpring_;

	private Population  merge_;

	private int generation;


	int evaluations_;

	Operator crossover_;
	Operator mutation_;


	int maxEvaluations_;

	int sizeOfNeiborhoodRepleaced_;
	int sizeOfMatingNeiborhood_;

	HashMap parameters;

	 // DMOEA

	List<double[]> igdHistory;
	private ParentsSelection selection_ ;//= new BinaryTournament();
	private NSGAIIComparator comparator_binary;
	private  NSGAIIComparator  comparator_Dominance;
	private NSGAIIComparator comparator_nextGen;
	private boolean outNormal_;
	private String directoryname;


	public void initPopulation() throws JMException, ClassNotFoundException {
		population_ = new Population(populationSize_);
 		for (int i = 0; i < populationSize_; i++) {
			Solution newSolution = new Solution(problem_,random);
			problem_.repair(newSolution,null);
			evaluations_++;
			problem_.evaluate(newSolution);
		 	population_.add(newSolution);
		}
	}
	private void Sort(int[] permutation ,Population pop) throws JMException{
		for(int i=0;i<pop.size();i++){
			for(int j=0;j<pop.size();j++){
				int p = permutation[i];
				int q = permutation[j];
				if(comparator_binary.execute(pop.get(p),pop.get(q)) == 1){
					int empty = permutation[i];
					permutation[i] = permutation[j];
					permutation[j] = empty;
				}
			}
		}
	}

	private void CrowdingDistance(Population a){
		for(int i = 0;i< a.size();i++){
				a.get(i).setCrowedDistance(0.0);
		}
		int Objectives = a.get(0).getNumberOfObjectives();

		double max,min;
		double em;
		for(int key = 0;key< Objectives; key++){
				int [] e = a.sortObjectivereturnperm(key);
				a.get(e[0]).setCrowedDistance(Double.POSITIVE_INFINITY);
				a.get(e[a.size() - 1]).setCrowedDistance(Double.POSITIVE_INFINITY);
				max = a.get(e[a.size() - 1]).getObjective(key);
				min = a.get(e[0]).getObjective(key);
				if (max - min < 1.0E-14){
					continue;
				}

			for(int  n = 1;n < a.size() -1 ;n++){
					Solution sp = a.get(e[n]);
					em = sp.getCrowdDistance_();
					em = em + (a.get(e[n + 1]).getObjective(key)  - a.get(e[n - 1]).getObjective(key))/(max - min);
					sp.setCrowedDistance(em);
			  }
		}
	}
	int time  = -1;
	@Override
	protected void buildImpl(CommandSetting setting) throws ReflectiveOperationException, NamingException, IOException, notFoundException {
		evaluations_  = 0;
		maxEvaluations_ =setting.getAsInt(ParameterNames.N_OF_EVALUATIONS); 
		isMAX_ =setting.getAsBool(ParameterNames.IS_MAX); 		
//		comparator_binary = new NSGAIIComparatorBinary(parameters);
		
		problem_ = ((ProblemSet)(setting.get(ParameterNames.PROBLEM_SET))).get(setting.get(ParameterNames.TASK_NUMBER));
		directoryname = "output";
		generation=0;
		populationSize_ = setting.getAsInt(ParameterNames.POPULATION_SIZE);
		time = setting.getAsInt("times");
	}

	public void initialize(int seed) throws ClassNotFoundException, JMException {
		super.initialize(seed);
		

		initPopulation();

//		population_.printVariablesToFile(directoryname + "/InitialVAR/InitialVAR" + time + ".dat");
//		population_.printObjectivesToFile(directoryname + "/InitialFUN/InitiaFUN" + time + ".dat");

		merge_ = new Population(populationSize_*2);
		igdHistory = new ArrayList<double[]>();
		double[] igd = new double[2];
		int counter = 0;
		NDSRanking ranking = new NDSRanking(isMAX_,random);
		ranking.setPop(population_);
		ranking.Ranking();
		for(int i=0;i<ranking.getworstrank();i++){
			CrowdingDistance(ranking.get(i));
		}
		ranking = null;
		igd[0] = counter;
		igd[1] = (IGD.CalcNormalizeIGD_To_NonDominated(population_.getAllObjectives(), IGDRef.getNormalizeRefs(tasknumber),IGDRef.getMaxValue(tasknumber),IGDRef.getMinValue(tasknumber),random));
//		igd[1] = (IGD.CalcIGD(population_.getAllObjectives(), IGDRef.getRefs(tasknumber)));

		igdHistory.add(igd.clone());
		
	}

	
	@Override
	public void recombination() throws JMException {
		offSpring_ = new Population(populationSize_);
		Solution[] parents = new Solution[2];
		Solution offspring = null;
		Solution offspring_offset = null;

		for(int i=0;i < populationSize_;i++){


			int one = parentsSelection.selection(population_);
			int two = parentsSelection.selection(population_);
			
			parents[0] = population_.get(one);
			parents[1] = population_.get(two);

			crossover.crossover(offspring,offspring_offset,parents[0],parents[1]);
			mutation.mutation(offspring_offset, (offspring));
			problem_.repair(offspring_offset,null);
			problem_.evaluate(offspring_offset);
			evaluations_ = evaluations_ + 1;

			offSpring_.add(offspring_offset);
			if(evaluations_ == maxEvaluations_){
				return;
			}
		}
		return;
	}

	@Override
	public void nextGeneration() throws JMException {

		NDSRanking Ranking = new NDSRanking(isMAX_,random);
		Ranking.setPop(merge_);

		Ranking.Ranking();

		population_ = new Population(populationSize_);


		int rank = 0;
		while (population_.size() + Ranking.get(rank).size()  <= populationSize_) {
			Population d = Ranking.get(rank++);
			d.AssignID();
			CrowdingDistance(d);
			population_.merge(d);
		}

		if(populationSize_ != population_.size()){
			Population ret = Ranking.get(rank);
			int []  permu = new int[ret.size()];
			ret.AssignID();
			CrowdingDistance(ret);
			Permutation.setPermutation(permu);

			Sort(permu,ret);
			int size = populationSize_ - population_.size();
			Population d = new Population(size);
			for(int i = 0;i<size;i++){
				d.add(ret.get(permu[i]));
			}

			assert d.size() + population_.size() == populationSize_ : "たのしいな" + ret.size() +"	"+ population_.size()+"	"+ populationSize_;
			permu = new int[d.size()];
			Permutation.setPermutation(permu);
			d.AssignID();
			CrowdingDistance(d);
			Sort(permu,d);

			for(int i=0;i<d.size();i++){
				population_.add(d.get(permu[i]));
			}
			}
			assert population_.size()  == populationSize_ :population_.size()  + "	" + populationSize_;
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
		return evaluations_/populationSize_;
	}

	@Override
	public Population getPopulation() {
		return population_;
	}
	@Override
	public Population execute() throws JMException, ClassNotFoundException {
		return null;
	}


}
