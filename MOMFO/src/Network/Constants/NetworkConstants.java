package Network.Constants;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import Network.Container.Container;

public class NetworkConstants {

	public static final int PORT_NUMBER = 21127;

	public static final int BUFFER_SIZE = 512;

	public static final int Timeout = 1000000;

	public static final String FINISH_STATUS = "###FINISH###";

	public static final String BUSY_STATUS = "###BUSY###";

	public static final String YET_STATUS = "###YET###";

	public static final String FREE_STATUS = "###FREE###";

	public static final String ERROR_STATUS = "###ERROR###";

	public static final String SUCCESS_STATUS = "###SUCCESS###";

	public static final String CLEAR_STATUS = "###CLEAR###";

	public static final String TERMINATE_SIGNAL = "###terminate_signal###";

	public static final String MASTER_HOST = "hughes.cs.osakafu-u.ac.jp";

	public static final Long WaitTime = (long) 500;

	public static String recieveFile(File file) {
		System.out.println("start");

		byte[] buffer = new byte[BUFFER_SIZE];
		ServerSocket serverSocket = null;
		Socket socket = null;
		InputStream inputStream = null;
		OutputStream outputStream = null;

		try {
			serverSocket = new ServerSocket(PORT_NUMBER);

			socket = serverSocket.accept();
			inputStream = socket.getInputStream();
			outputStream = new FileOutputStream(file);

			int fileLength = 0;

			while ((fileLength = inputStream.read(buffer)) > 0) {
				outputStream.write(buffer, 0, fileLength);
			}

			outputStream.close();
			inputStream.close();
			socket.close();
			serverSocket.close();

		} catch (IOException e) {
			return ERROR_STATUS;
		}
		return SUCCESS_STATUS;
	}

	public static String recieveFile(String filePath) {
		return recieveFile(new File(filePath));
	}

	// this is the client method
	public static String sendFile(File file, String hostName) {
		byte[] buffer = new byte[BUFFER_SIZE];
		Socket socket = null;
		System.out.println("start");

		InputStream inputStream = null;
		OutputStream outputStream = null;

		try {

			socket = new Socket(hostName, PORT_NUMBER);
			socket.setSoTimeout(Timeout);
			inputStream = new FileInputStream(file);
			outputStream = socket.getOutputStream();

			int fileLength = 0;
			while ((fileLength = inputStream.read(buffer)) > 0) {
				outputStream.write(buffer, 0, fileLength);
			}
			outputStream.flush();
			outputStream.close();
			inputStream.close();
			socket.close();

		} catch (IOException e) {

			return ERROR_STATUS;
		}
		try {
			Thread.sleep(NetworkConstants.WaitTime);
		} catch (InterruptedException e) {

		}
		return SUCCESS_STATUS;
	}

	public static String sendFile(String filepath, String hostName) {
		return sendFile(new File(filepath), hostName);
	}

	public static String sendObject(Object obj, String hostName) {
		System.out.println("sendObject has just started");
		Socket socket = null;
		ObjectOutputStream outputStream = null;
		Container datagram = new Container(obj);

		try {

			socket = new Socket(hostName, PORT_NUMBER);
			socket.setSoTimeout(Timeout);

			outputStream = new ObjectOutputStream(socket.getOutputStream());

			outputStream.writeObject(datagram);

			outputStream.flush();
			outputStream.close();
			socket.close();
		} catch (IOException e) {
			return ERROR_STATUS;
		}

		try {
			Thread.sleep(NetworkConstants.WaitTime);
		} catch (InterruptedException e) {

		}
		return SUCCESS_STATUS;
	}

	public static Container recieveObject() {
		System.out.println("start");

		ServerSocket serverSocket = null;
		Socket socket = null;
		ObjectInputStream inputStream = null;
		Container ret = null;

		try {
			serverSocket = new ServerSocket(PORT_NUMBER);


			socket = serverSocket.accept();

			inputStream = new ObjectInputStream(socket.getInputStream());
			ret = (Container) inputStream.readObject();


			inputStream.close();
			socket.close();
			serverSocket.close();
			ret.setStatus(SUCCESS_STATUS);
		} catch (IOException e) {
			ret = new Container(null);
			System.out.println();
			ret.setStatus(ERROR_STATUS);
			System.out.println(e.getMessage());
		} catch (ClassNotFoundException e2){
			ret = new Container(null);
			ret.setStatus(ERROR_STATUS);
		}
		return ret;
	}
}
