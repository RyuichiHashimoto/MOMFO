package momfo.metaheuristics.parallelsmsemoa;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import momfo.util.Front;

public class Allcalc {

	static double[] referencePoint;




	public static  void SetRef(double[] ref){
		referencePoint = ref;
	}


	public static double[] calclate(Front  arg,double[] ref,boolean is, int cpu){

		double[] ret = new double[arg.size()];
		
		
		ExecutorService ex = Executors.newFixedThreadPool(cpu);
		List<Future<Double>> hvs = new ArrayList<Future<Double>>();

		System.out.println("death");
		
		for(int i = 0; i <arg.size();i++){
			Future<Double> igd = (Future<Double>)(ex.submit(new eachCalcHV(arg,ref,i,is)));
			hvs.add(igd);
		}
		System.out.println("death");
		
		ex.shutdown ();

		for(int i=0;i<arg.size();i++){
			try {
				ret[i] = hvs.get(i).get();
			} catch (InterruptedException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		}
		System.out.println(ret[0]);
		return ret;
	}
}
