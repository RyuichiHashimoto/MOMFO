package momfo.result;

import java.io.IOException;

import Network.SolverResult;
import lib.lang.NeedOverriden;
import momfo.core.GAFramework;

abstract public class GAResult extends SolverResult<GAFramework> {
	@NeedOverriden public void beforeTrial() throws IOException {};
	@NeedOverriden public void afterInitialization() throws IOException {};
	@NeedOverriden public void afterGeneration() throws IOException {};
	@NeedOverriden public void afterTrial() throws IOException {};
//	abstract public void build(CommandSetting st)throws ReflectiveOperationException, NamingException, IOException, notFoundException;
}
