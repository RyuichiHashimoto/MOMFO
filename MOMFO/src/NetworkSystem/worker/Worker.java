package NetworkSystem.worker;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public abstract class Worker implements NetWorkConnection,Runnable{

	private String MYPCNAME;

	private final int    MAXCPU;

	public Worker(){
		MAXCPU = Runtime.getRuntime().availableProcessors();
		try {
			MYPCNAME= InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			MYPCNAME = "NONE";
		}
	}

	public String GetName(){
		return MYPCNAME;
	}

	public int GetMaxCPU(){
		return MAXCPU;
	}



	public int SendFile(String filepath){
		  ServerSocket ServerSocket = null;
		  byte[] buffer = new byte[BUFFER_SIZE];
		  Socket socket = null;
		  int ret = 0;
		  try {
			  ServerSocket = new ServerSocket(PORTINDEX);
			  socket = ServerSocket.accept();

			  String line;
			  BufferedReader in =new BufferedReader(new InputStreamReader(socket.getInputStream()));
			  PrintWriter out = new PrintWriter(socket.getOutputStream(),true);
			  InputStream inputStream = new FileInputStream(filepath);
			  OutputStream outputStream = socket.getOutputStream();

			  while((line = in.readLine()) != null){
				  out.println(line);
			  }
			  outputStream.flush();
			  outputStream.close();
			  inputStream.close();
			  ret = SUCCESS;
			  System.out.println("");
		  }catch (IOException e){
			  e.printStackTrace();
			  ret = NETWORKEXCEPTION;
		  } finally{
			  try {
			        if (socket != null) {
			          socket.close();
			        }
			  } catch (IOException e){}

			  try {
				  if(ServerSocket != null){
					  ServerSocket.close();
				  }
			  }catch (IOException e){}
		  }
		  return ret;
	}

	public int RecieveFile(String filepath){
		Socket socket = null;
		byte[] buffer = new byte[BUFFER_SIZE];
		int ret;
		try {
			// 特定のホストのポート番号を見て空いていれば接続する．
			socket = new Socket(HOSTNAME,PORTINDEX);
			System.out.println("Connection is success");
			InputStream  inputStream  = (socket.getInputStream());
			OutputStream outputStream = new FileOutputStream(filepath);

			int fileLength;
			while((fileLength = inputStream.read(buffer)) > 0){
				outputStream.write(buffer,0, fileLength);
			}
			outputStream.flush();
			outputStream.close();
			inputStream.close();
			ret = SUCCESS;
			System.out.println("File transfer is successful");
		} catch (IOException e){
			e.printStackTrace();
			ret = NETWORKEXCEPTION;
			System.out.println("File transfer can not be finished ");
		} finally {
			try {
				if(socket != null){
					socket.close();
				}
			} catch (IOException e){}
		}
		return ret;
	}

}
