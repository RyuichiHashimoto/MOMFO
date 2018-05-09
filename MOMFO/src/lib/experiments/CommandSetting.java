package lib.experiments;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Set;

import javax.naming.NameNotFoundException;
import javax.naming.NamingException;

import lib.directory.DirectoryMaker;
import lib.experiments.Exception.CommandSetting.notFoundException;
import lib.io.FileConstants;
import lib.lang.Generics;

public class CommandSetting extends AbstractMap<String,Object> implements Serializable {

	protected HashMap<String, Object> parameter;

	public CommandSetting(CommandSetting d) {
		parameter = new HashMap<String, Object>(d.getHashMap());
		// System.out.println("eoupu");
	}

	public CommandSetting() {
		parameter = new HashMap<String, Object>();
	}

	public CommandSetting(HashMap<String, Object> parameter_) {
		parameter = new HashMap<String, Object>(parameter_);
	}

	public CommandSetting(String[] array) {
		System.err.println("output.sin");
		// TODO Auto-generated constructor stub
	}

	public String[] getAsSArray(String key, String delimiter) throws NameNotFoundException, notFoundException {
		Object o = get(key);
		if (o instanceof String) {
			return ((String) o).split(delimiter);
		} else if (o instanceof String[]) {
			return (String[]) o;
		} else {
			throw new ClassCastException();
		}
	}

	public String[] getAsSArray(String key, String delimiter, String[] def) throws notFoundException {
		if (containKey(key)) {
			try {
				return getAsSArray(key, delimiter);
			} catch (NameNotFoundException e) {
				throw new IllegalStateException(e);
			}
		} else {
			return def;
		}
	}
	public HashMap<String, Object> getHashMap() {
		return parameter;
	}

	public void clear() {
		parameter.clear();
	}

	public Set<String> getKeySet() {
		return parameter.keySet();
	}

	public Object put(String key, Object value) {
		if (!parameter.containsKey(key))
			parameter.put(key, value);
		
		return this;
	}

	// put a element.
	// To avoid the unexpected parameter setting, warn against overwriting parameter.
	private void warningPut(String key, Object value, boolean suppressWarning) {
		int size = parameter.size();
		parameter.put(key, value);
		if (!suppressWarning && size == parameter.size()) {
			System.err.println("\u001b[1;33;mWarning\u001b[m: " + key + " is already set. Overwritten by " + value);
		}
	}
	
	/**
	 * If the key is not found, throws an exception instead of returning null.
	 * @param key
	 * @return
	 * @throws NameNotFoundException The key is not found.
	 */
	public <T> T get(String key) throws NameNotFoundException{
		Object retval = parameter.get(key);
		if (retval == null) throw new NameNotFoundException(key + " is not specified.");
		return Generics.cast(retval);
	}
	public boolean getAsBool(String key) throws NameNotFoundException, notFoundException {
		return Boolean.parseBoolean(getAsStr(key));
	}
	
	public boolean getAsBool(String key, boolean def) {
		if (containsKey(key)) {
			Object val;
			try {
				val = get(key);
			} catch (NameNotFoundException e) {
				throw new IllegalStateException();
			}
			if (val instanceof Boolean) {
				return ((Boolean) val).booleanValue();
			} else {
				return Boolean.parseBoolean((String) val);
			}
		} else {
			return def;
		}
	}
	

	/**
	 * Returns the value associated by the specified key as int.
	 * @param key
	 * @throws NameNotFoundException
	 */
	public int getAsInt(String key) throws NameNotFoundException {
		Object o = get(key);
		if (o instanceof Integer) {
			return ((Integer) o).intValue();
		} else if (o instanceof Double) {
			double dv = (Double) o;
			return doubleToInt(dv);
		} else if (o instanceof String) {
			double dv = Double.parseDouble((String) o);
			if (Numeric.isInteger(dv)) {
				return (int) dv;
			} else {
				throw new ClassCastException((String) o);
			}
		} else {
			throw new ClassCastException(o.toString());
		}
	}

	public long getAsLong(String key, long def) {
		if (containsKey(key)) {
			try {
				return getAsLong(key);
			} catch (NameNotFoundException e) {
				throw new IllegalStateException();
			}
		} else {
			return def;
		}
	}

	/**
	 * Returns the value associated by the specified key as int.
	 * @param key
	 * @throws NameNotFoundException
	 */
	public long getAsLong(String key) throws NameNotFoundException {
		Object o = get(key);
		if (o instanceof Long) {
			return ((Long) o).longValue();
		} else if (o instanceof Integer) {
			return ((Integer) o).longValue();
		} else if (o instanceof Double) {
			double dv = (Double) o;
			return doubleToInt(dv);
		} else if (o instanceof String) {
			return Long.parseLong((String) o);
		} else {
			throw new ClassCastException(o.toString());
		}
	}

	private int doubleToInt(double x) throws ClassCastException {
		if (Numeric.isInteger(x)) {
			return (int) x;
		} else {
			throw new ClassCastException(String.valueOf(x));
		}
	}

	public int getAsInt(String key, int def) {
		if (containsKey(key)) {
			try {
				return getAsInt(key);
			} catch(NameNotFoundException e) {
				throw new IllegalStateException();
			}
		} else {
			return def;
		}
	}


	public double getAsDouble(String key, double def) {
		if (containsKey(key)) {
			try {
				return getAsDouble(key);
			} catch(NameNotFoundException e) {
				throw new IllegalStateException();
			}
		} else {
			return def;
		}
	}

	public double getAsDouble(String key) throws NameNotFoundException {
		Object o = get(key);
		if (o instanceof Double) {
			return ((Double) o).doubleValue();
		} else if (o instanceof String) {
			return Double.parseDouble((String) o);
		} else if (o instanceof Integer) {
			return ((Integer) o).doubleValue();
		} else {
			throw new ClassCastException(o.toString());
		}
	}

	public String getAsStr(String key) throws NameNotFoundException {
		return get(key).toString();
	}

	public String getAsStr(String key, String def) {
		if (containsKey(key)) {
			try {
				return getAsStr(key);
			} catch (NamingException e) {
				throw new IllegalStateException(e);
			}
		} else {
			return def;
		}
	}

	public Class<?> getAsClass(String key) throws NameNotFoundException {
		return Generics.cast(get(key));
	}

	public Class<?> getToClass(String key) throws NameNotFoundException, ClassNotFoundException {
		return getToClass(key, Thread.currentThread().getContextClassLoader());
	}

	/**
	 *
	 * @param key
	 * @param defaultPkg add the packages to the search path.
	 *
	 * @return
	 * @throws NameNotFoundException
	 * @throws ClassNotFoundException
	 */
	public Class<?> getToClass(String key, String defaultPkg) throws NameNotFoundException, ClassNotFoundException {
		return getToClass(key, Thread.currentThread().getContextClassLoader(), defaultPkg);
	}

	public Class<?> getToClass(String key, ClassLoader cl) throws NameNotFoundException, ClassNotFoundException {
		return getToClass(key, cl, null);
	}

	public Class<?> getToClass(String key, ClassLoader cl, String defaultPkg) throws NameNotFoundException, ClassNotFoundException {
		Object value = get(key);
		if (value instanceof Class) {
			return (Class<?>) value;
		}
		String fqcn = value.toString();
		if (fqcn.startsWith("class ")) {
			// "class " is added by Class#toString
			fqcn = fqcn.substring("class ".length());
		}
		try {
			return (cl == null) ? Class.forName(fqcn) : cl.loadClass(fqcn);
		} catch(ClassNotFoundException e) {
			if (defaultPkg != null) {
				fqcn = defaultPkg + "." + fqcn;
				return (cl == null) ? Class.forName(fqcn) : cl.loadClass(fqcn);
			} else {
				throw e;
			}
		}
	}

	public <T> T getAsInstanceByName(String key, String defaultPackage, String defaultValue) throws ReflectiveOperationException {

		if (!containsKey(key)) set(key, defaultValue);

		try {
			return getAsInstanceByName(key, defaultPackage);
		} catch (NamingException e) {
			throw new IllegalStateException();
		}
	}

	public <T> T getAsInstanceByName(String key, String defaultPackage) throws NamingException, ReflectiveOperationException {
		String className = get(key);
		if (!defaultPackage.endsWith(".")) defaultPackage += ".";
		if (!className.contains(".")) set(key, defaultPackage + className);
		return getAsInstance(key);
	}
	
	public void putForce(String key, Object value) {
		parameter.put(key, value);
	}

	public CommandSetting clone() {
		return new CommandSetting(this);
	}

	public boolean containKey(String key) {
		return this.parameter.containsKey(key);
	}

	public <T> T getAsInstance(String key) throws NamingException, ReflectiveOperationException {
		return getAsInstance(key, Thread.currentThread().getContextClassLoader());
	}

	public <T> T getAsInstance(String key, String deflt) throws NamingException, ReflectiveOperationException {
		if (!containsKey(key)) set(key, deflt);
		return getAsInstance(key);
	}

	public <T> T getAsInstance(String key, ClassLoader cl) throws NamingException, ReflectiveOperationException {
		return getAsInstance_(key, cl);
	}

	private <T> T getAsInstance_(String key, ClassLoader cl) throws NamingException, ReflectiveOperationException {
		try {
			return Generics.cast(getToClass(key, cl).newInstance());
		} catch (InstantiationException e) {
			// e has no message in Java7 etc
			throw new InstantiationException(
				"Failed to instantiate "+ getAsStr(key)
				+" (associated with "+ key +") : "+ e.getMessage());
		}
	}

	public <T> T getAsInstance(String key, Object ... args) throws NamingException, ReflectiveOperationException {
		Class<?>[] argsCls = new Class<?>[args.length];
		for (int i = 0; i < argsCls.length; i++) {
			argsCls[i] = args[i].getClass();
		}
		return getAsInstance(key, argsCls, args);
	}

	public <T> T getAsInstance(String key, Class<?>[] argCls, Object[] args) throws NamingException, ReflectiveOperationException {
		return getAsInstance_(key, argCls, args, Thread.currentThread().getContextClassLoader());
	}

	private <T> T getAsInstance_(String key, Class<?>[] argCls, Object[] args, ClassLoader cl) throws ReflectiveOperationException, NameNotFoundException {
		try {
			return Generics.cast(getToClass(key, cl).getDeclaredConstructor(argCls).newInstance(args));
		} catch (InstantiationException e) {
			// e has no message in Java7 etc
			throw new InstantiationException(
				"Failed to instantiate "+ getAsStr(key)
				+" (associated with "+ key +") : "+ e.getMessage());
		}
	}

	
	public void subscriptHash() {
		System.out.println("------------------start------------------");

		for (String key : parameter.keySet()) {
			System.out.println(key + "\t" + parameter.get(key));
		}
		System.out.println("------------------end------------------");
	}

	public void subcriptToFile(String filePath) {

		try {
			FileOutputStream fos = new FileOutputStream(filePath);
			OutputStreamWriter osw = new OutputStreamWriter(fos);
			BufferedWriter bw = new BufferedWriter(osw);

			for (String key : parameter.keySet()) {
				bw.write(key + "	" + parameter.get(key) + FileConstants.NEWLINE_DEMILITER);
			}
			bw.close();
		} catch (IOException e) {

			String[] arg = filePath.split(FileConstants.FILEPATH_DEMILITER);

			// args is the args remove the last element.
			String[] args = new String[arg.length - 1];
			for (int i = 0; i < args.length; i++) {
				args[i] = arg[i];
			}

			String dir = String.join(FileConstants.FILEPATH_DEMILITER, args);
			DirectoryMaker.Make(dir);
			System.out.println(e.getMessage());
			subcriptToFile(filePath);
			return;
		}

	}

	public CommandSetting set(String key, Object Value) {
		put(key, Value);
		return this;
	}

	
	public static void main(String[] args) throws NumberFormatException, notFoundException, NameNotFoundException {
		CommandSetting commandSetting = new CommandSetting();
		commandSetting.put("A", "B");
		commandSetting.put("B", "0.5");
		commandSetting.put("C", "B");
		commandSetting.put("D", "0.5");
		commandSetting.put("F", "0.5");
		commandSetting.put("E", "outpuit");

		commandSetting.subscriptHash();
		// commandSetting.subcriptToFile("odfasdf/output.dat");
		System.out.println(commandSetting.getAsStr("A"));
	}

	@Override
	public Set<Entry<String, Object>> entrySet() {
		// TODO Auto-generated method stub
		return null;
	}

}
