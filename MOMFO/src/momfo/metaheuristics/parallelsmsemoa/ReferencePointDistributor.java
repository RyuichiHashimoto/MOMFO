package momfo.metaheuristics.parallelsmsemoa;

public class ReferencePointDistributor {

	public static double[] getReferencePoint(int key,int dimension){

		double[] ret = new double[dimension];

		double point = getValue(key);


		for(int i = 0 ;i < ret.length ; i++){
			ret[i] = point;
		}
		return ret;
	}


	private  static double getValue(int key){
		double ret = 0;
		switch (key){
		case 1: ret = 1; break;
		case 2: ret = 1.1; break;
		case 3: ret = 1.5; break;
		case 4: ret = 1000; break;
		default:
			assert true : "ReferencePointDisributor key がおかしんだよ" + key;
		}

		return ret;
	}

}
