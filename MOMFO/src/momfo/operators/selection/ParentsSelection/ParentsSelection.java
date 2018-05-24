package momfo.operators.selection.ParentsSelection;

import javax.naming.NameNotFoundException;

import lib.experiments.CommandSetting;
import lib.experiments.JMException;
import lib.lang.NeedOverriden;
import momfo.core.Population;
import momfo.operators.selection.Selection;

public abstract class ParentsSelection extends Selection{

	public abstract int selection(Population pop) throws JMException;

	@NeedOverriden
	public void build(CommandSetting st) throws NameNotFoundException, JMException{
		super.build(st);
	}

}
