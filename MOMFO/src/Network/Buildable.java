package Network;

import java.io.IOException;

import javax.naming.NamingException;

import lib.experiments.CommandSetting;
import lib.experiments.JMException;

public interface Buildable {
	void build(CommandSetting s) throws NamingException, IOException, ReflectiveOperationException, IllegalArgumentException,  JMException;
}
