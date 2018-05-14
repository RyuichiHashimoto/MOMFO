package momfo.operators.decoder;

/*
 * A decoder function returns the not-decoded object; 
 * 
 */

public class NullDecoder extends Decoder{

	@Override
	public double[] decoder(double[] val) {
		return val;
	}

}
