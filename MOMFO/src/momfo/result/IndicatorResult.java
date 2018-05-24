package momfo.result;

import java.io.IOException;
import java.io.Serializable;

import javax.naming.NameNotFoundException;
import javax.naming.NamingException;

import Network.GridComputing.StreamProvider;
import lib.experiments.CommandSetting;
import lib.experiments.JMException;
import lib.experiments.ParameterNames;
import lib.lang.Generics;
import momfo.operators.evaluator.Evaluator;

public class IndicatorResult extends GAResult {

	public static final String Default_pckg = "momfo.operators.evaluator";

	protected int recGenIdx_;
	protected int[] recordGens_;
	protected Evaluator[] evoEvaluator;
	StreamProvider streamProvider;
	int nOfTrial;
	int offSet;
	boolean isMultitask;

	@Override
	public void build(CommandSetting s) throws NamingException, ReflectiveOperationException, IOException,
			 IllegalArgumentException, JMException {
		super.build(s);
		streamProvider = (StreamProvider) s.get(ParameterNames.STREAM_PROVIDER);
		isMultitask = s.getAsBool(ParameterNames.IS_MULTITASK);
		nOfTrial = 0;
		if (s.containsKey(ParameterNames.SEED_OFFSET)) {
			offSet = s.getAsInt(ParameterNames.SEED_OFFSET);
		} else {
			offSet = 0;
		}

		if (s.containsKey(ParameterNames.EVO_EVALUATOR)) {
			Object[] tempSolEval = Generics.cast(s.getAsInstanceArrayByName(ParameterNames.EVO_EVALUATOR));
			evoEvaluator = new Evaluator[tempSolEval.length];

			for (int i = 0; i < evoEvaluator.length; i++) {
				evoEvaluator[i] = Generics.cast(tempSolEval[i]);
				evoEvaluator[i].build((s));
			}
		}

		s.putForce(ParameterNames.EVO_EVALUATOR, evoEvaluator);

	}

	@Override
	public void afterInitialization() {

		for (int i = 0; i < evoEvaluator.length; i++) {
			evoEvaluator[i].initialize();
		}

		if(isMultitask) {
			for (int i = 0; i < evoEvaluator.length; i++) {
				evoEvaluator[i].evaluate(solver.getPopulationSet());
			}
		} else {
			for (int i = 0; i < evoEvaluator.length; i++) {
				evoEvaluator[i].evaluate(solver.getPopulation());
			}
		}
	}

	@Override
	public void afterGeneration() {

		if(isMultitask) {
			for (int i = 0; i < evoEvaluator.length; i++) {
				evoEvaluator[i].evaluate(solver.getPopulationSet());
			}
		} else {
			for (int i = 0; i < evoEvaluator.length; i++) {
				evoEvaluator[i].evaluate(solver.getPopulation());
			}
		}
	}

	@Override
	public void afterTrial() throws IOException, NameNotFoundException, NamingException {
		nOfTrial++;
		for(int i = 0;i < evoEvaluator.length;i++) {
			evoEvaluator[i].addArchive();
		}
	}

	@Override
	public void save() throws IOException {
		for(int i = 0;i < evoEvaluator.length;i++) {
			evoEvaluator[i].save(streamProvider,"",offSet);
		}
	}

	@Override
	public void close() throws IOException {
		writer.close();
	}


	@Override
	public Serializable getMemento(){
		return evoEvaluator;
	}

	@Override
	protected String getOutputName(CommandSetting s) throws NamingException {
		return "Null";
	}

	@Override
	public void save(CommandSetting s, Object... results) throws IOException, NamingException{
		StreamProvider sp = s.get(ParameterNames.STREAM_PROVIDER);
		isMultitask = s.getAsBool(ParameterNames.IS_MULTITASK);

		Evaluator[] evaluator = new Evaluator[((Object[])(results[0])).length];
		for(int i = 0;i < evaluator.length;i++) {
			evaluator[i] = Generics.cast(((Object[])(results[0]))[i]);
			evaluator[i].save(sp,s.getAsStr(ParameterNames.NAME_SPACE),s.getAsInt(ParameterNames.SEED_OFFSET));
		}
	}

}
