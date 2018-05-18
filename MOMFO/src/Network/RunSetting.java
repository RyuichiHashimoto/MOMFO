package Network;

import java.io.IOException;

import javax.naming.NameNotFoundException;
import javax.naming.NamingException;

import Network.GridComputing.FileStreamProvider;
import Network.GridComputing.StreamProvider;
import lib.experiments.CommandSetting;
import lib.experiments.ParameterNames;
import lib.experiments.Exception.CommandSetting.CannotConvertException;
import lib.experiments.Exception.CommandSetting.notFoundException;
import lib.lang.NotImplementYet;
import momfo.util.JMException;

public class RunSetting {
	public static final String OUTPUT_DIR = "dir";
	@Deprecated
	public static final String SETTING_WRITER = "info_writer";
	public static final String STREAM_PROVIDER = "__STREAM_PROVIDER__";
	public static final String NAME_SPACE = "nameSpace";

	//	public static final String SOLVER = "Solver";
	public static final String RESULT = "Result";
	public static final String RESULT_DELIMITER = ",";
	@Deprecated
	public static final String SETTING_FILE_NAME = "info.ini";
	@Deprecated
	public static final String DEFAULT_FILE_NAME = "population.mtd";

	public static final String NUMBER_OF_RUNS = "nTrials";
	public static final String SEED_OFFSET = "seedOffset"; // TODO: this also exists in GeneticAlgorithm

	private RunSetting() {
	}

	/**
	 * @param args
	 *  [0] : The setting file to read. <br>
	 *  rest : Specifies setting in the form of "-<i>settingKey settingValue</i>".
	 *         If the key is already specified in setting file (specified by args[0]),
	 *         the already associated value is overridden.
	 * @throws Throwable
	 */

	public static void main(String[] args) throws Throwable {
		CommandSetting s = new CommandSetting(args);
		Solver solver = buildSolver(s);
		System.out.println("running solver...");
		solver.throwingRun();
		System.out.println("finish!!");
	}

	public static Solver buildSolver(CommandSetting s)
			throws IOException, NamingException, ReflectiveOperationException, notFoundException,
			IllegalArgumentException, CannotConvertException, JMException, NotImplementYet {
		// make directory for output
		StreamProvider sp;

		if (s.containsKey(OUTPUT_DIR)) {
			sp = new FileStreamProvider(s.getAsStr(OUTPUT_DIR));
			checkmultitask(s);
		} else {
			String dir = makeOutpuDir(s);
			sp = new FileStreamProvider(dir);
			s.put(OUTPUT_DIR, dir);
		}
		s.set(STREAM_PROVIDER, sp);
		Solver solver = (Solver) s.getAsInstance(ParameterNames.SOLVER);
		s.putForce(ParameterNames.SOLVER, solver);

		solver.build(s);
		return solver;
	}

	private static void checkmultitask(CommandSetting s) throws NameNotFoundException {
		String ga = s.getAsStr(ParameterNames.GA);

		if(ga.contains("MultitaskMOEAD") || ga.contains("Island")){
			s.put(ParameterNames.IS_MULTITASK,true);
		} else {
			s.put(ParameterNames.IS_MULTITASK,false);
		}
	}

	public static String getPackageLast(String str) {
		String[] out = str.split(".");
		return out[out.length - 1];
	}

	public static String makeOutpuDir(CommandSetting st)
			throws notFoundException, NamingException, ReflectiveOperationException, NotImplementYet {
		String ga = st.getAsStr(ParameterNames.GA);
		String problemSet = st.getAsStr(ParameterNames.PROBLEM_SET);
		int taskNumber = st.getAsInt(ParameterNames.TASK_NUMBER);
		String ret = "result/";
		if (ga.equalsIgnoreCase("NSGAII")) {
			st.put(ParameterNames.IS_MULTITASK, false);
			ret += ga + "/" + problemSet + "/" + "Task" + String.valueOf(taskNumber + 1);
		} else if (ga.equalsIgnoreCase("MOEAD")) {
			st.put(ParameterNames.IS_MULTITASK, false);
			if (!st.getAsBool(ParameterNames.IS_MAX) && !st.getAsStr(ParameterNames.SCALAR_FUNCTION).endsWith("Min")) {
				st.putForce(ParameterNames.SCALAR_FUNCTION, st.getAsStr(ParameterNames.SCALAR_FUNCTION) + "ForMin");
			}
			ret += ga + "/" + st.getAsStr(ParameterNames.SCALAR_FUNCTION) +"/" + problemSet +  "/" + "Task"
					+ String.valueOf(taskNumber + 1);
		} else if (ga.equalsIgnoreCase("MultitaskMOEAD")) {
			st.put(ParameterNames.IS_MULTITASK, true);
			ret += ga + "/" + st.getAsStr(ParameterNames.SCALAR_FUNCTION) + "/" + problemSet;

		} else {
			throw new NotImplementYet();
		}
		System.out.println(ret);
		return ret;

	}

}
