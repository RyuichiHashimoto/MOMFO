package momfo.problems.SOP;


import momfo.util.Configuration;
import momfo.util.JMException;

public class FunctionFactory {
	/**
	 * Gets a crossover operator through its name.
	 *
	 * @param name
	 *            of the operator
	 * @return the operator
	 * @throws JMException
	 */
	public static Function getFunctionProblem(String name, int numberOfDistanceVariabels,double l,double u) throws JMException {

		if (name.equalsIgnoreCase("Sphere"))
			return new Sphere(	numberOfDistanceVariabels,u,l);
		else if (name.equalsIgnoreCase("Rastrigin")){
			return new Rastrigin(	numberOfDistanceVariabels,u,l);
		}else if (name.equalsIgnoreCase("Ackley")){
			return new Ackley(	numberOfDistanceVariabels,u,l);
		}  else if (name.equalsIgnoreCase("Rosenbrock")){
			return new Rosenbrock(	numberOfDistanceVariabels,u,l);
		}  else if (name.equalsIgnoreCase("Schwefel")){
			return new Schwefel(	numberOfDistanceVariabels,u,l);
		}  else if (name.equalsIgnoreCase("Griewank")){
			return new Griewank(	numberOfDistanceVariabels,u,l);
		}  else if (name.equalsIgnoreCase("Weierstrass")){
			return new Weierstrass(	numberOfDistanceVariabels,u,l);
		} else if (name.equalsIgnoreCase("Mean")){
			return new Mean(	numberOfDistanceVariabels,u,l);
		}else if (name.equalsIgnoreCase("HappyCat")){
			return new HappyCatFunction(	numberOfDistanceVariabels,u,l);
		} else if (name.equalsIgnoreCase("BentCigar")){
			return new BentCigarFunction(	numberOfDistanceVariabels,u,l);
		}

	/*	else if (name.equalsIgnoreCase("NonUniformMutation"))
			return new NonUniformMutation(parameters);
		else if (name.equalsIgnoreCase("SwapMutation"))
			return new SwapMutation(parameters);
*/  else {
			Configuration.logger_.severe("Function Problem '" + name + "' not found ");
			Class cls = java.lang.String.class;
			String name2 = cls.getName();
			throw new JMException("Exception in " + name2 + ".getMutationOperator()");
		}
	}
}
