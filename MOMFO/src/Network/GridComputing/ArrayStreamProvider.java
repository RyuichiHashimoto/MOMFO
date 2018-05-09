package Network.GridComputing;

import java.io.ByteArrayOutputStream;
import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.util.HashMap;

public class ArrayStreamProvider extends StreamProvider{
	public HashMap<String, ByteArrayOutputStream> arrayOutputStreams = new HashMap<>();
	public HashMap<String, CharArrayWriter> arrayWriters = new HashMap<>();

	@Override
	public OutputStream getOutputStream(String path) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		arrayOutputStreams.put(path, baos);
		return baos;
	}

	@Override
	public Writer getWriter(String path) throws IOException {
		CharArrayWriter caw = new CharArrayWriter();
		arrayWriters.put(path, caw);
		return caw;
	}

	public void clear() {
		arrayOutputStreams.clear();
		arrayWriters.clear();
	}
}
