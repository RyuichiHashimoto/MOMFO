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
public class SBXCrossoverWithoutSwap2 extends Crossover {
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
	public SBXCrossoverWithoutSwap2(HashMap<String, Object> parameters) {
		super(parameters);
		name = "SBXCrossover";
		if (parameters.get("Crossoverprobability") != null)
			crossoverProbability_ = (Double) parameters.get("Crossoverprobability");
		if (parameters.get("CrossoverdistributionIndex") != null)
			distributionIndex_ = (Double) parameters.get("CrossoverdistributionIndex");
	} // SBXCrossover

	/**
	 * Perform the crossover operation.
	 *
	 * @param probability
	 *            Crossover probability
	 * @param parent1
	 *            The first parent
	 * @param parent2
	 *            The second parent
	 * @return An array containing the two offsprings
	 */
	public Solution[] doCrossover(double probability, Solution parent1, Solution parent2) throws JMException {
		Solution [] offSpring = new Solution[2];

	    offSpring[0] = new Solution(parent1);
	    offSpring[1] = new Solution(parent2);
	    int numberOfVariables = offSpring[0].getNumberOfVariables();
	    
	    if(Random.nextDoubleIE() < probability){
		    for(int i = 0; i < numberOfVariables;i++){
		     	double u = Random.nextDoubleIE();
		    	double cf = 0;

		    	if (u<=0.5){
		    		cf = Math.pow(2.0*u, 1.0/(distributionIndex_ + 1.0));
		    	} else if (u>0.5){
		    		cf = Math.pow(2.0*(1.0-u), -1.0/(distributionIndex_ + 1.0));
		    	} else {
		    		cf = -100000;
		    		System.out.println("ErrorMassage");
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

		if (parents.length != 2) {
			Configuration.logger_.severe("SBXCrossover.execute: operator needs two " + "parents");
			Class cls = java.lang.String.class;
			String name = cls.getName();
			throw new JMException("Exception in " + name + ".execute()");
		} // if



		Solution[] offSpring;
		offSpring = doCrossover(crossoverProbability_, parents[0], parents[1]);


		return offSpring;
	} // execute
} // SBXCrossover
