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
	
	private ClientComm() throws SocketException {
		receive = new byte[65535];
		DatagramSocket ds = new DatagramSocket(2024);
		
		try {
			String ip_str = "127.0.0.1";
			ip = InetAddress.getByName(ip_str);

			//add timeout for retransmitting a message
			//ds.setSoTimeout(Constants.TIMEOUT_MILLISECONDS);


		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ds_client = ds;
	}
		
	
	public void clientSend(byte[] marshalled) throws IOException {
		//System.out.println("[Client] The request ID is: " + id.toString());

		byte[] buffer = marshalled;
		//change here for stub testing
		//TODO: add UUID id into marshall method
		
		DatagramPacket dp_send = new DatagramPacket(buffer, buffer.length, ip, 2023);
		ds_client.send(dp_send);

		//TODO: before call AtLeastOnceDrive, it should know and pass the previous request_id
		//call At least once driver, pass datagram packet, uuid, etc. 
		//driver gets message from client listen when a message from server is received
		//if server reply uuid (same as the message it is replying to) equals this.uuid, then nothing happens (success)
		//if server no reply in X seconds, then retransmit (call clientsend again)(retransmit counter - 1)
		//if retransmit counter is drained, then display network error
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
		System.out.println("called");
		
		while (true) {
			dp_receive = new DatagramPacket(receive, receive.length);
			
			ds_client.receive(dp_receive);
			//call driver
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

	private void AtLeastOnceDriver(DatagramPacket initialPacket) throws IOException{
		boolean wait_for_response = true;
		int attempt = 0;
		int time_out = Constants.TIMEOUT_MILLISECONDS;
		int updated_time_out = time_out;

		Random rand = new Random();

		//use a random number to determine whether it should be actually sent out

		if(wait_for_response){
			while(attempt <= Constants.MAX_ATTEMPTS) {
				ds_client.setSoTimeout(time_out);

				try {
					if (rand.nextInt(9) != 0) {
						ds_client.send(initialPacket);
					} else {
						System.out.println("Packet Loss");
						continue;
					}

					byte[] receive = new byte[65535];
					DatagramPacket ack_packet = new DatagramPacket(receive, receive.length);

					int startTime = (int) System.currentTimeMillis();
					//simulate ack_packet loss
					if (rand.nextInt(9) != 5){
						ds_client.receive(ack_packet);
					}
					int endTime = (int) System.currentTimeMillis();

					updated_time_out -= endTime - startTime;
					//keep updating timeout duration
					if (updated_time_out <= 0) {
						System.out.println("No Message Received From Server In " + updated_time_out +
								"Seconds. Resending...");
					}
					ds_client.setSoTimeout(updated_time_out);


				} catch (Exception e){
					e.printStackTrace();
				}

				attempt += 1;
			}
			if (attempt == Constants.MAX_ATTEMPTS){
				System.out.println("Maximum " + Constants.MAX_ATTEMPTS + "Attempts Reached.");
				System.out.println("Please Check Your Internet Connection and Try Again Later.");
			}

		}
		else {
			ds_client.send(initialPacket);
		}

	}


}
