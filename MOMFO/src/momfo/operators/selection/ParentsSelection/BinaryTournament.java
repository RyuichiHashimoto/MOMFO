package momfo.operators.selection.ParentsSelection;

import javax.naming.NameNotFoundException;

import lib.experiments.CommandSetting;
import lib.experiments.NeedParameters;
import lib.experiments.ParameterNames;
import momfo.core.Population;
import momfo.util.JMException;
import momfo.util.Comparator.Comparator;

public class BinaryTournament extends ParentsSelection{

	protected Comparator comparator;
	
	@NeedParameters({ParameterNames.BinaryTounamentComparator})
	public void build(CommandSetting parameters) throws NameNotFoundException, JMException {
		super.build(parameters);
		comparator = parameters.get(ParameterNames.BinaryTounamentComparator);
	}
	

	@Override
	public int selection(Population pop) throws JMException {
		int ret;
		int a = 0;
		int b = 0;
		int size = pop.size();
		a = random.nextIntIE(0, size);
		do {
			b = random.nextIntIE(0,size);
		}while( a == b);
		if(comparator.execute(pop.get(a), pop.get(b)) == 1){
			ret = a;
		} else {
			ret = b;
		}
		return ret;
	}



}
