package momfo.problems.MOMFOP.NTU;

import java.io.IOException;

import lib.experiments.CommandSetting;
import momfo.core.ProblemSet;
import momfo.problems.MOMFOP.NTU.base.IO;
import momfo.problems.MOMFOP.NTU.base.MMDTLZ;
import momfo.problems.MOMFOP.NTU.base.MMZDT;

public class NIMS extends ProblemSet{


	public NIMS(CommandSetting st) throws IOException {
		ProblemSet ps1 = getT1();
		ProblemSet ps2 = getT2();
		ProblemSet problemSet = new ProblemSet(2);
		problemSet.setProblemSetName("NIMS");
		problemSet.setMaxDimensionOfObjective(2);
		add(ps1.get(0)).add(ps2.get(0));
	}

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
		problemSet.get(0).setIGDRefFile("Data/PF/sphere.pf");


		problemSet.add(prob);
		return problemSet;
	}

	public static ProblemSet getT2() throws IOException {
		ProblemSet problemSet = new ProblemSet(1);

		MMZDT prob = new MMZDT("sphere",20, 2,  -20,20);
		problemSet.get(0).setIGDRefFile("Data/PF/concave.pf");

		prob.setHType("concave");
		double[][] matrix = IO.readMatrixFromFile("Data/MData/M_NIMS_2.txt");
		prob.getgFunction().setRotationMatrix(matrix);



		problemSet.add(prob);
		return problemSet;
	}
}
