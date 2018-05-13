package momfo.result;

import static mo.solutionset.Solutionset.*;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Serializable;
import java.io.Writer;

import javax.naming.NamingException;

import lib.experiment.Setting;
import lib.io.output.NullWriter;
import lib.util.ArrayUtility;
import mo.problem.MOProblem;
import mo.solutionset.Solutionset;
import mo.solver.ga.GAFramework;
import mo.solver.ga.Individual;
import mo.util.Dominance;
import optsys.platform.RunSetting;
import optsys.platform.StreamProvider;

public class BetterSolutionsetResult extends GAResult {
	private static final NullWriter NULL_WRITER = new NullWriter();

	protected StreamProvider sp;
	protected int holdGen;
	protected Individual[] holdSet;
	protected String output = "run";
	protected String nameSpace;
	protected String header;
	protected String currentFile;
	protected boolean isMaximize;

	@Override
	public void build(Setting s) throws ReflectiveOperationException, NamingException, IOException {
		solver = s.get(RunSetting.SOLVER);
		sp = s.get(RunSetting.STREAM_PROVIDER);
		nameSpace = s.getAsStr(RunSetting.NAME_SPACE, "");
		if (nameSpace.length() != 0) nameSpace += "_";
		nameSpace += "bettersol/";
		MOProblem mop = solver.setting.get(GAFramework.PROBLEM);
		isMaximize = mop.isMaximize();
		header = Solutionset.MTDHeader(mop.nObjectives(), isMaximize);
		header += Solutionset.mappingToString(NON_DOMINATED, true);
		header += Solutionset.mappingToString(NON_OVERLAPPING, true);
	}

	@Override
	public void afterInitialization() throws IOException {
		writer = nextFile();
		writer.write(header);
		writer.write("\n");

		holdGen = 1;
		holdSet = ArrayUtility.deepClone(solver.getPopulation().getUniqueNonDominated());
	}

	protected Writer nextFile() throws IOException {
		currentFile = nameSpace + output + solver.getRun() + ".mtd";
		return new BufferedWriter(sp.getWriter(currentFile));
	}

	protected void writeSolution() throws IOException {
		writer.write(Solutionset.mappingToString("generation", holdGen));
		writer.write(Solutionset.toText(holdSet, isMaximize));
		writer.write("\n");
	}

	@Override
	public void afterGeneration() throws IOException {
		Individual[] ss = solver.getPopulation().getUniqueNonDominated();
		if (Dominance.isBetter(ss, holdSet)) {
			writeSolution();
			holdSet = ArrayUtility.deepClone(ss);
			holdGen = solver.getGeneration();
		}
	}

	@Override
	public void afterTrial() throws IOException {
		if (holdGen != 1) {
			writeSolution();
		}
		writer.close();
		// if writer is not overwritten, IOExceptions are thrown by calling save().
		writer = NULL_WRITER;
	}

	@Override
	public void exceptionRise() throws IOException {
		close();
	}

	@Override
	public void save() throws IOException {
		writer.flush();
	}

	@Override
	public void close() throws IOException {
		writer.close();
	}

	@Override
	public Serializable getMemento() {
		throw new UnsupportedOperationException("Not implemented yet.");
	}

	@Override
	public void save(Setting s, Object... results) throws IOException, NamingException {
		throw new UnsupportedOperationException("Not implemented yet.");
	}

	@Override
	protected String getOutputName(Setting s) throws NamingException {
		return "";
	}

}
