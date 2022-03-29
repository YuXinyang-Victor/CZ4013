package commtest;

import java.io.IOException;

import distributedBank.ServerComm;

public class ServerDriver {
	public static void main(String[] args) throws IOException {
		System.out.println("server running1");
		ServerComm server_comm_module = new ServerComm();
		System.out.println("server running2");
		server_comm_module.serverListen();
		System.out.println("server running3");
		
		
		
	}
}
