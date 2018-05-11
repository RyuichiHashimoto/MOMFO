package momfo.util.Comparator.MOEADComparator;

import java.util.HashMap;

import lib.math.BuildInRandom;
import momfo.util.JMException;
import momfo.util.ReferencePoint;
import momfo.util.WeightedVector;
import momfo.util.Comparator.Comparator;
import momfo.util.ScalarzingFunction.ScalarzingFunction;

public abstract class MOEADComparator extends Comparator {

	public MOEADComparator(boolean ismax, BuildInRandom random, ScalarzingFunction d) throws JMException {
		super(ismax,random);
		ScalaringFunction_ = d;
	}

	protected ScalarzingFunction ScalaringFunction_;

	protected ReferencePoint referencePoint;

	protected WeightedVector weightedVector;

	protected double[] Max;
	protected double[] Min;

	public void setMaxPoint(double[] d){Max = d;};
	public void setMinPoint(double[] d){Min = d;};

	public ScalarzingFunction getScalarzingFunction(){return ScalaringFunction_;};

	public void setRefernecePoint(double[] d){
		setRefernecePoint(new ReferencePoint(d));
	}

	public void setRefernecePoint(ReferencePoint d){
		referencePoint = d;
	}

	public void setWeightedVector(double[] d){
		setWeightedVector(new WeightedVector(d));
	}

	public void setWeightedVector(WeightedVector d){
		weightedVector = d;
	}





}
