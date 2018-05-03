package momfo.util.ScalarzingFunction;

import momfo.util.JMException;

public class WeightedSum extends ScalarzingFunction{

	public double execute(double[] ind ,double[] weight,double[] referencePoint) throws JMException{
		double sum =0 ;
			for(int i=0;i<weight.length;i++){
				sum += weight[i]*ind[i];
			}


			return sum;
	};

	public String getFunctionName() {
		return "WeightedSum";
	}

}
