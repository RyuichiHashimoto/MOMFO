package momfo.operators.selection.ParentsSelection;

import javax.naming.NameNotFoundException;

import lib.experiments.CommandSetting;
import lib.experiments.JMException;
import momfo.core.Population;


/*
 * 非復元抽出
 *
 */

public class RandomSelectionWithoutReplacement extends ParentsSelection {
	
	
	
	@Override
	public int selection(Population pop) throws JMException {
		assert pop.size() >1 : "population size is " + pop.size();
		int[] perm = new int[2];
		perm[0] = random.nextIntII(0, pop.size() - 1);
		return perm[0];
	}
	
	public void build(CommandSetting st) throws NameNotFoundException, JMException{
		super.build(st);
	}
	

}
