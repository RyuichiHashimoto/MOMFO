package momfo.operators.initializer;

import java.io.IOException;

import javax.naming.NamingException;

import lib.experiments.CommandSetting;
import lib.lang.NeedVerified;
import lib.lang.NotVerifiedYet;
import momfo.core.Solution;

public class RealArray extends Initializer{

	double[] upperlimit;
	double[] lowerlimit;


	@Override
	public void build(CommandSetting st) throws NamingException, ReflectiveOperationException, IOException {
		super.build(st);

		upperlimit = problem.getUpperLimit().clone();
		lowerlimit = problem.getLowerLimit().clone();

		assert upperlimit.length == lowerlimit.length : this.getName()+": a length of upperlimit is " + upperlimit.length + ", and one of lower limit is " + lowerlimit.length;
	}


	@Override
	@NeedVerified
	public void initialize(Solution sol) throws NotVerifiedYet {
		for(int i = 0;i <upperlimit.length;i++) {
			double value = lowerlimit[i] + (upperlimit[i]-lowerlimit[i])*mt.nextDoubleII();
			sol.setValue(i, value);
		}
		throw new NotVerifiedYet(this);
	}

}
