//  SBXCrossover.java
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

package momfo.operators.crossover;

import static lib.experiments.ParameterNames.*;

import javax.naming.NameNotFoundException;

import lib.experiments.CommandSetting;
import lib.experiments.NeedParameters;
import momfo.core.Solution;
import momfo.util.JMException;

/**
 * This class allows to apply a SBX crossover operator using two parent
 * solutions.
 */
public class UniformCrossover extends Crossover {


	@Override
	@NeedParameters({RANDOM_GENERATOR,CROSSOVERProbability})
	public void build(CommandSetting s) throws NameNotFoundException {
		super.build(s);

		double cp = (Double) s.get(CROSSOVERProbability);
		if(cp < 0) throw new IllegalArgumentException(CROSSOVERProbability + " must be non-negative but was "+ cp);

		crossoverProbability = cp;
	}

	@Override
	public Solution[] crossover(Solution[] parent) throws JMException {
		return  doCrossover(crossoverProbability ,parent[0],parent[1]);

	}


	public Solution[] doCrossover(double probability, Solution parent1, Solution parent2) throws JMException {
		Solution[] ret = new Solution[2];

		Solution offSpring1;

		ret[0] = new Solution(parent1);
		ret[1] = new Solution(parent1);

		int i;
		double valueX1, valueX2;

		int numberOfVariables = parent1.getNumberOfVariables();
        //交差するかどうか
		if (random.nextDoubleIE() <= probability) {
			for (i = 0; i < numberOfVariables; i++) {
				valueX1 =  (parent1.getValue(i));
				valueX2 =  (parent2.getValue(i));

				if (random.nextDoubleIE()<= 0.5) {
					ret[0].setValue(i, valueX1);
					ret[1].setValue(i, valueX2);
				} else {
					ret[0].setValue(i, valueX2);
					ret[1].setValue(i, valueX1);
				}
			} // if
		} // if

		return ret;
	}

}
