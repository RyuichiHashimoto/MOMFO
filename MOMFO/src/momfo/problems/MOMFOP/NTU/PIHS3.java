package momfo.problems.MOMFOP.NTU;

import java.io.FileNotFoundException;
import java.io.IOException;

import momfo.core.Problem;
import momfo.core.ProblemSet;
import momfo.problems.MOMFOP.NTU.base.IO;
import momfo.problems.MOMFOP.NTU.base.MMDTLZ;
import momfo.problems.MOMFOP.NTU.base.MMZDT;
import momfo.util.Indicator.IGDRef;


public class PIHS3 {

	public static ProblemSet getProblem() throws IOException {
		IGDRef.clear();
		ProblemSet ps1 = getT1();
		ProblemSet ps2 = getT2();
		ProblemSet ps3 = getT3();

		ProblemSet problemSet = new ProblemSet(3);
		problemSet.setProblemSetName("PIHS3");
		problemSet.setMaxDimensionOfObjective(2);
		problemSet.add(ps1.get(0));
		problemSet.add(ps2.get(0));
		problemSet.add(ps3.get(0));

		return problemSet;
	}


	public static ProblemSet getT1() throws IOException {
		ProblemSet problemSet = new ProblemSet(1);

		MMZDT prob = new MMZDT("sphere", 50, 1, -100,100);
		IGDRef.AddRefFiles("Data/PF/convex.pf");
		prob.setHType("convex");
		((Problem)prob).setName("PIHS1");

		problemSet.add(prob);
		return problemSet;
	}


	public static ProblemSet getT2() throws IOException {
		ProblemSet problemSet = new ProblemSet(1);

		IGDRef.AddRefFiles("Data/PF/convex.pf");
		MMZDT prob = new MMZDT("Rastrigin",50, 1,  -100,100);
		prob.setHType("convex");
		((Problem)prob).setName("CIHS2");

		double[] shiftValues = IO.readShiftValuesFromFile("Data/ShiftData/S_PIHS3_2.txt");
		prob.getgFunction().setShiftMatrix(shiftValues);


		problemSet.add(prob);
		return problemSet;
	}

	private static ProblemSet getT3() throws FileNotFoundException, IOException {
		ProblemSet problemSet = new ProblemSet(1);

		IGDRef.AddRefFiles("Data/PF/circle.pf");
		MMDTLZ prob = new MMDTLZ("bentcigar",2, 50, 1, -100,100);

		((Problem)prob).setName("PIHS3");

		double[] shiftValues = IO.readShiftValuesFromFile("Data/ShiftData/S_PIHS3_3.txt");
		prob.getgFunction().setShiftMatrix(shiftValues);

		problemSet.add(prob);
		return problemSet;
	}


	public static void main(String[] args) {

	}

}
