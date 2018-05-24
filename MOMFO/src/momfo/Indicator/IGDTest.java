package momfo.Indicator;

import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import org.junit.jupiter.api.Test;

import lib.experiments.JMException;
import momfo.Indicator.Hypervolume.WFGHV;
import momfo.util.Front;
import momfo.util.POINT;

class IGDTest {

	@Test
	void Hypervolumetest() {
		for (int t = 0; t< fileName.length;t++) {
			double HV = test(true,fileName[t],ReferencePointData.get(fileName[t]));
			if( Math.abs(HV -HypervolumeData.get(fileName[t])) > 1.0E-14) {
				fail(" HyperVolume value is wrong. my program gets " + HV + " but correct answer is "+HypervolumeData.get(fileName[t]));
			}
			System.out.println(fileName[t] + " is clear");
		}
		System.out.println("all clear");

	}

	public static final String[] fileName = { "obj2_run100.csv", "o2_100.csv", "greedyselection3.csv",
			"3obj_100itm.csv", "3obj_to3.csv", "obj3.csv", "hv3.csv", "3obj1000p.csv", "obj4.csv", "obj6.csv",
			"obj10_run100.csv", "obj10_run100_1.csv" };
	public static final HashMap<String, Double> HypervolumeData = new HashMap<String, Double>() {
		{
			put("obj2_run100.csv", 3.898536078800001E8);
			put("o2_100.csv", 390826053.);
			put("greedyselection3.csv", 7889997319123.);
			put("3obj_100itm.csv", 5.6004552925E10);
			put("3obj_to3.csv", 8.015853341026E12);
			put("obj3.csv", 7.292285490125354E12);
			put("hv3.csv", 6.598839339754399e12);
			put("3obj1000p.csv", 1.0521125980832676E32);
			put("obj4.csv", 1.22004850299651088E17);
			put("obj6.csv", 3.69678751004069E25);
			put("obj10_run100.csv", 3.8474509823967244E42);

			put("obj10_run100_1.csv", 6.357573600954142E38);
		}
	};

	public static double[] getRef(int obj, int val) {
		double[] ret = new double[obj];
		for (int i = 0; i < ret.length; i++) {
			ret[i] = val;
		}
		return ret;
	}

	public static final HashMap<String, double[]> ReferencePointData = new HashMap<String, double[]>() {
		{
			put("obj2_run100.csv", getRef(2, 0));
			put("o2_100.csv", getRef(2, 0));
			put("greedyselection3.csv", getRef(3, 0));
			put("3obj_100itm.csv", getRef(3, 0));
			put("3obj_to3.csv", getRef(3, 0));
			put("obj3.csv", getRef(3, 0));
			put("hv3.csv", getRef(3, 0));
			put("3obj1000p.csv", getRef(3, 0));
			put("obj4.csv", getRef(4, 0));
			put("obj6.csv", getRef(6, 0));
			put("obj10_run100.csv", getRef(10, 0));
			put("obj10_run100_1.csv", getRef(10, 10000));
		}
	};

	public static double test(boolean it ,String na,double[] ref){
		String ddd = it ? "max": "min";

		String name = "Data\\hypervolumechecker\\" +ddd+"\\"+ na ;
//		String name = "Data\\IWFG\\" +ddd+"\\"+ na ;

		Front[] f_ =  FileReading(name);

		for(int i=0;i<f_.length;i++){
		//	f_[i].subscript();
		}

		int size = f_[0].getDimension();

		double[] ret = new double[size];

		for(int i=0;i<size;i++){
			ret[i] = 0.0;
		}

		double average = 0;
		double retval = 0;
		for(int i=0;i<f_.length;i++){
			WFGHV d = new WFGHV(ret,null);
			try {
				retval = d.wfg(f_[i], ref, it);
				average += retval / f_.length;
			} catch (JMException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
//			System.out.println((i+1) + "	" + retval);
		}
//		System.out.println("average" + "	" + average);
		return average;
	}

	public static Front[] FileReading(String name){
		Front[] ret = null;

		try(BufferedReader br = new BufferedReader(new FileReader(name))){
			String line;
			String[] S;
			int counter=0;
			int obj = Integer.parseInt(br.readLine());
			int trial = Integer.parseInt(br.readLine());
			ret = new Front[trial];
			POINT emp = new POINT(obj);



			for(int time = 0;time < trial;time++){
				int nOfP = Integer.parseInt(br.readLine());
				ret[time] = new Front();

				for(int p=0;p<nOfP;p++){
					line = br.readLine();
					S  = line.split("	");
					double[] mo = new double[obj];

					for(int dimension  =0;dimension<obj;dimension++){
						mo[dimension] = Double.parseDouble(S[dimension]);
					}

					ret[time].add(mo);

				}
			}
		} catch (IOException e){
			e.printStackTrace();
		}
		return ret;
	}

}
