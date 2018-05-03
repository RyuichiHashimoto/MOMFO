package NetworkSystem.worker;

import java.io.ObjectInputStream;

public class Master extends Worker {

	private int numberOfConsumedTasks;

	private int numberOfTasks;

	@Override
	public void Communication(ObjectInputStream one, ObjectInputStream two) {
		// TODO 自動生成されたメソッド・スタブ

	}

	public int CountSlaves(){
		return 10;
	}


	@Override
	public void Send() {
	}

	@Override
	public void recieve() {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void run() {
		Send();
	}

	public int CountTasks() {
		return 0;
	}

	public void subscriptAllHOSTNAME() {

	}

	public Object getObservers() {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}


}
