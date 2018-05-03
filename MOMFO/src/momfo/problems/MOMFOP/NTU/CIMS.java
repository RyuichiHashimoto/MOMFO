package momfo.problems.MOMFOP.NTU;

import java.io.IOException;

import momfo.core.Problem;
import momfo.core.ProblemSet;
import momfo.problems.MOMFOP.NTU.base.IO;
import momfo.problems.MOMFOP.NTU.base.MMDTLZ;
import momfo.problems.MOMFOP.NTU.base.MMZDT;
import momfo.util.Indicator.IGDRef;

public class CIMS {

	public static ProblemSet getProblem() throws IOException {
		ProblemSet ps1 = getT1();
		ProblemSet ps2 = getT2();
		ProblemSet problemSet = new ProblemSet(2);
		problemSet.setProblemSetName("CIMS");
		problemSet.setMaxDimensionOfObjective(2);
		problemSet.add(ps1.get(0));
		problemSet.add(ps2.get(0));
		return problemSet;

	}


	public static ProblemSet getT1() throws IOException {
		ProblemSet problemSet = new ProblemSet(1);

		MMZDT prob = new MMZDT("rosenbrock",10,  1, -5,5);

		IGDRef.AddRefFiles("Data/PF/concave.pf");
		((Problem)prob).setName("CIMS1");
		prob.setHType("concave");
		problemSet.add(prob);
		return problemSet;
	}


	public static ProblemSet getT2() throws IOException {
		ProblemSet problemSet = new ProblemSet(1);

		IGDRef.AddRefFiles("Data/PF/circle.pf");
		MMDTLZ prob = new MMDTLZ("mean",2, 10, 1, -5,5);


		double[] shiftValues = IO.readShiftValuesFromFile("Data/ShiftData/S_CIMS_2.txt");
		prob.getgFunction().setShiftMatrix(shiftValues);

		double[][] matrix = IO.readMatrixFromFile("Data/MData/M_CIMS_2.txt");
		prob.getgFunction().setRotationMatrix(matrix);


		((Problem)prob).setName("CIMS2");

		problemSet.add(prob);
		return problemSet;
	}
}
