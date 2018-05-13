package momfo.result;

import static mo.solver.ga.GAFramework.*;
import static optsys.platform.RunSetting.*;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Serializable;

import javax.naming.NamingException;
import javax.sound.midi.Sequence;

import Network.Solver;
import Network.GridComputing.RunSetting;
import Network.GridComputing.StreamProvider;
import experiments.Setting;
import lib.experiments.CommandSetting;
import lib.math.VectorOperation;
import lib.util.ArrayUtility;
import momfo.Indicator.Indicator;

public class IndicatorResult extends GAResult {
	protected int recGenIdx_;
	protected int[] recordGens_;
	protected int nTrials;
	protected Indicator[] indicators_;
	protected double[][] values_;
	protected double[] norm;

	@Override
	public void build(Setting s) throws NamingException, ReflectiveOperationException, IOException {
		super.build(s);
		recGenIdx_ = 0;
		if (s.containsKey("recordGenerations")) {
			Sequence sequencer = Sequence.getSequence(s.getAsStr("recordGenerations"));			
			recordGens_ = toInt(sequencer.until(s.getAsInt("nGenerations")));
		} else {
			recordGens_ = ArrayUtility.sequence(1, s.getAsInt("nGenerations"));
		}
		nTrials = s.getAsInt(NTRIALS);

		// instantiate indicators
		String[] inds = s.getAsSArray("indicators");
		indicators_ = new Indicator[inds.length];
		for (int i = 0; i < indicators_.length; i++) {
			indicators_[i] = (Indicator) Class.forName(inds[i]).newInstance(); // TODO: pass setting
			Solver.buildObject(indicators_[i], s);
		}
		// fill values with 0
		values_ = new double[indicators_.length][recordGens_.length];

		if (s.getAsBool("normalize", false)) norm = new double[indicators_.length];
	}

	@Override
	public void afterInitialization() {
		recGenIdx_ = 0;
		afterGeneration();
	}

	@Override
	public void afterGeneration() {
		if (solver.getGeneration() != recordGens_[recGenIdx_]) return;

		for (int i = 0; i < indicators_.length; i++) {
			double value = indicators_[i].value(solver.getPopulation());
			if (norm != null && recGenIdx_ == 0) norm[i] = value;
			if (norm != null) {
				values_[i][recGenIdx_] += value / norm[i];
			} else {
				values_[i][recGenIdx_] += value;
			}
		}
		recGenIdx_++;
	}

	@Override
	public void save() throws IOException {
		for (int g = 0; g < values_[0].length; g++) {
			for (int i = 0; i < values_.length; i++) {
				writer.write(Double.toString(values_[i][g] / nTrials));
				if (i != values_.length - 1) writer.write("\t");
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
		return values_;
	}

	@Override
	public void save(Setting s, Object... results) throws IOException, NamingException {
		writer = ((StreamProvider) s.get(STREAM_PROVIDER)).getWriter(getOutputName(s));
		writer = new BufferedWriter(writer);
		nTrials = s.getAsInt(NTRIALS);
		values_ = (double[][]) results[0];
		for (int i = 1; i < results.length; i++) {
			double[][] data = (double[][]) results[i];
			for (int j = 0; j < data.length; j++) {
				VectorOperation.vAddTo(values_[j], data[j]);
			}
		}
		save();
	}

	@Override
	protected String getOutputName(Setting s) throws NamingException {
		return s.getAsStr(RunSetting.NAME_SPACE, "") + "indicators.csv";
	}

	@Override
	protected String getOutputName(CommandSetting s) throws NamingException {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public void save(CommandSetting s, Object... results) throws IOException, NamingException {
		// TODO 自動生成されたメソッド・スタブ
		
	}
}
