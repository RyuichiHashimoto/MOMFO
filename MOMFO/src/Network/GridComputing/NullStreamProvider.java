package Network.GridComputing;

import java.io.IOException; 
import java.io.OutputStream;

import lib.io.output.NullOutputStream;

public class NullStreamProvider extends StreamProvider {

	@Override
	public OutputStream getOutputStream(String path) throws IOException {
		return new NullOutputStream();
	}

}
