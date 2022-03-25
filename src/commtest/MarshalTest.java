package commtest;

import java.nio.ByteBuffer;

import distributedBank.ClientMarshal;
import distributedBank.CurrencyType;

public class MarshalTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String name = "john-smith";
		String password_hash = "79400f341e44cd9be5fc91c000186786f2ac2e2a";
		CurrencyType currency = CurrencyType.SGD;
		double amount = 1145.14;
		
		ClientMarshal tobytes = new ClientMarshal();
		byte[] byte_msg = tobytes.marshal(name, password_hash, currency, amount);
		String msg = new String(byte_msg);
		System.out.println(msg);
		
		int test1 = 114514;
		double test2 = 114.514;
		byte[] test1_b = tobytes.marshalAccNumber(test1);
		byte[] test2_b = tobytes.marshalAmount(test2);
		int length_1 = test1_b.length;
		byte test1_b_2 = Integer.valueOf(test1).byteValue();
		String str1 = new String(test1_b);
		String str2 = new String(test2_b);
		System.out.println(str1);
		System.out.println(str2);
		
		
		ByteBuffer buffer = ByteBuffer.allocate(length_1);
		buffer.putInt(test1);
		byte[] test1_c = buffer.array();
		buffer.clear();
		String str3 = new String(test1_c);
		System.out.println(str3);
		
		
		
		
	}

}
