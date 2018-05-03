package momfo.problems.SOP;

public class Schwefel extends Function{



	public Schwefel(int numberofVariables,double upper,double lower){

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
	public double evaluate(double []sol) {
//		double[] x = decode(solution.getValue());

		double sum = 418.9829*numberOfDistanceVariables_;

		double sum2 = 0;

		double[] x = new double[sol.length];
		for(int i = 0;i< sol.length;i++){
			x[i] = sol[i] - shiftMatrix_[i];
		}

		x = rotationMatrix.calc(rotationMatrix_, x);




		for(int i = 0;i<numberOfDistanceVariables_;i++){
			sum2 += x[i]*Math.sin(Math.sqrt(Math.abs(x[i])));
		}

		sum = sum - sum2;
		return sum;
	}




}
