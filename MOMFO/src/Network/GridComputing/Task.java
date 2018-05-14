package Network.GridComputing;

import static Network.RunSetting.*;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

import javax.naming.NamingException;

import Network.SolverResult;
import lib.experiments.CommandSetting;
import lib.experiments.Exception.CommandSetting.notFoundException; 


public class Task implements Serializable {

	// TODO: retry count
	protected CommandSetting CommandSetting_;
	public final String taskName;
	protected String[] resultName;

	public Task(CommandSetting s) throws NamingException, notFoundException {
		CommandSetting_ = s.clone();
		String dirName = s.getAsStr("dir");
		taskName = new File(dirName).getName() +"/"+ s.getAsStr(NAME_SPACE, "");
		resultName = s.getAsSArray(RESULT, RESULT_DELIMITER);
	}

	public CommandSetting getCommandSetting() {
		return CommandSetting_;
	}

	public void writeResult(Master master, Object[] mementos) throws IOException, NamingException, ReflectiveOperationException {
		StreamProvider sProvider;
		if (CommandSetting_.containsKey("dir")) {
			String dest = CommandSetting_.getAsStr("dir", null);
			sProvider = new FileStreamProvider(dest);
		} else {
			sProvider = new FileStreamProvider();
		}
		sProvider = new SpecialFileStreamProvider(sProvider);
		CommandSetting_.set(STREAM_PROVIDER, sProvider);
		for (int i = 0; i < mementos.length; i++) {
			save(master, resultName[i], mementos[i]);
		}
	}

	protected void save(Master master, String cls, Object ... mementos) throws IOException, NamingException, ClassNotFoundException, InstantiationException, IllegalAccessException {
		SolverResult<?> result = master.loader.newInstance(cls);
		result.save(CommandSetting_, mementos);
		result = null; // for unloading. (this is maybe unnecessary)
	}

	public void errorHappened() {}

	@Override
	public String toString() {
		return taskName;
	}
}