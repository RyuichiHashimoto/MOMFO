package momfo.core;

import javax.naming.NameNotFoundException;

import experiments.Setting;
import experiments.SettingWriter;
import momfo.util.DirectoryMaker;
import momfo.util.JMException;
import momfo.util.Random;

/*
 * ここでアルゴリズムの実行を行う．
 * このクラスを継承したクラスでは，インターフェース(experimentes.startクラス)からアルゴリズムへの橋渡しをする
 *
 *
 */







public abstract class AlgorithmMain implements Runnable{

	protected Algorithm algorithm;


	//0は独立探索，1はマルチタスク
	int AlgorithmType_;

	protected int tasknumber;

	protected Setting setting_;

	protected String DirectoryName;;

	public Algorithm getAlgorithm(){
		return algorithm;
	}
	
	public AlgorithmMain(Setting test){
		setting_ = test;
		try {
			tasknumber = test.getAsInt("TaskNumber");
		} catch (NameNotFoundException e) {
			tasknumber  = -1;
		}
	}

	public final void MakeDirectory(String name){
		DirectoryMaker.Make(name + "/" + "FinalFUN");
		DirectoryMaker.Make(name + "/" + "IGDHistory");
		DirectoryMaker.Make(name + "/" + "FinalVAR");
		DirectoryMaker.Make(name + "/" + "InitialFUN");
		DirectoryMaker.Make(name + "/" + "InitialVAR");
		DirectoryMaker.Make(name + "/" + "Animation");

		DirectoryMaker.Make(name + "/" + "Setting");
	}

	public final void write(){

		SettingWriter.clear();
		SettingWriter.merge(setting_.get());
		SettingWriter.write(DirectoryName + "/Setting");

	}

	public final void MakeDirectory() {
		System.out.println(DirectoryName);
		MakeDirectory(DirectoryName);
	}
	public final void MakeDirectory_multi(){
		System.out.println(DirectoryName);
		for(int t = 0 ; t < algorithm.getProblemSet().countProblem();t++){
			MakeDirectory(DirectoryName.replace("Task1", "Task" + String.valueOf(t+1)));

		}
	}
	public final void setSeed(int seed){
		Random.set_seed(seed);
	}

	//実際に実行する場所
	//seed値は試行ごとに更新する．
	//現試行番号 +　設定したseed値　になる．
	public final void execute(int nowTrial) throws NameNotFoundException, ClassNotFoundException, JMException{
		int counter=0;
		long initTime = System.currentTimeMillis();
		int  NumberOfRun = setting_.getAsInt("NumberOfTrial");
		do {
			counter++;

			setSeed(nowTrial + counter + setting_.getAsInt("Seed"));
			algorithm.setInputParameter("times",nowTrial+counter);
			algorithm.setTaskNumber(tasknumber);
			System.out.println("Task"+String.valueOf(tasknumber+1)+"	"+counter + "th start");
			long innerinitTime = System.currentTimeMillis();
			algorithm.execute();
			long estimatedTime = System.currentTimeMillis() - innerinitTime;
			System.out.println("Task"+String.valueOf(tasknumber+1)+"	"+counter + "thstart  trial time is"  + estimatedTime + "ms" );

		} while(counter<NumberOfRun);
		long estimatedTime = System.currentTimeMillis() - initTime;
		setting_.add("exuecutionTime",  estimatedTime +"ms" );
	};

	public void run(){

	}

	public final void run(int nowTrial) throws ClassNotFoundException, JMException, NameNotFoundException{
		setParameter();
		MakeDirectory();
		write();
		execute(nowTrial);
		write();
	};
	//test
	public final void runMultitask(int nowTrial) throws ClassNotFoundException, JMException, NameNotFoundException{
		setParameter();
		MakeDirectory_multi();
		write();
		execute(nowTrial);
		write();
	};

	public abstract void setParameter() throws NameNotFoundException, ClassNotFoundException, JMException;

}
