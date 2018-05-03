package momfo.metaheuristics.parallelsmsemoaigd;

import java.util.concurrent.Callable;

import momfo.util.Front;

public class eachIGDCalclator implements Callable<Double> {

	final Front front_;

	final int index_;

	final double[][] refs_;

	private static final double POWER = 2.0;

	public eachIGDCalclator(Front d,double[][] refs,int index){
		front_ = d;
		refs_ = refs;
		index_ = index;
	}


	@Override
	public Double call() throws Exception {
		double sum = 0;
		for(int i=0;i<refs_.length;i++){
			double min = Double.MAX_VALUE;
			for(int ja = 0;ja <front_.size();ja++){
				if(index_ == ja) continue;
				min = Math.min(min, distance(refs_[i],front_.get(ja).get()));
			}
			sum += min;
		}
		return sum/refs_.length;
	}


	public static double distance(double[] one,double[] two){
		double ret = 0;
		for(int i = 0 ;i<one.length;i++){
			ret += Math.pow(one[i] - two[i], POWER);
		}
		return Math.pow(ret,1d/POWER);
	}

}
