package momfo.operators.decoder;

import java.io.IOException;

import javax.naming.NameNotFoundException;
import javax.naming.NamingException;

import lib.experiments.CommandSetting;
import lib.experiments.Exception.CommandSetting.notFoundException;
import momfo.util.JMException;

/*
 * A decoder function returns the not-decoded object; 
 * 
 */

public class NullDecoder extends Decoder{

	@Override
	public double[] decoder(double[] val) {
		return val;
	}

	@Override
	public void build(CommandSetting s) throws NameNotFoundException, JMException, NamingException,
			ReflectiveOperationException, IOException, notFoundException {
		// TODO Auto-generated method stub
		
	}

}
