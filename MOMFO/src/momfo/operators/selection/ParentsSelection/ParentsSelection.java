package momfo.operators.selection.ParentsSelection;

import javax.naming.NameNotFoundException;

import lib.experiments.CommandSetting;
import lib.lang.NeedOverriden;
import momfo.core.Population;
import momfo.core.Solution;
import momfo.operators.selection.Selection;
import momfo.util.JMException;

public abstract class ParentsSelection extends Selection{
	
	public abstract int selection(Population pop) throws JMException;
				
	@NeedOverriden
	public void build(CommandSetting st) throws NameNotFoundException, JMException {
		super.build(st);	
	}
	
}
