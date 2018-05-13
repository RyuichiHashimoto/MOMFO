package momfo.problems.MOMFOP.NTU;

import java.io.IOException;

import lib.experiments.CommandSetting;
import momfo.core.ProblemSet;
import momfo.problems.MOMFOP.NTU.base.IO;
import momfo.problems.MOMFOP.NTU.base.MMDTLZ;


public class PILS extends ProblemSet{

	public PILS(CommandSetting st) throws IOException {
		ProblemSet ps1 = getT1();
		ProblemSet ps2 = getT2();
		ProblemSet problemSet = new ProblemSet(2);
		problemSet.setProblemSetName("PILS");
		problemSet.setMaxDimensionOfObjective(2);
		add(ps1.get(0)).add(ps2.get(0));
	}


	public static ProblemSet getProblem() throws IOException {
		ProblemSet ps1 = getT1();
		ProblemSet ps2 = getT2();
		ProblemSet problemSet = new ProblemSet(2);
		problemSet.setProblemSetName("PILS");
		problemSet.setMaxDimensionOfObjective(2);
		problemSet.add(ps1.get(0));
		problemSet.add(ps2.get(0));
		return problemSet;

	}

	public static ProblemSet getT1() throws IOException {
		ProblemSet problemSet = new ProblemSet(1);

		MMDTLZ prob = new MMDTLZ("griewank",2, 50, 1, -50,50);
		problemSet.get(0).setIGDRefFile("Data/PF/circle.pf");
		problemSet.add(prob);
		return problemSet;
	}

	public static ProblemSet getT2() throws IOException {
		ProblemSet problemSet = new ProblemSet(1);

		MMDTLZ prob = new MMDTLZ("ackley",2, 50, 1, -100,100);
		problemSet.get(0).setIGDRefFile("Data/PF/circle.pf");

		double[] shiftValues= IO.readShiftValuesFromFile("Data/ShiftData/S_PILS_2.txt");
		prob.getgFunction().setShiftMatrix(shiftValues);


		problemSet.add(prob);
		return problemSet;
	}

}
