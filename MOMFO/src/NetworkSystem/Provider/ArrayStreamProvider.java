package NetworkSystem.Provider;

import java.io.ByteArrayOutputStream;
import java.io.CharArrayWriter;
import java.util.HashMap;

public class ArrayStreamProvider {
	public HashMap<String, ByteArrayOutputStream> arrayOutputStreams = new HashMap<>();
	public HashMap<String, CharArrayWriter> arrayWriters = new HashMap<>();
/*
	public OutputStream getOutputStream(String path) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		arrayOutputStreams.put(path, baos);
		return baos;
	}
	public Writer getWriter(String path) throws IOException {
		CharArrayWriter caw = new CharArrayWriter();
		arrayWriters.put(path, caw);
		return caw;
	}
*/
	public void clear() {
		arrayOutputStreams.clear();
		arrayWriters.clear();
	}
}
