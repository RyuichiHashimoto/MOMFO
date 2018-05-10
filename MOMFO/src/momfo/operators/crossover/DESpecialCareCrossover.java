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
public class DESpecialCareCrossover extends Crossover {
	double upperCR;
    double upperF;
	double lowerCR;
    double lowerF;


	@Override
	@NeedParameters({RANDOM_GENERATOR,DE_CR_UPPER,DE_F_UPPER,DE_CR_LOWER,DE_F_LOWER})
	public void build(CommandSetting st) throws NameNotFoundException {
		super.build(st);
		double upperCr = st.getAsDouble(DE_CR_UPPER);
		double upperf = st.getAsDouble(DE_CR_UPPER);
		double lowerCr = st.getAsDouble(DE_CR_LOWER);
		double lowerf = st.getAsDouble(DE_CR_LOWER);

		if (upperCr < 0) throw new IllegalArgumentException(DE_CR_UPPER + " must be non-negative but was "+ upperCr);
		if (lowerCr >1) throw new IllegalArgumentException(DE_CR_LOWER + " must be non-negative but was "+ lowerCr);
		if (upperf < 0) throw new IllegalArgumentException(DE_F_UPPER + " must be non-negative but was "+ upperf);
		if (lowerf >1) throw new IllegalArgumentException(DE_F_LOWER + " must be non-negative but was "+ lowerf);
		if (upperf < lowerf) throw new IllegalArgumentException(DE_F_UPPER + " must be bigger than " + DE_F_LOWER);
		if (upperCr < lowerCr) throw new IllegalArgumentException(DE_CR_UPPER + " must be bigger than " + DE_CR_LOWER);

		upperCR =  upperCr;
		lowerCR =  lowerCr;
		upperF =  upperf;
		lowerF =  lowerf;
	}

	@Override
	public void crossover(Solution offspring1, Solution offspring2, Solution[] parent) throws JMException {
		Solution[] off = doCrossover(parent[0],parent[1],parent[2]);
		offspring1 = off[0];
		offspring2 = null;
	}



	public Solution[] doCrossover(Solution parent1, Solution parent2,Solution parent3) throws JMException {
		Solution [] offSpring = new Solution[3];

	    offSpring[0] = new Solution(parent1);

	    int specificVar = random.nextIntIE(parent1.getNumberOfVariables());
	    double F = random.nextDoubleIE() *(upperF - lowerF) + lowerF;
		double CR = random.nextDoubleIE() *(upperCR - lowerCR) + lowerCR;
		int k = random.nextIntIE(parent1.getNumberOfVariables());

		for (int var = 0; var < parent1.getNumberOfVariables();var++){
			double val = Math.abs(parent1.getValue(var) + F * (parent2.getValue(var) - parent3.getValue(var)));

			if (val > parent1.getUpperlimit(var)) val = parent1.getUpperlimit(var);
			if (val < parent1.getLowerlimit(var)) val = parent1.getLowerlimit(var);

			if (k == val || random.nextDoubleIE() < CR){
				offSpring[0].setValue(var, val);
			}
			else{
				offSpring[0].setValue(var, parent1.getValue(var));
			}
		}
		return offSpring;
	}

}
