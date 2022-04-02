package distributedBank;

import utils.LRUCache;

import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
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
	boolean atMostOnce = true;
	
	static int uuid_length = 36;

	private final LRUCache<UUID, byte[]> message_history = new LRUCache<>(50);
	
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
	
	public void serverSend(byte[] marshalled, boolean contain_uuid) throws IOException {
		ByteBuffer byte_buffer = ByteBuffer.allocate(marshalled.length + 4);
		if(contain_uuid) {
			ByteBuffer c = ByteBuffer.allocate(4);
			c.putInt(1);
			byte[] indicator = c.array();
			byte_buffer.put(indicator);
			byte_buffer.put(marshalled);
		}
		else {
			ByteBuffer c = ByteBuffer.allocate(4);
			c.putInt(0);
			byte[] indicator = c.array();
			byte_buffer.put(indicator);
			byte_buffer.put(marshalled);
		}
		
		byte[] buffer = byte_buffer.array();
		//change here for stub testing
		
		DatagramPacket dp_send = new DatagramPacket(buffer, buffer.length, client_address, client_port);
		ds_server.send(dp_send);
	}
	
	public void serverSend(byte[] marshalled, InetAddress ip, int port) throws IOException {
		ByteBuffer byte_buffer = ByteBuffer.allocate(marshalled.length + 4);
		ByteBuffer c = ByteBuffer.allocate(4);
		c.putInt(1);
		byte[] indicator = c.array();
		byte_buffer.put(indicator);
		byte_buffer.put(marshalled);
		
		byte[] buffer = marshalled;
		//change here for stub testing
		
		DatagramPacket dp_send = new DatagramPacket(buffer, buffer.length, ip, port);
		ds_server.send(dp_send);
	}
	
	public static byte[] serverAddUUID(byte[] msg, byte[] uuid) {
		int buf_len = msg.length + uuid_length;
		ByteBuffer buf = ByteBuffer.allocate(buf_len);
		buf.put(uuid);
		buf.put(msg);
		byte[] message = buf.array();
		
		return message;
		
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

	public boolean invoSemProcess(byte[] msg) throws IOException {
		UUID uuid = ServerUnmarshal.getUUID(msg); //incoming msg contains uuid and so do the messages cached. Difference: incoming - client msg format. cached - reply format
		// Check for cached value in case of duplicate message
		if(atMostOnce) {
			if(message_history.get(uuid) == null) {
				//this message has not been stored. Store the UUID - message pair
				System.out.println("New Reply Should be Cached");
				//message_history.set(uuid, msg);
				return true; //true gives green light for operations to continue
			}
			else {
				//this message exists. Trigger "send reply"
				byte[] cached_msg = message_history.get(uuid);
				System.out.println("Duplicate request. Sending cached reply");
				serverSend(cached_msg, true);
				return false; //false denotes that the operation should not continue
			}
		}
		else {
			//at least once invocation semantics executed, so operation must be carried out
			return true; //operation to be carried out no matter if it is duplicate
		}
	}
	
	public void addMsgToCache(byte[] msg, UUID uuid) {
		message_history.set(uuid, msg);
	}
}
