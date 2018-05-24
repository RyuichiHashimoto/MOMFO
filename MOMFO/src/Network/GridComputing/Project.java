package Network.GridComputing;

import static Network.RunSetting.*;

import java.io.IOException;
import java.io.Serializable;

import javax.naming.NamingException;

import lib.experiments.CommandSetting;
import lib.experiments.ParameterNames;
import lib.misc.SerialVersion;
import lib.misc.Variable;
import lib.util.StringUtility;

/**
 * This class manages a project (a task consists of subtasks).
 *
 * @author Hiroyuki Masuda
 */
public class Project extends Task {
	private static final long serialVersionUID = SerialVersion.UID("Project", 0);

	/** Splits a task into subtasks.
	 * @throws notFoundException */
	public static Task[] newProject(@Variable CommandSetting s, int eachRuns) throws IOException, NamingException, ReflectiveOperationException{
		int trials = s.getAsInt(NUMBER_OF_RUNS);
		int totalJobs = (int) Math.ceil((double) trials / eachRuns);
		Project project = new Project(s, totalJobs);

		int seedOffset = s.getAsInt(ParameterNames.SEED_OFFSET, 0);
		// split into subtasks
		s.putForce(NUMBER_OF_RUNS, Integer.toString(eachRuns));
		SubTask[] retval = new SubTask[totalJobs];
		for (int i = 0; i < retval.length; i++) {
			// change SEED_OFFSET and # trials if necessary
			s.putForce(ParameterNames.SEED_OFFSET, Integer.toString(seedOffset + i * eachRuns));
			if (i == retval.length - 1 && trials % eachRuns != 0) {
				s.putForce(NUMBER_OF_RUNS, Integer.toString(trials % eachRuns));
			}
			retval[i] = new SubTask(s, project, i);
		}

		for(int i = 0;i < retval.length;i++) {
			for (String key: retval[i].getSetting().keySet()) {
				if( !(retval[i].getSetting().get(key) instanceof Serializable)) {
					System.out.println(key + "	" + "key w");
				}
			}
		}

		return retval;
	}



	private int nRemainTasks_;
	private final Object[][] results_;

	private Project(CommandSetting s, int nSplit) throws NamingException, ReflectiveOperationException{
		super(s);
		nRemainTasks_ = nSplit;
		results_ = new Object[nSplit][];
	}

	private synchronized void finish(Master master, int subTaskID, Object[] mementos) throws IOException, NamingException,  ReflectiveOperationException {
		// save the result of a subtask
		results_[subTaskID] = mementos;
		if (--nRemainTasks_ == 0) {// all the subtasks finished
			// make StreamProvider
			StreamProvider sProvider;
			if (setting_.containsKey("dir")) {
				String dest = setting_.getAsStr("dir", null);
				sProvider = new FileStreamProvider(dest);
			} else {
				sProvider = new FileStreamProvider();
			}
			sProvider = new SpecialFileStreamProvider(sProvider);
			setting_.set(STREAM_PROVIDER, sProvider);

			// Write each Result
			int nResults = mementos.length;
			for (int rst = 0; rst < nResults; rst++) {
				// make an array of the mementos of a result
				Object[] mem = new Object[results_.length];
				for (int task = 0; task < mem.length; task++) {
					mem[task] = results_[task][rst];
				}
				save(master, resultName[rst], mem);
			}
		}
	}

	public int countTotalTasks() {
		return results_.length;
	}

	static class SubTask extends Task {
		private static final long serialVersionUID = SerialVersion.UID("SubTask", 0);

		private final int id_;
		private final Project project_;

		private SubTask(CommandSetting s, Project p, int id) throws NamingException {
			super(s);
			id_ = id;
			project_ = p;
		}

		@Override
		public void writeResult(Master master, Object[] result) throws IOException, NamingException, ReflectiveOperationException {
//			project_.finish(master, id_, result);
			super.writeResult(master, result);
		}

		@Override
		public String toString() {
			return super.toString()
				+" ("+ StringUtility.ordinal(id_ + 1) +" in "+
				project_.countTotalTasks() +")";
		}
	}
}
