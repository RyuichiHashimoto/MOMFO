package momfo.util.Comparator.MOEADComparator;

import java.util.HashMap;

import momfo.core.Solution;
import momfo.util.JMException;
import momfo.util.ScalarzingFunction.ScalarzingFunction;

public class  ConstrainSumMOEADComparator extends MOEADComparator{


	double parameter = 10;

	public ConstrainSumMOEADComparator(HashMap<String, Object> parameters) {
		super(parameters);

		// TODO 自動生成されたコンストラクター・スタブ
	}

	public ConstrainSumMOEADComparator(HashMap<String, Object> parameters,ScalarzingFunction d ){
		super(parameters,d);

		if(parameters != null)
			if(parameters.containsKey("parameter")){
				parameter = (double)parameters.get("parameter");
			}
	}
	public ConstrainSumMOEADComparator(HashMap<String, Object> parameters,ScalarzingFunction d , double para){
		super(parameters,d);
		parameter = para;
	}


	@Override
	public int execute(Object one, Object two) throws JMException { 
		Solution one_sol = (Solution)one;
		Solution two_sol = (Solution)two;
		assert one_sol.getNumberOfConstraint() == two_sol.getNumberOfConstraint() : "one Sslution has " + one_sol.getNumberOfConstraint() + " and the other has "  + two_sol.getNumberOfConstraint();
		assert weightedVector != null  : "WeightedVector";
		assert  referencePoint != null : "ReferencePoint";
		double scalar_one = ScalaringFunction_.execute(one_sol, weightedVector.get(), referencePoint.get());
		double scalar_two = ScalaringFunction_.execute(two_sol, weightedVector.get(), referencePoint.get());
		double parameter_one = one_sol.getViolation();
		double parameter_two = two_sol.getViolation();


		if(one_sol.getFeasible() && two_sol.getFeasible()){
			if ((isMAX_) == (scalar_one  > scalar_two )){
				return 1;
			} else if ((isMAX_) == (scalar_one   < scalar_two )){
				return -1;
			}
		} else if (one_sol.getFeasible() && !two_sol.getFeasible()){
			return 1;
		}else if (!one_sol.getFeasible() && two_sol.getFeasible()){
			return -1;
		} else if (!one_sol.getFeasible() && !two_sol.getFeasible()){
			if(parameter_one < parameter_two){
				return 1;
			} else if (parameter_one > parameter_two){
				return -1;
			} else {
				return 0;
			}
		} else {
			System.err.println("=============notion============");
			System.err.println("in ConstrainSumMOEADComparator may has bug");
			return 0;
		}
		return 0;
	}

}
