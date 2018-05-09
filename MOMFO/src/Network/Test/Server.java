package Network.Test;

import Network.Constants.NetworkConstants;

public class Server {

	public static void main(String[] args) {
		String status = NetworkConstants.sendFile(args[0],args[1]);
		
		
		System.out.println(status);
	}
	
}
