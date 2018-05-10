package momfo.problems.MOMFOP.NTU;

import java.io.IOException;

import momfo.Indicator.IGDRef;
import momfo.core.Problem;
import momfo.core.ProblemSet;
import momfo.problems.MOMFOP.NTU.base.IO;
import momfo.problems.MOMFOP.NTU.base.MMDTLZ;
import momfo.problems.MOMFOP.NTU.base.MMZDT;

public class NIMS {

	public static ProblemSet getProblemSet() throws IOException {
		ProblemSet ps1 = getT1();
		ProblemSet ps2 = getT2();
		ProblemSet problemSet = new ProblemSet(2);
		problemSet.setProblemSetName("NIMS");
		problemSet.setMaxDimensionOfObjective(3);
		problemSet.add(ps1.get(0));
		problemSet.add(ps2.get(0));
		return problemSet;

	}

	public static ProblemSet getT1() throws IOException {
		ProblemSet problemSet = new ProblemSet(1);

		MMDTLZ prob = new MMDTLZ("rosenbrock",3, 20, 1, -20,20);

		((Problem)prob).setName("NIMS1");
		IGDRef.AddRefFiles("Data/PF/sphere.pf");
		problemSet.add(prob);
		return problemSet;
	}

	public static ProblemSet getT2() throws IOException {
		ProblemSet problemSet = new ProblemSet(1);

		MMZDT prob = new MMZDT("sphere",20, 2,  -20,20);
		prob.setHType("concave");
		IGDRef.AddRefFiles("Data/PF/concave.pf");
		double[][] matrix = IO.readMatrixFromFile("Data/MData/M_NIMS_2.txt");
		prob.getgFunction().setRotationMatrix(matrix);

		((Problem)prob).setName("NIMS2");


		problemSet.add(prob);
		return problemSet;
	}
}
