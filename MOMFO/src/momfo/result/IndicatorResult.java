package momfo.result;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import javax.naming.NamingException;

import Network.RunSetting;
import Network.Solver;
import Network.GridComputing.StreamProvider;
import lib.experiments.CommandSetting;
import lib.experiments.ParameterNames;
import lib.experiments.Exception.CommandSetting.CannotConvertException;
import lib.experiments.Exception.CommandSetting.notFoundException;
import lib.util.ArrayUtility;
import momfo.operators.evaluator.Evaluator;
import momfo.util.JMException;

public class IndicatorResult extends GAResult {
	protected int recGenIdx_;
	protected int[] recordGens_;
	protected Evaluator[] evaluator;
	private double[][] values;

	@Override
	public void build(CommandSetting s) throws NamingException, ReflectiveOperationException, IOException, notFoundException, IllegalArgumentException, CannotConvertException, JMException {
		super.build(s);
		
		String[] evoevalus = s.getAsSArray(ParameterNames.EVO_EVALUATOR);
		String[] finevalus = s.getAsSArray(ParameterNames.FIN_EVALUATOR);
		
		String[] evol = new String[evoevalus.length + finevalus.length]; 
		
		for(int i = 0 ; i < evoevalus.length;i++) {
			evol[i] = evoevalus[i];
		}
		
		for(int i = 0 ; i < finevalus.length;i++) {
			evol[i+evoevalus.length] = finevalus[i];
		}
		

		evaluator =  new Evaluator[evol.length];
		
		for (int i = 0; i < evoevalus.length; i++) {
			evaluator[i] = (Evaluator) Class.forName(evoevalus[i]).newInstance(); // TODO: pass setting
			Solver.buildObject(evaluator[i], s);
		}
		for (int i = 0; i < finevalus.length; i++) {
			evaluator[evoevalus.length +i] = (Evaluator) Class.forName(finevalus[i]).newInstance(); // TODO: pass setting
			Solver.buildObject(evaluator[evoevalus.length+i], s);
		}		
//		 fill values with 0
//		values_[] = new double[indicators_.length][recordGens_.length];

//		if (s.getAsBool("normalize", false)) norm = new double[indicators_.length];
		
		
	}

	public IndicatorResult() {
		
	}
	
	@Override
	public void afterInitialization() {
		recGenIdx_ = 0;
		afterGeneration();
	}

	@Override
	public void afterGeneration() {

	}

	@Override
	public void save() throws IOException {
		

	}


	@Override
	public void close() throws IOException {
		writer.close();
	}

	@Override
	public Serializable getMemento() {
		return values;
	}

	@Override
	public void save(CommandSetting s, Object... results) throws IOException, NamingException {
		assert false;
		for(int i = 0;i < evaluator.length;i++) {
			writer = ((StreamProvider) s.get(ParameterNames.STREAM_PROVIDER)).getWriter(getOutputName(s));
			writer = new BufferedWriter(writer);
			values = ArrayUtility.listToDArray( (List<Double[]>) evaluator.getValue()).clone();
		}
		save();
	}

	@Override
	protected String getOutputName(CommandSetting s) throws NamingException {
		return s.getAsStr(RunSetting.NAME_SPACE, "") + "indicators.csv";
	}

}
