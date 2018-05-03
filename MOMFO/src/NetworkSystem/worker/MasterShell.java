package NetworkSystem.worker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.asg.cliche.Command;
import org.asg.cliche.Shell;
import org.asg.cliche.ShellFactory;
import org.asg.cliche.ShellManageable;

/*
 * 基本的にここを起動する．
 *
 *
 */



public class MasterShell implements ShellManageable{

	private static final String PROMPT = ">";

	public MasterShell(Master d){
		master_ = d;
	}

	private Master master_;
	private Shell shell_;

	@Command // two,
	public int add(int a, int b) {
	     return a + b;
	}

	@Command // two,
	public int cores() {
		return master_.CountSlaves();
	}
	@Command(description="Shows the status of the current tasks")
	public int Tasks() {
		return master_.CountTasks();
	}

	@Command // two,
	public void End() {

	}

	@Command // two,
	public void quit() {
		System.out.println("you want to quit this program(yes or no)");
		System.out.print(PROMPT);
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String d = null;
		try {
			d = br.readLine();
		} catch (IOException e) {
			System.out.println("please put quit command again if you want to finish this Program");
		}
		if(d.startsWith("yes") || d.endsWith("y")){
			System.exit(-1);
		}

	}

	@Command // two,
	public void HOST(){
		master_.subscriptAllHOSTNAME();
	}


	@Override
	public void cliEnterLoop() {
			System.out.println("Start of ShellScprint");
	}
	 public static void main(String[] args) throws IOException {
			Master master = new Master();
			MasterShell shell = new MasterShell(master);
	// 		master.addObserver(shell);
			new Thread(master).start();
			shell.shell_ = ShellFactory.createConsoleShell("",
					"Grid Computing System - Master Shell", shell);
			shell.shell_.commandLoop();
	 }

	@Override
	public void cliLeaveLoop() {
		System.out.println("End of ShellScprint");
	}





}
