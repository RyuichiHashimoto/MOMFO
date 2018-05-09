package Network.Test;

import Network.Constants.NetworkConstants;

public class Reciever {

	public static void main(String[] args) {
		String status = NetworkConstants.recieveFile(args[0]);
		System.out.println(status);
	}

}
