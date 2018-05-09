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
		} else if (name.equalsIgnoreCase("SMSEMOAIGD")) {
			return new SMSEMOAIGDMain(parameters);
		} else if (name.equalsIgnoreCase("MOMFEA")) {
			return new MOMFEAMain(parameters);
		} else if (name.equalsIgnoreCase("MOMFEA_UP")) {
			return new MOMFEA_UpdateMain(parameters);
		} else if (name.equalsIgnoreCase("CMOMFEA")) {
			return new CMOMFEAMain(parameters);
		} else if (name.equalsIgnoreCase("MultitaskMOEAD")) {
			return new MultitaskMOEAD_Main(parameters);
		} else if (name.equalsIgnoreCase("MultitaskMOEAD_DE")) {
			return new MultitaskMOEAD_DE_main(parameters);
		} else if (name.equalsIgnoreCase("ManyMOMFEA")) {
			return new ManyMOMFEAMain(parameters);
		} else {
			System.err.print("AlgorithmMainFactory.getAlgorithmMain. " + " AlgorithmMain '" + name + "' not found ");
			throw new JMException("Exception in " + name + ".getCrossoverOperator()");
		} // else
	} // getCrossoverOperator

}
