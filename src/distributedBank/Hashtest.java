package distributedBank;

//This is only a driver to test the hash function
public class Hashtest {
	public static void main(String[] args) {
		String hashed = GenerateHash.generateHash("longpassword123123");
		System.out.println(hashed);
		System.out.println(hashed.length());
		System.out.println(hashed.getBytes());
		System.out.println(hashed.getBytes().length);
	}
}
