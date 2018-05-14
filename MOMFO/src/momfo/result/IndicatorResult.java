package momfo.result;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import javax.naming.NamingException;

import Network.GridComputing.RunSetting;
import Network.GridComputing.StreamProvider;
import lib.experiments.CommandSetting;
import lib.experiments.ParameterNames;
import lib.util.ArrayUtility;
import momfo.operators.evaluator.Evaluator;

public class IndicatorResult extends GAResult {
	protected int recGenIdx_;
	protected int[] recordGens_;
	protected Evaluator evaluator;
	private double[][] values;

	@Override
	public void build(CommandSetting s) throws NamingException, ReflectiveOperationException, IOException {

		evaluator = s.getAsInstance(ParameterNames.EVO_EVALUATOR);

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

		for (int g = 0; g < values[0].length; g++) {
			for (int i = 0; i < values.length; i++) {
				writer.write(Double.toString( values[i][g] ));
				if (i !=  values.length - 1) writer.write("\t");
			}
			writer.write("\n");
		}
		close();
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
		writer = ((StreamProvider) s.get(ParameterNames.STREAM_PROVIDER)).getWriter(getOutputName(s));
		writer = new BufferedWriter(writer);
		values = ArrayUtility.listToDArray( (List<Double[]>) evaluator.getValue()).clone();

		save();
	}

	@Override
	protected String getOutputName(CommandSetting s) throws NamingException {
		return s.getAsStr(RunSetting.NAME_SPACE, "") + "indicators.csv";
	}

}
