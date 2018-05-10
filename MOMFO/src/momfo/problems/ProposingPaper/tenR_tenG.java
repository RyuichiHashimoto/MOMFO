package momfo.problems.ProposingPaper;

import java.io.IOException;

import momfo.Indicator.IGDRef;
import momfo.core.Problem;
import momfo.core.ProblemSet;
import momfo.problems.MOMFOP.NTU.base.IO;
import momfo.problems.MOMFOP.NTU.base.MMZDT;


public class tenR_tenG {

	public static ProblemSet getProblem() throws IOException {
		IGDRef.clear();
		ProblemSet ps1 = getT1();
		ProblemSet ps2 = getT2();
		ProblemSet problemSet = new ProblemSet(2);
		problemSet.setProblemSetName("10R10G");
		problemSet.setMaxDimensionOfObjective(2);
		problemSet.add(ps1.get(0));
		problemSet.add(ps2.get(0));
		return problemSet;
	}
	public static ProblemSet getProblem(int time) throws IOException {
		IGDRef.clear();
		ProblemSet ps1 = getT1(time);
		ProblemSet ps2 = getT2(time);
		ProblemSet problemSet = new ProblemSet(2);
		problemSet.setProblemSetName("10R10G");

		problemSet.setMaxDimensionOfObjective(2);
		problemSet.add(ps1.get(0));
		problemSet.add(ps2.get(0));
		return problemSet;
	}
	public static ProblemSet getT1() throws IOException {
		ProblemSet problemSet = new ProblemSet(1);

		MMZDT prob = new MMZDT("rastrigin",10, 1,  -5,5);
		IGDRef.AddRefFiles("Data\\PF\\convex.pf");

		((Problem)prob).setName("CIHS1");
	//	double[][] matrix = IO.readMatrixFromFile("Data/MData/M_NIMS_2.txt");
//		prob.getgFunction().setRotationMatrix(matrix);
		problemSet.add(prob);

		return problemSet;
	}


	public static ProblemSet getT1(int time) throws IOException {
		ProblemSet problemSet = new ProblemSet(1);

		MMZDT prob = new MMZDT("rastrigin",10, 1,  -5,5);
		IGDRef.AddRefFiles("Data\\PF\\convex.pf");

		((Problem)prob).setName("CIHS1");
		double[][] matrix = IO.readMatrixFromFile("Data/MData/10R10G/Task1/roration"+time + ".dat");
		prob.getgFunction().setRotationMatrix(matrix);
		problemSet.add(prob);

		return problemSet;
	}
	public static ProblemSet getT2(int time) throws IOException {
		ProblemSet problemSet = new ProblemSet(1);

		IGDRef.AddRefFiles("Data\\PF\\convex.pf");
		MMZDT prob = new MMZDT("griewank",10, 1,  -512,512);
		double[][] matrix = IO.readMatrixFromFile("Data/MData/10R10G/Task2/roration"+time + ".dat");
		prob.getgFunction().setRotationMatrix(matrix);
		prob.setHType("convex");
		((Problem)prob).setName("CIHS2");

		problemSet.add(prob);
		return problemSet;
	}


	public static ProblemSet getT2() throws IOException {
		ProblemSet problemSet = new ProblemSet(1);

		IGDRef.AddRefFiles("Data\\PF\\convex.pf");
		MMZDT prob = new MMZDT("griewank",10, 1,  -512,512);
		prob.setHType("convex");
		((Problem)prob).setName("CIHS2");

		problemSet.add(prob);
		return problemSet;
	}
}
