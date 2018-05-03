package momfo.problems.SOP;

public class Weierstrass extends Function{


	//this problem have no constrain.
	static int k_max_ = 20;
	static double a = 0.5;
	int    b = 3;

	public Weierstrass(int numberofVariables,double upper,double lower){

		numberOfDistanceVariables_ = numberofVariables;



		LowerLimit = new double[numberOfDistanceVariables_];
		UpperLimit = new double[numberOfDistanceVariables_];

		for(int i = 0;i < numberOfDistanceVariables_;i++){
			LowerLimit[i] = lower;
			UpperLimit[i] = upper;
		}


//		problemName_  = "Sphere";
	}




	public double pow(double d,  int a){
		double s = 1;
		for(int i=0;i < a;i++){
			s = s*d;
		}
		return s;
	}


	@Override
	public double evaluate(double[] sol) {
//		double[] x = decode(solution.getValue());
		double[] x = new double[sol.length];
		for(int i = 0;i< sol.length;i++){
			x[i] = sol[i] - shiftMatrix_[i];
		}

		x = rotationMatrix.calc(rotationMatrix_, x);

		double config = 0;
		double sum=0;
		double sum1 = 0;
		for(int i = 0;i<numberOfDistanceVariables_;i++){
			for(int k = 0;k<=k_max_;k++){
				sum = sum + Math.pow(a, k)*Math.cos(2*Math.PI*Math.pow(b, k)*(x[i] + 0.5));
			}
		}

		for(int k = 0;k<=k_max_;k++){
			config = config + x.length* Math.pow(a, k)*Math.cos((0.5*2*Math.PI*Math.pow(b, k)));
			sum1 = sum1 + Math.pow(a, k)*Math.cos(0.5*2*Math.PI*Math.pow(b, k));
		}

			return sum - sum1*numberOfDistanceVariables_;
	}

}
