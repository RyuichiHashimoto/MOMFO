package momfo.util.Indicator;

import java.util.HashMap;

import momfo.core.Population;
import momfo.util.JMException;

public class IGDPLUSS extends IGD{


	public double dist(double[] r,double[] s){
		double sum = 0;
		if (isMAXproblem_){
			for ( int i=0;i < r.length;i++){
				sum += Math.max(s[i] - r[i], 0);
			}
		} else {
			for ( int i=0;i < r.length;i++){
				sum += Math.max(r[i] -s[i], 0);
			}
		}
		return sum;
	}






	public Object execute(Population ind, HashMap<String, Object> d) throws JMException {
		assert d.containsKey("referencePoint") : "execute in IGDPluss: there are no referecenPoint in the HashMap";
		System.out.println("this inplement is unconfirmed , So before use this inmpement , We must confirm this Implement");


		isMAXproblem_ = (boolean ) d.get("IsMAX");

		double[][] referencePoint = (double[][])d.get("referencePoint");

		double[][] Objectives = ind.getAllObjectives();


		return IGD(Objectives,referencePoint);
	}

	public String getIndicatorName() {
		return "IGDPluss";
	}

}
