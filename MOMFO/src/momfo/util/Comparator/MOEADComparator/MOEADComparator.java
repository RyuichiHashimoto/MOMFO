package momfo.util.Comparator.MOEADComparator;

import java.util.HashMap;

import momfo.util.JMException;
import momfo.util.ReferencePoint;
import momfo.util.WeightedVector;
import momfo.util.Comparator.Comparator;
import momfo.util.ScalarzingFunction.ScalarzingFunction;

public abstract class MOEADComparator extends Comparator {

	public MOEADComparator(HashMap<String, Object> parameters) throws JMException {
		super(parameters);
		// TODO 自動生成されたコンストラクター・スタブ
	}

	public MOEADComparator(HashMap<String, Object> parameters,ScalarzingFunction d ) throws JMException {
		super(parameters);
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
