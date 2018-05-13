package momfo.Indicator.IGD;

import javax.naming.NameNotFoundException;

import lib.experiments.CommandSetting;
import lib.math.Distance;
import momfo.core.Population;
import momfo.util.Ranking.NDSRanking;

public class IGDWithNondominated extends IGDCalclator {


	public void build(CommandSetting st) throws NameNotFoundException{
		super.build(st);
	}

	@Override
	public double calcNormalizeIGD(double[][] objective, boolean[] checker,
			double[][] referencePoint, double[] maxValue, double[] minValue){
		double ret = 0;
		int refSize = referencePoint.length;
		int obj = referencePoint[0].length;

		NDSRanking rank = new NDSRanking(isMAXproblem_, random);

		int counter = 0;
		for (int i = 0; i < checker.length; i++) {
			if (checker[i])
				counter++;
		}
		double[][] checkedObj = new double[counter][obj];
		counter = 0;

		for (int i = 0; i < checker.length; i++) {
			if (checker[i]) {
				for (int o = 0; o < obj; o++) {
					checkedObj[counter][o] = objective[i][o];
				}
				counter++;
			}
		}
		rank.setPop(new Population(checkedObj));
		rank.Ranking();

		double[][] NonDominated = rank.get(0).getAllObjectives();

		double[][] normalizeObjective = new double[NonDominated.length][obj];

		for (int pop = 0; pop < NonDominated.length; pop++) {
			for (int o = 0; o < NonDominated[0].length; o++) {
				normalizeObjective[pop][o] = (NonDominated[pop][o] - minValue[o]) / (maxValue[o] - minValue[o]);
			}
			//				System.out.println(referencePoint[pop][0]*referencePoint[pop][0] + referencePoint[pop][1]*referencePoint[pop][1]);
		}

		for (int i = 0; i < refSize; i++) {
			double min = Double.POSITIVE_INFINITY;
			for (int pop = 0; pop < NonDominated.length; pop++) {
				min = Math.min(min, Distance.calcEuclid(normalizeObjective[pop], referencePoint[i]));
			}
			ret += Math.pow(min, POWER);
		}
		return Math.pow(ret, 1d/POWER) / refSize;
	}

	@Override
	public double calcIGD(double[][] objective, boolean[] check, double[][] referencePoint) {
		double[][] normalizeObjective = new double[objective.length][referencePoint[0].length];
		double ret = 0;

		NDSRanking rank = new NDSRanking(isMAXproblem_, random);
		rank.setPop(new Population(objective));
		rank.Ranking();

		int refSize = referencePoint.length;
		double[][] NonDominated = rank.get(0).getAllObjectives();

		for(int i = 0;i <refSize;i++){
			double min = Double.POSITIVE_INFINITY;
			for(int pop = 0;pop<objective.length;pop++){
					min = Math.min(min,Distance.calcEuclid(normalizeObjective[pop],referencePoint[i]));
			}
			ret += Math.pow(min, POWER);
		}
		return Math.pow(ret, 1d/POWER) / refSize;
	}


}
