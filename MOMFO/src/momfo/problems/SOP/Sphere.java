package momfo.problems.SOP;

public class Sphere extends Function{



	public Sphere(int numberofVariables,double upper,double lower){

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

		double sum = 0;
		for(int i = 0;i< x.length;i++){
			x[i] = sol[i] - shiftMatrix_[i];
		}
		x = rotationMatrix.calc(rotationMatrix_, x);

		for(int i = 0;i<numberOfDistanceVariables_;i++){
			sum += x[i]*x[i];
		}
		return sum;
	}




}
