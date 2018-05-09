package lib.lang;


import java.io.File;
import java.net.URISyntaxException;

/**
 * Offers advanced file path treatment methods.
 * @author Hiroyuki MASUDA
 */
public class PathTreat {
	private PathTreat() {}

	/**
	 * Convert relative path to absolute path.
	 * @param path relative file path
	 * @return absolute file path
	 */
	public static String toAbs(String path) {
		return (new File(path)).getAbsolutePath();
	}

	/**
	 * Removes suffix
	 * @param path file path
	 * @return file path without suffix
	 */
	public static String removeSuffix(String path) {
		int dot = path.lastIndexOf(".");
		if (dot == -1) return path;
		return path.substring(0, dot);
	}

	public static String getSuffix(String path) {
		int dot = path.lastIndexOf(".");
		if (dot == -1) return "";
		return path.substring(dot + 1);
	}

	/**
	 * Returns current directory in File.
	 * @return current directory in File
	 */
	public static File getCurrentDir() {
		return new File(getCurrent());
	}

	/**
	 * Returns current directory in String.
	 * @return current directory in String
	 */
	public static String getCurrent() {
		return System.getProperty("user.dir");
	}

	/**
	 * Returns directory where the given class resides.
	 * Throws exception when the class is primitive, system class or class in jar file.
	 * @param c class to get directory
	 * @return directory where class <i>c</i> exists
	 */
	public static File classDir(Class<?> c) {
		try {
			return new File(c.getResource(c.getSimpleName() +".class").toURI()).getParentFile();
		} catch (URISyntaxException e) {
			throw new InternalError();
		}
	}

	/*
	public static File getClassPos() {
		return getCallerClassDir();
	}

	public static String getClassPosStr() {
		return getCallerClassDir().getAbsolutePath();
	}
	public static File relativeToFile(String path) {
		return new File(getCallerClassDir(), path);
	}

	public static String relativePath(String path) {
		return getCallerClassDir().getAbsolutePath() + "/"+ path;
	}
	*/
}
