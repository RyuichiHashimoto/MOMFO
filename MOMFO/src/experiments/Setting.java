package experiments;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import javax.naming.NameNotFoundException;
import javax.naming.NamingException;


public class Setting{

	/** Symbol for the end of the sub-setting specification. */
	private static final String CLOSING_SUBSETTING = ":end";
	/** Symbol to specify the sub-setting in command line style specification. */
	/** Symbol to remove the element via command line. */
	public static final String NULL = "";
	private  static final String DEMILITER = ":";
	private static final String COMMENT    = "#";
	private static final String SPACE = " ";
	private static final String TAB = "\t";
	private static final String END = "END";
	private static final String NEWLINE= "\n";

	private final HashMap<String, Object> settings_;

	public final boolean containsKey(String key){
		return settings_.containsKey(key);
	}

	public HashMap<String,Object >get(){return settings_;}

	public void experiment_setting(String name){
		try(BufferedReader br = new BufferedReader(new FileReader(name))){
		String[] S;
		String line;
		while((line = br.readLine())!= null){
			if(line.startsWith(COMMENT)) continue;
			if(line.startsWith(NEWLINE)) continue;
			if(line.startsWith("---")) continue;
			if (line.length() == 0) continue;

			line.replaceAll(TAB, NULL);
			line.replaceAll(SPACE, NULL);
			S  = line.split(DEMILITER);
			System.out.print(S[0] + " " + S[1] + "\n");
			add(S[0],S[1]);
		}

		} catch (IOException e){
			e.printStackTrace();
		}
	}



	public <T> T get(String key) throws NameNotFoundException{
		Object retval = settings_.get(key);
		if (retval == null) throw new NameNotFoundException(key + " is not specified. ");
		return Generics.cast(retval);
	}

	public Setting(){
		settings_ = new HashMap<String,Object>();
	}
	public Setting(HashMap<String,Object> test){
		settings_ =(HashMap<String, Object>) test.clone();
	}

	public Setting(String name){
		settings_ = new HashMap<String,Object>();
		experiment_setting(name);
	}

	//上書き保存する
	public void addCoerction(String key , Object value){
			settings_.put(key, value);
	}

	//上書きしない
	public void add(String key , Object value){
		if(!settings_.containsKey(key)){
			settings_.put(key, value);
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


	public boolean getAsBool(String key) throws NameNotFoundException {
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

	private int doubleToInt(double x) throws ClassCastException {
		if (Numeric.isInteger(x)) {
			return (int) x;
		} else {
			throw new ClassCastException(String.valueOf(x));
		}
	}

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

	public Setting set(String terminateSignal, Object true1) {
		Setting setting = new Setting();
		setting.set(terminateSignal, true1);
		return setting;
	}

}
