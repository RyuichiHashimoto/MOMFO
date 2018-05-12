package momfo.util.ScalarzingFunction;

import javax.naming.NameNotFoundException;

import lib.experiments.CommandSetting;
import lib.experiments.ParameterNames;
import momfo.util.JMException;

public class PBI extends ScalarzingFunction {




	double theta = -1000;

	public PBI(){
	}

	public void build(CommandSetting setting) throws NameNotFoundException  {
		if(setting.getAsStr(ParameterNames.PBI_PARAMETER).contains(ParameterNames.SETTING_FILE_DEMILITER))
			theta = setting.getAsDArray(ParameterNames.PBI_PARAMETER)[setting.getAsInt(ParameterNames.TEMP_TASK_NUMBER)];
		else
			theta = setting.get(ParameterNames.PBI_PARAMETER);
	}

	public double execute(double[] ind ,double[] weigh,double[] referencePoint) throws JMException{
		double dt,dn;
		double scalar_weight=0;
		double d1_reg=0;
		double config = 0;
		double [] weight = new double[weigh.length];
		for(int i=0;i<weight.length;i++){
			 weight[i]  = weigh[i];
		}

		for(int i=0;i<weight.length;i++){
			d1_reg += (ind[i]-  referencePoint[i] )*weight[i];
			scalar_weight += weight[i]*weight[i];
			}
		scalar_weight = Math.sqrt(scalar_weight);
		dt = d1_reg/scalar_weight;

		dn=0;
		for(int i=0;i<weight.length;i++){
			dn += (ind[i]-  referencePoint[i] - dt*weight[i]/scalar_weight)*(ind[i]-  referencePoint[i] - dt*weight[i]/scalar_weight);
			}
		dn = Math.sqrt(dn);

		double sum = Math.abs(dt) +  theta*dn;

		return -1*(sum);
	};

}
