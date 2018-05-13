package momfo.problems.MOMFOP.NTU;

import java.io.IOException;

import lib.experiments.CommandSetting;
import momfo.core.ProblemSet;
import momfo.problems.MOMFOP.NTU.base.MMDTLZ;
import momfo.problems.MOMFOP.NTU.base.MMZDT;


public class NIHS extends ProblemSet{



	public NIHS(CommandSetting st) throws IOException {
		ProblemSet ps1 = getT1();
		ProblemSet ps2 = getT2();
		this.setProblemSetName("NIHS");
		this.setMaxDimensionOfObjective(2);
		add(ps1.get(0)).add(ps2.get(0));
	}



	public static ProblemSet getProblem() throws IOException {
		ProblemSet ps1 = getT1();
		ProblemSet ps2 = getT2();
		ProblemSet problemSet = new ProblemSet(2);
		problemSet.setProblemSetName("NIHS");
		problemSet.setMaxDimensionOfObjective(2);
		problemSet.add(ps1.get(0));
		problemSet.add(ps2.get(0));
		return problemSet;

	}

	public static ProblemSet getT1() throws IOException {
		ProblemSet problemSet = new ProblemSet(1);

		MMDTLZ prob = new MMDTLZ("rosenbrock",2, 50, 1, -80,80);
		problemSet.get(0).setIGDRefFile("Data/PF/circle.pf");

		problemSet.add(prob);
		return problemSet;
	}

	public static ProblemSet getT2() throws IOException {
		ProblemSet problemSet = new ProblemSet(1);
		problemSet.get(0).setIGDRefFile("Data/PF/convex.pf");

		MMZDT prob = new MMZDT("sphere",50, 1,  -80,80);
		prob.setHType("convex");

		problemSet.add(prob);
		return problemSet;
	}
}
