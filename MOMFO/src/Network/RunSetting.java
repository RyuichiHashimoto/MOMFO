package Network;


import java.io.IOException;

import javax.naming.NamingException;

import Network.GridComputing.FileStreamProvider;
import Network.GridComputing.StreamProvider;
import lib.experiments.CommandSetting;
import lib.experiments.ParameterNames;
import lib.experiments.Exception.CommandSetting.CannotConvertException;
import lib.experiments.Exception.CommandSetting.notFoundException;
import lib.lang.NotImplementYet;
import momfo.core.ProblemSet;
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


	private RunSetting() {}

	/**
	 *
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


	public static Solver buildSolver(CommandSetting s) throws IOException, NamingException, ReflectiveOperationException, notFoundException, IllegalArgumentException, CannotConvertException, JMException, NotImplementYet {
		// make directory for output
		StreamProvider sp;

		if (s.containsKey(OUTPUT_DIR)) {
			sp = new FileStreamProvider(s.getAsStr(OUTPUT_DIR));
		} else {
			sp = new FileStreamProvider(makeOutpuDir(s));
		}
		s.set(STREAM_PROVIDER, sp);
		Solver solver = (Solver) s.getAsInstance(ParameterNames.SOLVER);
		s.putForce(ParameterNames.SOLVER, solver);

		solver.build(s);
		return solver;
	}

	/*
	 * This function returns the java code
	 */

	public static String getPackageLast(String str){
		String[] out = str.split(".");
		System.out.println(out.length);
		return out[out.length-1];
	}

	public static String makeOutpuDir(CommandSetting st) throws notFoundException, NamingException, ReflectiveOperationException, NotImplementYet{
//		String ga = getPackageLast(st.getAsStr(ParameterNames.GA));

		String ga = "NSGAII";
		String problemSet = "CIHS";
		int  taskNumber = st.getAsInt(ParameterNames.TASK_NUMBER);
		String ret = "result/";

		if(ga.equalsIgnoreCase("NSGAII")){
			ret += ga + "/" + problemSet + "/" + "Task" + String.valueOf(taskNumber+1);
		} else  if (ga.equalsIgnoreCase("MOEAD")){
			ret += ga + "/" + problemSet + "/" + st.getAsSArray(ParameterNames.SCALAR_FUNCTION, ",")[taskNumber] + "/"+"Task" + String.valueOf(taskNumber);
		} else if (ga.equalsIgnoreCase("MultitaskMOEAD")){
			ProblemSet p = st.getAsInstanceByName(ParameterNames.PROBLEM_SET, "momfo.problems.MOMFO.NTU");
			ret += ga + "/" + problemSet + "/" + st.getAsSArray(ParameterNames.SCALAR_FUNCTION, ",")[taskNumber] + "/"+"Task" + String.valueOf(taskNumber);
		} else {
			throw new NotImplementYet();
		}
		return ret;

	}

}
