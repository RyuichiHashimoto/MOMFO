package momfo.operators.selection.ParentsSelection;

import lib.experiments.CommandSetting;
import lib.experiments.JMException;
import momfo.core.Population;

public class NullSelection extends ParentsSelection {

	public NullSelection() {
		
	}
	
	@Override
	public void build(CommandSetting st) {
		return;
	}
	
	@Override
	public int selection(Population pop) throws JMException {
		return -1;
	}

}
