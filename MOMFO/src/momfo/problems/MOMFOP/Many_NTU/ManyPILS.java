package momfo.problems.MOMFOP.Many_NTU;

import java.io.IOException;

import momfo.Indicator.IGDRef;
import momfo.core.Problem;
import momfo.core.ProblemSet;
import momfo.problems.MOMFOP.Many_NTU.base.MMDTLZ;
import momfo.problems.MOMFOP.NTU.base.IO;


public class ManyPILS {
	public static ProblemSet getProblem() throws IOException {
		ProblemSet ps1 = getT1();
		ProblemSet ps2 = getT2();
		ProblemSet problemSet = new ProblemSet(2);
		problemSet.setProblemSetName("ManyPILS");
		problemSet.setMaxDimensionOfObjective(2);
		problemSet.add(ps1.get(0));
		problemSet.add(ps2.get(0));
		return problemSet;

	}

	public static ProblemSet getT1() throws IOException {
		ProblemSet problemSet = new ProblemSet(1);

		MMDTLZ prob = new MMDTLZ("griewank",2, 50, 1, -50,50);

		((Problem)prob).setName("PILS1");
		IGDRef.AddRefFiles("Data/PF/circle.pf");
		problemSet.add(prob);
		return problemSet;
	}

	public static ProblemSet getT2() throws IOException {
		ProblemSet problemSet = new ProblemSet(1);

		MMDTLZ prob = new MMDTLZ("ackley",2, 50, 1, -100,100);
		IGDRef.AddRefFiles("Data/PF/circle.pf");
		double[] shiftValues= IO.readShiftValuesFromFile("Data/ShiftData/S_PILS_2.txt");
		prob.getgFunction().setShiftMatrix(shiftValues);

		((Problem)prob).setName("PILS2");

		problemSet.add(prob);
		return problemSet;
	}

}
