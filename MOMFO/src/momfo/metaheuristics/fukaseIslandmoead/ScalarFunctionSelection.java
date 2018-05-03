package momfo.metaheuristics.fukaseIslandmoead;

import java.util.HashMap;

import momfo.core.Solution;
import momfo.operators.selection.Selection;
import momfo.util.JMException;
import momfo.util.ReferencePoint;
import momfo.util.WeightedVector;
import momfo.util.Comparator.MOEADComparator.MOEADComparator;
import momfo.util.ScalarzingFunction.ScalarzingFunction;


public class ScalarFunctionSelection extends Selection{

	MOEADComparator comparator_;

	public ScalarFunctionSelection(HashMap<String, Object> parameters, MOEADComparator compara){
		super(parameters);

		comparator_ = compara;
	}

	public ScalarFunctionSelection(HashMap<String, Object> parameters) {
		super(parameters);
		int a = 1;
		assert  a == 1 : "you must not use this Constructor  in MultiScalarFunctionSelection";
	}

	public ScalarzingFunction getScalarzingFunction(){return comparator_.getScalarzingFunction();};

	public void setRefernecePoint(double[] d){
		comparator_.setRefernecePoint(d);
	}

	public void setRefernecePoint(ReferencePoint d){
		comparator_.setRefernecePoint(d);
	}

	public void setWeightedVector(double[] d){
		comparator_.setWeightedVector(new WeightedVector(d));
	}

	public void setWeightedVector(WeightedVector d){
		comparator_.setWeightedVector(new WeightedVector(d));
	}



	@Override
	public Object execute(Object object) throws JMException {




		Solution[] solutionSet_ = (Solution[]) object;
		int number = 0;

		for(int i=0;i <solutionSet_.length;i++){
			if(comparator_.execute(solutionSet_[i], solutionSet_[number]) == 1){
				number = i;
			}
		}
		assert number >= 0 : "wrong";
		return number;
	}




}
