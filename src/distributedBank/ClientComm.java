package distributedBank;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;

public class ClientComm {
	// This is the communication module for client
	
	DatagramSocket ds_client;
	InetAddress ip;
	byte[] receive;
	
	public ClientComm() throws SocketException {
		receive = new byte[65535];
		DatagramSocket ds = new DatagramSocket(2022);
		
		try {
			String ip_str = "127.0.0.1";
			ip = InetAddress.getByName(ip_str);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ds_client = ds;
	}
		
	
	public void clientSend(byte[] marshalled) throws IOException {
		byte[] buffer = marshalled;
		//change here for stub testing
		
		DatagramPacket dp_send = new DatagramPacket(buffer, buffer.length, ip, 2023);
		ds_client.send(dp_send);
	}
	
	public void clientListen() throws IOException {
		//This method should be called immediately after construction of client comm module
		
		DatagramPacket dp_receive = null;
		
		while (true) {
			dp_receive = new DatagramPacket(receive, receive.length);
			
			ds_client.receive(dp_receive);
			ClientUnmarshal unpacker = new ClientUnmarshal();
			String msg = unpacker.unmarshal(receive);  //Let server convert everything to string message then send. client just need to display after unmarshaling
			MainUI.displayMsg(msg); 
			
			receive = new byte[65535];
			
			
			
			
		}
	}
	

}
