package Network.GridComputing;


import static Network.GridComputing.ServerEvent.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.Observable;

import javax.naming.NamingException;

import lib.experiments.CommandSetting;


/**
 * This thread manages the slave in the remote service.
 * Events of the slave or worker thread are accessible by
 * the observer.
 *
 * @author Hiroyuki Masuda
 *
 */


public class WorkerThread extends Observable implements Runnable {
	static final String TERMINATE_SIGNAL = "###terminate_signal###";
	private static final CommandSetting TERMINATER = new CommandSetting().set(TERMINATE_SIGNAL, Boolean.TRUE);
	
	public final String HOST_NAME;
	private final Socket slaveSocket_;
	private final Master master_;
	/**
	 * Thread instance for this Runnable.
	 * Methods such as interrupt() are accessible via this field.
	 */
	private Thread thread_;
	private Task task_;
	private Throwable errorCause_;

	WorkerThread(Socket socket, Master master) {
		slaveSocket_ = socket;
		HOST_NAME = socket.getInetAddress().getHostName();
		master_ = master;

		for (int i = 0; i < master.getObservers().size(); i++) {
			addObserver(master.getObservers().get(i));
		}
	}

	/**
	 * Starts a new thread. In order to have instance of Thread,
	 * this method needs to be called to start the thread. That is
	 * this class must be started by this method.
	 */
	public Thread start() {
		thread_ = new Thread(this);
		thread_.start();
		return thread_;
	}

	public Thread getThread() {
		return thread_;
	}

	@Override
	public void run() {
		if (thread_ == null) {
			throw new IllegalStateException(
					"Call start() in this class not via Thread.");
		}
		
		try {
			runImpl();
		} catch (Throwable t) {
			// irregular termination
			errorCause_ = t;
			setChanged();
			notifyObservers(SLAVE_DEAD);			
		}
	}

	protected void runImpl() throws Exception {
		try (
				ObjectInputStream ois = new ObjectInputStream(slaveSocket_.getInputStream());
				ObjectOutputStream oos = new ObjectOutputStream(slaveSocket_.getOutputStream());){
			
			communicate(ois, oos);
			
			// kill this thread
			oos.writeObject(TERMINATER);
			oos.flush();
			// TODO: Sometimes slave won't receive TERMINATER
			try {Thread.sleep(10);} catch (InterruptedException e) {}
			slaveSocket_.shutdownOutput();
			setChanged();
			notifyObservers(SLAVE_FINISHED);
		} finally {
			slaveSocket_.close();
		}
	}

	private void communicate(ObjectInputStream ois, ObjectOutputStream oos) throws Exception {
		while (master_.isAlive()) {
			try {
				task_ = master_.getTask();
				setChanged();
				notifyObservers(TASK_START);
			} catch (InterruptedException e) {
				// master#shutdown() is invoked
				continue;
			}
			oos.writeObject(task_.getCommandSetting());
			oos.flush();
			receiveResult(ois);
			oos.reset();
		}
	}

	private void receiveResult(ObjectInputStream ois) throws IOException, NamingException, ReflectiveOperationException {
		switch ((SlaveResponse) ois.readObject()) {
		case FINISH:
			// read results
			int nResults = ois.readInt();
			Object[] results = new Object[nResults];
			for (int i = 0; i < nResults; i++) {
				results[i] = ois.readObject();
			}
		//	task_.writeResult(master_, results);
			setChanged();
			notifyObservers(TASK_DONE);
			break;
		case EXCEPTION:
		//	task_.errorHappened();
			errorCause_ = (Throwable) ois.readObject();
			setChanged();
			notifyObservers(TASK_THROW_EXCPTION);
			break;
		case DEAD:
			throw new SocketException("Slave Dead.");
		}
		task_ = null;
	}

	/**
	 * Whether this thread is executing the task.
	 * The thread is considered to be executing after taking a
	 * task from the master. Then, the thread finishes an execution
	 * after the thread receives all the results/exceptions.
	 */
	public boolean isExecuting() {
		return task_ != null;
	}

	public Task getTask() {
		return task_;
	}

	/**
	 * Return the cause of the death of slave or
	 * the exception thrown in the slave.
	 */
	public Throwable getErrorCause() {
		return errorCause_;
	}

	@Override
	public String toString() {
		return HOST_NAME +"_"+ getThread().getId();
	}
}
