package lib.io.output;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class AccumulatingOutputStream extends OutputStream {
	private OutputStream out_;
	private ByteArrayOutputStream buf_ = new ByteArrayOutputStream();
	private boolean allowWrite_;


	public AccumulatingOutputStream(OutputStream out) {
		out_ = out;
		allowWrite_ = false;
	}


	@Override
	public void write(int b) throws IOException {
		if (allowWrite_) {
			out_.write(b);
		} else {
			buf_.write(b);
		}
	}

	@Override
	public void flush() throws IOException {
		if (allowWrite_) out_.flush();
	}

	public void allowWrite(boolean allow) throws IOException {
		allowWrite_ = allow;
		if (allow) {
			buf_.writeTo(out_);
			buf_.reset();
		}
	}

	/**
	 * Clear buffer. Accumulated data are discarded unless they are already written in the stream.
	 */
	public void reset() {
		buf_.reset();
	}

	@Override
	public void close() throws IOException {
//		buf_.close(); // ByteArrayOutputStream.close does noting
		out_.close();
	}

}
