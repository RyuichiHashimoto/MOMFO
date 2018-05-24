package momfo.operators.selection.ParentsSelection;

import javax.naming.NameNotFoundException;

import lib.experiments.CommandSetting;
import lib.experiments.JMException;
import momfo.core.Population;

public class RandomSelectionWithReplacement extends ParentsSelection{

	@Override
	public int selection(Population pop) throws JMException {
		int perm;
		perm = random.nextIntII(0, pop.size() - 1);

		return perm;
	}
	
	public void build(CommandSetting st) throws NameNotFoundException, JMException{
		super.build(st);
	}
	

}
