package momfo.problems.MOMFOP.NTU;

import java.io.IOException;

import momfo.Indicator.IGDRef;
import momfo.core.Problem;
import momfo.core.ProblemSet;
import momfo.problems.MOMFOP.NTU.base.IO;
import momfo.problems.MOMFOP.NTU.base.MMDTLZ;
import momfo.problems.MOMFOP.NTU.base.MMZDT;

public class PIMS {

	public static ProblemSet getProblem() throws IOException {
		ProblemSet ps1 = getT1();
		ProblemSet ps2 = getT2();
		ProblemSet problemSet = new ProblemSet(2);
		problemSet.setMaxDimensionOfObjective(2);
		problemSet.setProblemSetName("PIMS");
		problemSet.add(ps1.get(0));
		problemSet.add(ps2.get(0));

		return problemSet;

	}

	public static ProblemSet getT1() throws IOException {
		ProblemSet problemSet = new ProblemSet(1);

		MMDTLZ prob = new MMDTLZ("sphere",2, 50, 1, 0,1);

		double[] shiftValues= IO.readShiftValuesFromFile("Data/ShiftData/S_PIMS_1.txt");
		prob.getgFunction().setShiftMatrix(shiftValues);
		IGDRef.AddRefFiles("Data/PF/circle.pf");

		double[][] matrix = IO.readMatrixFromFile("Data/MData/M_PIMS_1.txt");

		prob.getgFunction().setRotationMatrix(matrix);

		((Problem)prob).setName("PIMS1");

		problemSet.add(prob);

		return problemSet;
	}

	public static ProblemSet getT2() throws IOException {
		ProblemSet problemSet = new ProblemSet(1);

		IGDRef.AddRefFiles("Data/PF/concave.pf");
		MMZDT prob = new MMZDT("rastrigin",50, 1,  0,1);

		double[][] matrix = IO.readMatrixFromFile("Data/MData/M_PIMS_2.txt");
		prob.getgFunction().setRotationMatrix(matrix);
		prob.setHType("concave");
		
		((Problem)prob).setName("PIMS2");

		problemSet.add(prob);

		return problemSet;
	}







}
