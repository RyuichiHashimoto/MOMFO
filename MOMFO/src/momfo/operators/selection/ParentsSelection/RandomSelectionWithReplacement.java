package momfo.operators.selection.ParentsSelection;

import javax.naming.NameNotFoundException;

import lib.experiments.CommandSetting;
import momfo.core.Population;
import momfo.util.JMException;

public class RandomSelectionWithReplacement extends ParentsSelection{

	@Override
	public int selection(Population pop) throws JMException {
		int perm;
		perm = random.nextIntII(0, pop.size() - 1);

		return perm;
	}
	
	public void build(CommandSetting st) throws NameNotFoundException, JMException {
		super.build(st);
	}
	

}
