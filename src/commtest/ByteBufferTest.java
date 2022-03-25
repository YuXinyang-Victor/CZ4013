package commtest;

import java.nio.ByteBuffer;

public class ByteBufferTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ByteBuffer b = ByteBuffer.allocate(5);
		b.putInt(1);
		byte[] opcode_byte = b.array();
		int len = opcode_byte.length;
		System.out.println(len);
		ByteBuffer wrapped = ByteBuffer.wrap(opcode_byte);
		int opcode = wrapped.getInt();
		System.out.println(opcode);
		
	}

}
