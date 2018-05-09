package Network.GridComputing;

import static lib.util.ArrayUtility.*; 

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.net.URLClassLoader;

import lib.lang.Generics;

public class MasterLoader {
	private URLClassLoader cloader;

	public MasterLoader(URL url) throws IOException {
		cloader = new URLClassLoader(veco(url));
	}

	public synchronized Class<?> getClass(String path) throws ClassNotFoundException {
		return cloader.loadClass(path);
	}

	public synchronized <T> T newInstance(String path) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		return Generics.cast(getClass(path).newInstance());
	}

	public synchronized void unload() throws IOException {
		WeakReference<URLClassLoader> ref = new WeakReference<URLClassLoader>(cloader);
		cloader = null;
		System.gc();
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {}
		if (ref.get() != null) throw new IOException("Unloading failed. ");
		throw new UnsupportedOperationException("Not implemented yet.");
	}

	public synchronized void changeClass() {
		throw new UnsupportedOperationException("Not implemented yet.");
	}
}
