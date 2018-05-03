package momfo.problems.SOP;

public class HappyCatFunction extends Function{




	public HappyCatFunction(int numberofVariables,double upper,double lower){

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
		int D = sol.length;

		
		double[] x = new double[sol.length];
		for(int i = 0;i< sol.length;i++){
			x[i] = sol[i] - shiftMatrix_[i];
		}
 
		x = rotationMatrix.calc(rotationMatrix_, x);
		
		
		
		double sum1 = 0;
		double sum2 = 0;
		
		for (int i = 0; i < x.length; i++) {
			sum1 += x[i];
			sum2 += (x[i] * x[i]);
		}

		double res = Math.pow(Math.abs(sum2 - D), 0.25) + (0.5 * sum2 + sum1) / D + 0.5;

		return res;
	}

	public static void main(String[] args) {
		int startPos = 0;
		int endPos = 20;
		double[] variable = new double[endPos - startPos];

		HappyCatFunction fun = new HappyCatFunction(0, 0, 0);

		for(int val = 0; val < endPos;val++){
			variable[val] = 0.33;
		}

		System.out.println("HappyCatFunction:	" +  fun.evaluate(variable));

//		System.out.println("BentCigarFunction:	" +  getBentcigar(variable,startPos,endPos));

	}

}
