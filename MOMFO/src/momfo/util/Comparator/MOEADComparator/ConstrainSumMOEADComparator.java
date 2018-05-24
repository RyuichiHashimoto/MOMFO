package momfo.util.Comparator.MOEADComparator;

import lib.experiments.JMException;
import lib.math.BuildInRandom;
import momfo.core.Solution;
import momfo.util.ScalarzingFunction.ScalarzingFunction;

public class  ConstrainSumMOEADComparator extends MOEADComparator{

	public ConstrainSumMOEADComparator(){
		super();
	}

	public ConstrainSumMOEADComparator(boolean ismax, BuildInRandom random,ScalarzingFunction d , double para) throws JMException{
		super(ismax,random,d);

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
			throw new JMException("in ConstrainSumMOEADComparator may has bug");
		}
		return 0;
	}

}
