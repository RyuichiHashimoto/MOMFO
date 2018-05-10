package momfo.problems.MOMFOP.NTU;

import java.io.IOException;

import momfo.Indicator.IGDRef;
import momfo.core.Problem;
import momfo.core.ProblemSet;
import momfo.problems.MOMFOP.NTU.base.IO;
import momfo.problems.MOMFOP.NTU.base.MMDTLZ;
import momfo.problems.MOMFOP.NTU.base.MMZDT;

public class NILS {


	public static ProblemSet getProblem() throws IOException {
		ProblemSet ps1 = getT1();
		ProblemSet ps2 = getT2();
		ProblemSet problemSet = new ProblemSet(2);
		problemSet.setProblemSetName("NILS");
		problemSet.setMaxDimensionOfObjective(3);
		problemSet.add(ps1.get(0));
		problemSet.add(ps2.get(0));
		return problemSet;

	}

	public static ProblemSet getT1() throws IOException {
		ProblemSet problemSet = new ProblemSet(1);
		MMDTLZ prob = new MMDTLZ("griewank",3, 25, 1, -50,50);

		IGDRef.AddRefFiles("Data/PF/sphere.pf");
		double shiftValues[] = IO.readShiftValuesFromFile("Data/ShiftData/S_NILS_1.txt");
		prob.getgFunction().setShiftMatrix(shiftValues);

		((Problem)prob).setName("NILS1");

		problemSet.add(prob);
		return problemSet;
	}

	public static ProblemSet getT2() throws IOException {
		ProblemSet problemSet = new ProblemSet(1);
		IGDRef.AddRefFiles("Data/PF/concave.pf");
		MMZDT prob = new MMZDT("ackley",50, 2,  -100,100);
		prob.setHType("concave");
		((Problem)prob).setName("NILS2");

		problemSet.add(prob);
		return problemSet;
	}
}
