package momfo.result;

import java.io.IOException;
import java.io.Serializable;

import javax.naming.NamingException;

import lib.experiments.CommandSetting;

public class NullResult extends GAResult{

	@Override
	protected String getOutputName(CommandSetting s) throws NamingException {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public void save() throws IOException {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void close() throws IOException {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void save(CommandSetting s, Object... results) throws IOException, NamingException {

	}

	@Override
	public Serializable getMemento() {
		return "";
	}

}
