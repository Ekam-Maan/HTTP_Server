package core;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.time.format.DateTimeFormatter;  
import java.time.LocalDateTime; 

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import HTTP.HttpParser;
import HTTP.HttpParsingException;
import HTTP.HttpRequest;

public class HttpRequestProcessThread extends Thread{
	
	class Lock{
		
	}
	
	private static int clientNumber=0;
	private Socket socket;
	private String inputRequest= "";
	private HttpRequest parsedRequest;
	private static Object moniter1 = new Object();
	private static Object moniter2 = new Object();
	
	private static HashMap <String, Lock> readingFrom = new HashMap<String,Lock>();
	private static HashMap <String, Lock> writingIn = new HashMap<String,Lock>();
	private static ArrayList<String> restrictedFiles = new ArrayList<String>();
	private String method;
	private String requestTarget;
	private String workingDirectory;
	private String requestBody;
	private String responseBody="";
	private final static Logger LOGGER = LoggerFactory.getLogger(HttpRequestProcessThread.class);
	public HttpRequestProcessThread(Socket socket, String directory) {
		this.socket= socket;
		this.workingDirectory = directory;
	}
	
	public void run() {
		
		InputStream in = null;
		DataInputStream din = null;
		OutputStream outputStream = null;
		restrictedFiles.add("C:\\Users\\ekamj\\eclipse-workspace\\HTTP_Server\\src\\main\\java\\core/file1"); // restricted files
		try {
			in = socket.getInputStream();
			 din = new DataInputStream(socket.getInputStream());
		    outputStream = socket.getOutputStream();
		    HttpParser parser= new HttpParser();
		    try {
				parsedRequest = parser.parseHttpRequest(in);
				method= parsedRequest.getMethod().name();
				requestTarget = parsedRequest.getRequestTarget();
				requestBody= parsedRequest.getBody();
				System.out.println(parsedRequest);
			} catch (HttpParsingException e) {
				// TODO Auto-generated catch block
				System.out.println(e.getErrorCode());
				e.printStackTrace();
			}
		   
//		    int _byte;
//		    while( (_byte = in.read()) >= 0) {
//		    	//inputRequest +=(char)_byte;
//		    	System.out.print((char)_byte);
//		    	  System.out.println("thereeee....");
////		    	if((char)_byte == '\r') {
////		    		
////		    		break;
////		    	}
//		    	//System.out.println("response: cp0");
//		    }
//		    System.out.println("--------------------------");
//		    System.out.print(inputRequest);
		   // System.out.println("response: cp1");
		    String statusLine = "HTTP/1.1 ";
		    if(method.equals("GET")) {
		    	 if(requestTarget.equals("/")) { // if the request Target is empty send the list of files 
				    	//System.out.println("requestTarget.equals(\"\\\\\")");
				    	//List<String> results = new ArrayList<String>();
				    	responseBody = "\nFiles in current directory:\n";
				    	File[] files = new File(workingDirectory).listFiles();
				    	statusLine += "202 OK\r\n";
				    	for(File file :files) {
				    		responseBody += (file.getName()+"\n");
				    	}
				    }else {
				    	File file = new File(workingDirectory+requestTarget);
				    	System.out.println(workingDirectory+requestTarget);
				    	System.out.println(restrictedFiles.get(0));
				    	if(restrictedFiles.contains(workingDirectory+requestTarget)) {
				    		responseBody += "ERROR 403 FORBIDDEN";
				    		 statusLine += "403 FORBIDDEN\r\n";
				    	}
				    	else if(file.exists() ) {
				    		 Scanner sc = new Scanner(file);
				    		 responseBody += "\n";
				    		 if(writingIn.get(workingDirectory+requestTarget) != null) {
				    			 synchronized((writingIn.get(workingDirectory+requestTarget))) {
				    				 readingFrom.put(workingDirectory+requestTarget, new Lock());
					    			 while (sc.hasNextLine()) {
						    		      responseBody += sc.nextLine();
						    		  }
					    			  try {
											sleep(20000);
										} catch (InterruptedException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
					    			
					    		 }
				    			 statusLine += "202 OK\r\n";
				    		 }else {
				    			 readingFrom.put(workingDirectory+requestTarget, new Lock());
				    			 synchronized(readingFrom.get(workingDirectory+requestTarget)) {
				    				 while (sc.hasNextLine()) {
						    		      responseBody += sc.nextLine();
						    		  }
				    				  try {
											sleep(20000);
										} catch (InterruptedException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
				    			 }
				    			 
				    			 statusLine += "202 OK\r\n";
				    		 }
				    		 readingFrom.remove(workingDirectory+requestTarget); // done reading
				    		
				    		   
				    		    responseBody += "\n";
				    		    statusLine += "202 OK\r\n";
				    	}else {
				    		statusLine += "404 NOT FOUND\r\n";
				    		responseBody += "\nERROR 404: FILE NOT FOUND\n";
				    	}
				    }
		    }else if(method.equals("POST")) {
		    	
		    //	System.out.println(_byte);
		    	System.out.println("------");
		    	File file = new File(workingDirectory+requestTarget);
		    	
		    	if(!file.exists()) {
		    		statusLine += "404 NOT FOUND\r\n";
		    		responseBody += "\nERROR 404: FILE NOT FOUND\n";
		    	}else {
		    		 // now two writers cannot write at same time, AND even at different locations too.
		    			FileWriter myWriter = new FileWriter(file, true);
		    			if(readingFrom.containsKey(workingDirectory+requestTarget)) {
		    				synchronized(readingFrom.get(workingDirectory+requestTarget)){
		    					writingIn.put(workingDirectory+requestTarget, new Lock());
						        myWriter.write(requestBody+"\n");
						       
						        try {
									sleep(20000);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
							}
						        myWriter.close();
					    	}
		    			}else {
		    				writingIn.put(workingDirectory+requestTarget, new Lock());
		    				synchronized(writingIn.get(workingDirectory+requestTarget)) {
		    					myWriter.write(requestBody+"\n");
							       
						        try {
									sleep(20000);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
						        myWriter.close();
		    				}
		    				 
					       
		    			}
		    			
					     writingIn.remove(workingDirectory+requestTarget);
					     statusLine += "202 OK\r\n";
					     responseBody += "\nDATA POSTED SUCESSFULLY.\n";
		    			
		    		
			    	//body += "DONE!";
		    	}
		    	
		    }
		    
		    final String CRLF= "\r\n";
		    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
		    LocalDateTime now = LocalDateTime.now();  
		    
		    String headers = dtf.format(now)+"\r\n"
		    		+ "Server: localhost:8080\r\n"+
                     "Content-Length: "+responseBody.getBytes().length+"\r\n"+
                     "Connection: Closed\r\n";
		    String response = statusLine + headers + CRLF+ responseBody + CRLF + CRLF;
		    System.out.println("\n"+response);
		    
//		    body += CRLF+ dtf.format(now)+"\r\n"
//		    		+ "Server: localhost:8080\r\n"
//		    		+ "Content-Length: "+responseBody.getBytes().length+"\r\n"
//		    		+ "Connection: Closed\r\n"
//		    		+ "Content-Type: text/html; charset=iso-8859-1"+CRLF +responseBody+CRLF+ CRLF; 
		String html= "<html><head><title>server</title></head><body><h1></h1>Hello World! from server client: "+ (++clientNumber) +".</body></html>";
//		
//		String response = "HTTP/1.1 200 OK" +CRLF// status line : HTTP VERSION RESPONSE_CODE RESPONSE_MSG
//				+ "Content-Length: "+ responseBody.getBytes().length + CRLF // HEADER
//						+ CRLF +responseBody +CRLF + CRLF;
		//String response = body;
		outputStream.write(response.getBytes());
	//	System.out.println("response: "+ response.getBytes().toString());
		
		
		
		
//		try {
//			sleep(100);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			LOGGER.error("Problem with comunication",e);
			System.out.println("response: cp0.0");
			e.printStackTrace();
		}finally {
			System.out.println("response: cp0.1");
			try {
				if(in != null)
				   in.close();
				if(outputStream != null)
				   outputStream.close();
				if(socket!= null)
				   socket.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

}
