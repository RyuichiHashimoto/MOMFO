package momfo.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class fileReader {
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
}
