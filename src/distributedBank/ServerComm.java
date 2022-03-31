package distributedBank;

import utils.LRUCache;

import java.io.IOException;
import java.net.*;
import java.util.Random;
import java.util.UUID;


//Singleton Class
public class ServerComm  {
	//This is the communication module for server
	private static ServerComm server_comm = null;
	DatagramSocket ds_server;
	InetAddress client_address;
	int client_port;
	byte[] receive;
	boolean atMostOnce;

	private final LRUCache<UUID, DatagramPacket> messageHistory = new LRUCache<>(50);
	
	public static ServerComm getServerComm() throws SocketException {
		if (server_comm == null) {
			server_comm = new ServerComm();
			System.out.println("servercomm created");
		}
		
		return server_comm;
	}
	
	public ServerComm() throws SocketException {
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
	
	public void serverSend(byte[] marshalled, InetAddress ip, int port) throws IOException {
		byte[] buffer = marshalled;
		//change here for stub testing
		
		DatagramPacket dp_send = new DatagramPacket(buffer, buffer.length, ip, port);
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

	public DatagramPacket receiveRequest(boolean shouldCache) throws IOException {
		byte[] dataBuffer = new byte[4096];
		DatagramPacket msg = new DatagramPacket(dataBuffer, dataBuffer.length);

		Random random = new Random();


		try{
			ds_server.receive(msg);
			if(random.nextInt(10) > 8){
				System.out.println("Message Lost Simulated");
			}
			System.out.println("Success");
			byte[] data = msg.getData();
			String message = msg.toString();

			UUID uuId = ServerUnmarshal.getUUID(data);


			// Check for cached value in case of duplicate message
			if(atMostOnce){
				DatagramPacket storedMessage = messageHistory.get(uuId);
				if(storedMessage != null){
					System.out.println("Duplicated Message Received: " + ServerUnmarshal.getUUID(data));
					ds_server.send(storedMessage);
					return null;
				}
				if(shouldCache){
					messageHistory.set(uuId, msg);
				}
				else{
					System.out.println("New Request Received");
					return msg;
				}
			} else {
				System.out.println("[Server] At Least Once Request");
				return msg;
			}

		}
		catch (SocketTimeoutException exception) {
			throw exception;
		}
		catch (IOException e) {
			e.printStackTrace();
		}

		return msg;
	}
}
