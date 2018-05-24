package momfo.operators.mutation;

import static lib.experiments.ParameterNames.*;

import javax.naming.NameNotFoundException;

import lib.experiments.CommandSetting;
import lib.experiments.JMException;
import lib.experiments.NeedParameters;
import momfo.core.Solution;

public class PolynomialMutation extends Mutation {

	private Double distributionIndex;

	@Override
	@NeedParameters({ MUTATIONProbability, PMDisIndex, RANDOM_GENERATOR })
	public void build(CommandSetting s) throws NameNotFoundException, JMException {
		super.build(s);

		double mp = s.getAsDouble(MUTATIONProbability);

		double mdisind = s.getAsDouble(PMDisIndex);
		if (mdisind < 0)
			throw new IllegalArgumentException(PMDisIndex + " must be non-negative but was " + mdisind);

		mutationProbability = mp;
		distributionIndex = mdisind;

	}

	@Override
	public Solution mutation( Solution parent) throws JMException {

		return doMutation(mutationProbability, parent);
	}

	public Solution doMutation(double probability, Solution solution) throws JMException {

		probability = probability >= 0 ? probability : -1 * probability / solution.getNumberOfVariables();
		Solution ret = new Solution(solution);
		double rnd, delta1, delta2, mut_pow, deltaq;
		double y, yl, yu, val, xy;
		for (int var = 0; var < solution.getNumberOfVariables(); var++) {
			if (random.nextDoubleII() <= probability) {
				y = solution.getValue(var);
				yl = solution.getLowerlimit(var);
				yu = solution.getUpperlimit(var);
				delta1 = (y - yl) / (yu - yl);
				delta2 = (yu - y) / (yu - yl);
				rnd = random.nextDoubleII();
				mut_pow = 1.0 / (distributionIndex + 1.0);
				if (rnd <= 0.5) {
					xy = 1.0 - delta1;
					val = 2.0 * rnd + (1.0 - 2.0 * rnd) * (Math.pow(xy, (distributionIndex + 1.0)));
					deltaq = java.lang.Math.pow(val, mut_pow) - 1.0;
				} else {
					xy = 1.0 - delta2;
					val = 2.0 * (1.0 - rnd) + 2.0 * (rnd - 0.5) * (java.lang.Math.pow(xy, (distributionIndex + 1.0)));
					deltaq = 1.0 - (java.lang.Math.pow(val, mut_pow));
				}
				y = y + deltaq * (yu - yl);
				if (y < yl)
					y = yl;
				if (y > yu)
					y = yu;
				ret.setValue(var, y);
			}
		}
		return ret;
	}

}
