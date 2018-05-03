package momfo.operators.crossover;
import java.util.HashMap;

import momfo.util.Configuration;
import momfo.util.JMException;

public class CrossoverFactory {
	public static Crossover getCrossoverOperator(String name,
		HashMap parameters) throws JMException {
		if (name.equalsIgnoreCase("SBXCrossover")){
			return new SBXCrossover(parameters);
		}else if (name.equalsIgnoreCase("SBXCrossover2")){
			return new SBXCrossover2(parameters);
		} else if (name.equalsIgnoreCase("SBXCrossoverWithoutSwap")){
			return new SBXCrossoverWithoutSwap(parameters);
		} else if (name.equalsIgnoreCase("SBXCrossoverWithoutSwap2")){
			return new SBXCrossoverWithoutSwap2(parameters);
		}else if (name.equalsIgnoreCase("UniformCrossover")){
			return new UniformCrossover(parameters);
		}
/*		else if (name.equalsIgnoreCase("SinglePointCrossover"))
			return new SinglePointCrossover(parameters);
		else if (name.equalsIgnoreCase("PMXCrossover"))
			return new PMXCrossover(parameters);
		else if (name.equalsIgnoreCase("TwoPointsCrossover"))
			return new TwoPointsCrossover(parameters);
		else if (name.equalsIgnoreCase("HUXCrossover"))
			return new HUXCrossover(parameters);
		else if (name.equalsIgnoreCase("DifferentialEvolutionCrossover"))
			return new DifferentialEvolutionCrossover(parameters);
		else if (name.equalsIgnoreCase("BLXAlphaCrossover"))
			return new BLXAlphaCrossover(parameters);
		}*/ else{
			Configuration.logger_
					.severe("CrossoverFactory.getCrossoverOperator. " + "Operator '" + name + "' not found ");
			throw new JMException("Exception in " + name + ".getCrossoverOperator()");
		} // else
	} // getCrossoverOperator

}
