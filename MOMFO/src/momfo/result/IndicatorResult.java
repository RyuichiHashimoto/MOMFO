package momfo.result;

import java.io.IOException;
import java.io.Serializable;

import javax.naming.NameNotFoundException;
import javax.naming.NamingException;

import Network.GridComputing.StreamProvider;
import lib.experiments.CommandSetting;
import lib.experiments.ParameterNames;
import lib.experiments.Exception.CommandSetting.CannotConvertException;
import lib.experiments.Exception.CommandSetting.notFoundException;
import lib.lang.Generics;
import momfo.operators.evaluator.Evaluator;
import momfo.util.JMException;

public class IndicatorResult extends GAResult {

	public static final String Default_pckg = "momfo.operators.evaluator";

	protected int recGenIdx_;
	protected int[] recordGens_;
	protected Evaluator[] finEvaluator;
	protected Evaluator[] evoEvaluator;

	private double[][] values;
	StreamProvider streamProvider;
	int nOfTrial;
	int offSet;

	@Override
	public void build(CommandSetting s) throws NamingException, ReflectiveOperationException, IOException,
			notFoundException, IllegalArgumentException, CannotConvertException, JMException {
		super.build(s);
		
		streamProvider = (StreamProvider) s.get(ParameterNames.STREAM_PROVIDER);

		nOfTrial = 0;
		if (s.containsKey(ParameterNames.SEED_OFFSET)) {
			offSet = s.getAsInt(ParameterNames.SEED_OFFSET);
		} else {
			offSet = 0;
		}

		String[] evo = s.getAsSArray(ParameterNames.EVO_EVALUATOR);
		Object[] tempSolEval = Generics.cast(s.getAsInstanceArrayByName(ParameterNames.FIN_EVALUATOR));

		finEvaluator = new Evaluator[tempSolEval.length];
		for(int i = 0; i < finEvaluator.length;i++){
			finEvaluator[i] = Generics.cast(tempSolEval[i]);
			finEvaluator[i].build(s);
		}
		s.putForce(ParameterNames.FIN_EVALUATOR, finEvaluator);
		
				
//		System.out.println( s.getAsInstance(ParameterNames.FIN_EVALUATOR));
		
		tempSolEval = Generics.cast(s.getAsInstanceArrayByName(ParameterNames.EVO_EVALUATOR));
		evoEvaluator = new Evaluator[tempSolEval.length];

		for(int i = 0; i < finEvaluator.length;i++){
			evoEvaluator[i] = Generics.cast(tempSolEval[i]);
			evoEvaluator[i].build((s));
		}
		s.putForce(ParameterNames.EVO_EVALUATOR,evoEvaluator);

	}

	public IndicatorResult() {

	}

	@Override
	public void afterTrial() throws IOException, NameNotFoundException, NamingException {
//		nOfTrial++;
//		Object[] d = finEvaluator[0].getValue();
//		ArrayList<Double> a = Generics.cast(d[0]);
//		System.out.println(a.get(a.size()-1));
	}

	@Override
	public void afterInitialization() throws IOException, NameNotFoundException, NamingException {
		
		
	}

	@Override
	public void save() throws IOException {
		writer.flush();
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
	protected String getOutputName(CommandSetting s) throws NamingException {
		return "Null";
	}

	@Override
	public void save(CommandSetting s, Object... results) throws IOException, NamingException {

	}

}
