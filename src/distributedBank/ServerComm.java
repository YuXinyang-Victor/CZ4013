package distributedBank;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class ServerComm {
	//This is the communication module for server
	
	DatagramSocket ds_server;
	InetAddress ip;
	byte[] receive;
	
	public ServerComm() throws SocketException {
		receive = new byte[65535];
		DatagramSocket ds = new DatagramSocket(2022);
		
		try {
			byte[] ip_byte = {127,0,0,1};
			ip = InetAddress.getByAddress(ip_byte);
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
