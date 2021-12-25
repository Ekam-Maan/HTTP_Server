
public class Configuration {
	
	private int port;
	private String webroot;
	
	@Override
	public String toString() {
		return "Configuration [port=" + port + ", webroot=" + webroot + "]";
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public String getWebroot() {
		return webroot;
	}
	public void setWebroot(String webroot) {
		this.webroot = webroot;
	}
	

}
