package momfo.result;


import java.io.BufferedWriter;
import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.Serializable;
import java.io.Writer;

import javax.naming.NamingException;

import Network.GridComputing.StreamProvider;
import lib.experiments.CommandSetting;
import lib.experiments.ParameterNames;
import momfo.core.ProblemSet;

public class PopulationResult extends GAResult {
	public static final String DEFAULT_FILE_NAME = "population.mtd";
	protected boolean isMaximize;
	protected CharArrayWriter caWriter;

	@Override
	public void build(CommandSetting s) throws NamingException, IOException, ReflectiveOperationException {
		this.solver = s.get(ParameterNames.SOLVER);
		Writer w = ((StreamProvider) solver.setting.get(ParameterNames.STREAM_PROVIDER)).getWriter(getOutputName(s));
		writer = new BufferedWriter(w);
		if (w instanceof CharArrayWriter) caWriter = (CharArrayWriter) w;

		ProblemSet prob = s.get(ParameterNames.PROBLEM_SET);
//		int nObj = prob.nObjectives();
//		int nTrials = s.getAsInt(NTRIALS);
//		isMaximize = prob.isMaximize();
//		writer.write(Solutionset.MTDHeader(nObj, nTrials, isMaximize));
	}

	@Override
	public void afterTrial() throws IOException {
//		writer.write(solver.getPopulation().toText(isMaximize));
//		writer.write("\n");
	}

	@Override
	public void save() throws IOException {
		writer.flush();
	}


	@Override
	public void save(CommandSetting s, Object... results) throws IOException, NamingException {
		StreamProvider sp = s.get(ParameterNames.STREAM_PROVIDER);
		try (Writer w = new BufferedWriter(sp.getWriter(getOutputName(s)))) {
			String[] cast = new String[results.length];
			for (int i = 0; i < results.length; i++) {
				cast[i] = (String) results[i];
				w.write(cast[i]);
			}
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

	protected String getOutputName(CommandSetting s) throws NamingException {
		return null;
	}


	
}
