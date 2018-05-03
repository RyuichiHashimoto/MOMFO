package momfo.problems.SOP;

public class Ackley extends Function{



	public Ackley(int numberofVariables,double upper,double lower){

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

		double sum = 0;

		double sum1=0,sum2=0;

		for(int i= 0; i < numberOfDistanceVariables_;i++){
			sum1 += x[i]*x[i];
			sum2 += Math.cos(Math.PI*2*x[i]);
		}
		sum1 = sum1/numberOfDistanceVariables_;
		sum2 = sum2/numberOfDistanceVariables_;
		sum1 = Math.sqrt(sum1);

		sum = -20*Math.exp(-0.2*sum1) -Math.exp(sum2) + 20 + Math.exp(1);


		return sum;
	}




}
