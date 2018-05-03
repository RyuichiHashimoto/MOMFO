package momfo.util.Comparator.SMSEMOAComparator;

import java.util.HashMap;

import momfo.core.Solution;
import momfo.util.JMException;

public class SMSEMOASelectionComparator extends SMSEMOAComparator {

	public SMSEMOASelectionComparator(HashMap<String, Object> parameters) {
		super(parameters);
		// TODO 自動生成されたコンストラクター・スタブ
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
