package momfo.problems.MOMFOP.NTU;


import java.io.IOException;

import javax.naming.NamingException;

import lib.experiments.CommandSetting;
import lib.lang.Generics;
import lib.math.BuildInRandom;
import momfo.core.ProblemSet;
import momfo.core.Solution;
import momfo.problems.ProposingPaper.tenRC_tenA;
import momfo.problems.ProposingPaper.tenR_tenG;
import momfo.util.JMException;


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
		}else if (name.equalsIgnoreCase("10R10G")){
			return tenR_tenG.getProblem();
		}else if (name.equalsIgnoreCase("10A10R")){
			return tenRC_tenA.getProblem();
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


	public static void main(String[] args) throws JMException, IOException, NamingException, ReflectiveOperationException{
		String[] problemNameSet = {"CIHS","CIMS","CILS","PIHS","PIMS","PILS","NIHS","NIMS","NILS"};

		BuildInRandom random = new BuildInRandom(3);
		CommandSetting setting = new CommandSetting();
		for(int p = 0; p < problemNameSet.length;p++){
			String problemName = problemNameSet[p] ;
			ProblemSet problemSet = null;
			setting.putForce("Problem", "momfo.problems.MOMFOP.NTU." +problemName);

			problemSet = (ProblemSet)Generics.getToClass("momfo.problems.MOMFOP.NTU." +problemName, "momfo.problems.MOMFOP.NTU.").getDeclaredConstructor(CommandSetting.class).newInstance(setting);
//			System.out.println(problemSet.getProblemName());
			Solution sol = new Solution(problemSet,0,random);
			for(int val = 0 ; val < sol.getNumberOfVariables();val++){
				sol.setValue(val, 0.3);
			}
			problemSet.get(0).evaluate(sol);

//			System.out.print(problemName + "	Task1	");
			/*for(int o = 0;o < problemSet.get(0).getNumberOfObjectives();o++){
				System.out.print("a["+(o)+"]="+sol.getObjective(o)+";");
			}
			System.out.println();
			System.out.print("put(\""+problemName+"\",a.clone());");
			System.out.println();
			*/
//			System.out.println();
//			System.out.print(problemName + "	Task2	");

			sol = new Solution(problemSet.get(1),random);

			for(int val = 0 ; val < sol.getNumberOfVariables();val++){
				sol.setValue(val, 0.3);
			}

			problemSet.get(1).evaluate(sol);

			for(int o = 0;o < problemSet.get(1).getNumberOfObjectives();o++){
				System.out.print("b["+(o)+"]="+sol.getObjective(o)+";");
			}
			System.out.println();
			System.out.print("put(\""+problemName+"\",b.clone());");
			System.out.println();
		}
	}
}
