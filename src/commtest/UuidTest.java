package commtest;

import java.util.UUID;

public class UuidTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		UUID uuid = UUID.randomUUID();
		String uuid_str = uuid.toString();
		System.out.println(uuid_str);
		byte[] uuid_byte = uuid_str.getBytes();
		int len = uuid_byte.length;
		System.out.println(len);
		String str_in = new String(uuid_byte);
		UUID uuid_in = UUID.fromString(str_in);
		boolean eq = uuid.equals(uuid_in);
		System.out.println(eq);
	}

}
