package momfo.operators.selection.environmentalselection;

import javax.naming.NameNotFoundException;

import lib.experiments.CommandSetting;
import lib.experiments.JMException;
import momfo.core.Population;

public class MOEADSelection extends EnvironmentalSelection{


	public MOEADSelection(){
		super();
	}

	public void build(CommandSetting st) throws NameNotFoundException{
		super.build(st);
	}

	@Override
	public Population getNextPopulation(Population merge_) throws JMException {
		return null;
	}

}
