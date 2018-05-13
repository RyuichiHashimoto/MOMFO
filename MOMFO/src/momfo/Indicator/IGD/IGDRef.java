package momfo.Indicator.IGD;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.NameNotFoundException;

public class IGDRef {


	double[][] ref;
	
	private int taskCounter;

	private String  refDataFileName;
	private double[] maxValue;
	private double[] minValue;
	private double[][] NormalizeRefs;

	public void build(String filePath) throws NameNotFoundException, FileNotFoundException, IOException{
		refDataFileName = filePath;
		AddRefFiles(filePath);
	}

	public IGDRef(String path) throws NameNotFoundException, FileNotFoundException, IOException {
		build(path);
	}

	public double[][] getRefs(){
		return ref;
	}
	public double[][] getNormalizeRefs(){
		return NormalizeRefs;
	}
	public double[] getMaxValue(){
		return maxValue;
	}
	public double[] getMinValue(){
		return minValue;
	}

	public void AddRefFiles(String filename) throws FileNotFoundException, IOException{
		refDataFileName = (filename);
		double[][] ref_ = FileReading(filename,"	");
		ref = ref_.clone();
		setMaxAndMin(ref.clone());
		setNormalizeRef(ref,maxValue,minValue);
	}
	
	public void setNormalizeRef(double[][] ref,double[] max,double[] min){
		double[][] ret = new double[ref.length][ref[0].length];
		for(int i=0;i<ref.length;i++){
			for(int j=0;j<ref[i].length;j++){
				ret[i][j] = (ref[i][j] - min[j])/(max[j]-min[j]);
			}
		}
		NormalizeRefs = ret.clone();
	}


	public void setMaxAndMin(double[][] refs){
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
		 maxValue = max.clone();
		 minValue = min.clone();
	}

	public double[][] FileReading(String name,String DEMILITER) throws FileNotFoundException, IOException {
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
	
		
		
		

		System.out.println("end");

	}

}
