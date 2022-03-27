package distributedBank;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;


//Singleton Class
public class ServerComm {
	//This is the communication module for server
	private static ServerComm server_comm = null;
	DatagramSocket ds_server;
	InetAddress client_address;
	int client_port;
	byte[] receive;
	
	public static ServerComm getServerComm() throws SocketException {
		if (server_comm == null) {
			server_comm = new ServerComm();
			System.out.println("servercomm created");
		}
		
		return server_comm;
	}
	
	private ServerComm() throws SocketException {
		receive = new byte[65535];
		DatagramSocket ds = new DatagramSocket(2023);
		
		
		ds_server = ds;
	}
	
	public void serverSend(byte[] marshalled) throws IOException {
		byte[] buffer = marshalled;
		//change here for stub testing
		
		DatagramPacket dp_send = new DatagramPacket(buffer, buffer.length, client_address, client_port);
		ds_server.send(dp_send);
	}
	
	//serverListen: call unmarshaling, categorize the information received, and call manager to do the job according to opcode
	
	public void serverListen() throws IOException {
		//This method should be called immediately after construction of server module
		
		DatagramPacket dp_receive = null;
		
		while (true) {
			dp_receive = new DatagramPacket(receive, receive.length);
			
			ds_server.receive(dp_receive);
			
			byte[] received_msg = dp_receive.getData();
			System.out.println(received_msg);
			
			//call request handler from server main
			
			//Testing lines
			String message = new String(received_msg);
			System.out.println(message);
			
			client_address = dp_receive.getAddress();
			client_port = dp_receive.getPort();
			int msg_length = dp_receive.getLength();
			
			System.out.println("The msg comes from: " + client_address.toString() + " port: " + client_port + " with length: " + msg_length);
			
			//end of testing lines
			
			ServerMain.mainHandler(received_msg);
			
			receive = new byte[65535];
			
			
			
			
		}
	}
}
