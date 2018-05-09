package momfo.problems.MOMFOP.NTU;


import java.io.IOException;

import momfo.core.ProblemSet;
import momfo.core.Solution;
import momfo.problems.MOMFOP.Many_NTU.ManyCIHS;
import momfo.problems.MOMFOP.Many_NTU.ManyCILS;
import momfo.problems.MOMFOP.Many_NTU.ManyCIMS;
import momfo.problems.MOMFOP.Many_NTU.ManyNIHS;
import momfo.problems.MOMFOP.Many_NTU.ManyNILS;
import momfo.problems.MOMFOP.Many_NTU.ManyNIMS;
import momfo.problems.MOMFOP.Many_NTU.ManyPIHS;
import momfo.problems.MOMFOP.Many_NTU.ManyPILS;
import momfo.problems.MOMFOP.Many_NTU.ManyPIMS;
import momfo.problems.ProposingPaper.tenRC_tenA;
import momfo.problems.ProposingPaper.tenR_tenG;
import momfo.util.JMException;
import momfo.util.Indicator.IGDRef;


public class ProblemSetFactory{
	/**
	 * Gets a crossover operator through its name.
	 *
	 * @param name
	 *            of the operator
	 * @return the operator
	 * @throws JMException
	 * @throws IOException
	 */
	public static ProblemSet getProblemSet(String name) throws JMException, IOException {
		IGDRef.clear();
		if (name.equalsIgnoreCase("CIHS"))
			return CIHS.getProblem();
		else if (name.equalsIgnoreCase("CIMS")){
			return CIMS.getProblem();
		}else if (name.equalsIgnoreCase("CILS")){
			return CILS.getProblem();
		}else if (name.equalsIgnoreCase("PIHS")){
			return PIHS.getProblemSet();
		}else if (name.equalsIgnoreCase("PIMS")){
			return PIMS.getProblem();
		}else if (name.equalsIgnoreCase("PILS")){
			return PILS.getProblem();
		}else if (name.equalsIgnoreCase("NIHS")){
			return NIHS.getProblem();
		}else if (name.equalsIgnoreCase("NIMS")){
			return NIMS.getProblemSet();
		}else if (name.equalsIgnoreCase("NILS")){
			return NILS.getProblem();
		}else if (name.equalsIgnoreCase("CIHS3")){
			return CIHS3.getProblem();
		}else if (name.equalsIgnoreCase("PIHS3")){
			return PIHS3.getProblem();
		}else if (name.equalsIgnoreCase("NIHS3")){
			return NIHS3.getProblem();
		}else if (name.equalsIgnoreCase("10R10G")){
			return tenR_tenG.getProblem();
		}else if (name.equalsIgnoreCase("10A10R")){
			return tenRC_tenA.getProblem();
		}else if (name.equalsIgnoreCase("ManyCIHS"))
			return ManyCIHS.getProblem();
		else if (name.equalsIgnoreCase("ManyCIMS")){
			return ManyCIMS.getProblem();
		}else if (name.equalsIgnoreCase("ManyCILS")){
			return ManyCILS.getProblem();
		}else if (name.equalsIgnoreCase("ManyPIHS")){
			return ManyPIHS.getProblemSet();
		}else if (name.equalsIgnoreCase("ManyPIMS")){
			return ManyPIMS.getProblem();
		}else if (name.equalsIgnoreCase("ManyPILS")){
			return ManyPILS.getProblem();
		}else if (name.equalsIgnoreCase("ManyNIHS")){
			return ManyNIHS.getProblem();
		}else if (name.equalsIgnoreCase("ManyNIMS")){
			return ManyNIMS.getProblemSet();
		}else if (name.equalsIgnoreCase("ManyNILS")){
			return ManyNILS.getProblem();
		}
  else {
			Class cls = java.lang.String.class;
			String name2 = cls.getName();
			throw new JMException("Exception in " + name2 + ".getMutationOperator()");
		}
	}


	public static ProblemSet getProblemSet_one(String name) throws JMException, IOException {

		if (name.equalsIgnoreCase("CIHS"))
			return CIHS.getT1();
		else if (name.equalsIgnoreCase("CIMS")){
			return CIMS.getT1();
		}else if (name.equalsIgnoreCase("CILS")){
			return CILS.getT1();
		}else if (name.equalsIgnoreCase("PIHS")){
			return PIHS.getT1();
		}else if (name.equalsIgnoreCase("PIMS")){
			return PIMS.getT1();
		}else if (name.equalsIgnoreCase("PILS")){
			return PILS.getT1();
		}else if (name.equalsIgnoreCase("NIHS")){
			return NIHS.getT1();
		}else if (name.equalsIgnoreCase("NIMS")){
			return NIMS.getT1();
		}else if (name.equalsIgnoreCase("NILS")){
			return NILS.getT1();
		}

	/*	else if (name.equalsIgnoreCase("NonUniformMutation"))
			return new NonUniformMutation(parameters);
		else if (name.equalsIgnoreCase("SwapMutation"))
			return new SwapMutation(parameters);
*/  else {
			Class cls = java.lang.String.class;
			String name2 = cls.getName();
			throw new JMException("Exception in " + name2 + ".getMutationOperator()");
		}
	}


	public static ProblemSet getProblemSet_two(String name) throws JMException, IOException {

		if (name.equalsIgnoreCase("CIHS"))
			return CIHS.getT2();
		else if (name.equalsIgnoreCase("CIMS")){
			return CIMS.getT2();
		}else if (name.equalsIgnoreCase("CILS")){
			return CILS.getT2();
		}else if (name.equalsIgnoreCase("PIHS")){
			return PIHS.getT2();
		}else if (name.equalsIgnoreCase("PIMS")){
			return PIMS.getT2();
		}else if (name.equalsIgnoreCase("PILS")){
			return PILS.getT2();
		}else if (name.equalsIgnoreCase("NIHS")){
			return NIHS.getT2();
		}else if (name.equalsIgnoreCase("NIMS")){
			return NIMS.getT2();
		}else if (name.equalsIgnoreCase("NILS")){
			return NILS.getT2();
		}

	/*	else if (name.equalsIgnoreCase("NonUniformMutation"))
			return new NonUniformMutation(parameters);
		else if (name.equalsIgnoreCase("SwapMutation"))
			return new SwapMutation(parameters);
*/  else {
			Class cls = java.lang.String.class;
			String name2 = cls.getName();
			throw new JMException("Exception in " + name2 + ".getMutationOperator()");
		}
	}


	public static void main(String[] args) throws JMException, IOException{
//		String[] problemNameSet = {"CIHS","CIMS","CILS","PIHS","PIMS","PILS","NIHS","NIMS","NILS"};

		String[] problemNameSet = {"CIHS3","PIHS3","NIHS3"};
//		String[] problemNameSet = {"PIHS3","NIHS3"};

		for(int p = 0; p < problemNameSet.length;p++){

			String problemName = problemNameSet[p] ;
			ProblemSet problemSet = getProblemSet(problemName);

			Solution sol = new Solution(problemSet,0);
			for(int val = 0 ; val < sol.getNumberOfVariables();val++){
				sol.setValue(val, 0.3);				
			}
			problemSet.get(0).evaluate(sol);

			System.out.print(problemName + "	Task1	");
			for(int o = 0;o < problemSet.get(0).getNumberOfObjectives();o++){
				System.out.print(sol.getObjective(o)+"	");
			}

			System.out.println();
			System.out.print(problemName + "	Task2	");

			sol = new Solution(problemSet,1);

			for(int val = 0 ; val < sol.getNumberOfVariables();val++){
				sol.setValue(val, 0.3);
			}

			problemSet.get(1).evaluate(sol);

			for(int o = 0;o <problemSet.get(1).getNumberOfObjectives() ;o++){
				System.out.print(sol.getObjective(o)+"	");
			}
			System.out.println();
			System.out.print(problemName + "	Task3	");
			
			sol = new Solution(problemSet,2);

			for(int val = 0 ; val < sol.getNumberOfVariables();val++){
				sol.setValue(val, 0.3);
			}

			problemSet.get(2).evaluate(sol);

			for(int o = 0;o <problemSet.get(1).getNumberOfObjectives() ;o++){
				System.out.print(sol.getObjective(o)+"	");
			}
			System.out.println();

		}
	}
}
