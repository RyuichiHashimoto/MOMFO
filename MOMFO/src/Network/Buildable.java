package Network;

import java.io.IOException;

import javax.naming.NamingException;

import lib.experiments.CommandSetting;
import lib.experiments.Exception.CommandSetting.CannotConvertException;
import lib.experiments.Exception.CommandSetting.notFoundException;
import momfo.util.JMException;

public interface Buildable {
	void build(CommandSetting s) throws NamingException, IOException, ReflectiveOperationException, notFoundException, IllegalArgumentException, CannotConvertException, JMException;
}
