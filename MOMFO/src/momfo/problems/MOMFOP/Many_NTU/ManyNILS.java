package momfo.problems.MOMFOP.Many_NTU;

import java.io.IOException;

import momfo.Indicator.IGD.IGDRef;
import momfo.core.Problem;
import momfo.core.ProblemSet;
import momfo.problems.MOMFOP.Many_NTU.base.MMDTLZ;
import momfo.problems.MOMFOP.Many_NTU.base.MMZDT;
import momfo.problems.MOMFOP.NTU.base.IO;

public class ManyNILS {


	public static ProblemSet getProblem() throws IOException {
		ProblemSet ps1 = getT1();
		ProblemSet ps2 = getT2();
		ProblemSet problemSet = new ProblemSet(2);
		problemSet.setProblemSetName("ManyNILS");
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
