package momfo.Indicator;

import java.io.FileNotFoundException;
import java.io.IOException;

import javax.naming.NameNotFoundException;

import lib.experiments.CommandSetting;
import lib.experiments.ParameterNames;
import lib.experiments.Exception.CommandSetting.notFoundException;



/*
 * this inplement is unconfirmed , So before use this inmpement , We must confirm this Implement
 *
 *
 */
public abstract class IGDCalclator  extends Indicator{

	public double POWER;

	public void build(CommandSetting st) throws NameNotFoundException, notFoundException, ReflectiveOperationException {
		super.build(st);
		try{
			POWER = st.get(ParameterNames.IGD_POWER);
		}catch(NameNotFoundException e) {
			POWER = 2.0;
		}
	}


	public final double calcIGD(double[][]  objective,double[][] referencePoint) {
		boolean[] checker = new boolean[objective.length];
		for(int i =0; i < checker.length;i++) checker[i] = true;
		return calcIGD(objective,checker,referencePoint);
	};

	public final double calcIGD(double[][]  objective,IGDRef ref) {
		boolean[] checker = new boolean[objective.length];
		for(int i =0; i < checker.length;i++) checker[i] = true;
		return calcIGD(objective,checker,ref.getRefs());
	};

	public final double calcNormalizeIGD(double[][]  objective,IGDRef ref) {
		boolean[] checker = new boolean[objective.length];
		for(int i =0; i < checker.length;i++) checker[i] = true;
		return calcNormalizeIGD(objective,checker,ref.getNormalizeRefs(),ref.getMaxValue(),ref.getMinValue());
	};

	public final double calcNormalizeIGD(double[][]  objective , double[][] referencePoint,double[] maxValue,double[] minValue){
		boolean[] checker = new boolean[objective.length];
		for(int i =0; i < checker.length;i++) checker[i] = true;
		return calcNormalizeIGD(objective, checker ,referencePoint,maxValue,minValue);

	}

	abstract public double calcIGD(double[][]  objective ,boolean[] check ,double[][] referencePoint);

	abstract public double calcNormalizeIGD(double[][]  objective, boolean[] checker , double[][] referencePoint,double[] maxValue,double[] minValue);


	public static void main(String[] args) throws FileNotFoundException, IOException{

	}


}
