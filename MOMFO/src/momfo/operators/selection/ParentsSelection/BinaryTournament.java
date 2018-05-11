package momfo.operators.selection.ParentsSelection;

import static lib.experiments.ParameterNames.PROBLEM_SET;

import javax.naming.NameNotFoundException;
import javax.naming.NamingException;

import lib.experiments.CommandSetting;
import lib.experiments.NeedParameters;
import lib.experiments.ParameterNames;
import lib.experiments.Exception.CommandSetting.notFoundException;
import lib.math.BuildInRandom;
import momfo.core.Population;
import momfo.core.ProblemSet;
import momfo.util.JMException;
import momfo.util.Comparator.Comparator;

public class BinaryTournament extends ParentsSelection {

	protected Comparator comparator;

	@NeedParameters({ ParameterNames.BinaryTounamentComparator })
	public void build(CommandSetting s) throws NameNotFoundException, JMException, notFoundException {
		super.build(s);

		try {
			String genotypePack = s.get(ParameterNames.BinaryTounamentComparator);

			comparator = (Comparator) s.getToClass(ParameterNames.BinaryTounamentComparator, genotypePack)
					.getDeclaredConstructor(Boolean.class, BuildInRandom.class).newInstance(false,random);
		} catch (NamingException e) {
			comparator = null;
			System.out.print(e.getMessage());
		} catch (ReflectiveOperationException e) {
			comparator = null;
			System.out.println("elsdjfl;ajdf;lj");
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
