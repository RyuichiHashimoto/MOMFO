package Network;

import static Network.CommonSolverEvent.*;
import static Network.GridComputing.RunSetting.*;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.NoSuchElementException;

import javax.naming.NameNotFoundException;
import javax.naming.NamingException;
import javax.sound.midi.Sequence;

import Network.GridComputing.asg.cliche.Command;
import lib.experiments.CommandSetting;
import lib.experiments.Exception.CommandSetting.CannotConvertException;
import lib.experiments.Exception.CommandSetting.notFoundException;
import lib.lang.Generics;
import lib.lang.NeedOverriden;
import momfo.util.JMException;

public abstract class Solver implements Runnable, Buildable {

	public static void buildObject(Buildable b, CommandSetting s) throws ReflectiveOperationException, NamingException,
			IOException, IllegalArgumentException, notFoundException, CannotConvertException, JMException {
		setParameters(b, s);
		b.build(s);
	}

	public static void setParameters(Object obj, CommandSetting s) throws ReflectiveOperationException, NamingException,
			IllegalArgumentException, notFoundException, CannotConvertException {
		if (obj == null)
			return;
		Class<?> cls = obj.getClass();
		do {
			for (Field field : cls.getDeclaredFields()) {
				Parameter param = field.getAnnotation(Parameter.class);
				if (param == null)
					continue;
				// resolve name (key) for CommandSetting specification
				String name;
				if (Parameter.NOT_SPECIFIED.equals(param.name())) {
					name = field.getName();
					if (name.endsWith("_")) {
						name = name.substring(0, name.length() - 1);
					}
				} else {
					name = param.name();
				}

				// whether the value is set or not
				if (!s.containsKey(name)) {
					if (param.required()) {
						throw new NoSuchElementException(
								"Parameter [" + name + "] is reqired by " + obj + ": \n" + param.description());
					}
					continue;
				}

				setParam(obj, field, s, name);
			}
		} while ((cls = cls.getSuperclass()) != null);
	}

	protected static void setParam(Object o, Field f, CommandSetting s, String key) throws ReflectiveOperationException,
			NameNotFoundException, IllegalArgumentException, notFoundException, CannotConvertException {
		// Field class is not immutable. And it is not cached.
		// Modification of field applies only the given field, f.
		// Therefore, f.setAccessible(false) at the end of this method means
		// nothing.
		f.setAccessible(true);
		Class<?> cls = f.getType();
		if (cls == int.class) {
			f.set(o, s.getAsInt(key));
		} else if (cls == double.class) {
			f.set(o, s.getAsDouble(key));
		} else if (cls == boolean.class) {
			f.set(o, s.getAsBool(key));
		} else if (Sequence.class.isAssignableFrom(cls)) {
			assert false;

			// f.set(o, Sequence.getSequence(s.getAsStr(key)));
		} else {
			f.set(o, s.get(key));
		}
	}

	public CommandSetting setting;
	private Throwable thrown_;
	public ArrayList<SolverResult<?>> results = new ArrayList<>();

	@Override
	public void build(CommandSetting s) throws NamingException, IOException, ReflectiveOperationException,
			notFoundException, IllegalArgumentException, CannotConvertException, JMException {
		setting = s;
		buildImpl();
		// instantiate Results
		String[] resultName = s.getAsSArray(RESULT, RESULT_DELIMITER, new String[0]);
		for (int i = 0; i < resultName.length; i++) {
			SolverResult<?> rst = Generics.cast(Class.forName(resultName[i]).newInstance());
			Solver.buildObject(rst, s);
			results.add(rst);
		}
	}

	abstract protected void buildImpl() throws NamingException, IOException, ReflectiveOperationException, JMException, notFoundException;

	@NeedOverriden
	public void reset() {
		thrown_ = null;
	}

	abstract protected void solve() throws IOException, ClassNotFoundException, JMException, NameNotFoundException;

	/**
	 * The same as {@link #run()} except for this method can throw exceptions.
	 */
	public void throwingRun() throws IOException, Throwable {
		run();
		if (isErrorHappend()) {
			throw getThrown();
		}
	}

	@Override
	public final void run() {
		try {
			notifyEvent(BEFORE_RUN);
			solve();
			notifyEvent(AFTER_RUN);
		} catch (Throwable e) {
			thrown_ = e;
			e.printStackTrace();
			try {
				notifyEvent(EXCEPTION_RISE);
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
		saveResults();
	}

	private void saveResults() {
		for (int i = 0; i < results.size(); i++) {
			try {
				results.get(i).save();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					results.get(i).close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void notifyEvent(SolverEvent event) throws IOException {
		for (int i = 0; i < results.size(); i++) {
			event.notifyEvent(results.get(i));
		}
	}

	@Command
	public boolean isErrorHappend() {
		return thrown_ != null;
	}

	@Command
	public Throwable getThrown() {
		return thrown_;
	}

	@Command
	public CommandSetting getCommandSetting() {
		return setting;
	}

	@Command
	public Object getCommandSetting(String key) throws NamingException, notFoundException {
		return setting.get(key);
	}

	@Command
	@Override
	public String toString() {
		return this.getClass().getCanonicalName();
	}
}
