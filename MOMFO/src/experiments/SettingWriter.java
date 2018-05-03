package experiments;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;

public class SettingWriter {


	protected static Map<String , Object> Parameters_ = new HashMap<String,Object>();



	public static void write(String name){
		File newDir = new File(name);


		newDir.mkdir();


		String filename = name + "/setting.st";




		try {
			FileOutputStream fos = new FileOutputStream(filename);
			OutputStreamWriter osw = new OutputStreamWriter(fos);
			BufferedWriter bw = new BufferedWriter(osw);

			bw.write("------------------Base Scription-------------");
			bw.newLine();
			bw.newLine();
			baseScript(bw);
			bw.newLine();

			bw.write("------------------suubScription Scription-------------");
			bw.newLine();
			bw.newLine();

			if (Parameters_ != null) {
				for(String key : Parameters_.keySet()){
					bw.write(key+ ":" + Parameters_.get(key));
					bw.newLine();
				}
			}
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	private static void baseScript(BufferedWriter test) throws IOException {
		oneScript(test,"Problem");
		oneScript(test,"Objectives");

		oneScript(test,"isMax");
		oneScript(test,"CrossoverName");
		oneScript(test,"CrossoverProbability");
		oneScript(test,"CrossoverDistribution");
		oneScript(test,"MutationName");
		oneScript(test,"MutationProbability");
		oneScript(test,"MutationDistribution");
		oneScript(test,"maxEvaluations");
		oneScript(test,"exuecutionTime");
		oneScript(test,"populationSize");
		oneScript(test,"Seed");

	}

	private static void oneScript(BufferedWriter br,String name) throws IOException{
		if(Parameters_.containsKey(name)){
			br.write(name + ":" + Parameters_.get(name));
			br.newLine();
			Parameters_.remove(name);
		}
	}


	public static void clear(){
		Parameters_.clear();
	}

	public static void merge(Map<String , Object> d){
		for(String key : d.keySet()){
			Parameters_.put(key, d.get(key));
		}
	}


	public static void add(String name, Object object) {
		if (Parameters_ == null) {
			Parameters_  = new HashMap<String, Object>();
		}
		Parameters_.put(name, object);
	}

	public static Object get(String name) {
		return Parameters_.get(name);
	}




}
