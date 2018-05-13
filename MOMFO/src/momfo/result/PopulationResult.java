package momfo.result;

import static mo.solver.ga.GAFramework.*;
import static optsys.platform.RunSetting.*;

import java.io.BufferedWriter;
import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.Serializable;
import java.io.Writer;

import javax.naming.NamingException;

import optsys.platform.RunSetting;
import optsys.platform.StreamProvider;
import lib.experiment.Setting;
import mo.problem.MOProblem;
import mo.solutionset.Solutionset;
import mo.solver.ga.GAFramework;

public class PopulationResult extends GAResult {
	public static final String DEFAULT_FILE_NAME = "population.mtd";
	protected boolean isMaximize;
	protected CharArrayWriter caWriter;

	@Override
	public void build(Setting s) throws NamingException, IOException, ReflectiveOperationException {
		this.solver = s.get(RunSetting.SOLVER);
		Writer w = ((StreamProvider) solver.setting.get(STREAM_PROVIDER)).getWriter(getOutputName(s));
		writer = new BufferedWriter(w);
		if (w instanceof CharArrayWriter) caWriter = (CharArrayWriter) w;

		MOProblem prob = s.get(GAFramework.PROBLEM);
		int nObj = prob.nObjectives();
		int nTrials = s.getAsInt(NTRIALS);
		isMaximize = prob.isMaximize();
		writer.write(Solutionset.MTDHeader(nObj, nTrials, isMaximize));
	}

	@Override
	public void afterTrial() throws IOException {
		writer.write(solver.getPopulation().toText(isMaximize));
		writer.write("\n");
	}

	@Override
	public void save() throws IOException {
		writer.flush();
	}

	@Override
	public void save(Setting s, Object... results) throws IOException, NamingException {
		StreamProvider sp = s.get(STREAM_PROVIDER);
		try (Writer w = new BufferedWriter(sp.getWriter(getOutputName(s)))) {
			String[] cast = new String[results.length];
			for (int i = 0; i < results.length; i++) {
				cast[i] = (String) results[i];
			}
			w.write(Solutionset.join(cast));
		}
	}

	@Override
	public Serializable getMemento() {
		return caWriter == null ? null : caWriter.toString();
	}

	@Override
	public void close() throws IOException {
		writer.close();
	}

	@Override
	protected String getOutputName(Setting s) throws NamingException {
		return s.getAsStr(NAME_SPACE, "") + DEFAULT_FILE_NAME;
	}
}
