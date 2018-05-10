package momfo.problems.MOMFOP.Many_NTU;

import java.io.IOException;

import momfo.Indicator.IGDRef;
import momfo.core.Problem;
import momfo.core.ProblemSet;
import momfo.problems.MOMFOP.Many_NTU.base.MMZDT;
import momfo.problems.MOMFOP.NTU.base.IO;

public class ManyPIHS {

	public static ProblemSet getProblemSet() throws IOException {
		ProblemSet ps1 = getT1();
		ProblemSet ps2 = getT2();
		ProblemSet problemSet = new ProblemSet(2);
		problemSet.setProblemSetName("ManyPIHS");
		problemSet.setMaxDimensionOfObjective(2);
		problemSet.add(ps1.get(0));
		problemSet.add(ps2.get(0));
		return problemSet;

	}

	public static ProblemSet getT1() throws IOException {
		ProblemSet problemSet = new ProblemSet(1);

		MMZDT prob = new MMZDT("sphere",50, 1,  -100,100);
		prob.setHType("convex");
		((Problem)prob).setName("PIHS1");
		IGDRef.AddRefFiles("Data/PF/convex.pf");
		problemSet.add(prob);
		return problemSet;
	}

	public static ProblemSet getT2() throws IOException {
		ProblemSet problemSet = new ProblemSet(1);
		IGDRef.AddRefFiles("Data/PF/convex.pf");
		MMZDT prob = new MMZDT("rastrigin",50, 1,  -100,100);
		prob.setHType("convex");

		double[] shiftValues = IO.readShiftValuesFromFile("Data/ShiftData/S_PIHS_2.txt");
		prob.getgFunction().setShiftMatrix(shiftValues);

		((Problem)prob).setName("PIHS2");

		problemSet.add(prob);
		return problemSet;
	}
}
