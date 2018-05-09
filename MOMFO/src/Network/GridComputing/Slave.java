package Network.GridComputing;

import static Network.GridComputing.RunSetting.SOLVER;
import static Network.GridComputing.RunSetting.STREAM_PROVIDER;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.ref.WeakReference;
import java.net.Socket;
import java.net.URL;
import java.net.URLClassLoader;

import Network.Solver;
import Network.SolverResult;
import Network.Constants.NetworkConstants;
import lib.experiments.CommandSetting;

/*
 * This class manage the assigned tasks to be performed from a Master node.
 *
 * @Auther Ryuichi Hashimoto
 */


public class Slave {

	public int numberOfCurrentExe;

	int nowExecounter = 0;

	int totalCounter = 0;

	CommandSetting setting;

	String masterHostName = "";


	public static void main(String[] args) throws Exception {
		Slave slave = new Slave();
		Socket socket = new Socket("Doctor", NetworkConstants.PORT_NUMBER);
//		Socket socket = new Socket(NetworkConstants.MASTER_HOST, NetworkConstants.PORT_NUMBER);
		ObjectOutputStream oos;
		SlaveDeadHook deadhook;
		// notify the unexpected death of salve to the master
		try {
			// TODO: If slave is dead during idle time,
			// SlaveResponse.DEAD is ignored by the WorkerThead
			oos = new ObjectOutputStream(socket.getOutputStream());
			deadhook = new SlaveDeadHook(oos);
			Runtime.getRuntime().addShutdownHook(deadhook);
		} catch (IOException e) {
			e.printStackTrace();
			socket.close();
			return;
		}
		ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
		slave.runClient(ois, oos);
		Runtime.getRuntime().removeShutdownHook(deadhook);
		socket.close();
	}

	/** Accepts a task and return the result. */
	private void runClient(ObjectInputStream ois, ObjectOutputStream oos) throws IOException, ReflectiveOperationException {
		setting = (CommandSetting) ois.readObject();

		// receive task
		while (!setting.containKey(NetworkConstants.TERMINATE_SIGNAL)) {
			// run the solver
			Throwable thrown = null;
			try {
				runSolver();
			} catch (Throwable t) {
				thrown = t;
			}

			// send results
			if (thrown == null) {
				// successfully finished
				oos.writeObject(SlaveResponse.FINISH);
				oos.writeInt(solver_.results.size());

				for (SolverResult<?> r: solver_.results) {
					oos.writeObject(r.getMemento());
					r.close();
				}

			} else {
				// error occurred during the run
				oos.writeObject(SlaveResponse.EXCEPTION);
				oos.writeObject(thrown);
				thrown = null; // for unloading
			}
			oos.flush();
			oos.reset();

			// prepare for the next run
			unload();
			setting = (CommandSetting) ois.readObject();  // accepts next setting
		}
	}

	private static final int TRIAL = 30;

	private void unload() throws IOException {
		//streamProvider_.clear();
//		streamProvider_ = new ArrayStreamProvider();
		setting.clear();
		setting = null;

		WeakReference<URLClassLoader> refCL = new WeakReference<URLClassLoader>(loader_);
		loader_.close();
		loader_ = null;
		Thread.currentThread().setContextClassLoader(null);
		for (int i = 0; i < TRIAL; i++) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {}
			System.gc();
			if (refCL.get() == null) return;
		}
		throw new IOException("Unloading failed.");
	}

	private ArrayStreamProvider streamProvider_ = new ArrayStreamProvider();
	private URLClassLoader loader_;
	private Solver solver_;

	/** Runs optimizer. */
	protected void runSolver() throws Throwable {
		loader_ = new URLClassLoader(veco(new URL("jar:file:emoa/emoa.jar!/")));
		Thread.currentThread().setContextClassLoader(loader_);

		solver_ = (Solver) loader_.loadClass(setting.getAsStr(SOLVER)).newInstance();
		setting.put(SOLVER, solver_);
		setting.put(STREAM_PROVIDER, streamProvider_);
		solver_.build(setting);
	    solver_.throwingRun();

	}
	public static <T> T[] veco(T ... v) {
		return v;
	}
}

enum SlaveResponse {
	FINISH, EXCEPTION, DEAD
}


/**
 * Send SlaveResponse#DEAD to the master when the slave is
 * terminated unexpectedly.
 */
class SlaveDeadHook extends Thread {
	private ObjectOutputStream oos_;
	public boolean unexpected = true;

	public SlaveDeadHook(ObjectOutputStream oos) {
		oos_ = oos;
	}

	@Override
	public void run() {
		try {
			if (unexpected) {
				oos_.writeObject(SlaveResponse.DEAD);
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
}

