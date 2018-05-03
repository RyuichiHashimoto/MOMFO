package momfo.problems.SOP;

public class Griewank extends Function{




	public Griewank(int numberofVariables,double upper,double lower){

		numberOfDistanceVariables_ = numberofVariables;



		LowerLimit = new double[numberOfDistanceVariables_];
		UpperLimit = new double[numberOfDistanceVariables_];

		for(int i = 0;i < numberOfDistanceVariables_;i++){
			LowerLimit[i] = lower;
			UpperLimit[i] = upper;
		}


//		problemName_  = "Sphere";
	}


	@Override
	public double evaluate(double[] sol) {
		assert sol.length == numberOfDistanceVariables_;

		double sum = 0;
		double sum1=0,sum2=1;
		double[] x = new double[sol.length];
		for(int i = 0;i< sol.length;i++){
			x[i] = sol[i] - shiftMatrix_[i];
		}
 
		x = rotationMatrix.calc(rotationMatrix_, x);

		for(int i = 0 ; i < numberOfDistanceVariables_;i++){
			sum1 += x[i]*x[i];
			sum2 *=  Math.cos(x[i]/Math.sqrt(i+1));
		}

		sum = 1 + sum1/4000 - sum2;

		return sum;
	}




}
