package NetworkSystem.worker;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import experiments.Setting;

public class Slave extends Worker{

	private int ISDEAD;



	public static void main(String[] argv) throws Exception{
			Slave slave = new Slave();
			ObjectOutputStream oos;
			SlaveDeadHook deadhook;
			Socket socket = null;
			try {
				socket = new Socket(HOSTNAME,PORTINDEX);
			} catch (IOException e) {
				System.err.print("I can't found such a HOSTNAME or PORTINDEX");
				e.printStackTrace();
				System.exit(-1);
			}

			try {
				oos = new ObjectOutputStream(socket.getOutputStream());
				deadhook = new SlaveDeadHook(oos);
				Runtime.getRuntime().addShutdownHook(deadhook);
			} catch (IOException e) {
				e.printStackTrace();
				socket.close();
				return;
			}
			ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
			slave.runClient(ois,oos);
			Runtime.getRuntime().removeShutdownHook(deadhook);
			socket.close();

	}

	private void runClient(ObjectInputStream ois, ObjectOutputStream oos) throws ClassNotFoundException, IOException {
		setting = (Setting) ois.readObject();

		while(!setting.containsKey(TERMINATE_SIGNAL)){
			Throwable thrown = null;
			try{
				runEeperiments();
			} catch (Throwable d){
				thrown = d;
			}
			if (thrown == null){
				oos.writeObject(SUCCESS);

				assert false: "not yet";

			} else {
				oos.writeObject(EXECUTEEXCEPTION);
				oos.writeObject(thrown);
				thrown = null;
			}
			oos.flush();
			oos.reset();
			setting = (Setting)ois.readObject();
		}

	}
	private void runEeperiments() {
		// TODO 自動生成されたメソッド・スタブ

	}
	private Setting setting;
/*	private ArrayStreamProvider streamProvider_ = new ArrayStreamProvider();
	private Setting setting;
	private URLClassLoader loader_;
	private AlgorithmMain main_;
*/
	private void runSolver() {


	}


	@Override
	public void run() {

	}



	@Override
	public void Communication(ObjectInputStream one, ObjectInputStream two) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	//プログラムが終わったことを伝達するのがメイン
	//変更あり
	public void Send() {
/*		  ServerSocket ServerSocket = null;
		  byte[] buffer = new byte[BUFFER_SIZE];
		  Socket socket = null;
		  int test;
		  try {
			  ServerSocket = new ServerSocket(PORT_INDEX);
			  socket = ServerSocket.accept();

			  ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());//new ObjectOutputStream(test);

			  String line;

			  while((line = in.readLine()) != null){
				  System.out.println("recieve!!: " + line);
				  out.println(line);
				  System.out.println("Transmission!!: " + line);
			  }

		  }catch (IOException e){
			  e.printStackTrace();
			  return
		  } finally{
			  try {
			        if (socket != null) {
			          socket.close();
			        }
			  } catch (IOException e){}

			  try {
				  if(ServerSocket != null){
					  ServerSocket.close();
				  }
			  }catch (IOException e){}
		  }

*/

	}

	@Override
	public void recieve() {
		// TODO 自動生成されたメソッド・スタブ
	}

	public int getIsDEAD(){
		return ISDEAD;
	}

}

class SlaveDeadHook extends Thread implements NetWorkConnection {
	private ObjectOutputStream oos_;
	public boolean unexpected = true;

	public SlaveDeadHook(ObjectOutputStream oos) {
		oos_ = oos;
	}

	@Override
	public void run() {
		try {
			if (unexpected) {
				oos_.writeObject(DEAD);
				oos_.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				// closing Stream of a ServerSocket will also close the socket.
				oos_.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void Communication(ObjectInputStream one, ObjectInputStream two) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void Send() {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void recieve() {
		// TODO 自動生成されたメソッド・スタブ

	}
}
