package momfo.util.ScalarzingFunction;

import lib.experiments.JMException;

public class TchebycheffForMin  extends ScalarzingFunction{

	public double execute(double[] ind,double[] weight,double[] referencePoint) throws JMException{
		double max=-1;
		double fnvue;
		double[] weigh = new double[weight.length];

		for(int i=0;i<weight.length;i++){
			if(Math.abs(weight[i]) < 1.0E-14){
				weigh[i] = 1.0E-6;
			} else {
				weigh[i] = weight[i];
			}
		}

		max = weigh[0]*Math.abs( referencePoint[0] - ind[0]);
		for(int i=0;i<weight.length;i++){
			fnvue = weigh[i]*Math.abs(referencePoint[i] - ind[i]);
			if(max < fnvue)
				max = fnvue;
		}
//
			return (max);
	}


	public String getFunctionName() {
		return "TchebycheffFroMin";
	}


}
