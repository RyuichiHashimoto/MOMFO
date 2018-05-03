package momfo.metaheuristics.smsemoa_igd;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class FileReading {

	public static double[][] Reading(int Obj){
		int populationSize = 10000;
		double[][] ret = new double[populationSize][Obj];
		String name = "Data/IGDRef/"+ String.valueOf(Obj) +"OBJ.csv";
		String DEMILITER = ",";
		try(BufferedReader br = new BufferedReader(new FileReader(name))){
			for(int p = 0; p< populationSize;p++){
				String solStr = br.readLine();
				String[] solArrayStr = solStr.split(DEMILITER);
				for(int o = 0; o < Obj ; o++){
					ret[p][o] = Double.parseDouble(solArrayStr[o]);
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ret;
	}

	public static void main(String[] args){
		double[][] d = Reading(3);
		for(int i=0;i<d.length;i++){
			for(int j=0;j<d[i].length;j++){
				System.out.print(d[i][j] + "	");
			}
			System.out.println();
		}
	}
	
}
