package momfo.metaheuristics.NSGAIII;

import momfo.util.POINT; 

public class ASF {

	public static double ASF(POINT d,double[] weightVector){
		int size = weightVector.length;
		double ret = Double.NEGATIVE_INFINITY;
		assert weightVector.length == d.getDimension() :"重みベクトルと参照点の次元数が異なる";

		for(int i=0;i<size;i++){
			ret = Math.max(ret, d.get(i)/weightVector[i]);
		}

		return ret;
	}

	public static double ASF(double[] ds, double[] ref) {
		int size = ref.length;
		double ret = Double.NEGATIVE_INFINITY;
		assert ref.length == ds.length :"重みベクトルと参照点の次元数が異なる";

		for(int i=0;i<size;i++){
			ret = Math.max(ret, ds[i]/ref[i]);
		}

		return ret;
	}


}
