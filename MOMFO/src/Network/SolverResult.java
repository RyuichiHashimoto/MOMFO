package Network;


import java.io.BufferedWriter; 
import java.io.IOException;
import java.io.Serializable;
import java.io.Writer;

import javax.naming.NamingException;


import Network.GridComputing.RunSetting;
import Network.GridComputing.StreamProvider;
import lib.experiments.CommandSetting;
import lib.lang.NeedOverriden;

// TODO: implement AutoCloseable?
abstract public class SolverResult<T extends Solver> implements Buildable {

	protected T solver;
	protected Writer writer;


	@Override
	public void build(CommandSetting s) throws ReflectiveOperationException, NamingException, IOException {
		this.solver = s.get(RunSetting.SOLVER);
		writer = new BufferedWriter(((StreamProvider) solver.Setting.get(RunSetting.STREAM_PROVIDER)).getWriter(getOutputName(s)));
	}

	abstract protected String getOutputName(CommandSetting s) throws NamingException;

	@NeedOverriden public void beforeRun() throws IOException {};
	@NeedOverriden public void afterRun() throws IOException {};
	@NeedOverriden public void exceptionRise() throws IOException {};

	abstract public void save() throws IOException;
	abstract public void close() throws IOException;

	abstract public void save(CommandSetting s, Object ... results) throws IOException, NamingException;
	abstract public Serializable getMemento();

}
