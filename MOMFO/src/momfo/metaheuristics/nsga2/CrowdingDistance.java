package momfo.metaheuristics.nsga2;

import momfo.core.Population;
import momfo.core.Solution;

public class CrowdingDistance {

	public static void calc(Population a) {
		for (int i = 0; i < a.size(); i++) {
			a.get(i).setCrowedDistance(0.0);
		}
		int Objectives = a.get(0).getNumberOfObjectives();

		double max, min;
		double em;
		for (int key = 0; key < Objectives; key++) {
			int[] e = a.sortObjectivereturnperm(key);
			a.get(e[0]).setCrowedDistance(Double.POSITIVE_INFINITY);
			a.get(e[a.size() - 1]).setCrowedDistance(Double.POSITIVE_INFINITY);
			max = a.get(e[a.size() - 1]).getObjective(key);
			min = a.get(e[0]).getObjective(key);
			if (max - min < 1.0E-14) {
				continue;
			}

			for (int n = 1; n < a.size() - 1; n++) {
				Solution sp = a.get(e[n]);
				em = sp.getCrowdDistance_();
				em = em + (a.get(e[n + 1]).getObjective(key) - a.get(e[n - 1]).getObjective(key)) / (max - min);
				sp.setCrowedDistance(em);
			}
		}
	}
	
}
