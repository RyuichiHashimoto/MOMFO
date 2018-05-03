package momfo.main;

import experiments.Setting;
import momfo.core.AlgorithmMain;
import momfo.util.JMException;

public class AlgorithmMainFactory {
	public static AlgorithmMain getAlgorithmMain(String name, Setting parameters) throws JMException {
		if (name.equalsIgnoreCase("NSGAII")) {
			return new NSGAIIMain(parameters);
		} else if (name.equalsIgnoreCase("Island")) {
			return new IslandMain(parameters);
		} else if (name.equalsIgnoreCase("NSGAIICDP")) {
			return new NSGAIICDPMain(parameters);
		} else if (name.equalsIgnoreCase("SMSEMOA")) {
			return new SMSEMOAMain(parameters);
		} else if (name.equalsIgnoreCase("MOEAD")) {
			return new MOEADMain(parameters);
		} else if (name.equalsIgnoreCase("NSGAIII")) {
			return new NSGAIIIMain(parameters);
		} else if (name.equalsIgnoreCase("SMSEMOAIGD")) {
			return new SMSEMOAIGDMain(parameters);
		} else if (name.equalsIgnoreCase("ParallelSMSEMOAIGD")) {
			return new ParallelSMSEMOAIGDMain(parameters);
		} else if (name.equalsIgnoreCase("ParallelSMSEMOA")) {
			return new ParallelSMSEMOAMain(parameters);
		} else if (name.equalsIgnoreCase("NSGAIII_YY")) {
			return new NSGAIII_YY(parameters);
		} else if (name.equalsIgnoreCase("MOMFEA")) {
			return new MOMFEAMain(parameters);
		} else if (name.equalsIgnoreCase("MOMFEA_UP")) {
			return new MOMFEA_UpdateMain(parameters);
		} else if (name.equalsIgnoreCase("CMOMFEA")) {
			return new CMOMFEAMain(parameters);
		} else if (name.equalsIgnoreCase("MultitaskMOEAD")) {
			return new MultitaskMOEAD_Main(parameters);
		} else if (name.equalsIgnoreCase("MultitaskMOEAD_with_random")) {
			return new MultitaskMOEAD_with_random_Main(parameters);
		} else if (name.equalsIgnoreCase("MultitaskMOEAD_with_best")) {
			return new MultitaskMOEAD_with_best_Main(parameters);
		} else if (name.equalsIgnoreCase("MultitaskMOEAD_with_best_binary")) {
			return new MultitaskMOEAD_with_best_binary_Main(parameters);
		} else if (name.equalsIgnoreCase("MultitaskMOEAD_DE")) {
			return new MultitaskMOEAD_DE_main(parameters);
		} else if (name.equalsIgnoreCase("NormalizeMOEAD")) {
			return new NormalizeMOEADMain(parameters);
		} else if (name.equalsIgnoreCase("ManyMOMFEA")) {
			return new ManyMOMFEAMain(parameters);
		} else if (name.equalsIgnoreCase("ParallelMultitaskMOEAD")) {
			return new ParallelMultitaskMOEAD_Main(parameters);
		} /*
			 * else if (name.equalsIgnoreCase("SBXCrossover2")){ return new
			 * SBXCrossover2(parameters); } else if
			 * (name.equalsIgnoreCase("UniformCrossover")){ return new
			 * UniformCrossover(parameters); } else if
			 * (name.equalsIgnoreCase("SinglePointCrossover")) return new
			 * SinglePointCrossover(parameters); else if
			 * (name.equalsIgnoreCase("PMXCrossover")) return new
			 * PMXCrossover(parameters); else if
			 * (name.equalsIgnoreCase("TwoPointsCrossover")) return new
			 * TwoPointsCrossover(parameters); else if
			 * (name.equalsIgnoreCase("HUXCrossover")) return new
			 * HUXCrossover(parameters); else if
			 * (name.equalsIgnoreCase("DifferentialEvolutionCrossover")) return
			 * new DifferentialEvolutionCrossover(parameters); else if
			 * (name.equalsIgnoreCase("BLXAlphaCrossover")) return new
			 * BLXAlphaCrossover(parameters); }
			 */ else {
			System.err.print("AlgorithmMainFactory.getAlgorithmMain. " + " AlgorithmMain '" + name + "' not found ");
			throw new JMException("Exception in " + name + ".getCrossoverOperator()");
		} // else
	} // getCrossoverOperator

}
