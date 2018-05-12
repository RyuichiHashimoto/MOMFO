package experiments;

import javax.naming.NameNotFoundException;

import lib.experiments.CommandSetting;
import lib.experiments.Exception.CommandSetting.notFoundException;

public class SettingTest {

	public static void main(String[] args)
			throws NameNotFoundException, notFoundException, ReflectiveOperationException {
		CommandSetting setting = new CommandSetting();

		setting.put("output", "99,99");

		int[] d = setting.getAsNArray("output");

		System.out.println(d[0]);
		d[0] = 3;

		setting.put("output", "99,99");

		d = setting.getAsNArray("output");

		System.out.println(d[0]);

	}

}
