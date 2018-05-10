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
public class DECrossover extends Crossover {

	double CR;
    double F;

    @Override
	@NeedParameters({RANDOM_GENERATOR,DE_CR,DE_F})
	public void build(CommandSetting st) throws NameNotFoundException {
		super.build(st);
		double cr = st.getAsDouble(DE_CR);
		double f = st.getAsDouble(DE_F);
		if (cr < 0) throw new IllegalArgumentException(DE_F + " must be non-negative but was "+ cr);
		if (f < 0) throw new IllegalArgumentException(DE_F +  " must be non-negative but was "+ f);
		CR = cr;
		F = f;
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

	    for(int var = 0; var < parent1.getNumberOfVariables();var++){

	    	if (random.nextDoubleII() < CR || var == specificVar){
	    		double d = parent1.getValue(var) + F * (parent2.getValue(var) - parent3.getValue(var));
	    		if(d > parent1.getUpperlimit(var)){
	    			d  = parent1.getUpperlimit(var);
	    		} else if (d < parent1.getLowerlimit(var)){
	    			d = parent1.getLowerlimit(var);
	    		}
	    		offSpring[0].setValue(var, d);
	    	} else if (random.nextDoubleII() >= CR && var != specificVar){
	    		offSpring[0].setValue(var, parent1.getValue(var));
	    	}
	    }
		return offSpring;
	} // doCrossover




}
