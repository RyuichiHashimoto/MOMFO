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

package momfo.metaheuristics.MOMFEA;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.naming.NamingException;

import lib.experiments.CommandSetting;
import lib.experiments.Exception.CommandSetting.notFoundException;
import lib.io.output.fileSubscription;
import lib.math.Permutation;
import momfo.Indicator.IGDCalclator;
import momfo.Indicator.IGDRef;
import momfo.core.GeneticAlgorithm;
import momfo.core.Operator;
import momfo.core.Population;
import momfo.core.Problem;
import momfo.core.ProblemSet;
import momfo.core.Solution;
import momfo.operators.selection.ParentsSelection.BinaryTournament;
import momfo.operators.selection.ParentsSelection.ParentsSelection;
import momfo.util.JMException;
import momfo.util.Sort;
import momfo.util.Comparator.Comparator;
import momfo.util.Comparator.CrowdingDistanceComparator;
import momfo.util.Comparator.ScalarFitnessComparator;
import momfo.util.Comparator.NSGAIIComparator.NSGAIIComparator;
import momfo.util.Comparator.NSGAIIComparator.NSGAIIComparatorDominance;
import momfo.util.Comparator.NSGAIIComparator.NSGAIIComparatorNextGen;
import momfo.util.Ranking.NDSRanking;


public class MOMFEA extends GeneticAlgorithm {

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

	double rmp;
	int maxEvaluations_;

	int sizeOfNeiborhoodRepleaced_;
	int sizeOfMatingNeiborhood_;

	HashMap parameters;

	public MOMFEA(Problem problem) {
		super(problem);
	} // DMOEA

	public MOMFEA(ProblemSet problemSet_) {
		super(problemSet_);
		// TODO 自動生成されたコンストラクター・スタブ
	}

	private ParentsSelection selection_ ;//= new BinaryTournament();
	private Comparator comparator_binary;
	private  NSGAIIComparator  comparator_Dominance;
	private NSGAIIComparator comparator_nextGen;
	private boolean outNormal_;
	private String directoryname;

	public void setting() throws JMException{
		evaluations_  = 0;
		parameters.put("RandomGenerator", random);
		comparator_binary = new ScalarFitnessComparator(parameters);
		comparator_nextGen = new NSGAIIComparatorNextGen(parameters);
		comparator_Dominance = new NSGAIIComparatorDominance(parameters);
		maxEvaluations_ = ((Integer) this.getInputParameter("maxEvaluations")).intValue();
		isMAX_    = ((boolean)this.getInputParameter("ismax"));
		comparator_binary.setIs(isMAX_);
		comparator_nextGen.setIs(isMAX_);
		comparator_Dominance.setIs(isMAX_);
		selection_ = new BinaryTournament(null,comparator_binary);
	//	outNormal_ = ((boolean) this.getInputParameter("outputNormal"));
		outNormal_ = false;
		directoryname = ((String) this.getInputParameter("DirectoryName"));
		generation=0;
		populationSize_ = ((Integer)this.getInputParameter("populationSize"));
		rmp = ((double)this.getInputParameter("rmp"));
		crossover_ = operators_.get("crossover"); // default: DE crossover
		mutation_ = operators_.get("mutation"); // default: polynomial mutation
		int time = ((Integer) this.getInputParameter("times")).intValue();
	}
	public Population execute() throws JMException, ClassNotFoundException {
		setting();
		int time = ((Integer) this.getInputParameter("times")).intValue();

		//初期化
		initPopulation();


		double[] igd = new double[2];
		List<List<double[]>> igdHistory = new ArrayList<List<double[]>>();

		int counter = 0;
//		for(int t =0;t<problemSet_.countProblem();t++){
//			NDSRanking ranking = new NDSRanking(isMAX_);
//			ranking.setPop(population_);
//			ranking.Ranking();
//			for(int i=0;i<ranking.getworstrank();i++){
//				CrowdingDistance(ranking.get(i));
//			}
//			ranking = null;
//		}

		for(int t = 0 ; t < problemSet_.countProblem();t++){
			population_.printVariablesToFileWithSkillFactor(directoryname.replace("Task1","Task"+String.valueOf(t+1)) + "/InitialVAR/InitialVAR" + time + ".dat",t);
			population_.printObjectivesToFileWithSkillFactor(directoryname.replace("Task1","Task"+String.valueOf(t+1)) + "/InitialFUN/InitialFUN" + time + ".dat",t);
			List<double[]> d = new ArrayList<double[]>();
			boolean[] calc = new boolean[population_.size()]; //IGD計算をする個体の選出するための配列
			//SkillFactorの照合
			for(int p = 0 ; p < population_.size();p++){
				calc[p] = (population_.get(p).getSkillFactor() == t);
			}
			//IGD計算
//			igd[1] = (IGDCalclator.CalcIGD(population_.getAllObjectives(), calc,IGDRef.getRefs(t)));
//			igd[1] = (IGDCalclator.CalcNormalizeIGD(population_.getAllObjectives(), IGDRef.getNormalizeRefs(tasknumber),IGDRef.getMaxValue(tasknumber),IGDRef.getMinValue(tasknumber)));
			igd[0] = counter;
			igd[1] = (IGDCalclator.CalcNormalizeIGD_To_NonDominated(population_.getAllObjectives(),calc, IGDRef.getNormalizeRefs(t),IGDRef.getMaxValue(t),IGDRef.getMinValue(t),random));
			d.add(igd.clone());
			igdHistory.add(new ArrayList<double []>(d));
		}
		do {
			makeNextGeneration();
			counter++;
			merge_ = new Population(population_.size() + offSpring_.size());
			merge_.merge(population_);
			merge_.merge(offSpring_);

			GotoNextGeneration();

			for(int t = 0; t < problemSet_.countProblem();t++){
				boolean[] calc = new boolean[population_.size()]; //IGD計算をする個体の選出するための配列
				//SkillFactorの照合
				for(int p = 0 ; p < population_.size();p++){
					calc[p] = (population_.get(p).getSkillFactor() == t);
				}
				igd[0] = counter;
//				igd[1] = (IGDCalclator.CalcIGD(population_.getAllObjectives(), calc,IGDRef.getRefs(t)));
				igd[1] = (IGDCalclator.CalcNormalizeIGD_To_NonDominated(population_.getAllObjectives(),calc,IGDRef.getNormalizeRefs(t),IGDRef.getMaxValue(t),IGDRef.getMinValue(t),random));
				igdHistory.get(t).add(igd.clone());
			}
		} while (evaluations_ < maxEvaluations_ );

		for(int t = 0 ; t < problemSet_.countProblem();t++){

			population_.printVariablesToFileWithSkillFactor(directoryname.replace("Task1","Task"+String.valueOf(t+1)) + "/FinalVAR/FinalVAR" + time + ".dat",t);

			population_.printObjectivesToFileWithSkillFactor(directoryname.replace("Task1","Task"+String.valueOf(t+1)) + "/FinalFUN/FinalFUN" + time + ".dat",t);

			fileSubscription. printToFile(directoryname.replace("Task1","Task"+String.valueOf(t+1)) + "/IGDHistory/"+"IGDCalclator"+time+".dat",igdHistory.get(t));
		}//population_.printVariablesToFile("result/config/FinalVAR" + time + ".dat");
		return null;
	}


	public boolean makeNextGeneration() throws JMException{
		Solution[] parents = new Solution[2];
		offSpring_.clear();
		for (int p = 0; p < (populationSize_ / 2); p++) {

			parents[0] = population_.get((int) selection_.execute(population_));
			parents[1] = population_.get((int) selection_.execute(population_));
			//親個体のスキルファクター
			int[] sfs = new int[2];
			sfs[0] = parents[0].getSkillFactor();
			sfs[1] = parents[1].getSkillFactor();
			double rand = random.nextDoubleIE();

			Solution[] offSpring;

			//交叉と突然変異により生成
			if (sfs[0] == sfs[1] || rand < rmp) {
				offSpring = (Solution[]) crossover_.execute(parents);
				mutation_.execute(offSpring[0]);
				mutation_.execute(offSpring[1]);

				int p0 = random.nextIntIE(0, problemSet_.countProblem());
				int p1 = random.nextIntIE(0, problemSet_.countProblem());
			//	int p1 = 1 - p0;
				offSpring[0].setSkillFactor(sfs[p0]);
				offSpring[1].setSkillFactor(sfs[p1]);

				problemSet_.evaluate(offSpring[0]);
				problemSet_.evaluate(offSpring[1]);

				evaluations_ += 2;

			} else {
				offSpring = new Solution[2];
				offSpring[0] = new Solution(parents[0]);
				offSpring[1] = new Solution(parents[1]);

				mutation_.execute(offSpring[0]);
				mutation_.execute(offSpring[1]);

				offSpring[0].setSkillFactor(sfs[0]);
				offSpring[1].setSkillFactor(sfs[1]);


				problemSet_.evaluate(offSpring[0]);
				problemSet_.evaluate(offSpring[1]);

				evaluations_ += 2;
			}

			offSpring_.add(offSpring[0]);
			offSpring_.add(offSpring[1]);
			if (evaluations_ == maxEvaluations_){
				return true;
			}
		} // for

		return false;
	}



	public void initPopulation() throws JMException, ClassNotFoundException {
		population_ = new Population(populationSize_);
		offSpring_  = new Population(populationSize_);
		merge_ = new Population(populationSize_*2);
		for (int i = 0; i < populationSize_; i++) {
			Solution newSolution = new Solution(problemSet_,i%problemSet_.countProblem(),random);
			evaluations_++;
			problemSet_.evaluate(newSolution);
			population_.add(newSolution);
		}
	}



	private void GotoNextGeneration() throws JMException{

		SetScalarFitness(merge_);

		//factorialcostでソート
		merge_.ScalarFitnessSort();

		//ソート後merge
		population_.clear();
		for(int p = 0; p < populationSize_;p++){
			population_.add(merge_.get( p));
		}

	}

	private void SetScalarFitness(Population pop){

		//ランクの初期化
		for(int p = 0;p < pop.size();p++){
			for(int t = 0;t < problemSet_.countProblem();t++){
				pop.get(p).setFactorialRank(t,Integer.MAX_VALUE);
			}
		}


		//非劣解ランクと混雑距離の計算
		for(int t = 0; t< problemSet_.countProblem();t++){
			boolean[] judge = new boolean[pop.size()];
			for(int p = 0; p<pop.size();p++){
				judge[p] = (t == pop.get(p).getSkillFactor());
			}
			NDSRanking ranking = new NDSRanking(isMAX_,random);
			ranking.setPop(pop);
			ranking.Ranking(t);
			int counter = 0;
			for(int rank=0;rank<ranking.getworstrank();rank++){
				CrowdingDistance(ranking.get(rank));
				int[] perm = getRankbyCrowdingDistance(ranking.get(rank));
				for(int tes = 0; tes < perm.length;tes++){
					ranking.get(rank).get(perm[perm.length-tes-1]).setFactorialRank(t,counter);
					//Solution d = ranking.get(rank).get(perm[tes]);
					counter++;
				}
			}
		}

		//実際の割り当て
		for(int p = 0;p < pop.size();p++){
			int min =  Integer.MAX_VALUE;
			for(int t = 0 ; t < problemSet_.countProblem();t++){
				min = Integer.min(min, pop.get(p).getFactorialRank(t));
			}
			pop.get(p).setScalarFitness(1.0/(min+1));
		}
	//	System.out.println("test");
	}

	private int[] getRankbyCrowdingDistance(Population d){
		int[] perm = new int[d.size()];
		perm =  Permutation.setPermutation(perm);

		try {
			Sort.QuickSort(d.getAllSolutions(), perm, new CrowdingDistanceComparator(true,random), 0, perm.length-1);
		} catch (JMException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		return perm;
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
	//	System.out.println();
	//	System.out.println("関数内");

	}

	@Override
	public void recombination() throws JMException {
		Solution[] parents = new Solution[2];
		offSpring_.clear();
		for (int p = 0; p < (populationSize_ / 2); p++) {

			parents[0] = population_.get(parentsSelection.selection(population_));
			parents[1] = population_.get(parentsSelection.selection(population_));
			//親個体のスキルファクター
			int[] sfs = new int[2];
			sfs[0] = parents[0].getSkillFactor();
			sfs[1] = parents[1].getSkillFactor();
			double rand = random.nextDoubleIE();

			Solution[] offSpring;

			//交叉と突然変異により生成
			if (sfs[0] == sfs[1] || rand < rmp) {
				offSpring = (Solution[]) crossover.crossover(parents);
				offSpring[0] = mutation.mutation(offSpring[0]);
				offSpring[1] = mutation.mutation(offSpring[1]);
				
				int p0 = random.nextIntIE(0, problemSet_.countProblem());
				int p1 = random.nextIntIE(0, problemSet_.countProblem());
			//	int p1 = 1 - p0;
				offSpring[0].setSkillFactor(sfs[p0]);
				offSpring[1].setSkillFactor(sfs[p1]);

				problemSet_.evaluate(offSpring[0]);
				problemSet_.evaluate(offSpring[1]);

				evaluations_ += 2;

			} else {
				offSpring = new Solution[2];
				offSpring[0] = new Solution(parents[0]);
				offSpring[1] = new Solution(parents[1]);

				offSpring[0] = mutation.mutation(offSpring[0]);
				offSpring[1] = mutation.mutation(offSpring[1]);

				offSpring[0].setSkillFactor(sfs[0]);
				offSpring[1].setSkillFactor(sfs[1]);


				problemSet_.evaluate(offSpring[0]);
				problemSet_.evaluate(offSpring[1]);

				evaluations_ += 2;
			}

			offSpring_.add(offSpring[0]);
			offSpring_.add(offSpring[1]);
			if (evaluations_ == maxEvaluations_){
				break;
			}
		} // for

	}

	@Override
	public void nextGeneration() throws JMException {
		SetScalarFitness(merge_);

		//factorialcostでソート
		merge_.ScalarFitnessSort();

		//ソート後merge
		population_.clear();
		for(int p = 0; p < populationSize_;p++){
			population_.add(merge_.get( p));
		}
	}

	@Override
	public boolean terminate() {
		// TODO 自動生成されたメソッド・スタブ
		return false;
	}

	@Override
	public int getEvaluations() {
		// TODO 自動生成されたメソッド・スタブ
		return 0;
	}

	@Override
	public int getGeneration() {
		// TODO 自動生成されたメソッド・スタブ
		return 0;
	}

	@Override
	public Population getPopulation() {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	protected void buildImpl(CommandSetting s)
			throws ReflectiveOperationException, NamingException, IOException, notFoundException, JMException {
		// TODO 自動生成されたメソッド・スタブ
		
	}


}
