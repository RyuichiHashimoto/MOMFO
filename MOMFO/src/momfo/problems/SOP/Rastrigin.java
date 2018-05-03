package momfo.problems.SOP;

import java.io.IOException;

import momfo.problems.MOMFOP.NTU.base.IO;

public class Rastrigin extends Function{



	public Rastrigin(int numberofVariables,double upper,double lower){

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
		double val1 = 0 ;
		double val2 = 0 ;
		for(int i = 0;i< sol.length;i++){
			x[i] = sol[i] - shiftMatrix_[i];
			val1 = x[i]*x[i];
		}
		x = rotationMatrix.calc(rotationMatrix_, x);
		for(int i = 0;i< sol.length;i++){
			val2 = x[i]*x[i];
		}
//		System.out.println(val2 + "\t\t" + val1);


		for(int i = 0;i<numberOfDistanceVariables_;i++){
			sum += (x[i]*x[i] - 10*Math.cos(2*Math.PI*x[i]) + 10);
		}
		return sum;
	}

	public static void main(String[] args) throws IOException {
		Rastrigin rastrigin = new Rastrigin(49,-100,100);

		double[] sol = new double[49];
		for(int i =0;i<sol.length;i++){
			sol[i] = 0;
		}
		double[] shiftValues = IO.readShiftValuesFromFile("Data/ShiftData/S_PIHS_2.txt");
//		rastrigin.setShiftMatrix(shiftValues);
		rastrigin.setShiftMatrix(sol);


		double[][] rotationMatrix_ = new double[49][49];
		for (int i = 0; i < rotationMatrix_.length; i++) {
			for (int j = 0; j < rotationMatrix_.length; j++) {
				if (i != j)
					rotationMatrix_[i][j] = 0;
				else
					rotationMatrix_[i][j] = 1;
			}
		}
		rastrigin.setRotationMatrix(rotationMatrix_);

		System.out.println(rastrigin.evaluate(sol));

	}


}
