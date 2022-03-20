package distributedBank;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class GenerateHash {
	
	public GenerateHash() {
		
	}
	
	public static String generateHash(String input)  {
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("SHA-1");
		
		byte[] messageDigest = md.digest(input.getBytes());
		BigInteger bi = new BigInteger(1, messageDigest);
		
		String output = bi.toString(16);
		return output; 
		} 
		
		catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
		
	}
}
