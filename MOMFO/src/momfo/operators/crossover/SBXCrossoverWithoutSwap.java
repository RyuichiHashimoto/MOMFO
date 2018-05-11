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
public class SBXCrossoverWithoutSwap extends Crossover {

	private double distributionIndex;


	@Override
	@NeedParameters({RANDOM_GENERATOR,SBXDisIndex,CROSSOVERProbability})
	public void build(CommandSetting s) throws NameNotFoundException {
		super.build(s);
		double disindex = (Double) s.get(SBXDisIndex);;
		double cp = (Double) s.get(CROSSOVERProbability);
		if (disindex < 0) throw new IllegalArgumentException("distIdxSBX must be non-negative but was "+ disindex);
		if (cp < 0) throw new IllegalArgumentException("crosssover probability must be non-negative but was "+ cp);
		crossoverProbability = cp;
		distributionIndex = disindex;

	}

	@Override
	public Solution[] crossover(Solution[] parent) throws JMException {
		return doCrossover(crossoverProbability ,parent[0],parent[1]);
	}



	public Solution[] doCrossover(double probability, Solution parent1, Solution parent2) throws JMException {
		Solution [] offSpring = new Solution[2];

	    offSpring[0] = new Solution(parent1);
	    offSpring[1] = new Solution(parent2);
	    int numberOfVariables = offSpring[0].getNumberOfVariables();
	    for(int i = 0; i < numberOfVariables;i++){

	     	double u = random.nextDoubleIE();
	    	double cf = 0;

	    	if (u<=0.5){
	    		cf = Math.pow(2.0*u, 1.0/(distributionIndex + 1.0));
	    	} else if (u>0.5){
	    		cf = Math.pow(2.0*(1.0-u), -1.0/(distributionIndex + 1.0));
	    	} else {
	    		throw new JMException("something is wrong");
	    	}

	        double val_1 =0.5*((1+cf)*parent1.getValue(i) + (1-cf)*parent2.getValue(i));
	        if (val_1 >1.0)
	        	val_1 = 1.0;
	        else if (val_1 < 0.0)
	        	val_1 = 0.0;
	        offSpring[0].setValue(i, val_1);

	        double val_2 =0.5*((1+cf)*parent2.getValue(i) + (1-cf)*parent1.getValue(i));
	        if (val_2 >1.0)
	        	val_2 = 1.0;
	        else if (val_2 < 0.0)
	        	val_2 = 0.0;
	        offSpring[1].setValue(i, val_2);
	    }
	    return offSpring;
	} // doCrossover


} // SBXCrossover
