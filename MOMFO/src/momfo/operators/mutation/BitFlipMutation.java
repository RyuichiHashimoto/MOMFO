//  BitFlipMutation.java
//
//  Author:
//       Antonio J. Nebro <antonio@lcc.uma.es>
//       Juan J. Durillo <durillo@lcc.uma.es>
//
//  Copyright (c) 2011 Antonio J. Nebro, Juan J. Durillo
//
//  This program is free software: you can redistribute it and/or modify
//  it under the terms of the GNU Lesser General Public License as published by
//  the Free Software Foundation, either version 3 of the License, or
//  (at your option) any later version.
//
//  This program is distributed in the hope that it will be useful,
//  but WITHOUT ANY WARRANTY; without even the implied warranty of
//  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//  GNU Lesser General Public License for more details.
//
//  You should have received a copy of the GNU Lesser General Public License
//  along with this program.  If not, see <http://www.gnu.org/licenses/>.

package momfo.operators.mutation;

import static lib.experiments.ParameterNames.*;

import javax.naming.NameNotFoundException;

import lib.experiments.CommandSetting;
import lib.experiments.NeedParameters;
import momfo.core.Solution;
import momfo.util.JMException;

/**
 * This class implements a bit flip mutation operator.
 * NOTE: the operator is applied to binary or integer solutions, considering the
 * whole solution as a single encodings.variable.
 */
public class BitFlipMutation extends Mutation {

	@Override
	@NeedParameters({MUTATIONProbability,RANDOM_GENERATOR})
	public void build(CommandSetting s) throws NameNotFoundException {
		this.build(s);

		double mp = (Double) s.get(MUTATIONProbability);
		if(mp < 0) throw new IllegalArgumentException(MUTATIONProbability + " must be non-negative but was "+ mp);

		mutationProbability = mp;
	}

	@Override
	public Solution mutation(Solution parent) throws JMException {
		return doMutation(mutationProbability,parent);
	}




	public Solution doMutation(double probability, Solution solution) throws JMException {
		Solution ret = new Solution(solution);

		int size = solution.getNumberOfVariables();

		probability = probability >= 0 ? probability : (double) Math.abs(probability) / size;
		for (int i = 0; i < size; i++) {
			if (random.nextDoubleIE() < probability) {
				if ((int) Math.round(solution.getValue(i)) == 1) {
					ret.setValue(i, 0.0);
				} else if ((int) Math.round(solution.getValue(i)) == 0) {
					ret.setValue(i, 1.0);
				} else {
					throw new JMException("something is wrong in doMutation() in BitFlipMutation");
				}
			}
		}
		return ret;
	} // doMutation
} // BitFlipMutation
