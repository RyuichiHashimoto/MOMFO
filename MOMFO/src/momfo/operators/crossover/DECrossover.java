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

import java.util.HashMap;

import momfo.core.Solution;
import momfo.util.Configuration;
import momfo.util.JMException;
import momfo.util.Random;

/**
 * This class allows to apply a SBX crossover operator using two parent
 * solutions.
 */
public class DECrossover extends Crossover {
	/**
	 * EPS defines the minimum difference allowed between real values
	 */
	private static final double EPS = 1.0e-14;

	private static final double ETA_C_DEFAULT_ = 20.0;
	private Double crossoverProbability_ = 0.0;
	private double distributionIndex_ = ETA_C_DEFAULT_;

	/**
	 * Valid solution types to apply this operator
	 */

	/**
	 * Constructor Create a new SBX crossover operator whit a default index
	 * given by <code>DEFAULT_INDEX_CROSSOVER</code>
	 */
	public DECrossover(HashMap<String, Object> parameters) {
		super(parameters);
		name = "DECrossover";
		if (parameters.get("Crossoverprobability") != null)
			crossoverProbability_ = (Double) parameters.get("Crossoverprobability");
		if (parameters.get("CrossoverdistributionIndex") != null)
			distributionIndex_ = (Double) parameters.get("CrossoverdistributionIndex");
	} // SBXCrossover


	public Solution[] doCrossover(double probability, Solution parent1, Solution parent2,Solution parent3) throws JMException {
		Solution [] offSpring = new Solution[3];

	    offSpring[0] = new Solution(parent1);

	    int specificVar = Random.nextIntIE(parent1.getNumberOfVariables());
	    double CR = 1.0;
	    double F = 0.5;

	    for(int var = 0; var < parent1.getNumberOfVariables();var++){

	    	if (Random.nextDoubleII() < CR || var == specificVar){
	    		double d = parent1.getValue(var) + F * (parent2.getValue(var) - parent3.getValue(var));
	    		if(d > parent1.getUpperlimit(var)){
	    			d  = parent1.getUpperlimit(var);
	    		} else if (d < parent1.getLowerlimit(var)){
	    			d = parent1.getLowerlimit(var);
	    		}
	    		offSpring[0].setValue(var, d);
	    	} else if (Random.nextDoubleII() >= CR && var != specificVar){
	    		offSpring[0].setValue(var, parent1.getValue(var));
	    	}
	    }
		return offSpring;
	} // doCrossover

	/**
	 * Executes the operation
	 *
	 * @param object
	 *            An object containing an array of two parents
	 * @return An object containing the offSprings
	 */
	public Object execute(Object object) throws JMException {
		Solution[] parents = (Solution[]) object;

		if (parents.length != 3) {
			Configuration.logger_.severe("DECrossover.execute: operator needs two " + "parents");
			Class cls = java.lang.String.class;
			String name = cls.getName();
			throw new JMException("Exception in " + name + ".execute()");
		} // if



		Solution[] offSpring;
		offSpring = doCrossover(crossoverProbability_, parents[0], parents[1], parents[2]);


		return offSpring[0];
	} // execute
} // SBXCrossover
