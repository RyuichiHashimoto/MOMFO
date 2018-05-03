package momfo.problems.SOP;

public class BentCigarFunction extends Function{




	public BentCigarFunction(int numberofVariables,double upper,double lower){

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

		
		double sum = x[0] * x[0];
		for (int i = 1; i < x.length; i++)
			sum += (Math.pow(10, 6) * x[i] * x[i]);


		return sum;
	}

	public static void main(String[] args) {
		int startPos = 0;
		int endPos = 20;
		double[] variable = new double[endPos - startPos];

		
		
		
		BentCigarFunction fun = new BentCigarFunction(0, 0, 0);

		for(int val = 0; val < endPos;val++){
			variable[val] = 0.33;
		}

		System.out.println("BentCigarFunction:	" +  fun.evaluate(variable));

	}

}
