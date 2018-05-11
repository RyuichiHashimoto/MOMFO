package momfo.problems.MOMFOP.NTU;

import java.io.IOException;

import lib.experiments.CommandSetting;
import lib.io.output.fileSubscription;
import momfo.Indicator.IGDRef;
import momfo.core.Problem;
import momfo.core.ProblemSet;
import momfo.core.Solution;
import momfo.problems.MOMFOP.NTU.base.IO;
import momfo.problems.MOMFOP.NTU.base.MMZDT;
import momfo.util.JMException;

public class PIHS extends ProblemSet{

	public PIHS(CommandSetting st) throws IOException {
		ProblemSet ps1 = getT1();
		ProblemSet ps2 = getT2();
		ProblemSet problemSet = new ProblemSet(2);
		problemSet.setProblemSetName("PIHS");
		problemSet.setMaxDimensionOfObjective(2);
		
		add(ps1.get(0)).add(ps2.get(0));		
	}
	
	
	public static ProblemSet getProblemSet() throws IOException {
		ProblemSet ps1 = getT1();
		ProblemSet ps2 = getT2();
		ProblemSet problemSet = new ProblemSet(2);
		problemSet.setProblemSetName("PIHS");
		problemSet.setMaxDimensionOfObjective(2);
		problemSet.add(ps1.get(0));
		problemSet.add(ps2.get(0));
		return problemSet;

	}

	public static ProblemSet getT1() throws IOException {
		ProblemSet problemSet = new ProblemSet(1);

		MMZDT prob = new MMZDT("sphere",50, 1,  -100,100);
		prob.setHType("convex");
		((Problem)prob).setName("PIHS1");
		IGDRef.AddRefFiles("Data/PF/convex.pf");
		problemSet.add(prob);
		return problemSet;
	}

	public static ProblemSet getT2() throws IOException {
		ProblemSet problemSet = new ProblemSet(1);
		IGDRef.AddRefFiles("Data/PF/convex.pf");
		MMZDT prob = new MMZDT("rastrigin",50, 1,  -100,100);
		prob.setHType("convex");

		double[] shiftValues = IO.readShiftValuesFromFile("Data/ShiftData/S_PIHS_2.txt");
		prob.getgFunction().setShiftMatrix(shiftValues);

		((Problem)prob).setName("PIHS2");

		problemSet.add(prob);
		return problemSet;
	}

	public static void main(String[] args) throws IOException, JMException {
		ProblemSet problem = getT2();
		Solution sol = new Solution(problem.get(0),null);

		for(int i =1;i<50;i++){
			sol.setValue(i, 0.5);
		}
		double[][] obj = new double[501][2];

		for(int j = 0; j <= 500;j++){
			sol.setValue(0, (j)/500.0);
			problem.get(0).evaluate(sol);
			obj[j][0] = sol.getObjective(0);
			obj[j][1] = sol.getObjective(1);
		}
		System.out.println(sol.getObjective(0)+"	"+ sol.getObjective(1));
		fileSubscription.printToFile("do.dat", obj);
		
		

	}

}
