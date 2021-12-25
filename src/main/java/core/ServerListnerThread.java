package core;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerListnerThread extends Thread{
	private int hostnum =0;
	
	private int port;
	private String webRoot;
	private ServerSocket serverSocket;
	
	public ServerListnerThread(int port, String webRoot) throws IOException {
		this.port = port;
		this.webRoot = webRoot;
		this.serverSocket = new ServerSocket(port);
		}
	
	public void run() {
		try {
			while(serverSocket.isBound() && ! serverSocket.isClosed()) { 
				Socket socket=  serverSocket.accept();
				System.out.println("Connection Accepted");
				HttpRequestProcessThread processThread= new HttpRequestProcessThread (socket, webRoot);
				processThread.start();
				
				// this thread will only responsible to accept request, 
				//since all request are queued in the socketServer to get accepted and if we process requests in this and if a request takes long time
				// then other request will have to wait
				
			}
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if(serverSocket != null) {
					serverSocket.close();
				}
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
