package momfo.problems.MOMFOP.NTU;

import java.io.FileNotFoundException;
import java.io.IOException;

import momfo.core.Problem;
import momfo.core.ProblemSet;
import momfo.problems.MOMFOP.NTU.base.MMDTLZ;
import momfo.problems.MOMFOP.NTU.base.MMZDT;
import momfo.util.Indicator.IGDRef;


public class NIHS3 {

	public static ProblemSet getProblem() throws IOException {
		IGDRef.clear();
		ProblemSet ps1 = getT1();
		ProblemSet ps2 = getT2();
		ProblemSet ps3 = getT3();

		ProblemSet problemSet = new ProblemSet(3);
		problemSet.setProblemSetName("NIHS3");
		problemSet.setMaxDimensionOfObjective(2);
		problemSet.add(ps1.get(0));
		problemSet.add(ps2.get(0));
		problemSet.add(ps3.get(0));

		return problemSet;
	}


	public static ProblemSet getT1() throws IOException {
		ProblemSet problemSet = new ProblemSet(1);

		MMDTLZ prob = new MMDTLZ("rosenbrock",2, 50, 1, -80,80);
		IGDRef.AddRefFiles("Data/PF/circle.pf");
		((Problem)prob).setName("NIHS1");

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

	private static ProblemSet getT3() throws FileNotFoundException, IOException {
		ProblemSet problemSet = new ProblemSet(1);

		IGDRef.AddRefFiles("Data/PF/concave.pf");
		MMZDT prob = new MMZDT("happycat",50, 1,  -80,80);
		((Problem)prob).setName("NIHS3");
		prob.setHType("concave");

		problemSet.add(prob);
		return problemSet;
	}


	public static void main(String[] args) {

	}

}
