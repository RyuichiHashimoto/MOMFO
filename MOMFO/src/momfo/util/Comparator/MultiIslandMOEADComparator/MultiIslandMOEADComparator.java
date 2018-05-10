package momfo.util.Comparator.MultiIslandMOEADComparator;

import java.util.HashMap;

import momfo.util.JMException;
import momfo.util.ReferencePoint;
import momfo.util.WeightedVector;
import momfo.util.Comparator.Comparator;
import momfo.util.ScalarzingFunction.ScalarzingFunction;

public abstract class MultiIslandMOEADComparator extends Comparator {



	public MultiIslandMOEADComparator(HashMap<String, Object> parameters) throws JMException {
		super(parameters);
		// TODO 自動生成されたコンストラクター・スタブ
	}

	public MultiIslandMOEADComparator(HashMap<String, Object> parameters,ScalarzingFunction[] d ) throws JMException {
		super(parameters);
		ScalaringFunction_ = d;
		NowScalaringFunction_ = null;
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
