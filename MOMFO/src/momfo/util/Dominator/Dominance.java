package momfo.util.Dominator;

import momfo.core.Population;
import momfo.util.JMException;
import momfo.util.Comparator.Comparator;
import momfo.util.Comparator.DominationComparator;

public abstract class Dominance {

	double[] referencePoint_;

	protected Comparator comparator = new DominationComparator(null);

	public Dominance(double[] ref){
		referencePoint_ = ref;
	}

	public void setReferencePoint(double[] ref){
		referencePoint_ = ref;
	}
	public double[] getReferencePoint(){
		return referencePoint_;
	}


	public void setMAX(){
		comparator.setIsMAX();
	}

	public void setMIN(){
		comparator.setIsMIN();
	}

	public void set(boolean d){
		comparator.setIs(d);
	}

	public Comparator getComparator(){
		return comparator;
	}

	public abstract Population execute() throws JMException, ClassNotFoundException;






}
