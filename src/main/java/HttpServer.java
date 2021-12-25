import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import core.ServerListnerThread;
public class HttpServer {
	
	static Scanner kb = new Scanner(System.in);
	public static void main (String[] args) throws ParseException {
		System.out.println("Starting Server.....");
//		ConfigurationManager configManager = ConfigurationManager.getConfigManagerInstance();
//		configManager.loadConfigrationFile("src/main/java/http.json");
//		Configuration config = configManager.getCurrentCongifuration();
//		System.out.println(config);
		int portNumber = 8080;
	    String directory="C:\\Users\\ekamj\\eclipse-workspace\\HTTP_Server\\src\\main\\java\\core";
        Options options = new Options();
		
		// adding option -v 
		options.addOption("v", false, "prints debugging msgs");
		
		// adding option -p
		options.addOption("p", false, "specifies port number");
		
		// adding option -d
		options.addOption("d", false, "specifies the directory");
		
		CommandLineParser parser = new DefaultParser();
		CommandLine cmd = parser.parse(options, args);
		
		
		if(cmd.hasOption("v")) {
			System.out.println("Here are some of the commands that you can use:\n"
					+ "-v to view a debug message\n"
					+ "-p to specify the port number\n"
					+ "-d to specify the directory for the server.\n");
		}
		if(cmd.hasOption("-p")) {
			int idx = 3;
			if(!cmd.hasOption("v")) {
				idx = 2;
			}
			portNumber = Integer.parseInt(args[3]);
			System.out.println("The server is currently listening at port number: "+ portNumber +"\n");
		}
		if(cmd.hasOption("d")) {
			int idx = 5;
			if(!cmd.hasOption("v") && !cmd.hasOption("p")) {
				idx = 2;
			}else if (cmd.hasOption("v") && !cmd.hasOption("p")) {
				idx = 3;
			}else if (!cmd.hasOption("v") && cmd.hasOption("p")) {
				idx = 4;
			}
			directory =  args[idx];
			System.out.println("The directory is set to : "+ directory);
		}
		
		
		try {
			ServerListnerThread serverThread = new ServerListnerThread(portNumber,directory);
			serverThread.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}

}
