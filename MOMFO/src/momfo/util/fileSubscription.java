package momfo.util;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

public class fileSubscription {

	public static void printToFile(String path , double[]  cost, int k) {

		try {
			FileOutputStream fos = new FileOutputStream(path);
			OutputStreamWriter osw = new OutputStreamWriter(fos);
			BufferedWriter bw = new BufferedWriter(osw);
				for (int j = 0; j < cost.length; j++){
					bw.write(k*(j+1) + "	" + cost[j] + "");
					bw.newLine();
				}
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void printToFile(String path , double[][]  cost){
		try {
			FileOutputStream fos = new FileOutputStream(path);
			OutputStreamWriter osw = new OutputStreamWriter(fos);
			BufferedWriter bw = new BufferedWriter(osw);
				for (int j = 0; j < cost.length; j++){
					for(int k = 0;k<cost[j].length;k++){
						bw.write(cost[j][k] + "	");
					}
					bw.newLine();
				}
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void printToFile(String path, List<double[]> igdHistory) {
		try {
			FileOutputStream fos = new FileOutputStream(path);
			OutputStreamWriter osw = new OutputStreamWriter(fos);
			BufferedWriter bw = new BufferedWriter(osw);
				for (int j = 0; j < igdHistory.size(); j++){
					for(int k = 0;k<igdHistory.get(j).length;k++){
						bw.write(igdHistory.get(j)[k] + "	");
					}
					bw.newLine();
				}
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}




}
