package momfo.result;

import java.io.IOException;

import javax.naming.NameNotFoundException;
import javax.naming.NamingException;

import Network.SolverResult;
import lib.lang.NeedOverriden;
import momfo.core.GAFramework;
import momfo.core.Population;

abstract public class GAResult extends SolverResult<GAFramework> {
	@NeedOverriden public void beforeTrial() throws IOException {};
	@NeedOverriden public void afterInitialization() throws IOException, NameNotFoundException, NamingException {};
	@NeedOverriden public void afterGeneration() throws IOException {};
	@NeedOverriden public void afterTrial() throws IOException, NameNotFoundException, NamingException {}	
		// TODO 自動生成されたメソッド・スタブ	
//	abstract public void build(CommandSetting st)throws ReflectiveOperationException, NamingException, IOException, notFoundException;
	
	
	Population pop;
	Population[] popset;
	boolean isMultitask;
	
	public void build(Population pop_,Population[] popset_,boolean is){
		pop = pop_;;
		popset = popset_;
		isMultitask = is;
	}
}
