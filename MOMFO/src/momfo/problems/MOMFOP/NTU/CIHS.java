package momfo.problems.MOMFOP.NTU;

import java.io.IOException;

import lib.experiments.CommandSetting;
import momfo.core.ProblemSet;
import momfo.problems.MOMFOP.NTU.base.MMDTLZ;
import momfo.problems.MOMFOP.NTU.base.MMZDT;


public class CIHS extends ProblemSet{
	public CIHS(CommandSetting st) throws IOException {

		ProblemSet ps1 = getT1();
		ProblemSet ps2 = getT2();
		ProblemSet problemSet = new ProblemSet(2);

		setProblemSetName("CIHS");
		setMaxDimensionOfObjective(2);
		add(ps1.get(0)).add(ps2.get(0));

	}



	public static ProblemSet getProblem() throws IOException {
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
		
		
		problemSet.add(prob);
		problemSet.get(0).setIGDRefFile("Data/PF/circle.pf");

		return problemSet;
	}

	public static ProblemSet getT2() throws IOException {
		ProblemSet problemSet = new ProblemSet(1);

		MMZDT prob = new MMZDT("mean",50, 1,  -100,100);
		prob.setHType("concave");

		problemSet.add(prob);
		problemSet.get(0).setIGDRefFile("Data/PF/concave.pf");

		return problemSet;
	}


}
