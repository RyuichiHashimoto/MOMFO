package momfo.metaheuristics.smsemoa;

import java.util.HashMap;

import momfo.core.Population;
import momfo.operators.selection.Selection;
import momfo.util.JMException;
import momfo.util.ReferencePoint;

/*
 *
 *
 *
 * **********************************************************
 * **********************************************************
 * the population used in this class must be non-Dominated,**
 * **********************************************************
 * **********************************************************
 *
 * otherwise you can't true index that Solution has lowest contribution
 *
 *
 *
 */



public class SMSEMOASelection extends Selection {

	// this variable is shallow copy
	public  double[] referencePoint_;

	public SMSEMOASelection(HashMap<String, Object> parameters) {
		super(parameters);
		// TODO 自動生成されたコンストラクター・スタブ
	}

	public SMSEMOASelection(HashMap<String, Object> parameters, ReferencePoint ReferencePoint) {
		super(parameters);

	}



	public SMSEMOASelection(HashMap<String, Object> parameters,double[] ReferencePoint) {
		super(parameters);
		referencePoint_ = ReferencePoint;
	}

	public boolean  getOutPopulation(){

		return true;
	}

	public boolean isSolutionIsOK(){
		return false;
	};




	public Object execute(Object object) throws JMException {
		Population solutionSet = (Population)object;




		int number;
		return solutionSet;
	}



}
