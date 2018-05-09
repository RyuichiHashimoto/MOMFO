package Network.GridComputing;

import java.io.IOException;
import java.util.Set;

import Network.GridComputing.asg.cliche.Command;
import Network.GridComputing.asg.cliche.Shell;
import Network.GridComputing.asg.cliche.ShellFactory;
import Network.GridComputing.asg.cliche.ShellManageable;

/**
 * Provide simple shell to interact with the master of
 * the grid computing system.
 *
 * @author Hiroyuki Masuda
 */
public class MasterShell extends WorkerObserver implements ShellManageable {
	public static final String PROMPT = "> ";
	/**
	 * Start master and interactive shell.
	 * @param args not used
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		Master master = new Master("d");
		MasterShell shell = new MasterShell(master);
		master.addObserver(shell);
		new Thread(master).start();
		shell.shell_ = ShellFactory.createConsoleShell("",
				"Grid Computing System - Master Shell", shell);
		shell.shell_.commandLoop();
	}


	private Master master_;
	private Shell shell_;

	public MasterShell(Master m) {
		master_ = m;
	}

	@Override
	public void cliEnterLoop() {}

	@Override
	public void cliLeaveLoop() {
		master_.shutdown();
	}

	@Command(description="Shows the status of the current tasks")
	public String task() {
		return "Remaining: "+ master_.countTasks() +"\n"+
				"Running: "+ master_.countExecuting();
	}

	@Command(description="Shows the number of the slaves")
	public int cores() {
		return master_.countSlaves();
	}

	@Command(description="Shows the list of the slaves")
	public String list() {
		StringBuilder sb = new StringBuilder();
		Set<WorkerThread> threads = master_.getSlaves();
		for (WorkerThread wt: threads) {
			sb.append(wt);
			sb.append("\n");
		}
		return sb.toString();
	}

	@Command(description="Shows the current state of the tasks and slaves")
	public String state() {
		StringBuilder sb = new StringBuilder();
		Set<WorkerThread> threads = master_.getSlaves();
		for (WorkerThread wt: threads) {
			if (wt.isExecuting()) {
				sb.append(String.format("%-15s %-10s %s", wt, "Running", wt.getTask()));
			} else {
				sb.append(String.format("%-15s %-10s", wt, "Idling"));
			}
			sb.append("\n");
		}
		return sb.toString();
	}


	// Below are derived methods from SereverObserver

	@Override
	void taskStarted(WorkerThread thread) {
		System.out.print("\nStarting : "+ thread.getTask() +"\n"+ PROMPT);
	}

	@Override
	public void taskFinished(WorkerThread thread) {
		System.out.print("\nFinished: "+ thread.getTask() +"\n"+ PROMPT);
	}

	@Override
	public void exceptionThrown(WorkerThread thread) {
		System.out.println("\n\u001b[1;31mFailed: \u001b[m"+ thread.getTask());
		thread.getErrorCause().printStackTrace();
		System.out.print(PROMPT);
	}

	@Override
	public void slaveFinished(WorkerThread thread) {
		System.out.println("\nFinished: "+ thread);
		System.out.print(PROMPT);
	}

	@Override
	public void slaveDead(WorkerThread thread) {
		System.err.println("\n\u001b[1;33mDisconnected: \u001b[m"+ thread);
		thread.getErrorCause().printStackTrace();
		System.out.print(PROMPT);
	}
}
