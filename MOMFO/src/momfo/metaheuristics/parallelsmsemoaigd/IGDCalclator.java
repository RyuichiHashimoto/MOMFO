package momfo.metaheuristics.parallelsmsemoaigd;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import momfo.util.Front;

public class IGDCalclator {

	private static final double POWER = 2.0;

	public static double distance(double[] one,double[] two){
		double ret = 0;
		for(int i = 0 ;i<one.length;i++){
			ret += Math.pow(one[i] - two[i], POWER);
		}
		return Math.pow(ret,1d/POWER);
	}


	public static double calc(Front arg, double[][] igdFile){
		double sum = 0;
		for(int i=0;i<igdFile.length;i++){
			double min = Double.MAX_VALUE;
			for(int ja = 0;ja <arg.size();ja++){
				min = Math.min(min, distance(igdFile[i],arg.get(ja).get()));
			}
			sum += min;
		}
		return sum/igdFile.length;
	}

	public static double[] calclate(Front  arg,double[][] igdfile, int cpu){
		double[] ret = new double[arg.size()];

		ExecutorService ex = Executors.newFixedThreadPool(cpu);
		List<Future<Double>> igds = new ArrayList<Future<Double>>();
		for(int i = 0; i <arg.size();i++){
			Future<Double> igd = (Future<Double>)(ex.submit(new eachIGDCalclator(arg,igdfile,i)));
			igds.add(igd);
		}
		ex.shutdown ();

		for(int i=0;i<arg.size();i++){
				try {
					ret[i] = -1*igds.get(i).get();
				} catch (InterruptedException e) {
					// TODO 自動生成された catch ブロック
					e.printStackTrace();
				} catch (ExecutionException e) {
					e.printStackTrace();
				}
		}

		return ret;
	}


	public static List<Integer> getLowestContribution(double[] empt) {
		List<Integer> ret = new ArrayList<Integer>();

		ret.add(0);
		for(int i=1;i<empt.length;i++){
			if(empt[ret.get(0)] < empt[i]){
				ret.clear();
				ret.add(i);
			} else if (Math.abs(empt[i] -empt[ret.get(0)]) < 1.0E-14){
				ret.add(i);
			}
		}



		return ret;
	}


}
