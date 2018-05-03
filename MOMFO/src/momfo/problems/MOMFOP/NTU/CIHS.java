package momfo.problems.MOMFOP.NTU;

import java.io.IOException;

import momfo.core.Problem;
import momfo.core.ProblemSet;
import momfo.problems.MOMFOP.NTU.base.MMDTLZ;
import momfo.problems.MOMFOP.NTU.base.MMZDT;
import momfo.util.Indicator.IGDRef;


public class CIHS {

	public static ProblemSet getProblem() throws IOException {
		IGDRef.clear();
		ProblemSet ps1 = getT1();
		ProblemSet ps2 = getT2();
		ProblemSet problemSet = new ProblemSet(2);
		problemSet.setProblemSetName("CIHS");
		problemSet.setMaxDimensionOfObjective(2);
		problemSet.add(ps1.get(0));
		problemSet.add(ps2.get(0));
		return problemSet;
	}


	public static ProblemSet getT1() throws IOException {
		ProblemSet problemSet = new ProblemSet(1);

		MMDTLZ prob = new MMDTLZ("sphere",2, 50, 1, -100,100);
		IGDRef.AddRefFiles("Data/PF/circle.pf");

		((Problem)prob).setName("CIHS1");

		problemSet.add(prob);

		return problemSet;
	}

	public static ProblemSet getT2() throws IOException {
		ProblemSet problemSet = new ProblemSet(1);

		IGDRef.AddRefFiles("Data/PF/concave.pf");
		MMZDT prob = new MMZDT("mean",50, 1,  -100,100);
		prob.setHType("concave");
		((Problem)prob).setName("CIHS2");

		problemSet.add(prob);
		return problemSet;
	}
	
	
}
