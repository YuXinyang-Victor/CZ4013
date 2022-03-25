package distributedBank;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class ServerUnmarshal {
	public static int peekOpcode(byte[] message_in) {
		byte[] opcode_byte = Arrays.copyOfRange(message_in, 0, 5);
		ByteBuffer b = ByteBuffer.wrap(opcode_byte);
		int opcode = b.getInt();
		return opcode;
	}
	
	public static int getFieldLength(int curr_pos, byte[] message_in) {
		byte[] field_length_byte = Arrays.copyOfRange(message_in, curr_pos, curr_pos+4);
		ByteBuffer b = ByteBuffer.wrap(field_length_byte);
		int field_length = b.getInt();
		return field_length;
	}
	
	public static String getValue(int curr_pos, int field_length, byte[] message_in) {
		//This method returns everything as string, which corresponds to client marshal methods
		//as they were converted to strings before marshaling. The function receiving the string 
		//should convert to the required data type accordingly. 
		byte[] field_value_byte = Arrays.copyOfRange(message_in, curr_pos, curr_pos+field_length);
		String field_value = new String(field_value_byte);
		return field_value;
	}
	
	public static String getUUID(byte[] message_in) {
		String UUID = new String(); 
		//Since the field for UUID is fixed, just get it from the message and convert to string (or other data type as needed)
		return UUID; 
	}
	
}
