package momfo.util.Comparator;

import java.util.HashMap;

public class MOEAD_Comparator extends Comparator {

	double theta;



	public MOEAD_Comparator(HashMap<String, Object> parameters){
		super(parameters);
		comparatorName_ = "momfoEAD_Comparator";
	}

	public String getFunctionName() {
		return "PBI(5)_ForMin";
	}

	@Override
	public int execute(Object one, Object two) {
		return 0;
	}



}
