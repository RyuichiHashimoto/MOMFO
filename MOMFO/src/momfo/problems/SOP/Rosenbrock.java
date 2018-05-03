package momfo.problems.SOP;

public class Rosenbrock extends Function{



	public Rosenbrock(int numberofVariables,double upper,double lower){

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

		double sum = 0;
		double[] x = new double[sol.length];
		for(int i = 0;i< sol.length;i++){
			x[i] = sol[i] - shiftMatrix_[i];
		}

		x = rotationMatrix.calc(rotationMatrix_, x);



		for(int i = 0;i<numberOfDistanceVariables_ - 1;i++){
			sum += 100*(x[i]*x[i] - x[i+1])*(x[i]*x[i] - x[i+1]) + (x[i] - 1)*(x[i] - 1);
		}

		return sum;
	}




}
