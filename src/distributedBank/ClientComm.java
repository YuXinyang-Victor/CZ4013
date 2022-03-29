package distributedBank;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import java.nio.ByteBuffer;

import java.util.Scanner;
import java.util.UUID;
import java.time.Duration;
import java.time.Instant;
import java.time.*;

import java.util.Random;

import utils.Constants;


public class ClientComm {
	// This is the communication module for client
	private static ClientComm client_comm = null;
	DatagramSocket ds_client;
	InetAddress ip;
	byte[] receive;


	
	public static ClientComm getClientComm() throws SocketException {
		if (client_comm == null) {
			client_comm = new ClientComm();
		}
		
		return client_comm;
	}
	
	public ClientComm() throws SocketException {
		receive = new byte[65535];
		DatagramSocket ds = new DatagramSocket(2022);
		
		try {
			String ip_str = "127.0.0.1";
			ip = InetAddress.getByName(ip_str);

			//add timeout for retransmitting a message
			ds.setSoTimeout(Constants.TIMEOUT_MILLISECONDS);


		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ds_client = ds;
	}
		
	
	public void clientSend(UUID id, byte[] marshalled) throws IOException {
		System.out.println("[Client] The request ID is: " + id.toString());

		byte[] buffer = marshalled;
		//change here for stub testing
		//TODO: add UUID id into marshall method
		
		DatagramPacket dp_send = new DatagramPacket(buffer, buffer.length, ip, 2023);
		ds_client.send(dp_send);
	}
	
	public void clientRegister() throws IOException {
		int opcode = 0;
		
		int buf_len = 40; 
		
		ByteBuffer buf = ByteBuffer.allocate(buf_len);
		
		ByteBuffer c = ByteBuffer.allocate(4);
		c.putInt(opcode);
		byte[] opcode_byte = c.array();
		
		buf.put(opcode_byte);
		
		//insert uuid
		byte[] uuid = ClientMarshal.getUUID();
		buf.put(uuid);
		
		byte[] buffer = buf.array();
		
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
			distributedBank.displayMsg(msg); 
			
			receive = new byte[65535];
			
		}


	}

	// add setSocketTimeOut method for timeOut change
	public void setSocketTimeOut(int milliSeconds) throws SocketException {
		this.setSocketTimeOut(milliSeconds);
	}

	public String sendMessage(DatagramPacket datagramPacket, String request_id) throws IOException {
		boolean wait_for_response = true;
		int attempt = 0;
		UUID id = UUID.randomUUID();
		int time_out = Constants.TIMEOUT_MILLISECONDS;

		Random rand = new Random();
		int n = rand.nextInt(9);
		//use a random number to determine whether it should be actually sent out


		if (wait_for_response) {
			while(attempt <= Constants.MAX_ATTEMPTS){
				this.setSocketTimeOut(time_out);
				attempt += 1;

				try {
					if (n != 0) {
						//TODO: socket send msg to
						ds_client.send(datagramPacket);
					} else{
						System.out.println("Packet Loss");
					}

					int updated_time_out = time_out;

					while(true){
						int startTime = (int) System.currentTimeMillis();
						//TODO: recvfrom()
						InetAddress client_address = datagramPacket.getAddress();
						byte[] reply_msg = datagramPacket.getData();
						String reply_message = ServerUnmarshal.getUUID(reply_msg).toString();

						this.clientListen();
						//Socket receive data and address
						int endTime = (int) System.currentTimeMillis();

						//TODO: if address and id is correct, then return reply messgae

						if(client_address.equals("127.0.0.1") && ServerUnmarshal.getUUID(reply_msg).toString().equals(request_id)){
							return reply_message;
						}


						updated_time_out -= endTime - startTime;
						if (updated_time_out <= 0) {
							System.out.println("No Message Received From Server In " + time_out +
									"Seconds. Resending...");
						}
						this.ds_client.setSoTimeout(updated_time_out);

					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			System.out.println("Maximum " + Constants.MAX_ATTEMPTS + "Attempts Reached.");
			System.out.println("Please Check Your Internet Connection and Try Again Later.");

		}
		else {
			this.ds_client.send(datagramPacket);
		}

		return null;

	}



	

}
