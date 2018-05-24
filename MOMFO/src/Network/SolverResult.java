package Network;


import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Serializable;
import java.io.Writer;

import javax.naming.NameNotFoundException;
import javax.naming.NamingException;

import Network.GridComputing.StreamProvider;
import lib.experiments.CommandSetting;
import lib.experiments.JMException;
import lib.experiments.ParameterNames;
import lib.lang.NeedOverriden;

// TODO: implement AutoCloseable?
abstract public class SolverResult<T extends Solver>implements Buildable{

	protected T solver;
	protected Writer writer;


	@Override
	public void build(CommandSetting s) throws ReflectiveOperationException, NamingException, IOException, IllegalArgumentException, JMException {
		this.solver = s.get(ParameterNames.SOLVER);
		writer = new BufferedWriter(((StreamProvider) solver.setting.get(RunSetting.STREAM_PROVIDER)).getWriter(getOutputName(s)));
	}

	abstract protected String getOutputName(CommandSetting s) throws NamingException;

	@NeedOverriden public void beforeRun() throws IOException {};
	@NeedOverriden public void afterRun() throws IOException {};
	@NeedOverriden public void exceptionRise() throws IOException {};

	abstract public void save() throws IOException, NameNotFoundException, NamingException;
	abstract public void close() throws IOException;

	abstract public void save(CommandSetting s, Object ... results) throws IOException, NamingException,  ReflectiveOperationException;
	abstract public Serializable getMemento();

}
