package NetworkSystem.worker;

import javax.naming.NameNotFoundException;

import experiments.Setting;
import momfo.core.AlgorithmMain;
import momfo.main.AlgorithmMainFactory;
import momfo.util.JMException;

public class Task implements Runnable,Signal{

	private final Setting setting_;
	private AlgorithmMain algorithmMain;
	private int numberOfTrial;
	private int SIGNAL;

	public Task(Setting d){
		setting_ = d;
		String GAName = "";
		try {
			GAName = d.getAsStr("GeneticAlgorithm");
		} catch (NameNotFoundException e1) {
			GAName = "None";
		}

		try {
			numberOfTrial = setting_.getAsInt("nowTrial");
		} catch (NameNotFoundException e1) {
			numberOfTrial = 1;
		}

		try {
			algorithmMain =  AlgorithmMainFactory.getAlgorithmMain(GAName, setting_);
		} catch (JMException e) {
			e.printStackTrace();
			algorithmMain = null;
		}
	}

	@Override
	public void run() {
		if (algorithmMain != null){
			try {
				algorithmMain.run(numberOfTrial);
				SIGNAL = SUCCESS;
			} catch (ClassNotFoundException | NameNotFoundException | JMException e) {
				SIGNAL = EXECUTEEXCEPTION;
				e.printStackTrace();
			}
		}
	}

	@Override
	public int getSignal() {
		return SIGNAL;
	}

	public Setting getSetting(){
		return setting_;
	}













}


