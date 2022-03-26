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
	InetAddress ip;
	byte[] receive;
	
	public static ServerComm getServerComm() throws SocketException {
		if (server_comm == null) {
			server_comm = new ServerComm();
		}
		
		return server_comm;
	}
	
	private ServerComm() throws SocketException {
		receive = new byte[65535];
		DatagramSocket ds = new DatagramSocket(2023);
		
		try {
			String ip_str = "127.0.0.1";
			ip = InetAddress.getByName(ip_str);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ds_server = ds;
	}
	
	public void serverSend(byte[] marshalled) throws IOException {
		byte[] buffer = marshalled;
		//change here for stub testing
		
		DatagramPacket dp_send = new DatagramPacket(buffer, buffer.length, ip, 2022);
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
			
			InetAddress client_address = dp_receive.getAddress();
			int client_port = dp_receive.getPort();
			int msg_length = dp_receive.getLength();
			
			System.out.println("The msg comes from: " + client_address.toString() + " port: " + client_port + " with length: " + msg_length);
			
			//end of testing lines
			
			receive = new byte[65535];
			
			
			
			
		}
	}
}
