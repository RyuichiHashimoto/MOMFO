//  SBXCrossover1.java
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

/**
 * This class allows to apply a SBX crossover operator using two parent
 * solutions.
 */
public class SBXCrossover2 extends Crossover {
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
	public SBXCrossover2(HashMap<String, Object> parameters) {
		super(parameters);
		name = "SBXCrossover2";
		if (parameters.get("Crossoverprobability") != null)
			crossoverProbability_ = (Double) parameters.get("Crossoverprobability");
		if (parameters.get("CrossoverdistributionIndex") != null)
			distributionIndex_ = (Double) parameters.get("CrossoverdistributionIndex");
	} // SBXCrossover1

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
		double x1 = 0.0;
		double x2 = 0.0;
		double c1, c2;
		double rand;
		double X_MAX = 0;
		double X_MIN =0;
		double beta;
		double betaq;
		double alpha;
	  	int numberOfVariables = parent1.getNumberOfVariables() ;
	  	 if (random.nextDoubleIE() <= probability){
	  	      for (int i=0; i<numberOfVariables; i++){
	  	    	 if (random.nextDoubleIE() <= 0.5){
	  	    	  if (Math.abs(parent1.getValue(i) -parent2.getValue(i)) > 1.0E-14){
	  	    		  if (parent1.getValue(i) > parent2.getValue(i)){
	  	    			  x2 = parent1.getValue(i);
	  	    			  x1 = parent2.getValue(i);
	  	    		  } else if (parent1.getValue(i) < parent2.getValue(i)){
	  	    			  x2 = parent2.getValue(i);
	  	    			  x1 = parent1.getValue(i);
	  	    		  } else {
	  	    			  assert false : "do Crossover in the SBX .java";
	  	    		  }
				X_MAX = parent1.getUpperlimit(i);
				X_MIN = parent1.getLowerlimit(i);
				rand = random.nextDoubleIE();

				beta =1.0 + 2.0 * (x1 - X_MIN) / (x2 - x1);
				alpha = 2.0 - Math.pow(beta, -(distributionIndex_ +1));

				if (rand <= (1.0/alpha)){
					betaq = Math.pow((rand*alpha), 1.0/(distributionIndex_ +1));
				} else {
					betaq = Math.pow( 1.0 / (2 -  (rand*alpha)), 1.0/(distributionIndex_ +1));
				}
				c1 = 0.5 *((x1 + x2) - betaq * (x2 - x1));

				beta = 1.0 + (2.0*(X_MAX-x2)/(x2-x1));
                alpha = 2.0 - Math.pow(beta,-(distributionIndex_+1.0));
                 if (rand <= (1.0/alpha)){
 					betaq = Math.pow((rand*alpha), 1.0/(distributionIndex_ +1));
 				} else {
 					betaq = Math.pow( 1.0 / (2 -  (rand*alpha)), 1.0/(distributionIndex_ +1));
 				}
				c2 = 0.5 *((x1 + x2) + betaq * (x2 - x1));


				if (c1 < X_MIN) c1 = X_MIN;
				if (c1 > X_MAX) c1 = X_MAX;
				if (c2 < X_MIN) c2 = X_MIN;
				if (c2 > X_MAX) c2 = X_MAX;

				if (random.nextDoubleIE() <0.5){
					offSpring[0].setValue(i,c1);
					offSpring[1].setValue(i,c2);
				 } else {
						offSpring[0].setValue(i,c2);
						offSpring[1].setValue(i,c1);
				 }
	  	        } else {
	  	        	offSpring[0].setValue(i,parent1.getValue(i)) ;
	  	        	offSpring[1].setValue(i, parent2.getValue(i)) ;
	  	          }
	  	    	  }	  	      else {
	  	        	offSpring[0].setValue(i, parent1.getValue(i)) ;
	  	        	offSpring[1].setValue(i, parent2.getValue(i)) ;
	  	      }
	  	    }
	  	} else {
	  		for (int i=0;i<numberOfVariables;i++){
  	        	offSpring[0].setValue(i, parent1.getValue(i)) ;
  	        	offSpring[1].setValue(i, parent2.getValue(i)) ;
	  		}

	  		// if
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
			Configuration.logger_.severe("SBXCrossover1.execute: operator needs two " + "parents");
			Class cls = java.lang.String.class;
			String name = cls.getName();
			throw new JMException("Exception in " + name + ".execute()");
		} // if



		Solution[] offSpring;
		offSpring = doCrossover(crossoverProbability_, parents[0], parents[1]);

		return offSpring;
	} // execute
} // SBXCrossover1
