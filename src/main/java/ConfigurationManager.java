import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;

import util.json;

public class ConfigurationManager {
	
	private static ConfigurationManager myConfigManager; // singleton design pattern, because we need only one manager.
	private static Configuration myCurrentConfiguration;
	
	private ConfigurationManager() {
		
	}
	
	public static ConfigurationManager getConfigManagerInstance() {
		if(myConfigManager == null) {
			myConfigManager = new ConfigurationManager();
			}
		return myConfigManager;
	}
	
	//it is used to load configuration file by using the given path 
	public void loadConfigrationFile(String filePath) {
		FileReader fileReader;
		try {
			fileReader = new FileReader(filePath);
		} catch (FileNotFoundException e) {
			throw new HttpConfigurationException(e);
		}
		StringBuffer sb= new StringBuffer();
		int i;
		try {
			while((i= fileReader.read()) != -1) {
				sb.append((char)i);
			}
		} catch (IOException e) {
			throw new HttpConfigurationException(e);
		}
		JsonNode conf;
		try {
			conf = json.parse(sb.toString());
			//System.out.println(json.stringifyPretty(conf));
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new HttpConfigurationException("Error: Not able to parse the configration file, may be due to bad file path.",e);
		}
		try {
			myCurrentConfiguration = json.fromJson(conf, Configuration.class);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			throw new HttpConfigurationException("Error: Not able to parse the configration file,Internal.",e);
		}
		
	}
	
	// returns the current loaded configuration file
	public Configuration getCurrentCongifuration() {
		if(myCurrentConfiguration == null) {
			throw new HttpConfigurationException("No Configuration Set Yet.");
		}
		return myCurrentConfiguration;
	}

}
