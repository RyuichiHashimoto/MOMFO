package Network.Test;

import javax.naming.NameNotFoundException;

import Network.Constants.NetworkConstants;
import Network.Container.Container;
import lib.experiments.CommandSetting;

public class ObjectReciever {

	public static void main(String[] args) {
		Container d = NetworkConstants.recieveObject();
		CommandSetting output = (CommandSetting)d.get();

		try {
			System.out.println(output.getAsStr("what"));
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		output.subcriptToFile("output/output.dat");
//		System.out.println(d.getStatus());
	}
}
