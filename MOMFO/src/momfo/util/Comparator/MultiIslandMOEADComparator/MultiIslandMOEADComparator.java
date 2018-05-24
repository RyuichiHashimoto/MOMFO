package momfo.util.Comparator.MultiIslandMOEADComparator;

import java.util.HashMap;

import lib.experiments.JMException;
import lib.math.BuildInRandom;
import momfo.util.ReferencePoint;
import momfo.util.WeightedVector;
import momfo.util.Comparator.Comparator;
import momfo.util.ScalarzingFunction.ScalarzingFunction;

public abstract class MultiIslandMOEADComparator extends Comparator {
	
	public MultiIslandMOEADComparator(boolean ismax, BuildInRandom random, ScalarzingFunction[] d ) throws JMException {
		super(ismax,random);
		ScalaringFunction_ = d;		
	}


	protected ScalarzingFunction[] ScalaringFunction_;

	protected ScalarzingFunction NowScalaringFunction_;

	protected ReferencePoint referencePoint;

	protected WeightedVector weightedVector;


	public ScalarzingFunction getScalarzingFunction(int key){return ScalaringFunction_[key];};





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
