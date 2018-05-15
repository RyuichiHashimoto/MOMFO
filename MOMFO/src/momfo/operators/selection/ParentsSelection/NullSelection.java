package momfo.operators.selection.ParentsSelection;

import lib.experiments.CommandSetting;
import momfo.core.Population;
import momfo.util.JMException;

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
