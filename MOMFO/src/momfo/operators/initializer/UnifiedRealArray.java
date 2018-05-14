package momfo.operators.initializer;

import java.io.IOException;

import javax.naming.NamingException;

import lib.experiments.CommandSetting;
import lib.experiments.ParameterNames;
import lib.experiments.Exception.CommandSetting.notFoundException;
import lib.lang.NeedVerified;
import lib.lang.NotVerifiedYet;
import momfo.core.Solution;

public class UnifiedRealArray extends Initializer {

	double[] upperlimit;
	double[] lowerlimit;

	@Override
	public void build(CommandSetting st) throws notFoundException, NamingException, ReflectiveOperationException, IOException {
		super.build(st);
		int dims = -1;
		if(isMultitask) {
			dims =  problemSet.getMaxDimension();
			upperlimit = new double[dims];
			lowerlimit = new double[dims];
			for(int i=0;i<dims;i++) {
				upperlimit[i] = ParameterNames.UPPERLIMIT_UNIFIEDSPACE;
				lowerlimit[i] = ParameterNames.LOWERLIMIT_UNIFIEDSPACE;
			}			
		} else {
			dims = problem.getNumberOfVariables();
			upperlimit = new double[dims];
			lowerlimit = new double[dims];
			for(int i=0;i<dims;i++) {
				upperlimit[i] = ParameterNames.UPPERLIMIT_UNIFIEDSPACE;
				lowerlimit[i] = ParameterNames.LOWERLIMIT_UNIFIEDSPACE;
			}
		}
		assert dims <= 0: this.getName()+": the dimension of variable space is not found exception";		
	}

	@Override
	@NeedVerified
	public void initialize(Solution sol) throws NotVerifiedYet {
		for (int i = 0; i < upperlimit.length; i++) {
			double value = lowerlimit[i] + (upperlimit[i] - lowerlimit[i]) * mt.nextDoubleII();
			sol.setValue(i, value);
		}
		throw new NotVerifiedYet(this);
	}

}
