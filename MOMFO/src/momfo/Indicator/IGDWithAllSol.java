package momfo.Indicator;

import lib.math.Distance;

public class IGDWithAllSol extends IGDCalclator{

	@Override
	public double calcIGD(double[][]  objective ,boolean[] check ,double[][] referencePoint){
		double ret = 0;
		int refSize = referencePoint.length;

		for(int i = 0;i <refSize;i++){
			double min = Double.POSITIVE_INFINITY;
			for(int pop = 0;pop<objective.length;pop++){
				if(check[pop])
					min = Math.min(min,Distance.calcEuclid(objective[pop],referencePoint[i]));
			}
			ret += Math.pow(min,POWER);
		}
		return Math.pow(ret,1d/POWER)/refSize;
	}

	@Override
	public double calcNormalizeIGD(double[][]  objective, boolean[] checker , double[][] referencePoint,double[] maxValue,double[] minValue){
		assert maxValue.length == minValue.length;
//		assert objective[0].length == referencePoint[0].length;

		double[][] normalizeObjective = new double[objective.length][referencePoint[0].length];
		double ret = 0;
		int refSize = referencePoint.length;

		for(int pop = 0;pop<objective.length;pop++){
			for(int o = 0;o<objective[pop].length ;o++){
				normalizeObjective[pop][o] = (objective[pop][o] - minValue[o])/(maxValue[o] - minValue[o]);
			}
		}

		for(int i = 0;i <refSize;i++){
			double min = Double.POSITIVE_INFINITY;
			for(int pop = 0;pop<objective.length;pop++){
				if(checker[pop]){
					min = Math.min(min,Distance.calcEuclid(normalizeObjective[pop],referencePoint[i]));
				} else {
				}
			}
			ret += Math.pow(min,POWER);
		}
		return Math.pow(ret,1d/POWER)/refSize;

	}


}
