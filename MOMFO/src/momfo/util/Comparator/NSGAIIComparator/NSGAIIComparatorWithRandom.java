package momfo.util.Comparator.NSGAIIComparator;

import javax.naming.NameNotFoundException;

import lib.experiments.CommandSetting;
import lib.experiments.JMException;
import lib.math.BuildInRandom;
import momfo.core.Solution;

@SuppressWarnings("serial")
public class NSGAIIComparatorWithRandom extends NSGAIIComparator{

	public NSGAIIComparatorWithRandom() {
		super();
	}
	
	public void build(CommandSetting st) throws NameNotFoundException,  ReflectiveOperationException {
		super.build(st);
	}
	
	public NSGAIIComparatorWithRandom(boolean d, BuildInRandom random_) {
		super(d, random_);
	}

	@Override
	public int execute(Object one, Object two) throws JMException {
		Solution a = (Solution)one;
		Solution b = (Solution)two;


		if(a.getRank() < b.getRank()){
			return 1;
		} else if (a.getRank() > b.getRank()){
			return -1;
		}

		if(a.getCrowdDistance_() > b.getCrowdDistance_()){
			return 1;
		}  else if(a.getCrowdDistance_() < b.getCrowdDistance_()) {
			return -1;
		}

		if(random.nextDoubleIE() > 0.5){
			return 1;
		}  else  {
			return -1;
		}



	}
}
