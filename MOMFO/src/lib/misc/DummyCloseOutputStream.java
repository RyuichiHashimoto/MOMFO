package lib.misc;

import java.io.IOException;
import java.io.OutputStream;

public class DummyCloseOutputStream extends OutputStream {
	public OutputStream out_;

	public DummyCloseOutputStream() {
		out_ = System.out;
	}

	public DummyCloseOutputStream(OutputStream out) {
		out_ = out;
	}

	@Override
	public void write(int b) throws IOException {
		out_.write(b);
	}

}
