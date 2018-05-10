package lib.math;

import java.util.ArrayList;

import momfo.core.Population;
import momfo.core.Solution;
import momfo.util.POINT;

public class Distance {

	public static double calc(double[] a, double[] b) {
		assert (a.length == b.length) : "calc in Distance Class :: two array have diffrecent length";

		double sum = 0;
		for (int i = 0; i < a.length; i++) {
			sum += (a[i] - b[i]) * (a[i] - b[i]);
		}
		return Math.sqrt(sum);
	}

	public static double calc(POINT a, POINT b) {
		assert a.getDimension() == b.getDimension() : "calc in Distance class :: two POINT have difference size";
		double sum = 0.0;

		for (int i = 0; i < a.getDimension(); i++) {
			sum += (a.get(i) - b.get(i)) * (a.get(i) - b.get(i));
		}
		return Math.sqrt(sum);
	}

	public static void calcCrowdingDistance(Population front) {
		for (int i = 0; i < front.size(); i++) {
			front.get(i).setCrowedDistance(0.0);
		}

		double max, min;
		double em = 0;
		int Objectives = front.get(0).getNumberOfObjectives();

		for (int key = 0; key < Objectives; key++) {
			int[] e = front.sortObjectivereturnperm(key);
			front.get(e[0]).setCrowedDistance(Double.POSITIVE_INFINITY);
			front.get(e[front.size() - 1]).setCrowedDistance(Double.POSITIVE_INFINITY);
			max = front.get(e[front.size() - 1]).getObjective(key);
			min = front.get(e[0]).getObjective(key);
			if (max - min < 1.0E-14) {
				continue;
			}

			for (int n = 1; n < front.size() - 1; n++) {
				Solution sp = front.get(e[n]);
				em = sp.getCrowdDistance_();
				em = em + (front.get(e[n + 1]).getObjective(key) - front.get(e[n - 1]).getObjective(key)) / (max - min);
				sp.setCrowedDistance(em);
			}
		}
	}

	public static void calcCrowdingDistance(ArrayList<Solution> front) {
		for (int i = 0; i < front.size(); i++) {
			front.get(i).setCrowedDistance(0.0);
		}
		int Objectives = front.get(0).getNumberOfObjectives();

		double max, min;
		double em;
		for (int key = 0; key < Objectives; key++) {
			int[] e = sortObjectivereturnperm(front,key);
			front.get(e[0]).setCrowedDistance(Double.POSITIVE_INFINITY);
			front.get(e[front.size() - 1]).setCrowedDistance(Double.POSITIVE_INFINITY);
			max = front.get(e[front.size() - 1]).getObjective(key);
			min = front.get(e[0]).getObjective(key);
			if (max - min < 1.0E-14) {
				continue;
			}

			for (int n = 1; n < front.size() - 1; n++) {
				Solution sp = front.get(e[n]);
				em = sp.getCrowdDistance_();
				em = em + (front.get(e[n + 1]).getObjective(key) - front.get(e[n - 1]).getObjective(key)) / (max - min);
				sp.setCrowedDistance(em);
			}
		}
	}

	public static int[] sortObjectivereturnperm(ArrayList<Solution> front,int key){
		int populationSize = front.size();

		int [] perm = new int[populationSize];

		Permutation.setPermutation(perm);
		for(int i=0;i<populationSize-1;i++){
			for(int j=i+1;j<populationSize;j++){
				int p = perm[i];
				int q = perm[j];
				Solution me = front.get(p);
				Solution you = front.get(q);
				if(me.getObjective(key) > you.getObjective(key)){
					perm[i] = q;
					perm[j] = p;
				} else if(Math.abs(me.getObjective(key) - you.getObjective(key)) <1.0E-14  &&  me.getID() > you.getID()){
					perm[i] = q;
					perm[j] = p;
				}
			}
		}
		return perm;
	}
}
