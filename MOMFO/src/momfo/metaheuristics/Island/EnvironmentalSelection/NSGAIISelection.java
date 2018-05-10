package momfo.metaheuristics.Island.EnvironmentalSelection;

import java.util.HashMap;

import lib.math.BuildInRandom;
import lib.math.Permutation;
import momfo.core.Population;
import momfo.core.Solution;
import momfo.util.JMException;
import momfo.util.Comparator.NSGAIIComparator.NSGAIIComparatorBinary;
import momfo.util.Ranking.NDSRanking;

public class NSGAIISelection extends EnvironmentalSelection{

	NSGAIIComparatorBinary comparatorBinary;

	public NSGAIISelection(int popsize,boolean isMax,HashMap<String, Object> parameter_,BuildInRandom random) {
		super(popsize,isMax,parameter_,random);
	}

	@Override
	public Population getNextPopulation(Population pop) throws JMException {
		comparatorBinary = new NSGAIIComparatorBinary(null);
		NDSRanking Ranking = new NDSRanking(isMax_,random);
		Ranking.setPop(pop);

		Ranking.Ranking();

		Population retPop = new Population(populationSize);


		int rank = 0;
		while (retPop.size() + Ranking.get(rank).size()  <= populationSize) {
			Population d = Ranking.get(rank++);
			d.AssignID();
			CrowdingDistance(d);
			retPop.merge(d);
		}

		if(populationSize != retPop.size()){
			Population ret = Ranking.get(rank);
			int []  permu = new int[ret.size()];
			ret.AssignID();
			CrowdingDistance(ret);
			Permutation.setPermutation(permu);

			Sort(permu,ret);
			int size = populationSize - retPop.size();
			Population d = new Population(size);
			for(int i = 0;i<size;i++){
				d.add(ret.get(permu[i]));
			}

//			assert d.size() + population.size() == populationSize : "たのしいな" + ret.size() +"	"+ population_.size()+"	"+ populationSize_;
			permu = new int[d.size()];
			Permutation.setPermutation(permu);
			d.AssignID();
			CrowdingDistance(d);
			Sort(permu,d);

			for(int i=0;i<d.size();i++){
				retPop.add(d.get(permu[i]));
			}
			}
			//assert population_.size()  == populationSize_ :population_.size()  + "	" + populationSize_;
		return retPop;
	}

	private void Sort(int[] permutation ,Population pop) throws JMException{
		for(int i=0;i<pop.size();i++){
			for(int j=0;j<pop.size();j++){
				int p = permutation[i];
				int q = permutation[j];
				if(comparatorBinary.execute(pop.get(p),pop.get(q)) == 1){
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

}
