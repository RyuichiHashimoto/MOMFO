package momfo.util.ScalarzingFunction;

import momfo.util.Configuration;
import momfo.util.JMException;

public class ScalarzingFunctionFactory {

	public static ScalarzingFunction getScalarzingFunctionOperator(String name,double theta) throws JMException {

		if (name.equalsIgnoreCase("WeightedSum"))
			return new WeightedSum();
		else if(name.equalsIgnoreCase("WeightedSumForMin"))
			return new WeightedSumForMin();
		else if (name.equalsIgnoreCase("Tchebycheff"))
			return new Tchebycheff();
		else if (name.equalsIgnoreCase("TchebycheffForMin"))
			return new TchebycheffForMin();
		else if (name.equalsIgnoreCase("PBI") )
			return new PBI(theta);
		else if (name.equalsIgnoreCase("PBIForMin") )
			return new PBIForMin(theta);
		else if (name.equalsIgnoreCase("InvertedPBIForMin") )
			return new InvertedPBIForMIN(theta);
		else {
			Configuration.logger_.severe("Operator '" + name + "' not found ");
			Class cls = java.lang.String.class;
			String name2 = cls.getName();
			throw new JMException("Exception in " + name2 + ".getScalarzingFunctinoOperator()");
		}
	}

}
