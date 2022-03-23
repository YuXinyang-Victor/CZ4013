package commtest;

import java.io.IOException;

import distributedBank.ServerComm;

public class ServerDriver {
	public static void main(String[] args) throws IOException {
		ServerComm server_comm_module = new ServerComm();
		
		server_comm_module.serverListen();
		
		
		
	}
}
