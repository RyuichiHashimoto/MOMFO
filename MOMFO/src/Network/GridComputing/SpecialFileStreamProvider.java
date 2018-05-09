package Network.GridComputing;

import java.io.IOException;
import java.io.OutputStream;

import lib.io.output.NullOutputStream;
import lib.misc.DummyCloseOutputStream;


public class SpecialFileStreamProvider extends StreamProvider {
	public static final String STDOUT = "/dev/stdout";
	public static final String NULL_DEVICE = "/dev/null";

	protected StreamProvider delegation;

	public SpecialFileStreamProvider(StreamProvider defaultProvider) {
		delegation = defaultProvider;
	}

	@Override
	public OutputStream getOutputStream(String path) throws IOException {
		switch(path) {
		case STDOUT:
			return new DummyCloseOutputStream();
		case NULL_DEVICE:
			return new NullOutputStream();
		default:
			return delegation.getOutputStream(path);
		}
	}
}
