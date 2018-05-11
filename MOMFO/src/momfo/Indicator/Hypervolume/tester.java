package momfo.Indicator.Hypervolume;

import momfo.util.Front;
import momfo.util.JMException;

public class tester {




	public static void testIwfg(boolean it ,String na) {
		String ddd = it ? "max": "min";

		String name = "Data\\IWFG\\" +ddd+"\\"+ na ;

//		Front[] f_ =  FileReading(name);
		Front[] f_ =  null;

		for(int i=0;i<f_.length;i++){
		//	f_[i].subscript();
		}

		int size = f_[0].getDimension();


		double[] ret = new double[size];

		for(int i=0;i<size;i++){
			ret[i] = 0.6;
		}
		double[] contribution = new double[f_.length];
		double average = 0;
		double retval = 0;
		for(int i=0;i<f_.length;i++){
//			f_[i].subscript();
			WFGHV d = new WFGHV(ret,null);
			try {
				Front front_ = new Front(f_[i]);
				double dretval = d.wfg(front_,ret, it);

				for(int j = 0 ; j < f_[i].size();j++){
					Front sss = new Front(f_[i]);
					d = new WFGHV(ret,null);
					sss.remove(j);
					retval = dretval - d.wfg(sss, ret, it);
					contribution[i] =  retval;
					System.out.println((i+1) + "	" + contribution[i]);
				}

			} catch (JMException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
		}
	//	System.out.println("average" + "	" + average);


	}
/*
	public static double test(boolean it ,String na){
		String ddd = it ? "max": "min";
//		String name = "Data\\hypervolumechecker\\" +ddd+"\\"+ na ;
		String name = "Data\\IWFG\\" +ddd+"\\"+ na ;

		Front[] f_ =  FileReading(name);

		for(int i=0;i<f_.length;i++){
		//	f_[i].subscript();
		}

		int size = f_[0].getDimension();


		double[] ret = new double[size];

		for(int i=0;i<size;i++){
			ret[i] = 0.6;
		}

		double average = 0;
		double retval = 0;
		for(int i=0;i<f_.length;i++){
			WFGHV d = new WFGHV(ret);
			double[] retval_ = new double[f_[i].size()];
			try {
				Front front_ = new Front(f_[i]);

				for(int dd=0;dd<91;dd++){
					retval_[dd] = d.exclhv(front_,dd, ret, it);
					System.out.println(retval_[dd]);
				}
			} catch (JMException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();



			}
//			System.out.println((i+1) + "	" + retval);
		}
//		System.out.println("average" + "	" + average);
		return average;
	}
*/


}
