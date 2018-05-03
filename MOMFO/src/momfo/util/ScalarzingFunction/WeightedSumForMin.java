package momfo.util.ScalarzingFunction;

import momfo.util.JMException;

public class WeightedSumForMin extends ScalarzingFunction{

	public double execute(double[] ind ,double[] weigh,double[] referencePoint) throws JMException{
		double sum =0 ;

			for(int i=0;i<weigh.length;i++){

				sum += weigh[i]*ind[i];
			}

			return (sum);
	};

	public String getFunctionName() {
		return "WeightedSumForMin";
	}

}
