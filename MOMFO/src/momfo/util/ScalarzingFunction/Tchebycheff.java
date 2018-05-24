package momfo.util.ScalarzingFunction;

import lib.experiments.JMException;

public class Tchebycheff  extends ScalarzingFunction{

	
	
	
	public double execute(double[] ind,double[] weight,double[] referencePoint) throws JMException{
		double max=-1;
		double fnvue;
		max = weight[0]*Math.abs( referencePoint[0] - ind[0]);

			for(int i=0;i<weight.length;i++){
				fnvue = weight[i]*Math.abs(referencePoint[i] - ind[i]);
				if(max < fnvue)
					max = fnvue;
			}
//
			return -1*(max);
	}


	public String getFunctionName() {
		return "Tchebycheff";
	}


}
