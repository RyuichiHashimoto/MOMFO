package Network.Test;

import Network.Constants.NetworkConstants;
import lib.experiments.CommandSetting;

public class ObjectServer {


	public static void main(String[] args) {
		CommandSetting suc = new CommandSetting();
		suc.put("what", "SUCCESS");

		suc.put("hoge", "mimimimmiimi");
		NetworkConstants.sendObject(suc, args[0]);
		System.out.println();
	}
}
