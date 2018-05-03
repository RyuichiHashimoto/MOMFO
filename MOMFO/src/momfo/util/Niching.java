package momfo.util;

import java.util.ArrayList;
import java.util.List;

import Jama.Matrix;
import momfo.core.Population;
import momfo.core.Solution;

public class Niching {
	Population population;
	Population lastFront;
	Population mgPopulation;

	Population union;

	int obj;
	int remain;

	boolean normalization;

	double[][] lambda;

	double[] zideal;

	double[] zmax;

	double[][] extremePoints;

	double[] intercepts;

	double[][] normalizedObjective;
	int[] ClasterIndex;
	double[] Distance;


	public Niching(Population population, Population lastFront,
			double[][] lambda, int remain,  boolean normalization) {

		this.population = population;
		this.lastFront = lastFront;

		this.remain = remain;
		this.lambda = lambda;

		this.normalization = normalization;

		this.mgPopulation = new Population(population.size() + lastFront.size());
		mgPopulation.merge(population);
		mgPopulation.merge(lastFront);

//				population.union(lastFront);


		if (population.size() > 0)
			this.obj = population.get(0).getNumberOfObjectives();
		else
			this.obj = lastFront.get(0).getNumberOfObjectives();

		normalizedObjective = new double[mgPopulation.size()][this.obj];
		ClasterIndex = new int[mgPopulation.size()];
		Distance = new double[mgPopulation.size()];
		mgPopulation.AssignID();

	}

	public void execute() {
		computeIdealPoint();

		if (normalization){
			computeMaxPoint();
			computeExtremePoints();
			computeIntercepts();
			normalizePopulation();
		}

		associate();
		assignment();
	}

	void computeIdealPoint() {
		zideal = new double[obj];

		for (int j = 0; j < obj; j++) {
			zideal[j] = Double.MAX_VALUE;

			for (int i = 0; i < mgPopulation.size(); i++) {
				if (mgPopulation.get(i).getObjective(j) < zideal[j])
					zideal[j] = mgPopulation.get(i).getObjective(j);
			}
		}

	}

	void computeMaxPoint() {
		zmax = new double[obj];

		for (int j = 0; j < obj; j++) {
			zmax[j] = Double.MIN_VALUE;

			for (int i = 0; i < mgPopulation.size(); i++) {
				if (mgPopulation.get(i).getObjective(j) > zmax[j])
					zmax[j] = mgPopulation.get(i).getObjective(j);
			}
		}
	}

	void computeExtremePoints() {
		extremePoints = new double[obj][obj];

		for (int j = 0; j < obj; j++) {
			int index = -1;
			double min = Double.MAX_VALUE;

			for (int i = 0; i < mgPopulation.size(); i++) {
				double asfValue = asfFunction(mgPopulation.get(i), j);
				if (asfValue < min) {
					min = asfValue;
					index = i;
				}
			}

			for (int k = 0; k < obj; k++)
				extremePoints[j][k] = mgPopulation.get(index).getObjective(k);
		}
	}

	void computeIntercepts() {

		intercepts = new double[obj];

		double[][] temp = new double[obj][obj];

		for (int i = 0; i < obj; i++) {
			for (int j = 0; j < obj; j++) {
				double val = extremePoints[i][j] - zideal[j];
				temp[i][j] = val;
			}
		}

		Matrix EX = new Matrix(temp);

		if (EX.rank() == EX.getRowDimension()) {
			double[] u = new double[obj];
			for (int j = 0; j < obj; j++)
				u[j] = 1;

			Matrix UM = new Matrix(u, obj);

			Matrix AL = EX.inverse().times(UM);

			int j = 0;
			for (j = 0; j < obj; j++) {

				double aj = 1.0 / AL.get(j, 0) + zideal[j];

				if ((aj > zideal[j]) && (!Double.isInfinite(aj)) && (!Double.isNaN(aj)))
					intercepts[j] = aj;
				else
					break;
			}
			if (j != obj) {
				for (int k = 0; k < obj; k++)
					intercepts[k] = zmax[k];
			}

		} else {
			for (int k = 0; k < obj; k++)
				intercepts[k] = zmax[k];
		}

	}

	void normalizePopulation() {
		for (int i = 0; i < mgPopulation.size(); i++) {
			Solution sol = mgPopulation.get(i);

			for (int j = 0; j < obj; j++) {

				double val = (sol.getObjective(j) - zideal[j])
						/ (intercepts[j] - zideal[j]);

				normalizedObjective[i][j] = val;
//				sol.setNormalizedObjective(j, val);
			}
		}
	}

	public void associate() {

		for (int k = 0; k < mgPopulation.size(); k++) {

			Solution sol = mgPopulation.get(k);

			double min = calVDistance(sol, lambda[0]);
			int index = 0;

			for (int j = 1; j < lambda.length; j++) {
				double dist = calVDistance(sol, lambda[j]);
				if (dist < min) {
					min = dist;
					index = j;
				}
			}
			ClasterIndex[k] = index;
			Distance[k] = min;
//			sol.setClusterID(index);
//			sol.setVDistance(min);
		}

	}

	public void assignment() {
		int[] ro = new int[lambda.length];
		boolean[] flag = new boolean[lambda.length];

		for (int k = 0; k < population.size(); k++) {
			ro[ClasterIndex[k]]++;
		}

		int num = 0;

		while (num < remain) {
			int[] perm = new Permutation().intPermutation(ro.length);

			int min = Integer.MAX_VALUE;
			int id = -1;

			for (int i = 0; i < perm.length; i++) {
				if ((!flag[perm[i]]) && (ro[perm[i]] < min)) {
					min = ro[perm[i]];
					id = perm[i];
				}
			}

			List<Integer> list = new ArrayList<Integer>();

			for (int k = 0; k < lastFront.size(); k++) {
				if (ClasterIndex[lastFront.get(k).getID()] == id)
					list.add(k);
			}


			if (list.size() != 0) {
				int index = 0;
				if (ro[id] == 0) {
					double minDist = Double.MAX_VALUE;

					for (int j = 0; j < list.size(); j++) {
						if (Distance[lastFront.get(list.get(j)).getID()] < minDist) {
							minDist = Distance[lastFront.get(list.get(j)).getID()];
							index = j;
						}
					}
				} else {
					index = Random.nextIntII(0, list.size() - 1);
				}

				population.add(lastFront.get(list.get(index)));
				ro[id]++;

				lastFront.remove(list.get(index));
				num++;
			} else {
				flag[id] = true;
			}

		}
	}

	double asfFunction(Solution sol, int j) {
		double max = Double.MIN_VALUE;
		double epsilon = 1.0E-6;

		for (int i = 0; i < obj; i++) {

			double val = Math.abs(sol.getObjective(i) - zideal[i]);

			if (j != i)
				val = val / epsilon;

			if (val > max)
				max = val;
		}

		return max;
	}




	public double calVDistance(Solution sol, double[] ref){
		if (normalization)
			return calNormlizedVDistance(sol, ref);
		else
			return calUnNormalizedVDistance(sol, ref);
	}


	public double calNormlizedVDistance(Solution sol, double[] ref) {

		double ip = 0;
		double refLenSQ = 0;

		for (int j = 0; j < obj; j++) {

			ip += normalizedObjective[sol.getID()][j] * ref[j];
			refLenSQ += (ref[j] * ref[j]);
		}
		refLenSQ = Math.sqrt(refLenSQ);

		double d1 = Math.abs(ip) / refLenSQ;

		double d2 = 0;
		for (int i = 0; i < sol.getNumberOfObjectives(); i++) {
			d2 += (normalizedObjective[sol.getID()][i]  - d1 * (ref[i] / refLenSQ))
					* (normalizedObjective[sol.getID()][i]  - d1 * (ref[i] / refLenSQ));
		}
		d2 = Math.sqrt(d2);

		return d2;
	}


	double calUnNormalizedVDistance(Solution sol, double[] ref){

		double d1, d2, nl;

		d1 = d2 = nl = 0.0;

		for (int i = 0; i < sol.getNumberOfObjectives(); i++) {
			d1 += (sol.getObjective(i) - zideal[i]) * ref[i];
			nl += (ref[i] * ref[i]);
		}
		nl = Math.sqrt(nl);
		d1 = Math.abs(d1) / nl;


		d2 =0;
		for (int i = 0; i < sol.getNumberOfObjectives(); i++) {

			d2 += ((sol.getObjective(i) - zideal[i]) - d1
					* (ref[i] / nl)) * ((sol.getObjective(i) - zideal[i]) - d1
							* (ref[i] / nl));
		}
		d2 = Math.sqrt(d2);



		return d2;
	}
}
