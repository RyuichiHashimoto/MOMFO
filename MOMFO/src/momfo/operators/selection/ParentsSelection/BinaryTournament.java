package momfo.operators.selection.ParentsSelection;

import javax.naming.NameNotFoundException;
import javax.naming.NamingException;

import lib.experiments.CommandSetting;
import lib.experiments.JMException;
import lib.experiments.NeedParameters;
import lib.experiments.ParameterNames;
import momfo.core.Population;
import momfo.util.Comparator.Comparator;
import momfo.util.Comparator.NSGAIIComparator.NSGAIIComparatorWithRandom;

public class BinaryTournament extends ParentsSelection {

	protected Comparator comparator;

	@NeedParameters({ ParameterNames.BinaryTounamentComparator })
	public void build(CommandSetting s) throws NameNotFoundException, JMException{
		super.build(s);

		try {

			comparator = new NSGAIIComparatorWithRandom(s.getAsBool(ParameterNames.IS_MAX),random);
		} catch (NamingException e) {
			comparator = null;
			System.out.print(e.getMessage());
		} 
		;

	}

	@Override
	public int selection(Population pop) throws JMException {
		int ret;
		int a = 0;
		int b = 0;
		int size = pop.size();
		a = random.nextIntIE(0, size);
		do {
			b = random.nextIntIE(0, size);
		} while (a == b);
		if (comparator.execute(pop.get(a), pop.get(b)) == 1) {
			ret = a;
		} else {
			ret = b;
		}
		return ret;
	}

}
