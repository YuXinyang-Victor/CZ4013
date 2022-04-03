package distributedBank;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.UUID;

/**
 * Client side to unmarshal incoming messages
 */
public class ClientUnmarshal {
	
	
	public static String unmarshal(byte[] received) {
		boolean have_uuid = haveUUID(received);
		byte[] message_content;
		if(have_uuid) {
			message_content = Arrays.copyOfRange(received, 40, received.length);
		}
		else {
			message_content = Arrays.copyOfRange(received, 4, received.length);
		}
		
		String content = new String(message_content);
		
		//Need to elaborate: different return code corresponds to different type of return 
		return content;
	}
	
	public static UUID getUUID(byte[] received) {
		byte[] uuid_byte = Arrays.copyOfRange(received, 4, 40);
		String uuid_str  = new String(uuid_byte);
		UUID uuid = UUID.fromString(uuid_str);

		//Since the field for UUID is fixed, just get it from the message and convert to string (or other data type as needed)
		return uuid; 
	}
	
	public static UUID getSendUUID(byte[] received) {
		byte[] uuid_byte = Arrays.copyOfRange(received, 4, 40);
		String uuid_str  = new String(uuid_byte);
		UUID uuid = UUID.fromString(uuid_str);

		//Since the field for UUID is fixed, just get it from the message and convert to string (or other data type as needed)
		return uuid; 
	}
	
	public static boolean haveUUID(byte[] received) {
		byte[] check_uuid = Arrays.copyOfRange(received, 0, 4);
		ByteBuffer b = ByteBuffer.wrap(check_uuid);
		int have_uuid = b.getInt();
		if(have_uuid == 1) return true;
		else return false;
	}

}
