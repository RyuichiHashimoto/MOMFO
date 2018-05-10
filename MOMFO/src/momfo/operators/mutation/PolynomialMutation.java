package momfo.operators.mutation;

import java.util.HashMap;

import momfo.core.Solution;
import momfo.util.JMException;


public class PolynomialMutation extends Mutation {
	private static final double ETA_M_DEFAULT_ = 20.0;
	private final double eta_m_ = ETA_M_DEFAULT_;

	private Double mutationProbability_ = null;
	private Double distributionIndex_ = eta_m_;

	/**
	 * Valid solution types to apply this operator
	 */

	/**
	 * Constructor Creates a new instance of the polynomial mutation operator
	 */
	public PolynomialMutation(HashMap<String, Object> parameters) {
		super(parameters);
		name = "PolynomialMutation";

		if (parameters.get("Mutationprobability") != null)
			mutationProbability_ = (Double) parameters.get("Mutationprobability");
		if (parameters.get("MutationdistributionIndex") != null)
			distributionIndex_ = (Double) parameters.get("MutationdistributionIndex");
	} // PolynomialMutation

	/**
	 * Perform the mutation operation
	 * 
	 * @param probability
	 *            Mutation probability
	 * @param solution
	 *            The solution to mutate
	 * @throws JMException
	 *
	 */

	/*
    rnvec_temp=p.rnvec;
    for i=1:dim
        if rand(1)<1/dim
            u=rand(1);
            if u <= 0.5
                del=(2*u)^(1/(1+mum)) - 1;
                rnvec_temp(i)=p.rnvec(i) + del*(p.rnvec(i));

            else
                del= 1 - (2*(1-u))^(1/(1+mum));
                rnvec_temp(i)=p.rnvec(i) + del*(1-p.rnvec(i));
            end
        end
    end
    object.rnvec = rnvec_temp;
            */
	public void doMutation(double probability, Solution solution) throws JMException {


		probability = probability >= 0 ? probability : -1 * probability / solution.getNumberOfVariables();
		double rnd, delta1, delta2, mut_pow, deltaq;
		double y, yl, yu, val, xy;
			for (int var=0; var < solution.getNumberOfVariables(); var++) {
				if (random.nextDoubleII() <= probability)
				{
					y      = solution.getValue(var);
					yl     = solution.getLowerlimit(var);
					yu     = solution.getUpperlimit(var);
					delta1 = (y-yl)/(yu-yl);
					delta2 = (yu-y)/(yu-yl);
					rnd = random.nextDoubleII();
					mut_pow = 1.0/(distributionIndex_+1.0);
					if (rnd <= 0.5)	{
							xy     = 1.0-delta1;
							val    = 2.0*rnd+(1.0-2.0*rnd)*(Math.pow(xy,(distributionIndex_+1.0)));
							deltaq =  java.lang.Math.pow(val,mut_pow) - 1.0;
						}	else	{
							xy = 1.0-delta2;
							val = 2.0*(1.0-rnd)+2.0*(rnd-0.5)*(java.lang.Math.pow(xy,(distributionIndex_+1.0)));
							deltaq = 1.0 - (java.lang.Math.pow(val,mut_pow));
						}
				y = y + deltaq*(yu-yl);
				if (y<yl)
					y = yl;
				if (y>yu)
					y = yu;
				solution.setValue(var, y);
			}
		} // for

	} // doMutation

	/**
	 * Executes the operation
	 *
	 * @param object
	 *            An object containing a solution
	 * @return An object containing the mutated solution
	 * @throws JMException
	 */
	public Object execute(Object object) throws JMException {
		Solution solution = (Solution) object;
		doMutation(mutationProbability_, solution);


		return solution;
	} // execute

} // PolynomialMutation
