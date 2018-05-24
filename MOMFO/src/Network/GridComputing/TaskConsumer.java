package Network.GridComputing;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

import Network.Constants.NetworkConstants;



public class TaskConsumer extends Thread{


	private Master master;

	public TaskConsumer(Master master_){
		master = master_;
	}

	public void run(){

		try (ServerSocket ss = new ServerSocket(NetworkConstants.REQUEST_PORT_NUMBER)){
			while(master.isAlive()){
				try {
					acceptRequest(ss);
				} catch (IOException ioe) {
					ioe.printStackTrace();
				}
			}
		} catch (IOException e) {
			System.out.println("通信エラー発生");
		}
	}

	private void acceptRequest(ServerSocket ss) throws IOException {
		try (Socket s = ss.accept();) {
			if (!master.isAlive()) return;

			try (ObjectInputStream ois = new ObjectInputStream(s.getInputStream())) {

				Task[] tasks = (Task[]) ois.readObject();
				master.addTasks(tasks);
			} catch (ClassNotFoundException cnfe) {
				master.shutdown();
				throw new Error(cnfe);
			}
			return;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
