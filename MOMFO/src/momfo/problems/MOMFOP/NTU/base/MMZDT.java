package momfo.problems.MOMFOP.NTU.base;

import java.util.Map;

import lib.experiments.JMException;
import momfo.core.Problem;
import momfo.core.Solution;
import momfo.problems.SOP.Function;
import momfo.problems.SOP.FunctionFactory;

public class MMZDT extends Problem {

	Integer k_;


	String gType_;
	String f1Type_;
	String hType_;

	Function gfunction;

	public Function getgFunction(){
		return gfunction;
	};
	public MMZDT() {
		numberOfObjectives_ = 2;
	}
	public void setHType(String d){
		hType_ = d;

	}
	public MMZDT(String function,int numberOfVariables, int k, double lg, double ug) {
		numberOfObjectives_ = 2;
		numberOfVariables_ = numberOfVariables;
		variableType_ = 2;
		gType_ = function;
		f1Type_ = "linear";
		hType_ = "convex";
		k_ = k;
		try {
			gfunction =  FunctionFactory.getFunctionProblem(function,numberOfVariables_-k,lg ,ug);
		} catch (JMException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}

		upperLimit_ = new double[numberOfVariables_];
		lowerLimit_ = new double[numberOfVariables_];

		double[] shiftValues_ = new double[numberOfVariables_-k];
		double[][] rotationMatrix_ = new double[numberOfVariables_-k][numberOfVariables_-k];

		for (int var = 0; var < k_; var++) {
			lowerLimit_[var] = 0.0;
			upperLimit_[var] = 1.0;
		} // for
		for (int var = k_; var < numberOfVariables; var++) {
			lowerLimit_[var] = lg;
			upperLimit_[var] = ug;
		}

		shiftValues_ = new double[numberOfVariables_ - k_];
		for (int i = 0; i < shiftValues_.length; i++)
			shiftValues_[i] = 0;

		rotationMatrix_ = new double[numberOfVariables_ - k_][numberOfVariables_ - k_];
		for (int i = 0; i < rotationMatrix_.length; i++) {
			for (int j = 0; j < rotationMatrix_.length; j++) {
				if (i != j)
					rotationMatrix_[i][j] = 0;
				else
					rotationMatrix_[i][j] = 1;
			}
		}

		gfunction.setRotationMatrix(rotationMatrix_);
		gfunction.setShiftMatrix(shiftValues_);

	}

	public MMZDT(String function,int numberOfVariables, int k, double lg, double ug, String gType, String f1Type, String hType,
		double[] shiftValues, double[][] rotationMatrix) {
		numberOfObjectives_ = 2;
		numberOfVariables_ = numberOfVariables;
		k_ = k;
		variableType_ = 2;
		gType_ = gType;
		f1Type_ = f1Type;
		hType_ = hType;
		try {
			gfunction =  FunctionFactory.getFunctionProblem(function,numberOfVariables_,lg ,ug);
		} catch (JMException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}

		upperLimit_ = new double[numberOfVariables_-k_];
		lowerLimit_ = new double[numberOfVariables_-k_];

		for (int var = 0; var < k_; var++) {
			lowerLimit_[var] = 0.0;
			upperLimit_[var] = 1.0;
		} // for
		for (int var = k_; var < numberOfVariables; var++) {
			lowerLimit_[var] = lg;
			upperLimit_[var] = ug;
		}
		gfunction.setRotationMatrix(rotationMatrix);
		gfunction.setShiftMatrix(shiftValues);
	}

	@Override
	public double[] evaluate(double[] val) throws JMException {




		double[] xI = new double[k_];
		double[] xII = new double[numberOfVariables_ - k_];
		for (int i = 0; i < k_; i++)
			xI[i] = val[i];

		for (int i = k_; i < numberOfVariables_; i++)
			xII[i - k_] = val[i];



	//	xII = transformVariables(xII);
		double[] ret = new double[2];
		ret[0] = evalF1(xI);
		double g = gfunction.evaluate(xII)+1.0;
		ret[1] = g * evalH(ret[0], g);
					
		return ret;
		// System.out.println("g: " + g);
//		solution.rescaleObjectives(numberOfObjectives_);
//		solution.setObjective(0, f1);
//		solution.setObjective(1, f2);
	}



	private double[] transformVariables(double[] xII) {
		return  xII;
	}
	double evalF1(double[] xI) {
		if (f1Type_.equalsIgnoreCase("linear"))
			return F1_linear(xI);
		else if (f1Type_.equalsIgnoreCase("nonlinear"))
			return F1_nonlinear(xI);
		else {
			System.out.println("Error: f1 function type " + f1Type_ + " invalid");
			return Double.NaN;
		}
	}

	double evalH(double f1, double g) {
		if (hType_.equalsIgnoreCase("convex"))
			return H_convex(f1, g);
		else if (hType_.equalsIgnoreCase("concave"))
			return H_nonconvex(f1, g);
		else {
			System.out.println("Error: f1 function type " + f1Type_ + " invalid");
			return Double.NaN;
		}
	}

	double H_convex(double f1, double g) {
		return 1 - Math.pow(f1 / g, 0.5);
	}

	double H_nonconvex(double f1, double g) {
		return 1 - Math.pow(f1 / g, 2);
	}

	double F1_linear(double xI[]) {
		double sum = 0;
		for (int i = 0; i < xI.length; i++)
			sum += xI[i];

		return sum / xI.length;
	}

	double F1_nonlinear(double xI[]) {
		double r = 0;

		for (int i = 0; i < xI.length; i++)
			r += (xI[i] * xI[i]);

		r = Math.sqrt(r);

		return 1 - Math.exp(-4 * r) * Math.pow(Math.sin(5 * Math.PI * r), 4);
	}

	public void setGType(String gType) {
		gType_ = gType;
	}

	public void setF1Type(String f1Type) {
		f1Type_ = f1Type;
	}

	@Override
	public void repair(Solution d, Map<String, Object> a) throws JMException {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public double[] decode(Solution d) {
		double ret[] = new double[numberOfVariables_];
		for(int v = 0; v < numberOfVariables_;v++){
			ret[v] = (upperLimit_[v] - lowerLimit_[v])*d.getValue(v) + lowerLimit_[v];
		}
		return ret;
	}
	@Override
	public double[] decode(double[] val) {
		double ret[] = new double[numberOfVariables_];
		for(int v = 0; v < numberOfVariables_;v++){
			ret[v] = (upperLimit_[v] - lowerLimit_[v])*val[v] + lowerLimit_[v];
		}
		return ret;
	}
}
