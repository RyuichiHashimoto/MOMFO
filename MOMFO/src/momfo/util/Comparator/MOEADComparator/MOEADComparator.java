package momfo.util.Comparator.MOEADComparator;

import javax.naming.NameNotFoundException;

import lib.experiments.CommandSetting;
import lib.experiments.JMException;
import lib.experiments.ParameterNames;
import lib.lang.NeedOverriden;
import lib.math.BuildInRandom;
import momfo.util.ReferencePoint;
import momfo.util.WeightedVector;
import momfo.util.Comparator.Comparator;
import momfo.util.ScalarzingFunction.ScalarzingFunction;

public abstract class MOEADComparator extends Comparator {

	public MOEADComparator(boolean ismax, BuildInRandom random, ScalarzingFunction d) throws JMException {
		super(ismax,random);
		ScalaringFunction_ = d;
	}
	public MOEADComparator(){
		super();
	}
	@NeedOverriden
	public void build(CommandSetting st) throws NameNotFoundException, ReflectiveOperationException {
		super.build(st);
		isMultitask = st.getAsBool(ParameterNames.IS_MULTITASK);

		if(isMultitask) {
			int taskNumber = st.get(ParameterNames.TASK_NUMBER);
//			System.out.println(st.get(ParameterNames.SCALAR_FUNCTION).getClass().getName());
			ScalarzingFunction[] d = st.get(ParameterNames.SCALAR_FUNCTION);			
			ScalaringFunction_ = d[taskNumber];
		} else {
			ScalaringFunction_ = st.get(ParameterNames.SCALAR_FUNCTION);
		}
	}
	protected boolean isMultitask;
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
