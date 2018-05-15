package momfo.operators.selection.environmentalselection;

import javax.naming.NameNotFoundException;

import lib.experiments.CommandSetting;
import lib.experiments.ParameterNames;
import lib.experiments.Exception.CommandSetting.notFoundException;
import lib.math.Permutation;
import momfo.core.Population;
import momfo.metaheuristics.nsga2.CrowdingDistance;
import momfo.util.JMException;
import momfo.util.Comparator.NSGAIIComparator.NSGAIIComparatorWithRandom;
import momfo.util.Ranking.NDSRanking;

public class LabSpecifiedNSGAIISelection extends EnvironmentalSelection{

	NSGAIIComparatorWithRandom comparatorBinary;

	int populationSize = 0;

	public LabSpecifiedNSGAIISelection(){
		super();
	}

	public void build(CommandSetting st) throws NameNotFoundException, notFoundException {
		super.build(st);
		populationSize = st.getAsInt(ParameterNames.POPULATION_SIZE);
		comparatorBinary = st.get(ParameterNames.NSGAIIComparator);
	}

	@Override
	public Population getNextPopulation(Population merge_) throws JMException {
		NDSRanking Ranking = new NDSRanking(isMax, random);
		Ranking.setPop(merge_);

		Ranking.Ranking();

		Population retPop = new Population(populationSize);


		int rank = 0;
		while (retPop.size() + Ranking.get(rank).size() <= populationSize){
			Population d = Ranking.get(rank++);
			d.AssignID();
			CrowdingDistance.calc(d);
			retPop.merge(d);
		}

		if (populationSize != retPop.size()) {
			Population ret = Ranking.get(rank);
			int[] permu = new int[ret.size()];
			ret.AssignID();
			CrowdingDistance.calc(ret);
			Permutation.setPermutation(permu);

			Sort(permu, ret);
			int size = populationSize - retPop.size();
			Population d = new Population(size);
			for (int i = 0; i < size; i++) {
				d.add(ret.get(permu[i]));
			}

			permu = new int[d.size()];
			Permutation.setPermutation(permu);
			d.AssignID();
			CrowdingDistance.calc(d);
			Sort(permu, d);

			for (int i = 0; i < d.size(); i++) {
				retPop.add(d.get(permu[i]));
			}
		}
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
}
