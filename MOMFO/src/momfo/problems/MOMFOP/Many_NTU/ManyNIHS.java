package momfo.problems.MOMFOP.Many_NTU;

import java.io.IOException;

import momfo.Indicator.IGD.IGDRef;
import momfo.core.Problem;
import momfo.core.ProblemSet;
import momfo.problems.MOMFOP.Many_NTU.base.MMDTLZ;
import momfo.problems.MOMFOP.Many_NTU.base.MMZDT;


public class ManyNIHS {
	public static ProblemSet getProblem() throws IOException {
		ProblemSet ps1 = getT1();
		ProblemSet ps2 = getT2();
		ProblemSet problemSet = new ProblemSet(2);
		problemSet.setProblemSetName("ManyNIHS");
		problemSet.setMaxDimensionOfObjective(2);
		problemSet.add(ps1.get(0));
		problemSet.add(ps2.get(0));
		return problemSet;

	}

	public static ProblemSet getT1() throws IOException {
		ProblemSet problemSet = new ProblemSet(1);

		MMDTLZ prob = new MMDTLZ("rosenbrock",2, 50, 1, -80,80);

		((Problem)prob).setName("NIHS1");
		IGDRef.AddRefFiles("Data/PF/circle.pf");
		problemSet.add(prob);
		return problemSet;
	}

	public static ProblemSet getT2() throws IOException {
		ProblemSet problemSet = new ProblemSet(1);
		IGDRef.AddRefFiles("Data/PF/convex.pf");
		MMZDT prob = new MMZDT("sphere",50, 1,  -80,80);
		prob.setHType("convex");
		((Problem)prob).setName("NIHS2");

		problemSet.add(prob);
		return problemSet;
	}
}
