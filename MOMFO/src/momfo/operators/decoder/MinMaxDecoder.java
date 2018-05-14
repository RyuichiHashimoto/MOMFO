package momfo.operators.decoder;

public class MinMaxDecoder extends Decoder{

	@Override
	public double[] decoder(double[] val) {
		
		double[] ret = new double[val.length];
		
		double[] maxValue = problem.getUpperLimit();
		double[] minValue = problem.getLowerLimit();
		for(int t = 0;t <ret.length;t++) ret[t] = minValue[t] + (maxValue[t]-minValue[t])*val[t];
		return ret;
	}

}
