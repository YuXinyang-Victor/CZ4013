package distributedBank;

import java.net.InetAddress;

/**
 * Client Information class
 */
public class ClientInfo {
	InetAddress ip; 
	int port;
	long created_time;


	public ClientInfo(InetAddress ip, int port, long created_time) {
		this.ip = ip; 
		this.port = port; 
		this.created_time = created_time;
	}
	
	public InetAddress getIP() {
		return ip;
	}
	
	public int getPort() {
		return port;
	}
}
