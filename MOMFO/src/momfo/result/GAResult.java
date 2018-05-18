package momfo.result;

import java.io.IOException;

import javax.naming.NameNotFoundException;
import javax.naming.NamingException;

import Network.SolverResult;
import lib.lang.NeedOverriden;
import momfo.core.GAFramework;

abstract public class GAResult extends SolverResult<GAFramework> {
	@NeedOverriden public void beforeTrial() throws IOException {};
	@NeedOverriden public void afterInitialization() throws IOException, NameNotFoundException, NamingException {};
	@NeedOverriden public void afterGeneration() throws IOException {};
	@NeedOverriden public void afterTrial() throws IOException, NameNotFoundException, NamingException {}	
}
