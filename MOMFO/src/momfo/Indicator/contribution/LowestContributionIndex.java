package momfo.Indicator.contribution;

import momfo.Indicator.Indicator;
import momfo.util.Front;
import momfo.util.ReferencePoint;

public class LowestContributionIndex extends Indicator{


	public int Calc(Front a, ReferencePoint ref , boolean isMAX ){
		return 0;
		//return IWFG(a,ref,isMAX);
	}




//	@Override
/*	public Object execute(Population ind, HashMap<String, Object> d) throws JMException {
		assert ind.size() > 0  : "execute(Population , HashMap) in LowestContibutionIndex  class :: the population size is 0";
		assert ind.get(0).getNumberOfObjectives()  > 0: "execute(Population , HashMap) in LowestContibutionIndex  class ::  the top Solution of Population hava no Objective Value";
		assert d.containsKey("ReferencePoint") : "execute(Population , HashMap) in (LowestContibutionIndex)  class ::  the HashMap have not the ReferencePoint key" ;
		assert d.containsKey("isMAXProblem") : "execute(Population , HashMap) in (LowestContibutionIndex)  class ::  the HashMap have not the isMAXProblem key" ;

		isMAXproblem_ = (boolean)d.get("isMAXProblem");

		double[] ref_a = (double [])d.get("ReferencePoint");
		Front  fr= new Front(ind.getAllObjectives());
		ReferencePoint ref = new ReferencePoint(ref_a);

		return Calc(fr, ref,isMAXproblem_);
	}
*/
	@Override
	public String getIndicatorName() {
		return "LowestContributionIndex";
	}

}
