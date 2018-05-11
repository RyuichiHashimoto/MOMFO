package momfo.Indicator.IGD;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class IGDRef {

	private static List<double[][]> Refs = new ArrayList<double[][]>();

	private static int TaskCounter;

	private static List<String>  RefDataFileName = new ArrayList<String>();

	private static List<double[]> maxValue = new ArrayList<double []>();
	private static List<double[]> minValue= new ArrayList<double []>();
	private static List<double[][]> NormalizeRefs = new ArrayList<double[][]>();


	public static void clear(){
		Refs.clear();
		RefDataFileName.clear();
		NormalizeRefs.clear();
		maxValue.clear();
		minValue.clear();

		TaskCounter = 0;
	}

	public static double[][] getRefs(int key){
		return Refs.get(key);
	}
	public static double[][] getNormalizeRefs(int key){
		return NormalizeRefs.get(key);
	}
	public static double[] getMaxValue(int key){
		return maxValue.get(key);
	}
	public static double[] getMinValue(int key){
		return minValue.get(key);
	}


	public static int CountTask(){
		return TaskCounter;
	};

	public static void AddRefFiles(String filename) throws FileNotFoundException, IOException{
		RefDataFileName.add(filename);
		double[][] ref = FileReading(filename,"	");
		Refs.add(ref.clone());
		addMaxAndMin(ref.clone());
		addNormalizeRef(ref,maxValue.get(maxValue.size()-1),minValue.get(minValue.size()-1));
		TaskCounter++;
	}
	public static void addNormalizeRef(double[][] ref,double[] max,double[] min){
		double[][] ret = new double[ref.length][ref[0].length];
		for(int i=0;i<ref.length;i++){
			for(int j=0;j<ref[i].length;j++){
				ret[i][j] = (ref[i][j] - min[j])/(max[j]-min[j]);
			}
		}
		NormalizeRefs.add(ret.clone());
	}


	public static void addMaxAndMin(double[][] refs){
		int numberOfObjective = refs[0].length;

		double[] max = new double[numberOfObjective];
		double[] min = new double[numberOfObjective];

		for(int o = 0; o < numberOfObjective;o++){
			max[o] = Double.MIN_VALUE;
			min[o] = Double.MAX_VALUE;
		}
		for(int r = 0; r < refs.length;r++){
			for(int o = 0; o < numberOfObjective;o++){
				max[o] = Double.max(max[o], refs[r][o]);
				min[o] = Double.min(min[o], refs[r][o]);
			}
		}
		 maxValue.add(max.clone());
		 minValue.add(min.clone());
	}

	public static double[][] FileReading(String name,String DEMILITER) throws FileNotFoundException, IOException {
		double[][] ret = new double[0][0];

		try(BufferedReader br = new BufferedReader(new FileReader(name))){
			String objStr = br.readLine();
			String[] Data = objStr.split(DEMILITER);
//			System.out.println(Data[0]);
			double[] sol = new double[Data.length];
			int counter = 0;
			for(int i=0;i<sol.length;i++){
				try{
					sol[i] = Double.parseDouble(Data[i]);
					counter++;
				}catch(NumberFormatException e){
					break;
				}
			}
			int OBJ = counter;
			double[] sols = new double[OBJ];
			for(int i=0;i<OBJ;i++){
				sols[i] = sol[i];
			}

			List<double[] > Listpop = new ArrayList<double[]>();
			Listpop.add(sols.clone());
			while ( (objStr = br.readLine()) != null){
				Data = objStr.split(DEMILITER);
				for(int o = 0; o < OBJ ; o++){
					sols[o] = Double.parseDouble(Data[o]);
				}
				Listpop.add(sols.clone());
			}

			ret = new double[Listpop.size()][OBJ];
			for(int p = 0; p < Listpop.size(); p++){
				for(int o  = 0; o < OBJ ; o++){
					ret[p][o] = Listpop.get(p)[o];
				}
			}

		}
		return ret;

	}

	public static void main(String[] argv) throws FileNotFoundException, IOException{
		AddRefFiles("Data/PF/concave.pf");
		AddRefFiles("Data/PF/sphere.pf");

		for(int p=0;p<Refs.size();p++){
			double[][] ref = Refs.get(p);
			for(int r = 0; r < ref.length;r++){
				for(int o = 0; o < ref[r].length;o++){
						System.out.print(ref[r][o]+"	");
				}
				System.out.println();
			}
		}

		System.out.println("end");

	}

}
