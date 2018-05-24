package momfo.util.Comparator.SMSEMOAComparator;

import java.util.HashMap;

import lib.experiments.JMException;
import lib.math.BuildInRandom;
import momfo.core.Solution;

public class SMSEMOASelectionComparator extends SMSEMOAComparator {


	public SMSEMOASelectionComparator(boolean is, BuildInRandom random_) {
		super(is, random_);
		// TODO Auto-generated constructor stub
	}

	@Override

	public int execute(Object one, Object two) throws JMException {
		Solution solone = (Solution)one;
		Solution soltwo = (Solution)two;

		if(solone.getNumberOfDominatedSolution() < soltwo.getNumberOfDominatedSolution() ){
			return 1;
		} else if (solone.getNumberOfDominatedSolution() > soltwo.getNumberOfDominatedSolution()){
			return -1;
		}


		return 0;
	}




}
