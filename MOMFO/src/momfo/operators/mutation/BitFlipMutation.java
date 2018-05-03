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

import java.util.HashMap;

import momfo.core.Solution;
import momfo.util.JMException;
import momfo.util.Random;

/**
 * This class implements a bit flip mutation operator.
 * NOTE: the operator is applied to binary or integer solutions, considering the
 * whole solution as a single encodings.variable.
 */
public class BitFlipMutation extends Mutation {
  /**
   * Valid solution types to apply this operator
   */

  private Double mutationProbability_ = null ;

	/**
	 * Constructor
	 * Creates a new instance of the Bit Flip mutation operator
	 */
	public BitFlipMutation(HashMap<String, Object> parameters) {
		super(parameters) ;
		name = "BitFlipMutation";
  	if (parameters.get("Mutationprobability") != null)
  		mutationProbability_ = (Double) parameters.get("Mutationprobability") ;
	} // BitFlipMutation

	/**
	 * Perform the mutation operation
	 * @param probability Mutation probability
	 * @param solution The solution to mutate
	 * @throws JMException
	 */
	public void doMutation(double probability, Solution solution) throws JMException{
		int size = solution.getNumberOfVariables();

		probability = probability >= 0 ? probability : (double)Math.abs(probability)/size;

		for(int i = 0;i< size;i++){
			if(Random.nextDoubleIE() < probability){
				if((int)Math.round(solution.getValue(i)) == 1){
					solution.setValue(i, 0.0);
				} else if ((int)Math.round(solution.getValue(i))  == 0){
					solution.setValue(i, 1.0);
				} else {
					System.out.println("しょい込めない");
				}
			}
		}
	} // doMutation

	/**
	 * Executes the operation
	 * @param object An object containing a solution to mutate
	 * @return An object containing the mutated solution
	 * @throws JMException
	 */
	public Object execute(Object object) throws JMException {
		Solution solution = (Solution) object;

		doMutation(mutationProbability_, solution);
		return solution;
	} // execute
} // BitFlipMutation
