package momfo.operators.initializer;

import java.io.IOException;

import javax.naming.NamingException;

import lib.experiments.CommandSetting;
import lib.experiments.ParameterNames;
import lib.lang.NotVerifiedYet;
import momfo.core.Solution;

public class UnifiedRealArray extends Initializer {

	double[] upperlimit;
	double[] lowerlimit;

	@Override
	public void build(CommandSetting st) throws NamingException, ReflectiveOperationException, IOException {
		super.build(st);
		int dim = -1;
		if(isMultitask) {
			dim =  problemSet.getMaxDimension();
			upperlimit = new double[dim];
			lowerlimit = new double[dim];
			for(int i=0;i<dim;i++) {
				upperlimit[i] = ParameterNames.UPPERLIMIT_UNIFIEDSPACE;
				lowerlimit[i] = ParameterNames.LOWERLIMIT_UNIFIEDSPACE;
			}			
		} else {
			dim = problem.getNumberOfVariables();
			upperlimit = new double[dim];
			lowerlimit = new double[dim];
			
			for(int i=0;i<dim;i++) {
				upperlimit[i] = ParameterNames.UPPERLIMIT_UNIFIEDSPACE;
				lowerlimit[i] = ParameterNames.LOWERLIMIT_UNIFIEDSPACE;
			}
		}
		assert dim > 0: this.getName()+": the dimension of variable space must be more than 1, not " + dim;		
	}

	@Override
	public void initialize(Solution sol) throws NotVerifiedYet {
		for (int i = 0; i < upperlimit.length; i++) {
			double value = lowerlimit[i] + (upperlimit[i] - lowerlimit[i]) * mt.nextDoubleII();
			sol.setValue(i, value);			
		}
	}

}
