package Network.GridComputing;


import java.io.IOException;

import javax.naming.NamingException;

import Network.Solver;
import lib.experiments.CommandSetting;
import lib.experiments.Exception.CommandSetting.CannotConvertException;
import lib.experiments.Exception.CommandSetting.notFoundException;

public class RunSetting {
	public static final String OUTPUT_DIR = "dir";
	@Deprecated
	public static final String SETTING_WRITER = "info_writer";
	public static final String STREAM_PROVIDER = "__STREAM_PROVIDER__";
	public static final String NAME_SPACE = "nameSpace";

	public static final String SOLVER = "solver";
	public static final String RESULT = "result";
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
		CommandSetting s = new CommandSetting();
		Solver solver = buildSolver(s);
		solver.throwingRun();
	}


	public static Solver buildSolver(CommandSetting s) throws notFoundException, IllegalArgumentException, CannotConvertException, NamingException, IOException, ReflectiveOperationException  {
		// make directory for output
		StreamProvider sp;
		if (s.containKey(OUTPUT_DIR)) {
			sp = new FileStreamProvider(s.getAsStr(OUTPUT_DIR));
		} else {
			sp = new FileStreamProvider();
		}
		s.set(STREAM_PROVIDER, sp);

		// run
		Solver solver = (Solver) s.getAsInstance(SOLVER);
		s.put(SOLVER, solver);
		solver.build(s);

		return solver;
	}
}
