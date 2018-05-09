package Network.GridComputing;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

public abstract class StreamProvider {
	abstract public OutputStream getOutputStream(String path) throws IOException;

	public Writer getWriter(String path) throws IOException {
		return new OutputStreamWriter(getOutputStream(path));
	}

}
