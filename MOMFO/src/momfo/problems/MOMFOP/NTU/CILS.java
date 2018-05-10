package momfo.problems.MOMFOP.NTU;

import java.io.IOException;

import momfo.Indicator.IGDRef;
import momfo.core.Problem;
import momfo.core.ProblemSet;
import momfo.problems.MOMFOP.NTU.base.MMDTLZ;
import momfo.problems.MOMFOP.NTU.base.MMZDT;


public class CILS {

	public static ProblemSet getProblem() throws IOException {
		ProblemSet ps1 = getT1();
		ProblemSet ps2 = getT2();
		ProblemSet problemSet = new ProblemSet(2);
		problemSet.setProblemSetName("CILS");
		problemSet.setMaxDimensionOfObjective(2);
		problemSet.add(ps1.get(0));
		problemSet.add(ps2.get(0));
		return problemSet;

	}

	public static ProblemSet getT1() throws IOException {
		ProblemSet problemSet = new ProblemSet(1);

		MMDTLZ prob = new MMDTLZ("rastrigin",2, 50, 1, -2,2);

		((Problem)prob).setName("CILS1");
		IGDRef.AddRefFiles("Data/PF/circle.pf");
		problemSet.add(prob);
		return problemSet;
	}

	public static ProblemSet getT2() throws IOException {
		ProblemSet problemSet = new ProblemSet(1);
		IGDRef.AddRefFiles("Data/PF/convex.pf");
		MMZDT prob = new MMZDT("ackley",50, 1,  -1,1);

		((Problem)prob).setName("CILS2");

		problemSet.add(prob);
		return problemSet;
	}
}
