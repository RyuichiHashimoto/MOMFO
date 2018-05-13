package momfo.problems.MOMFOP.NTU;

import java.io.IOException;

import lib.experiments.CommandSetting;
import momfo.core.ProblemSet;
import momfo.problems.MOMFOP.NTU.base.IO;
import momfo.problems.MOMFOP.NTU.base.MMDTLZ;
import momfo.problems.MOMFOP.NTU.base.MMZDT;

public class NILS extends ProblemSet{


	public NILS(CommandSetting st) throws IOException {
		ProblemSet ps1 = getT1();
		ProblemSet ps2 = getT2();
		this.setProblemSetName("NIMS");
		this.setMaxDimensionOfObjective(2);
		add(ps1.get(0)).add(ps2.get(0));
	}
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
		prob.setIGDRefFile("Data/PF/sphere.pf");

		double shiftValues[] = IO.readShiftValuesFromFile("Data/ShiftData/S_NILS_1.txt");
		prob.getgFunction().setShiftMatrix(shiftValues);

		problemSet.add(prob);
		return problemSet;
	}

	public static ProblemSet getT2() throws IOException {
		ProblemSet problemSet = new ProblemSet(1);

		MMZDT prob = new MMZDT("ackley",50, 2,  -100,100);
		prob.setIGDRefFile("Data/PF/concave.pf");

		prob.setHType("concave");

		problemSet.add(prob);
		return problemSet;
	}
}
