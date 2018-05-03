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
public class UniformCrossover extends Crossover {
	/**
	 * EPS defines the minimum difference allowed between real values
	 */
	private static final double EPS = 1.0e-14;

	private static final double ETA_C_DEFAULT_ = 20.0;
	private Double crossoverProbability_ = 0.9;
	private double distributionIndex_ = ETA_C_DEFAULT_;

	/**
	 * Valid solution types to apply this operator
	 */

	/**
	 * Constructor Create a new SBX crossover operator whit a default index
	 * given by <code>DEFAULT_INDEX_CROSSOVER</code>
	 */
	public UniformCrossover(HashMap<String, Object> parameters) {
		super(parameters);
		name = "UniformCrossover";
		if (parameters.get("Crossoverprobability") != null){
			crossoverProbability_ = (Double) parameters.get("Crossoverprobability");
				}
		}
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
	public Solution doCrossover(double probability, Solution parent1, Solution parent2) throws JMException {

		Solution offSpring;

		offSpring = new Solution(parent1);

		int i;
		double valueX1, valueX2;

		int numberOfVariables = parent1.getNumberOfVariables();
        //交差するかどうか
		if (Random.nextDoubleIE() <= probability) {
			for (i = 0; i < numberOfVariables; i++) {
				valueX1 =  (parent1.getValue(i));
				valueX2 =  (parent2.getValue(i));



				if (Random.nextDoubleIE()<= 0.5) {
					offSpring.setValue(i, valueX1);
				} else {
					offSpring.setValue(i, valueX2);
				} // if
			} // if
		} // if

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
			Configuration.logger_.severe("UniformCrossover.execute: operator needs two " + "parents");
			Class cls = java.lang.String.class;
			String name = cls.getName();
			throw new JMException("Exception in " + name + ".execute()");
		} // if

		Solution ret;

		ret = doCrossover(crossoverProbability_, parents[0], parents[1]);

		return ret;
	} // execute
} // SBXCrossover
