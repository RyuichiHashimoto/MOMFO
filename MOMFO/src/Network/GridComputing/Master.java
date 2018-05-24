package Network.GridComputing;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicInteger;

import Network.Constants.NetworkConstants;


public class Master extends WorkerObserver implements Runnable{
	public final MasterLoader loader;
	private static final String JAR_PATH = "jar:file:master/master.jar!/";
	private ServerSocket slaveAcceptor;
	private TaskConsumer consumer;
	private BlockingQueue<Task> tasks;
	private final ArrayList<WorkerObserver> observers;
	private final CopyOnWriteArraySet<WorkerThread> threads;
	private final AtomicInteger nExecuting;
	boolean isAlive;
	public boolean isAlive(){
		return isAlive;
	}

	public static void main(String[] args) throws IOException {
		System.out.println("dir");

		System.out.println("end");
	}

	public Master() throws IOException{
		loader = new MasterLoader(new URL(JAR_PATH));
		nExecuting = new AtomicInteger();
		threads = new CopyOnWriteArraySet<WorkerThread>();
		tasks = new LinkedBlockingDeque<Task>();
		observers = new ArrayList<WorkerObserver>();
		observers.add(this);
		slaveAcceptor = new ServerSocket(NetworkConstants.SLAVE_PORT_NUMBER);
	}

	public void addObserver(WorkerObserver obs){
		if(isAlive()){
			System.err.println("Failed to add observer");
			return;
		}
			observers.add(obs);
	}



	@Override
	public void run() {

		isAlive = true;
		consumer = new TaskConsumer(this);
		consumer.start();

		try {
			while (isAlive()) {
				try {

					// accept a new slave and create a worker thread
					WorkerThread wt = new WorkerThread(slaveAcceptor.accept(), this);
					threads.add(wt);
					wt.start();

				} catch (IOException e) {
					// failed to accept slave
					// or the stream is closed in shutdown() to finish the thread
				}
			}
		} finally {
			try {
				slaveAcceptor.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void addTask(Task  task){
		tasks.add(task);
	}
	public void addTasks(Task[]  task){
		for(int i = 0; i < task.length;i++) {
			tasks.add(task[i]);
		}
	}


	public int countTasks(){
		return tasks.size();
	}
	ArrayList<WorkerObserver> getObservers() {
		return observers;
	}

	public Task getTask() throws InterruptedException {
		return tasks.take();
	}

	public int countExecuting() {
		return nExecuting.get();
	}

	public int countSlaves() {
		return threads.size();
	}

	public Set<WorkerThread> getSlaves() {
		return threads;
	}
	public void shutdown() {
		isAlive = false;
		// close slaveAcceptor to terminate the master thread (run method in this class)
		try {
			close();
		} catch(IOException e) {
			e.printStackTrace();
		}
		// terminate task consumer
		try {
			// create a new socket to finish TaskConsumer
			Socket s = new Socket("localhost", NetworkConstants.REQUEST_PORT_NUMBER);
			s.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// kills idling threads
		for (WorkerThread wt : threads) {
			// TODO: if wt.isExecuting
			wt.getThread().interrupt();
		}
		// wait up to specified time until all thread dies
		for (int i = 0; i < 10; i++) {
			try {
				if (threads.size() == 0) return;
				Thread.sleep(100);
			} catch (InterruptedException e) {
				break;
			}
		}

		// TODO: do not use stop & bad response
		// terminates executing threads
		for (WorkerThread th: threads) {
			th.getThread().stop();
		}
	}

	public void taskStarted(WorkerThread thread) {
		nExecuting.incrementAndGet();
	}
	public void taskFinished(WorkerThread thread) {
		nExecuting.decrementAndGet();
	}
	public void exceptionThrown(WorkerThread thread) {
		nExecuting.decrementAndGet();
	}

	@Override
	void slaveFinished(WorkerThread thread) {
		threads.remove(thread);
	}

	void checkState() throws IllegalStateException {
		if (!consumer.isAlive()) {
			throw new IllegalStateException("Consumer dead");
		}
		for (WorkerThread wt: threads) {
			throw new IllegalStateException(wt +" has dead.");
		}
	}


	@Override
	void slaveDead(WorkerThread thread) {
		slaveFinished(thread);
		if (thread.isExecuting()) {
			nExecuting.decrementAndGet();
			addTask(thread.getTask());
		}
	}

	/** This method is not need to be called if shutdown() is called. */
	public void close() throws IOException {
		synchronized (slaveAcceptor) {
			if (slaveAcceptor.isClosed()) return;
			isAlive = false;
			slaveAcceptor.close();
		}
	}

	public String countFinishing() {

		return null;
	}

}
