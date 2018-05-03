package momfo.metaheuristics.parallelsmsemoa;

import java.util.concurrent.Callable;

import momfo.util.Front;
import momfo.util.Indicator.Hypervolume.WFGHV;

public class eachCalcHV implements Callable<Double> {

	final Front front_;

	final int index_;

	final double[] refs_;

	final boolean isMAX_;

	private static final double POWER = 2.0;

	public eachCalcHV(Front d,double[] refs,int index,boolean is){
		front_ = d;
		refs_ = refs;
		index_ = index;
		isMAX_ = is;
	}


	@Override
	public Double call() throws Exception {
		double ret = 0;
		if (refs_.length == 2){
			assert false  : "ParallelSMSEMOA 2OBJ is not implement";
		} else if (refs_.length != 2){
			WFGHV wfghv_ = new WFGHV(refs_);
			 ret = wfghv_.exclhv(front_,index_,refs_,isMAX_);
		}
		return ret;
	}


	public static double distance(double[] one,double[] two){
		double ret = 0;
		for(int i = 0 ;i<one.length;i++){
			ret += Math.pow(one[i] - two[i], POWER);
		}
		return Math.pow(ret,1d/POWER);
	}

}
