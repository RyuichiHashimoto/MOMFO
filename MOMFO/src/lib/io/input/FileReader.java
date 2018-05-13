package lib.io.input;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import lib.io.FileConstants;

public class FileReader {

	public static List<String> FileReadingAsArray(String filePath) throws IOException {
		return FileReadingAsArray(new File(filePath));
	}

	public static List<String> FileReadingAsArray(File file) {
		List<String> ret = new ArrayList<String>(); 
		
		try {
			java.io.FileReader fileReader = new java.io.FileReader(file);
			BufferedReader br = new BufferedReader(fileReader);
			
			String line = br.readLine(); 
			
			while( line != null) {
				ret.add(line);
				line = br.readLine();
			}
			
			br.close();
			
		} catch (FileNotFoundException e) {
	
		} catch (IOException e1) {
			
		}

		return ret;
	}
	
	public static String FileReading(String filePath) {
		return FileReader.FileReading(new File(filePath));
	}

	public static String FileReading(File file) {
		String ret = "";
		
		try {
			java.io.FileReader fileReader = new java.io.FileReader(file);
			BufferedReader br = new BufferedReader(fileReader);
			
			String line = br.readLine(); 
			
			while( line != null) {
				ret = ret + line + FileConstants.NEWLINE_DEMILITER;
				line = br.readLine();
			}
			
			br.close();
		} catch (FileNotFoundException e) {
			ret = "null";
		} catch (IOException e1) {
			ret = "null";
		}
		return ret;
	}

	public static List<String> readDirective(String filePath){
		return readDirective(new File(filePath));
	}
	
	private static List<String> readDirective(File file) {
		try {
			BufferedReader br = new BufferedReader(new java.io.FileReader(file));
			
			
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		
		return null;
	}

	public static double[][] FileReadingAdDMat(String name,String DEMILITER) throws FileNotFoundException, IOException {
		double[][] ret = new double[0][0];
		try(BufferedReader br = new BufferedReader(new java.io.FileReader(name))){
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

	
	public static void main(String[] args) throws IOException {
		String args1 = FileReading("CommandSetting.st");	
		System.out.println(args1);
		
//		System.out.println(args1);
		System.out.println("end");
	}

}
