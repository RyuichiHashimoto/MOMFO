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

/**
 * This class allows to apply a SBX crossover operator using two parent
 * solutions.
 */
public class DESpecialCareCrossover extends Crossover {
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
	public DESpecialCareCrossover(HashMap<String, Object> parameters) {
		super(parameters);
		name = "DECrossover";
//		if (parameters.get("Crossoverprobability") != null)
//			crossoverProbability_ = (Double) parameters.get("Crossoverprobability");
//		if (parameters.get("CrossoverdistributionIndex") != null)
//			distributionIndex_ = (Double) parameters.get("CrossoverdistributionIndex");
	} // SBXCrossover


	public Solution[] doCrossover(double probability, Solution parent1, Solution parent2,Solution parent3) throws JMException {
		Solution [] offSpring = new Solution[3];

	    offSpring[0] = new Solution(parent1);

	    int specificVar = random.nextIntIE(parent1.getNumberOfVariables());
	    //double CR = 1.0;
	    //double F = 0.5;
	    double F = random.nextDoubleIE() *(1.0 - 0.2) + 0.2;
		double CR = random.nextDoubleIE() *(1.0 - 0.1) + 0.1;
		int k = random.nextIntIE(parent1.getNumberOfVariables());

		for (int var = 0; var < parent1.getNumberOfVariables();var++){
			double t = (MAXEVALS - evals + 1) / (double)MAXEVALS * (parent1.getUpperlimit(var) - parent1.getLowerlimit(var)) / 10.;	//fine tune the radius bound adaptively
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
	} // doCrossover

	double MAXEVALS =30000;
	double evals = 0;
	public void setMAXEVALS(double d){
		MAXEVALS = d;
	}
	public void setEVALS(double d){
		evals = d;
	}

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
