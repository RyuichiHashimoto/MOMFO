package lib.misc;

public class SerialVersion {
	private SerialVersion() {}

	/**
	 * A simple SerialVersionUID assigning convention.
	 * @param className The name of the class or a string to distinguish the class from others.
	 * @param version the version number of the class.
	 * @return The SerialVersionUID for the class of the given version.
	 */
	public static final long UID(String className, long version) {
		long retval = 0;
		for (int i = 0; i < className.length(); i++) {
			retval = retval * 31 + className.charAt(i);
		}
		return (retval << 13) + version;
	}
}
