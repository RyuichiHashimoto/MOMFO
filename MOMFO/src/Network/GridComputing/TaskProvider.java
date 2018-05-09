package Network.GridComputing;

import static Network.GridComputing.RunSetting.*;

import java.io.File;
import java.io.ObjectOutputStream;
import java.net.Socket;

import Network.Constants.NetworkConstants;
import lib.Structure.ObjectStack;
import lib.experiments.CommandSetting;
import lib.lang.PathTreat;

public class TaskProvider {
	public static final String SUBSETTING = "@";

	public static void main(String[] args) throws Exception {
		Task[] jobs = parseSetting(args);

		Socket ss = new Socket("localhost", NetworkConstants.PORT_NUMBER);
		try {
			ObjectOutputStream oos = new ObjectOutputStream(ss.getOutputStream());
			try {
				oos.writeObject(jobs);
				oos.flush();
			} finally {
				oos.close();
			}
		} finally {
			ss.close();
		}
	}

	public static Task[] parseSetting(String[] args) throws Exception {
		ObjectStack<String> arguments = new ObjectStack<String>(args, args.length);
		// parse arguments for SettingConsumer
		int every = 0;
		int split = 0;
		for (int i = 0; i < arguments.size(); i++) {
			if ("--every".equals(arguments.get(i))) {
				every = Integer.parseInt(arguments.get(i + 1));
				arguments.remove(i--, 2);
			} else if ("--split".equals(arguments.get(i))) {
				split = Integer.parseInt(arguments.get(i + 1));
				arguments.remove(i--, 2);
			}
		}
		if (every != 0 && split != 0) {
			throw new IllegalArgumentException(
					"Cannot specify both --every and --split.");
		}

		CommandSetting setting = new CommandSetting(arguments.toArray());

		// create job
		Task[] retval;
		if (split != 0) {
			int total = setting.getAsInt(NUMBER_OF_RUNS);
			every = (int) Math.ceil((double) total / split);
		}

		File current = new File(PathTreat.getCurrentDir(), setting.getAsStr("dir"));
		setting.set("dir", current.toString());
		if (every != 0) {
			retval = Project.newProject(setting, every);
		} else {
			retval = new Task[] {new Task(setting)};
		}
		return retval;
	}
}


