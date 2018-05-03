package momfo.problems.SOP;

public class Mean extends Function{



	public Mean(int numberofVariables,double upper,double lower){

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

		double[] x = new double[sol.length];

		for(int i = 0;i< sol.length;i++){
			x[i] = sol[i] - shiftMatrix_[i];
		}

		x = rotationMatrix.calc(rotationMatrix_, x);



		double mean = 0;
		for (int i = 0; i < x.length; i++)
			mean += Math.abs(x[i]);

		mean /= x.length;
		//return 9 * mean;
		return 9 * mean;

	}




}
