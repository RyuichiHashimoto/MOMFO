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
import lib.io.FileConstants;
import lib.lang.Generics;
import momfo.operators.evaluator.Evaluator;
import momfo.operators.evaluator.NullEvaluator;
import momfo.util.JMException;

public class IndicatorResult extends GAResult {

	public static final String Default_pckg = "momfo.operators.evaluator";

	protected int recGenIdx_;
	protected int[] recordGens_;
	protected Evaluator[] finEvaluator;
	protected Evaluator[] evoEvaluator;
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

		if(s.containsKey(ParameterNames.FIN_EVALUATOR)) {
			Object[] tempSolEval = Generics.cast(s.getAsInstanceArrayByName(ParameterNames.FIN_EVALUATOR));
			finEvaluator = new Evaluator[tempSolEval.length];
			for(int i = 0; i < finEvaluator.length;i++){
				finEvaluator[i] = Generics.cast(tempSolEval[i]);
				finEvaluator[i].build(s);
			}
		} else {
			finEvaluator = new Evaluator[1];
			finEvaluator[0] = new  NullEvaluator();
			finEvaluator[0].build(s);
		}
		s.putForce(ParameterNames.FIN_EVALUATOR, finEvaluator);
		
		if(s.containsKey(ParameterNames.EVO_EVALUATOR)) {
			Object[] tempSolEval = Generics.cast(s.getAsInstanceArrayByName(ParameterNames.EVO_EVALUATOR));
			evoEvaluator = new Evaluator[tempSolEval.length];

			for(int i = 0; i < evoEvaluator.length;i++){
				evoEvaluator[i] = Generics.cast(tempSolEval[i]);
				evoEvaluator[i].build((s));
			}
		}else {
			evoEvaluator = new Evaluator[1];
			evoEvaluator[0] = new  NullEvaluator();
			evoEvaluator[0].build(s);
		}
		
		s.putForce(ParameterNames.EVO_EVALUATOR,evoEvaluator);			

	}

	@Override
	public void afterInitialization() {

		for(int i = 0; i < finEvaluator.length;i++){
			finEvaluator[i].initialize();;
		}
		for(int i = 0; i < evoEvaluator.length;i++){
			evoEvaluator[i].initialize();;
		}


		for(int i = 0; i < evoEvaluator.length;i++){
			evoEvaluator[i].evaluate(solver.getPopulation());
		}
	}

	@Override
	public void afterGeneration() {
		for(int i = 0; i < evoEvaluator.length;i++){
			evoEvaluator[i].evaluate(solver.getPopulation());
		}
	}

	@Override
	public void afterTrial() throws IOException, NameNotFoundException, NamingException {
		nOfTrial++;
		for(int i = 0;i < finEvaluator.length;i++) {
			finEvaluator[i].evaluate(solver.getPopulation());
		}
		for(int i = 0; i < evoEvaluator.length;i++){
			if(!evoEvaluator[i].getClassName().contains("Null")) {
				evoEvaluator[i].setOutputFileName(evoEvaluator[i].getClassName() + FileConstants.FILEPATH_DEMILITER+evoEvaluator[i].getClassName()+nOfTrial + ".dat");
				evoEvaluator[i].save(streamProvider);;
			} else {

				
			}
		}
		
		
	}


	@Override
	public void save() throws IOException {
		writer.flush();
	}

	@Override
	public void close() throws IOException {
		writer.close();
	}
	public double[][] value;
	
	@Override
	public Serializable getMemento() {
		return value;
	}

	@Override
	protected String getOutputName(CommandSetting s) throws NamingException {
		return "Null";
	}

	@Override
	public void save(CommandSetting s, Object... results) throws IOException, NamingException {

	}

}
