package momfo.operators.selection.ParentsSelection;

import java.util.HashMap;

import javax.naming.NameNotFoundException;

import lib.experiments.CommandSetting;
import lib.experiments.Exception.CommandSetting.notFoundException;
import momfo.core.Population;
import momfo.util.JMException;


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
	
	public void build(CommandSetting st) throws NameNotFoundException, JMException, notFoundException {
		super.build(st);
	}
	

}
