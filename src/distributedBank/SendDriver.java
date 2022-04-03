package distributedBank;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import utils.Constants;

public class SendDriver {
	
	private static int attempted; 
	private static SendDriver driver= null;
	public boolean wait_for_response = true;
	public int time_out = Constants.TIMEOUT_MILLISECONDS;
	public UUID uuid; 
	public static boolean reply_received; 
	
	private SendDriver() {
		attempted = 0; 
		uuid = null;
		reply_received = false;
	}
	public static SendDriver getDriver() {
		if(driver == null) {
			driver = new SendDriver();
		}
		return driver;
	}
	
	private void flushData() {
		attempted = 0; 
		uuid = null;
		reply_received = false;
	}
	
	public void send(byte[] initial_msg) throws IOException{
		flushData();
		
		ClientComm clientcomm = ClientComm.getClientComm();
		Random rand = new Random();
		
		uuid = ClientUnmarshal.getSendUUID(initial_msg);
		System.out.println(uuid);

		//use a random number to determine whether it should be actually sent out

		if(wait_for_response){
			while (attempted < Constants.MAX_ATTEMPTS) {
				//ds_client.setSoTimeout(time_out);

				try {
					if (rand.nextInt(2) == 0) {
						clientcomm.clientSend(initial_msg);
					} else {
						System.out.println("Send Packet Loss");
						//continue, no need to do anything (pretend the message is sent)
						
						
					}
					attempted += 1;
					
					
					try {
						TimeUnit.SECONDS.sleep(5); //act as waiting time for response
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					//check if response is received. If yes, return (end this function)
					if(reply_received) return;
					
					System.out.println("No Message Received From Server In " + attempted*5 +
								" Seconds. Resending...");
					


				} catch (Exception e){
					e.printStackTrace();
				}

				
			}
			
			System.out.println("Maximum " + Constants.MAX_ATTEMPTS + "Attempts Reached.");
			System.out.println("Please Check Your Internet Connection and Try Again Later.");
			

		}
		else {
			clientcomm.clientSend(initial_msg);
		}
		

	}

	/**
	 * Check if has received the correct response from server
	 * @param message
	 */
	public void updateReceiveStatus(byte[] message) {
		Random rand = new Random();
		
		//get uuid from client unmarshal
		UUID uuid_received = ClientUnmarshal.getUUID(message);
		
		//compare uuid to the saved uuid
		boolean is_same = uuid_received.equals(uuid);
		System.out.println(uuid_received + "is" + is_same);
		
		//if uuid is same, update (use random to not update received value to simulate reply lost)
		if ((rand.nextInt(2) != 0) && is_same){
			reply_received = true;
		}
		else {
			System.out.println("reply packet lost simulation");
		}
	}
}
