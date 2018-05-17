package momfo.result;


import java.io.BufferedWriter;
import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.Serializable;
import java.io.Writer;

import javax.naming.NameNotFoundException;
import javax.naming.NamingException;

import Network.GridComputing.StreamProvider;
import lib.experiments.CommandSetting;
import lib.experiments.ParameterNames;

public class ObjectiveSpaceResult extends GAResult {
	public static final String DEFAULT_FILE_NAME = "population.mtd";
	protected boolean isMaximize;
	protected CharArrayWriter caWriter;
	protected CommandSetting st;

	int nOfTrial = 0;
	int offSet;

	@Override
	public void build(CommandSetting s) throws NamingException, IOException, ReflectiveOperationException {
		this.solver = s.get(ParameterNames.SOLVER);
		st = s;
		
		if(s.containsKey(ParameterNames.SEED_OFFSET)){
			offSet = s.getAsInt(ParameterNames.SEED_OFFSET);
		} else {
			offSet = 0;
		}
	}

	@Override
	public void afterTrial() throws IOException, NameNotFoundException, NamingException {
		Writer w = ((StreamProvider) solver.setting.get(ParameterNames.STREAM_PROVIDER)).getWriter(getOutputFinName());
		writer = new BufferedWriter(w);
		writer.write(solver.getPopulation().objectiveToStr());
		writer.flush();
	}

	@Override
	public void afterInitialization() throws IOException, NameNotFoundException, NamingException {
		nOfTrial++;
		Writer w = ((StreamProvider) solver.setting.get(ParameterNames.STREAM_PROVIDER)).getWriter(getOutputIniName());
		writer = new BufferedWriter(w);
		writer.write(solver.getPopulation().objectiveToStr());
		writer.flush();
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

	protected String getOutputFinName() throws NamingException {
		return "FinalFUN"+"/" + "FinalFUN"+ (offSet + nOfTrial)+".dat";
	}

	protected String getOutputIniName() throws NamingException {
		return "InitialFUN"+"/" + "InitialFUN"+ (offSet + nOfTrial)+".dat";
	}

	@Override
	protected String getOutputName(CommandSetting s) throws NamingException {
		return null;
	}


}
