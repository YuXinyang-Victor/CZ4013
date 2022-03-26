package commtest;

public class LengthTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String pwd = "1145141919810abcdeGFD";
		byte[] pwd_byte = pwd.getBytes();
		System.out.println(pwd_byte.length);
		
		//by this test, conclude that byte array length is the same as password string length. 
	}

}
