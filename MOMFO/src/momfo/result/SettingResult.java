package momfo.result;

import java.io.IOException;
import java.io.Serializable;
import java.io.Writer;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Map;

import javax.naming.NamingException;

import Network.RunSetting;
import Network.SolverResult;
import Network.GridComputing.StreamProvider;
import lib.experiments.CommandSetting;
import lib.experiments.FormatDate;
import momfo.core.GAFramework;

// TODO: sort
public class SettingResult extends SolverResult<GAFramework> {
	@Override
	public void build(CommandSetting s) throws ReflectiveOperationException, NamingException, IOException {
		super.build(s);
	}

	@Override
	public void beforeRun() throws IOException {
		CommandSetting s = solver.setting;
		s.set("JavaVersion", System.getProperty("java.version") +"/"+
				System.getProperty("sun.arch.data.model") +"bit");
		s.set("hostName", InetAddress.getLocalHost().toString());
		s.remove("time");
		s.remove("runAt");
		s.remove("finishedAt");
		FormatDate fd = FormatDate.getDate();
		writer.write("runAt: "+ fd);
		writer.write("\n");
		writer.write(solver.setting.toString());
		writer.flush();
		s.set("runAt", fd);
	}

	@Override
	public void afterRun() throws IOException {
		CommandSetting s = solver.setting;
		long time = solver.getEpocTime();
		FormatDate finish = FormatDate.getDate();
		s.set("time", time);
		s.set("finishedAt", finish);

		writer.write("time: "+ FormatDate.readbleTime(time));
		writer.write("\nfinishedAt: " + finish.toString());
		writer.write("\n");
	}

	@Override
	public void exceptionRise() throws IOException {
		close();
	}

	@Override
	public void save(CommandSetting s, Object ... results) throws IOException, NamingException {
		// to find the first time when a task is executed
		FormatDate earliest = null;
		ArrayList<String> host = new ArrayList<>();
		ArrayList<String> javaVer = new ArrayList<>();
		long comptTime = 0;
		for (int i = 0; i < results.length; i++) {
			CommandSetting set = (CommandSetting) results[i];
			FormatDate fd = set.get("runAt");
			if (earliest == null) {
				earliest = fd;
			} else if (earliest.compareTo(fd) > 0) {
				earliest = fd;
			}
			comptTime += set.getAsLong("time", 0);

			host.add(set.getAsStr("hostName", "??"));
			javaVer.add(set.getAsStr("JavaVersion", "??"));
		}
		s.set("hostName", host.toString());
		s.set("JavaVersion", javaVer.toString());
		s.remove("time");
		s.remove("finishedAt");
		StreamProvider sp = s.get(RunSetting.STREAM_PROVIDER);
		try (Writer w = sp.getWriter(getOutputName(s))) {
			w.write("runAt: "+ earliest);
			w.write("\n");
			w.write(s.toString());
			w.write("\n");
			w.write("time: "+ FormatDate.readbleTime(comptTime));
			w.write("\n");
			w.write("finishedAt: "+ FormatDate.getDate());
			w.write("\n");
		}
	}

	@Override
	public void save() throws IOException {
		writer.flush();
	}

	@Override
	public void close() throws IOException {
		writer.close();
	}

	@Override
	public Serializable getMemento() {
		CommandSetting s = solver.setting;
		for (Map.Entry<String, Object> e: s.entrySet()) {
			if (!(e.getValue() instanceof Serializable)) {
				e.setValue(e.getValue().toString());
			}
		}
		return s;
	}

	@Override
	protected String getOutputName(CommandSetting s) throws NamingException {
		return s.getAsStr(RunSetting.NAME_SPACE, "") + "info.ini";
	}



}
