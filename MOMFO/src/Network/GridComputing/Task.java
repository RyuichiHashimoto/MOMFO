package Network.GridComputing;


import java.io.File;
import java.io.IOException;
import java.io.Serializable;

import javax.naming.NamingException;

import Network.SolverResult;
import lib.experiments.CommandSetting;
import lib.experiments.ParameterNames;
import lib.misc.SerialVersion;


public class Task implements Serializable {
	private static final long serialVersionUID = SerialVersion.UID("Task", 0);

	// TODO: retry count
	protected CommandSetting setting_;
	public final String taskName;
	protected String[] resultName;

	public Task(CommandSetting s) throws NamingException{
		setting_ = s.clone();
		String dirName = s.getAsStr("dir");
		taskName = new File(dirName).getName() +"/"+ s.getAsStr(ParameterNames.NAME_SPACE, "");
		resultName = s.getAsSArray(ParameterNames.RESULT, ParameterNames.SETTING_FILE_DEMILITER);
	}

	public CommandSetting getSetting() {
		return setting_;
	}

	public void writeResult(Master master, Object[] mementos) throws IOException, NamingException, ReflectiveOperationException {
		StreamProvider sProvider;
		if (setting_.containsKey("dir")) {
			String dest = setting_.getAsStr("dir", null);
			sProvider = new FileStreamProvider(dest);
		} else {
			sProvider = new FileStreamProvider();
		}
		sProvider = new SpecialFileStreamProvider(sProvider);
		setting_.set(ParameterNames.STREAM_PROVIDER, sProvider);
		for (int i = 0; i < mementos.length; i++) {
			save(master, resultName[i], mementos[i]);
		}
	}

	protected void save(Master master, String cls, Object ... mementos) throws IOException, NamingException,ReflectiveOperationException {

		SolverResult<?> result = master.loader.newInstance(cls);
		result.save(setting_, mementos);
		result = null; // for unloading. (this is maybe unnecessary)
	}

	public void errorHappened() {}

	@Override
	public String toString() {
		return taskName;
	}
}