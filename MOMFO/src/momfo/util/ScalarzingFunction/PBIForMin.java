package momfo.util.ScalarzingFunction;

import momfo.util.JMException;

public class PBIForMin extends ScalarzingFunction {

	double theta;

	public PBIForMin(double a){
		theta = a;
	}


	public double execute(double[] ind ,double[] weigh,double[] referencePoint) throws JMException{
		assert ind.length == weigh.length : "the dimension of Solution is "  + ind.length + "  the size of weightedVector is " + weigh.length;

		double dt,dn;
		double scalar_weight=0;
		double d1_reg=0;
		double config = 0;
		double [] weight = new double[weigh.length];
		for(int n=0;n<weight.length;n++){
					weight[n] = weigh[n];
		}

		for(int i=0;i<weight.length;i++){
			d1_reg += (ind[i]-  referencePoint[i] )*weight[i];
			scalar_weight += weight[i]*weight[i];
			}
		scalar_weight = Math.sqrt(scalar_weight);
		dt = d1_reg/scalar_weight;

		dn=0;
		for(int i=0;i<weight.length;i++){
			dn += (ind[i]-  referencePoint[i] - dt*weight[i]/scalar_weight)*( ind[i] -  referencePoint[i] - dt*weight[i]/scalar_weight);
			}
		dn = Math.sqrt(dn);

		double sum = Math.abs(dt) +  theta*dn;

		return (sum);
	}


	public String getFunctionName() {
		return "PBI(5)_ForMin";
	}

}
