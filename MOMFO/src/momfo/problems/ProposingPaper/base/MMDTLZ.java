package momfo.problems.ProposingPaper.base;

import java.util.Map;

import momfo.core.Problem;
import momfo.core.Solution;
import momfo.problems.SOP.Function;
import momfo.problems.SOP.FunctionFactory;
import momfo.util.JMException;

public class MMDTLZ extends Problem {
	Integer alpha_;

	Function gfunction;

	public Function getgFunction(){
		return gfunction;
	};


	public MMDTLZ(String function,int numberOfObjectives, int numberOfVariables, int alpha, double lg, double ug) {
		numberOfObjectives_ = numberOfObjectives;
		numberOfVariables_ = numberOfVariables;

		variableType_ = 2;
		alpha_ = alpha;

		int num = numberOfVariables_ - numberOfObjectives_ + 1;

		try {
			gfunction =  FunctionFactory.getFunctionProblem(function,num,lg ,ug);
		} catch (JMException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		// System.out.println(num);

		double[] shiftValues_ = new double[num];
		double[][] rotationMatrix_ = new double[num][num];

		upperLimit_ = new double[numberOfVariables_];
		lowerLimit_ = new double[numberOfVariables_];

		for (int var = 0; var < numberOfObjectives_ - 1; var++) {
			lowerLimit_[var] = 0.0;
			upperLimit_[var] = 1.0;
		} // for

		for (int var = numberOfObjectives_ - 1; var < numberOfVariables; var++) {
			lowerLimit_[var] = lg;
			upperLimit_[var] = ug;
		}

		for (int i = 0; i < num; i++)
			shiftValues_[i] = 0;

		for (int i = 0; i < num; i++) {
			for (int j = 0; j < num; j++) {
				if (i != j)
					rotationMatrix_[i][j] = 0;
				else
					rotationMatrix_[i][j] = 1;
			}
		}
		gfunction.setRotationMatrix(rotationMatrix_);
		gfunction.setShiftMatrix(shiftValues_ );
	}

	public MMDTLZ(String function,int numberOfObjectives, int numberOfVariables, int alpha, double lg, double ug, String gType,
		double[] shiftValues, double[][] rotationMatrix) {
		numberOfObjectives_ = numberOfObjectives;
		numberOfVariables_ = numberOfVariables;
		try {
			gfunction =  FunctionFactory.getFunctionProblem(function,numberOfVariables_,lg ,ug);
		} catch (JMException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		variableType_ = 2;

		alpha_ = alpha;

		upperLimit_ = new double[numberOfVariables_];
		lowerLimit_ = new double[numberOfVariables_];

		for (int var = 0; var < numberOfObjectives_ - 1; var++) {
			lowerLimit_[var] = 0.0;
			upperLimit_[var] = 1.0;
		} // for

		for (int var = numberOfObjectives_ - 1; var < numberOfVariables; var++) {
			lowerLimit_[var] = lg;
			upperLimit_[var] = ug;
		}
		gfunction.setRotationMatrix(rotationMatrix);
		gfunction.setShiftMatrix(shiftValues);
	}

	public void evaluate(Solution solution) throws JMException {
//		double vars[] = scaleVariables(solution);
		double vars[] = decode(solution);

		double[] xI = new double[numberOfObjectives_ - 1];
		double[] xII = new double[numberOfVariables_ - numberOfObjectives_ + 1];

		for (int i = 0; i < numberOfObjectives_ - 1; i++)
			xI[i] = vars[i];

		for (int i = numberOfObjectives_ - 1; i < numberOfVariables_; i++)
			xII[i - numberOfObjectives_ + 1] = vars[i];



		double[] f = new double[numberOfObjectives_];

		double g = gfunction.evaluate(xII);
		for (int i = 0; i < numberOfObjectives_; i++)
			f[i] = 1 + g;

		for (int i = 0; i < numberOfObjectives_; i++) {
			for (int j = 0; j < numberOfObjectives_ - (i + 1); j++)
				f[i] *= Math.cos(Math.pow(xI[j], alpha_) * 0.5 * Math.PI);
			if (i != 0) {
				int aux = numberOfObjectives_ - (i + 1);
				f[i] *= Math.sin(Math.pow(xI[aux], alpha_) * 0.5 * Math.PI);
			} // if
		} // for

		for (int i = 0; i < numberOfObjectives_; i++)
			solution.setObjective(0 + i, f[i]);
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

}
