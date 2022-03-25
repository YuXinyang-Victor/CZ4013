package distributedBank;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class ServerUnmarshal {
	public int peekOpcode(byte[] message_in) {
		byte[] opcode_byte = Arrays.copyOfRange(message_in, 0, 5);
		ByteBuffer b = ByteBuffer.wrap(opcode_byte);
		int opcode = b.getInt();
		return opcode;
	}
}
